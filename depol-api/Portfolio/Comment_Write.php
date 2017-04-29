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
		
	$portfolioId = $_POST['id'];
	//$userId = $_POST['targer_user'];
	$userId = $_SESSION['ID'];
	$context = $_POST['context'];
	
	if (empty($portfolioId) || empty($userId) || empty($context)) {
		$json = ExceptionJson::jsonRequestParamsFail();
		echo $json;	
		return;		
	}

		
	//데이터베이스 연결
	$db = DataBase::global_instance();
	
	//리플 리스트
	$result = $db->commentWrite($userId, $portfolioId, $context);
	if ($result) {
		$result = $db->getCommentList($portfolioId);
		$arr = array(
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