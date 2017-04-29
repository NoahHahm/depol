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
		
	$commentId = $_GET['comment_id'];	
	$portfolioId = $_GET['id'];	

	if (empty($commentId) || empty($portfolioId)) {
		$json = ExceptionJson::jsonRequestParamsFail();
		echo $json;	
		return;	
	}

		
	//데이터베이스 연결
	$db = DataBase::global_instance();
	
	//리플 삭제
	$rows = $db->commentRemove($commentId);
	if ($rows) {
		
		$result = $db->getCommentList($portfolioId);
		if (!$result) {
			$result = array();
		}		
		
		$arr  = array(
			Consts::RESULT => Consts::SUCCESS,
			"data" => $result
		);
		$json = json_encode($arr);
		echo $json;
	} else {
		$json = ExceptionJson::jsonCommentListEmptyFail();
		echo $json;	
	}
?>