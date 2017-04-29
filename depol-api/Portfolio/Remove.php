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
	
	$portfolio_id = $_GET['id'];	
	if (empty($portfolio_id)) {
		$json = ExceptionJson::jsonRequestParamsFail();
		echo $json;	
		return;	
	}
	

	//데이터베이스 연결
	$db = DataBase::global_instance();
	//기존 이미지 제거
	$images_path_arr = $db->getImagesPath($portfolio_id);
	if($images_path_arr) {
		//이미지 파일 제거
		foreach ($images_path_arr as $path) {
			$path = str_replace("Portfolio/","", $path);
			unlink($path);
		}
	}
	
	//삭제
	$result = $db->removePortfolio($portfolio_id);
	if ($result) {
		$arr  = array(
			Consts::RESULT => Consts::SUCCESS
		);
	} else {
		$arr  = array(
			Consts::RESULT => Consts::REQUEST_REMOVE_FAIL
		);		
	}
	$json = json_encode($arr);
	echo $json;
?>