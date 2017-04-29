<?PHP

	require_once ("class/Consts.php");
	require_once ("class/DataBase.php");
	require_once ("class/ExceptionJson.php");
	
	header("Content-Type: application/json; charset=UTF-8");
	
	$email = $_POST['email'];
	$name = $_POST['name'];
	$password = $_POST['password'];
	
	if (empty($email) || empty($name) || empty($password)) {
		$json = ExceptionJson::jsonFail();
		echo $json;
		return;	
	}
	
	if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    	$json = ExceptionJson::jsonFail();
		echo $json;
		return;	
	}	
	
	//데이터베이스 연결
	$db = DataBase::global_instance();
	$result = $db->Register($email, $name, $password);		
	if ($result) {
		$arr = array(
			"result" => "success"
		);
		$json = json_encode($arr);
		echo $json;
	} else {
		$json = ExceptionJson::jsonDuplicationFail();
		echo $json;
	}
	
?>