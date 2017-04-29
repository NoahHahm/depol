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
	
	$like_id = $_GET['lid'];
	if (!is_numeric($like_id) || empty($like_id)) {
		$json = ExceptionJson::jsonRequestParamsFail();
		echo $json;	
		return;
	}
	
	//데이터베이스 연결
	$db = DataBase::global_instance();
	$result = $db->readLike($like_id);
	if ($result) {
		$arr = array(Consts::RESULT => Consts::SUCCESS);
	} else {
		$arr = array(Consts::RESULT => Consts::REQUEST_LIKE_READ_FAIL);
	}
	$json = json_encode($arr);
	echo $json;	
?>