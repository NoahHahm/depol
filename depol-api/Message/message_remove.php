<?PHP
	session_start();
	
	require_once ("../class/DataBase.php");
	require_once ("../class/ExceptionJson.php");
	require_once ("../class/Config.php");
	require_once ("../class/Consts.php");
	
	header("Content-Type: application/json; charset=UTF-8");
			
	//세션 에러
	if (empty($_SESSION['ID'])) {
		$json = ExceptionJson::jsonSessionFail();
		echo $json;	
		return;	
	}	
	
	$message_id = $_GET['mid'];
	if (!is_numeric($message_id) || empty($message_id)) {
		$json = ExceptionJson::jsonRequestParamsFail();
		echo $json;	
		return;
	}
		
	//데이터베이스 연결
	$db = DataBase::global_instance();
	$result = $db->removeMessage($message_id);
	if ($result) {
		$arr = array(Consts::RESULT => Consts::SUCCESS);
	} else {
		$arr = array(Consts::RESULT => Consts::REQUEST_MESSAGE_REMOVE_FAIL);
	}
	$json = json_encode($arr);
	echo $json;
?>