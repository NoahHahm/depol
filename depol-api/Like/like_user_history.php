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
	
	$portfolio_id = $_GET['pid'];
	if (!is_numeric($portfolio_id) || empty($portfolio_id)) {
		$json = ExceptionJson::jsonRequestParamsFail();
		echo $json;	
		return;
	}
	
	//데이터베이스 연결
	$db = DataBase::global_instance();
	$arr = $db->getLikeHistoryList($portfolio_id, Consts::REQUEST_USER_LIKE_HISTORY);
	if ($arr) {
		$arr = array(Consts::RESULT => Consts::SUCCESS,
							 "data" => $arr);
	} else {
		$arr = array(Consts::RESULT => Consts::DATA_NULL);
	}
	$json = json_encode($arr);
	echo $json;	
?>