<?php

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
	
	$followId = $_GET['fid'];
	if (!is_numeric($followId) || empty($followId)) {
		$json = ExceptionJson::jsonRequestParamsFail();
		echo $json;	
		return;
	}
	
	
	//데이터베이스 연결
	$db = DataBase::global_instance();
	$result = $db->unFollow($_SESSION['ID'], $followId);
	if ($result) {
		$result = array(Consts::RESULT => Consts::SUCCESS);	
	}
	$json = json_encode($result);
	echo $json;	
?>