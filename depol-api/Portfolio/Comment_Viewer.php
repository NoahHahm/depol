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
		
	$id = $_GET['id'];	
	if (!is_numeric($id)) {
		$json = ExceptionJson::jsonRequestParamsFail();
		echo $json;	
		return;
	}
		
	//데이터베이스 연결
	$db = DataBase::global_instance();
	
	//리플 리스트
	$rows = $db->getCommentList($id);
	if ($rows) {
		$result  = array(
			Consts::RESULT => Consts::SUCCESS,
			"data" => $rows
		);
		$json = json_encode($result);
		echo $json;
	} else {
		$json = ExceptionJson::jsonCommentListEmptyFail();
		echo $json;	
	}
?>