<?php 

	session_start();
	
	require_once ("../class/ExceptionJson.php");
	require_once ("../class/Config.php");
	require_once ("../class/Consts.php");
	require_once ("../class/DataBase.php");
	require_once ("../lib/PHPImageWorkshop/ImageWorkshop.php");
	
	//세션 에러
	if (empty($_SESSION['ID'])) {
		$json = ExceptionJson::jsonSessionFail();
		echo $json;	
		return;	
	}	
	$user_name = $_GET['name'];
	$user_status = $_GET['status'];
	$user_position = $_GET['position'];
		
	if (empty($user_name) || empty($user_status)) {
		$json = ExceptionJson::jsonRequestParamsFail();
		echo $json;	
		return;
	}		
	
	//데이터베이스 연결
	$db = DataBase::global_instance();
	
	//업데이트
	$result = $db->updateProfile($_SESSION['ID'], $user_name, $user_status, $user_position);
	if ($result) {
		$arr = $db->getUserInfo($_SESSION['ID'], Consts::REQUEST_LOGIN);
	} else {
		$arr  = array(
			Consts::RESULT => Consts::DATA_NULL
		);	
	}
	
	$json = json_encode($arr);
	echo $json;
?>