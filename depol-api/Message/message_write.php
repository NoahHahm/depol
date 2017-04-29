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
	
	$receive_user_id = $_POST['receive_user_id'];
	$context = $_POST['context'];
	if (!is_numeric($receive_user_id) || empty($receive_user_id) || empty($context)) {
		$json = ExceptionJson::jsonRequestParamsFail();
		echo $json;	
		return;
	}
		
	//데이터베이스 연결
	$db = DataBase::global_instance();
	$result = $db->writeMessage($_SESSION['ID'], $receive_user_id, $context);
	if ($result) {
		$arr = array(Consts::RESULT => Consts::SUCCESS);
	} else {
		$arr = array(Consts::RESULT => Consts::REQUEST_MESSAGE_WRITE_FAIL);
	}
	$json = json_encode($arr);
	echo $json;
?>