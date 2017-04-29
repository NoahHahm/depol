<?PHP
	session_start();
	
	require_once ("../class/ExceptionJson.php");
	require_once ("../class/Config.php");
	require_once ("../class/Consts.php");
	require_once ("../class/DataBase.php");
	
	header("Content-Type: application/json; charset=UTF-8");
		
	//세션 에러
	if (empty($_SESSION['ID'])) {
		$json = ExceptionJson::jsonSessionFail();
		echo $json;	
		return;	
	}
	
	$json_str = $_POST['json'];
	if (empty($json_str)) {
		$json = ExceptionJson::jsonRequestParamsFail();
		echo $json;	
		return;
	}
	$json = json_decode($json_str);
	
	
	$db = DataBase::global_instance();
	foreach($json->data as $key => $json_arr) {
		
		//날짜 계산
		$date = mktime(0, 0, 0, $json_arr->startMonth, 1, $json_arr->startYear);		
		$start_date = date("Y-m-d", $date);
		
		$date = mktime(0, 0, 0, $json_arr->endMonth, 1, $json_arr->endYear);
		$end_date = date("Y-m-d", $date);
		
		//PK 0인경우 추가
		if ($json_arr->pk == 0) {
			$id = $db->addEducation($_SESSION['ID'], $start_date, $end_date, $json_arr->text, $json_arr->subText);
			$id_arr[] = $id;
		} else {
			$result = $db->setEducation($_SESSION['ID'], $json_arr->pk, $start_date, $end_date, $json_arr->text, $json_arr->subText);
			$id_arr[] = $json_arr->pk;
		}
	}
	$id_arr = implode(',', $id_arr);
	$db->deleteEducation($_SESSION['ID'], $id_arr);
	
	$educationArr = $db->getEducation($_SESSION['ID']);
	if ($educationArr) {
		$arr = array(Consts::RESULT => Consts::SUCCESS, "educationInfo" => $educationArr);	
	} else {
		$arr = array(Consts::RESULT => Consts::DATA_NULL);	
	}
	$json = json_encode($arr);
	echo $json;
?>