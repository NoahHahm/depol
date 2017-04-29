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
	
	//{"text":"ㅋㅋ","subText":"","pk":1,"year":0}
	
	$db = DataBase::global_instance();
	foreach($json->data as $key => $json_arr) {
		
		//PK 0인경우 추가
		if ($json_arr->pk == 0) {
			$id = $db->addAward($_SESSION['ID'], $json_arr->year, $json_arr->text, $json_arr->subText);
			$id_arr[] = $id;
		} else {
			$result = $db->setAward($_SESSION['ID'], $json_arr->pk, $json_arr->year, $json_arr->text, $json_arr->subText);
			$id_arr[] = $json_arr->pk;
		}
	}
	$id_arr = implode(',', $id_arr);
	$db->deleteAward($_SESSION['ID'], $id_arr);
	$awardArr = $db->getAward($_SESSION['ID']);
	if ($awardArr) {
		$arr = array(Consts::RESULT => Consts::SUCCESS, "awardInfo" => $awardArr);	
	} else {
		$arr = array(Consts::RESULT => Consts::DATA_NULL);	
	}
	$json = json_encode($arr);
	echo $json;
?>