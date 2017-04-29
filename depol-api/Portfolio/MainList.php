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
	
	$tag = $_GET['TAG'];
	$page = $_GET['PAGE_NUM'];
		
	//데이터베이스 연결
	$db = DataBase::global_instance();
	$arr = $db->getPortfolioList($_SESSION['ID'], $tag, $page);
	if ($arr) {
		$result = array("result" => "SUCCESS", "pofol" => $arr);	
		$json = json_encode($result);
		echo $json;
	} else {
		$json = ExceptionJson::jsonPortfolioListEmptyFail();
		echo $json;	
	}
	
?>