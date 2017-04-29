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
	if (!is_numeric($id) || empty($id)) {
		$json = ExceptionJson::jsonRequestParamsFail();
		echo $json;	
		return;
	}
		
	//데이터베이스 연결
	$db = DataBase::global_instance();
	$arr = $db->getMyPageUserInfo($id);
	if ($arr) {
		$data = $db->getUserPortfolioList($id);
		if (!$data) {
			$data = array();
		}
		$abilityArr = $db->getAbility($id);
		if (!$abilityArr) {
			$abilityArr = array();
		}
		$careerArr = $db->getCareer($id);
		if (!$careerArr) {
			$careerArr = array();
		}
		$educationArr = $db->getEducation($id);
		if (!$educationArr) {
			$educationArr = array();
		}
		$certificationArr = $db->getCertification($id);
		if (!$certificationArr) {
			$certificationArr = array();
		}
		$awardArr = $db->getAward($id);
		if (!$awardArr) {
			$awardArr = array();
		}
		$userSubArr = $db->getUserSubInfo($id);
		if (!$userSubArr) {
			$userSubArr = array();
		}
		
		$result = array("result" => "SUCCESS", 
						"userId" => (int)$arr['user_id'], 
						"userName" => $arr['user_name'], 
						"userPosition" => $arr['user_job_position'], 
						"userPropicUri" => Config::SERVER_DOMAIN."/".$arr['user_url'], 
						"userRecruitStatus" => (int)$arr['user_status'], 
						"FollowingNum" => (int)$arr['following_count'], 
						"FollowerNum" => (int)$arr['follower_count'],
						"isFollowed" => (int)$arr['isFollow'],
						"isMine" => (int)($_SESSION['ID'] == $arr['user_id']) ? 1 : 0,
						"pofol" => $data,
						"basicInfo" => $userSubArr,
						"certificationInfo" => $certificationArr,	
						"abilityInfo" => $abilityArr,
						"educationInfo" => $educationArr,
						"awardInfo" => $awardArr,
						"workInfo" => $careerArr
						);
		$json = json_encode($result);
		echo $json;
	} else {
		$json = ExceptionJson::jsonPortfolioListEmptyFail();
		echo $json;	
	}
	
?>