<?PHP
	session_start();
	
	require_once ("class/ExceptionJson.php");
	require_once ("class/Facebook.php");
	require_once ("class/DataBase.php");
	require_once ("class/Config.php");
	require_once ("class/Consts.php");	
	
	header("Content-Type: application/json; charset=UTF-8");
	
	$access_token = $_GET['access_token'];
	if (empty($access_token)) {
		$json = ExceptionJson::jsonRequestParamsFail();
		echo $json;	
		return;
	}
		
	//카카오 서버 연결
	$url = "https://kapi.kakao.com/v1/user/me";
		
	$ch = curl_init();
	curl_setopt($ch, CURLOPT_URL, $url);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
	curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false);
	curl_setopt($ch, CURLOPT_HTTPHEADER, array(
    	'Authorization : Bearer '.$access_token
    ));    
	$result = curl_exec($ch);
    $http_status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
	curl_close($ch);
	$result = json_decode($result);
	
	// Error 200
	if ($http_status != 200) {
		$result = array(Consts::RESULT => Consts::REGISTER_KAKAO_NONEXISTENT);
		$json = json_encode($result);
		echo $json;	
		return;	
	}
	
	$db = DataBase::global_instance();
	$db->RegisterKakao($result->id, $result->properties->nickname, $result->properties->thumbnail_image);
	
	//사용자 데이터 얻어옴
	$result = $db->getUserInfo($result->id, Consts::REQUEST_KAKAO_LOGIN);
	if ($result) {			
		$_SESSION['ID'] = $result['userId'];
		$json = json_encode($result);
		echo $json;
	}
	
?>