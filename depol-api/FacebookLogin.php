<?PHP
	session_start();
	
	require_once ("class/ExceptionJson.php");
	require_once ("class/Facebook.php");
	require_once ("class/DataBase.php");
	require_once ("class/Config.php");
	require_once ("class/Consts.php");
	
	
	header("Content-Type: application/json; charset=UTF-8");
	
	//이메일 미존재
	//CAAUhMCog2m4BAFZBg7rCVeJfv6RIpbmUvpS9kPelu5tHIbIKbm3CCV6kxqvqPv3kesVaaZC7VOWeRyquhfTvtKHB2NEXEWExoTWiQbmF4INx20FdNBgfq35HWWJPglA2yH4K7HcIbMUQmZB6RUGc6uc35vAuKwYuTJnvJThv9S6wZBBmMVQT4OH5MyxOK5JLhL8pbeYBa4PgUEOp0MzH3oZCC83YNK9cPZARrAbYKz0wZDZD
	
	//이메일 존재
	//CAAUhMCog2m4BANA29CwdeElA1heH4Ep80t3QKDQIZCSPnuibAZCc4fjpc8ZAxXaEqkEnLKZADBh2ag5twbrpRX84bgdb6X5NYELnpgvRZBuQXm7Hts4yuAaGNVErbO4Rupbr6JYbfVESXz52qMLnWeJlfbNtbGWVUnhPZCnp5WUS3IDUrPv6UsmjU7WGis36tz0BSh5r6JRSkQNE3lSG1x9JSz2yge5XDTsyRSIZCZBdNgZDZD	
	$access_token = $_POST['access_token'];
	
	$facebook = new Facebook();
	$json = $facebook->getUserJson($access_token);
	if (empty($json->id)) {
		$json = ExceptionJson::jsonFail();
		echo $json;
		return;
	}
	
	//데이터베이스 연결
	$db = DataBase::global_instance();
	$db->RegisterFacebook($json->id, $json->email, $json->name);	
	
	//사용자 데이터 얻어옴
	$result = $db->getUserInfo($json->id, Consts::REQUEST_FACEBOOK_LOGIN);
	if ($result) {			
		$_SESSION['ID'] = $result['userId'];
		$json = json_encode($result);
		echo $json;
	}
	
?>