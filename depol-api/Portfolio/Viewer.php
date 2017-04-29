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
		
	//데이터베이스 연결
	$db = DataBase::global_instance();
	
	//포트폴리오 정보
	$rows = $db->getPortfolioInfo($_SESSION['ID'], $id);
	if ($rows) {
		$images = $db->getPortfolioImageList($id);
		$result  = array(
			Consts::RESULT => Consts::SUCCESS,			
			"userId" => $rows['user_id'],	
			"userPropicUri" => Config::SERVER_DOMAIN."/".$rows['user_url'],
			"userRecruitStatus" => $rows['user_status'],						
			"userName" => $rows['user_name'],
			"thumbPofolId" => (int)$rows['portfolio_id'],
			"pofolTitle" => $rows['title'],
			"pofolText" => $rows['context'],
			"thumbImgUri" => $rows['thumbnail_url'],
			"likeNum" => (int)$rows['likeNum'],	
			"commentNum" => (int)$rows['commentNum'],
			"isLiked" => (int)$rows['isLiked'],
			"isMine" => (int)($_SESSION['ID'] == $rows['user_id']) ? 1 : 0,
			"pofolImgUri" => $images
		);
		$json = json_encode($result);
		echo $json;
	} else {
		$json = ExceptionJson::jsonPortfolioListEmptyFail();
		echo $json;	
	}
?>