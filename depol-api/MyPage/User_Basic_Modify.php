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
	
	$name = $_POST['name'];
	$position = $_POST['position'];
	$location = $_POST['location'];
	$email = $_POST['email'];
	$birth = $_POST['birth'];
	$website = $_POST['website'];
		
	//데이터베이스 연결
	$db = DataBase::global_instance();
	$result = $db->setMemberInfo($_SESSION['ID'], $name, $position, $location, $email, $birth, $website);
	if ($result) {
		$result = array(Consts::RESULT => Consts::SUCCESS);		
	} else {
		$result = array(Consts::RESULT => Consts::REQUEST_RROFILE_UPDATE_FAIL);						
	}
	$json = json_encode($result);
	echo $json;	
?>