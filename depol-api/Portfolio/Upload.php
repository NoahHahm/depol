<?php 

	session_start();
	
	require_once ("../class/ExceptionJson.php");
	require_once ("../class/Config.php");
	require_once ("../class/Consts.php");
	require_once ("../class/DataBase.php");
	
	//세션 에러
	if (empty($_SESSION['ID'])) {
		$json = ExceptionJson::jsonSessionFail();
		echo $json;	
		return;	
	}	
	
	//{"category":[3,4],"data":[{"pofolHeight":0,"pofolWidth":0},{"pofolHeight":1280,"pofolWidth":720}],"pofolTitle":"ㄹㄹ","pofolText":"ㅠ튵","imgId":0}
	$category = $_POST['category'];
	$title = $_POST['title'];
	$context = $_POST['context'];	
		
	if (empty($title) || empty($context) || empty($category)) {
		$json = ExceptionJson::jsonRequestParamsFail();
		echo $json;	
		return;
	}
	
	
	//데이터베이스 연결
	$db = DataBase::global_instance();
	//$maxId = $db->getMaxPortfolioId();
	$filePath = "images/{$_SESSION['ID']}";
	
	//폴더 생성
	if(!is_dir($filePath)) {
		@mkdir($filePath, 0777);
	}
	$image_path = array();
	
	//파일 처리
	foreach($_FILES['images']['tmp_name'] as $key => $tmp_name)
	{
		$file_name = $_FILES['images']['name'][$key];
		$ext = pathinfo($file_name, PATHINFO_EXTENSION);
		$file_name = $filePath."/".$key.time().".".$ext;
		$file_tmp = $_FILES['images']['tmp_name'][$key];
		
		//썸네일 작업
		if ($key == 0) {
			//$image_path[$key] = $filePath."/".time()."_thumbnail.".$ext;	
		}
		$image_path[$key] = $file_name;
		move_uploaded_file($tmp_name, $image_path[$key]);			
	}
	
	//포트폴리오 추가
	for($i=0;$i<COUNT($image_path);$i++) {
		$image_path[$i] = "Portfolio/".$image_path[$i];		
	}
	$result = $db->addPortfolio($_SESSION['ID'], $title, $context, $image_path, $category);
	if ($result) {
		$arr  = array(
			Consts::RESULT => Consts::SUCCESS
		);
		$json = json_encode($arr);
		echo $json;
	}	
	
?>