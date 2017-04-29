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
				
		//PK 0인경우 추가
		if ($json_arr->pk == 0) {
			$id = $db->addCertification($_SESSION['ID'], $json_arr->text);
			$id_arr[] = $id;
		} else {
			$db->setCertification($json_arr->pk, $json_arr->text);
			$id_arr[] = $json_arr->pk;
		}
		
	}
	$id_arr = implode(',', $id_arr);
	$db->deleteCertification($_SESSION['ID'], $id_arr);
	$certificationArr = $db->getCertification($_SESSION['ID']);
	if ($certificationArr) {
		$arr = array(Consts::RESULT => Consts::SUCCESS, "certificationInfo" => $certificationArr);	
	} else {
		$arr = array(Consts::RESULT => Consts::DATA_NULL);	
	}
	$json = json_encode($arr);
	echo $json;
?>