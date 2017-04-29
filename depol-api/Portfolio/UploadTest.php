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
	$filePath = "images/{$_SESSION['ID']}";
	$thumbnail_path = time()."_thumbnail.png";
	$web_thumbnail_path = time()."_web_thumbnail.png";
	
	//폴더 생성
	if(!is_dir($filePath)) {
		@mkdir($filePath, 0777);
	}
		
	//파일 처리
	foreach($_FILES['images']['tmp_name'] as $key => $tmp_name)
	{
		$file_name = $_FILES['images']['name'][$key];
		$ext = pathinfo($file_name, PATHINFO_EXTENSION);
		$file_name = $filePath."/".$key.time().".".$ext;
		$file_tmp = $_FILES['images']['tmp_name'][$key];
		
		//썸네일 작업
		if ($key == 0) {
    		$layer = new ImageWorkshop(array(
			    'imageFromPath' => $tmp_name,
			));
    		
			$layer->resizeInPixel(500, null, true);	
			$layer->save($filePath, $web_thumbnail_path, true, null, 95);
			
			$layer->resizeInPixel(200, null, true);	
			$layer->save($filePath, $thumbnail_path, true, null, 95);
					
		}
		$image_path[] = $file_name;
		move_uploaded_file($tmp_name, $image_path[$key]);			
	}
	
	//포트폴리오 추가
	foreach($image_path as $path) {
		$image_replace_path[] = "Portfolio/".$path;			
	}
	$thumbnail_replace_path = "Portfolio/".$filePath."/".$thumbnail_path;
	$web_thumbnail_replace_path = "Portfolio/".$filePath."/".$web_thumbnail_path;
	
	
	$result = $db->addPortfolio($_SESSION['ID'], $title, $context, $thumbnail_replace_path, $web_thumbnail_replace_path,  $image_replace_path, $category);
	if ($result) {
		$arr  = array(
			Consts::RESULT => Consts::SUCCESS
		);
		$json = json_encode($arr);
		echo $json;
	} else {
		//썸네일 제거
		unlink($filePath."/".$thumbnail_path);

		//웹 썸네일 제거
		unlink($filePath."/".$web_thumbnail_path);

		//이미지 파일 제거
		foreach ($image_path as $path) {
			unlink($path);
		}
		$arr  = array(
			Consts::RESULT => Consts::DATA_UPLOAD_FAIL
		);
		$json = json_encode($arr);
		echo $json;		
	}	
	
?>