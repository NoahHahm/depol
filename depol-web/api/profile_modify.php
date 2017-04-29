<?PHP
	session_start();
	
	require_once ("../../class/ExceptionJson.php");
	require_once ("../../class/Config.php");
	require_once ("../../class/Consts.php");
	require_once ("../../class/DataBase.php");
	
	header("Content-Type: application/json; charset=UTF-8");
		
	//세션 에러
	if (empty($_SESSION['ID'])) {
		$json = ExceptionJson::jsonSessionFail();
		echo $json;	
		return;	
	}
	
	$name = $_GET['mname'];	
	$job = $_GET['mjob'];
	
	
	//데이터베이스 연결
	$db = DataBase::global_instance();
	$arr = $db->getUserInfo($_SESSION['ID'], Consts::REQUEST_LOGIN);
	
	//이름 처리
	if (!empty($name)) {
		$result = $db->updateProfile($_SESSION['ID'], $name, $arr['userRecruitStatus'], $arr['userPosition']);
	} else if (!empty($job)) {
		$result = $db->updateProfile($_SESSION['ID'], $arr['userName'], $arr['userRecruitStatus'], $job);
	}
	
	if ($result) {
		$result = array("result" => Consts::SUCCESS);
	} else {
		$result = array("result" => "fail");
	}
	$json = json_encode($result);
	echo $json;		
?>