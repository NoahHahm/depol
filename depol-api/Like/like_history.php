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
	
	//데이터베이스 연결
	$db = DataBase::global_instance();
	$arr = $db->getLikeHistoryList($_SESSION['ID'], Consts::REQUEST_LIKE_HISTORY);
	if ($arr) {
		$result = array(Consts::RESULT => Consts::SUCCESS,
							 "data" => $arr);
	} else {
		$result = array(Consts::RESULT => Consts::DATA_NULL);
	}
	$json = json_encode($result);
	echo $json;	
?>