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
	
	$json_str = $_POST['json'];
	if (empty($json_str)) {
		$json = ExceptionJson::jsonRequestParamsFail();
		echo $json;	
		return;
	}
	$json = json_decode($json_str);
	
	//{"data":[{"program":"플래시","pk":0,"level":1},{"program":"에디트플러스","pk":0,"level":2}],"count":2}
	$db = DataBase::global_instance();
	foreach($json->data as $key => $json_arr) {
		
		//PK 0인경우 추가
		if ($json_arr->pk == 0) {
			$id = $db->addAbility($_SESSION['ID'], $json_arr->program, $json_arr->level);
			$id_arr[] = $id;
		} else {
			$result = $db->setAbility($_SESSION['ID'], $json_arr->pk, $json_arr->program, $json_arr->level);
			$id_arr[] = $json_arr->pk;
		}
	}
	$id_arr = implode(',', $id_arr);
	$db->deleteAbility($_SESSION['ID'], $id_arr);
	$abilityArr = $db->getAbility($_SESSION['ID']);
	if ($abilityArr) {
		$arr = array(Consts::RESULT => Consts::SUCCESS, "abilityInfo" => $abilityArr);	
	} else {
		$arr = array(Consts::RESULT => Consts::DATA_NULL);	
	}
	$json = json_encode($arr);
	echo $json;
?>