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
	
	$email = $_GET['email'];
	if (empty($email)) {
		$json = ExceptionJson::jsonRequestParamsFail();
		echo $json;	
		return;
	}
	
	//EMAIL_DUPLICATED
	//데이터베이스 연결
	$db = DataBase::global_instance();
	$result = $db->CheckEmail($email);
	if ($result) {
		$arr = array(Consts::RESULT => Consts::EMAIL_DUPLICATION_FAIL);
		$json = json_encode($arr);
		echo $json;	
		return;			
	}	
	
	
	$result = $db->updateEmail($_SESSION['ID'], $email);
	if ($result) {
		$arr = array(Consts::RESULT => Consts::SUCCESS, "email" => $email);
	} else {
		$arr = array(Consts::RESULT => Consts::REQUEST_RROFILE_UPDATE_FAIL);
	}
	$json = json_encode($arr);
	echo $json;	
?>