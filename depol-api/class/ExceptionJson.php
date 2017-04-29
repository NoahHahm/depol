<?PHP
	
class ExceptionJson {
	
	public static function jsonFail() {
		$arr = array(Consts::RESULT => "fail");
		$json = json_encode($arr);
		return $json;		
	}
			
	public static function jsonDuplicationFail() {
		$arr = array(Consts::RESULT => Consts::REGISTER_DUPLICATION);
		$json = json_encode($arr);
		return $json;		
	}
	
	public static function jsonNonExistentFail() {
		$arr = array(Consts::RESULT => Consts::REGISTER_NONEXISTENT);
		$json = json_encode($arr);
		return $json;		
	}	
	
	public static function jsonPasswordFail() {
		$arr = array(Consts::RESULT => Consts::LOGIN_PASSWORD_FAIL);
		$json = json_encode($arr);
		return $json;		
	}	
	
	public static function jsonSessionFail() {
		$arr = array(Consts::RESULT => Consts::LOGIN_SESSION_FAIL);
		$json = json_encode($arr);
		return $json;		
	}
	
	public static function jsonPortfolioListEmptyFail() {
		$arr = array(Consts::RESULT => Consts::MAIN_PORTFOLIO_DATA_NULL);
		$json = json_encode($arr);
		return $json;		
	}
	
	
	public static function jsonLikeExistentFail() {
		$arr = array(Consts::RESULT => Consts::LIKE_EXISTENT);
		$json = json_encode($arr);
		return $json;		
	}
		
	public static function jsonUnLikeFail() {
		$arr = array(Consts::RESULT => Consts::UNLIKE_FAIL);
		$json = json_encode($arr);
		return $json;		
	}
		
	public static function jsonRequestParamsFail() {
		$arr = array(Consts::RESULT => Consts::REQUEST_PARAMS_FAIL);
		$json = json_encode($arr);
		return $json;		
	}
		
	public static function jsonCommentListEmptyFail() {
		$arr = array(Consts::RESULT => Consts::PORTFOLIO_COMMENT_DATA_NULL);
		$json = json_encode($arr);
		return $json;		
	}
	
	
	public static function jsonExtensionFail() {
		$arr = array(Consts::RESULT => Consts::REQUEST_EXTENSION_FAIL);
		$json = json_encode($arr);
		return $json;		
	}	
		
}
?>