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
		
	$keyword = $_GET['keyword'];	
	$category = $_GET['category'];
	if (empty($keyword) && empty($category)) {
		$json = ExceptionJson::jsonRequestParamsFail();
		echo $json;	
		return;
	}
		
	//데이터베이스 연결
	$db = DataBase::global_instance();
	$arr = $db->getSearchList($_SESSION['ID'], $keyword, $category);
	if ($arr) {
		$result = array(Consts::RESULT => Consts::SUCCESS, "pofol" => $arr);	
		$json = json_encode($result);
		echo $json;
	} else {
		$json = ExceptionJson::jsonPortfolioListEmptyFail();
		echo $json;	
	}
?>