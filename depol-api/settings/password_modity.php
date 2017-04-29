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
	
	$before_password = $_POST['bepass'];
	$after_password = $_POST['afpass'];
	if (empty($before_password) || empty($after_password)) {
		$json = ExceptionJson::jsonRequestParamsFail();
		echo $json;	
		return;
	}
	
	//데이터베이스 연결
	$db = DataBase::global_instance();
	$result = $db->checkPassword($_SESSION['ID'], $before_password);
	if (!$result) {
		$arr = array(Consts::RESULT => Consts::REQUEST_PASSWORD_FAIL);
		$json = json_encode($arr);
		echo $json;	
		return;
	}
	
	$result = $db->updatePassword($_SESSION['ID'], $after_password);
	if ($result) {
		$arr = array(Consts::RESULT => Consts::SUCCESS);
	} else {
		$arr = array(Consts::RESULT => Consts::REQUEST_RROFILE_UPDATE_FAIL);
	}
	$json = json_encode($arr);
	echo $json;	
?>