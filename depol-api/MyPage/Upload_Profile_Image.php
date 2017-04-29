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
		
	if (empty($_FILES['images'])) {
		$json = ExceptionJson::jsonRequestParamsFail();
		echo $json;	
		return;
	}		
	
	//파일 처리
	$file_name = $_FILES['images']['name'];
	$ext = pathinfo($file_name, PATHINFO_EXTENSION);
	if (!$ext == "png" || !$ext == "jpeg" || !$ext == "jpg") {
		$json = ExceptionJson::jsonExtensionFail();
		echo $json;	
		return;		
	}
	
	//데이터베이스 연결
	$db = DataBase::global_instance();
	$filePath = "images/{$_SESSION['ID']}";
	$thumbnail_path = time()."_thumbnail.png";
	$image_path = "MyPage/".$filePath."/".$thumbnail_path;
	
	if(is_dir($filePath)) {
		if ($dh = opendir($filePath)) {
			while($file = readdir($dh)) {
				if ($file == "." || $file == "..") continue;
				unlink($filePath."/".$file);	
			}
			closedir($dh);	
		}
	}
	
	//폴더 생성
	if(!is_dir($filePath)) {
		@mkdir($filePath, 0777);
	}
		
	$file_name = $filePath."/".$key.time().".".$ext;
	$file_tmp = $_FILES['images']['tmp_name'];
		
	//썸네일 작업
   	$layer = new ImageWorkshop(array(
	    'imageFromPath' => $file_tmp,
	));
   		
	$layer->resizeInPixel(100, null, true);	
	$layer->save($filePath, $thumbnail_path, true, null, 95);
	//move_uploaded_file($file_tmp, $filePath."/".$thumbnail_path);	
	
	//포트폴리오 추가
	$result = $db->updateProfileImage($_SESSION['ID'], $image_path);
	if ($result) {
		$arr  = array(
			Consts::RESULT => Consts::SUCCESS
		);
		$json = json_encode($arr);
		echo $json;
	} else {
		//썸네일 제거
		unlink($filePath."/".$thumbnail_path);
		$arr  = array(
			Consts::RESULT => Consts::DATA_UPLOAD_FAIL
		);
		$json = json_encode($arr);
		echo $json;		
	}	
	
?>