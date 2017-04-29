<?PHP
	session_start();
	
	require_once ("class/DataBase.php");
	require_once ("class/ExceptionJson.php");
	require_once ("class/Config.php");
	require_once ("class/Consts.php");
	
	header("Content-Type: application/json; charset=UTF-8");
		
	$email = $_POST['email'];
	$pass = $_POST['password'];
	
	if (empty($email) || empty($pass)) {
		$json = ExceptionJson::jsonFail();
		echo $json;
		return;	
	}
		
	//데이터베이스 연결
	$db = DataBase::global_instance();
	$result = $db->Login($email, $pass);	
	if (!is_numeric($result)) {
		echo $result;	
		return;
	}
	
	$result = $db->getUserInfo($result, Consts::REQUEST_LOGIN);
	$json = json_encode($result);
	$_SESSION['ID'] = $result['userId'];
	echo $json;
	
?>