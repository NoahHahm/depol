<?php


	
class DataBase {	
	
	protected $host_name = "localhost";
	protected $user_name = "";
	protected $user_password = "";
	protected $db_name = "";
	protected $conn;
	protected static $instance = null;
	
	private function __construct() {			
		$this->conn = mysqli_connect($this->host_name, $this->user_name, $this->user_password);
		mysqli_select_db($this->conn, $this->db_name);
		mysqli_set_charset($this->conn, "utf8");
		
    }
     
    public static function global_instance() {
        if($instance === null) {
            $instance = new DataBase();
        }         
        return $instance;
    }
	
	public function Close() {
		mysql_close($this->conn);
	}	
	
	//페이스북 회원가입
	public function RegisterFacebook($userId, $email, $name) {
		
		$result = $this->CheckFacebookId($userId);
		
		//페이스북 사용자 존재
		if ($result) {
			return FALSE;	
		} else {
			$result = @mysqli_query($this->conn, "INSERT INTO `depol_member` VALUES (NULL, IF('{$email}' = '', NULL, '{$email}'), '{$name}', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, {$userId}, NULL)");
			@mysqli_free_result($result);
			return $result;
		}
	}
	
	//카카오톡 회원가입
	public function RegisterKakao($kakao_id, $name, $thumbnail_image) {
	
		$result = $this->CheckKakaoId($kakao_id);
		
		//카카오톡 사용자 존재
		if ($result) {
			return FALSE;	
		} else {
			$result = @mysqli_query($this->conn, "INSERT INTO `depol_member` VALUES (NULL, IF('{$email}' = '', NULL, '{$email}'), '{$name}', NULL, IF('{$thumbnail_image}' = '', NULL, '{$thumbnail_image}'), NULL, NULL, NULL, NULL, NULL, NULL, NULL, {$kakao_id})");
			@mysqli_free_result($result);
			return $result;
		}
	}
			
	//일반 회원가입
	public function Register($email, $name, $password) {		
		$result = $this->CheckEmail($email);
		
		//이메일 사용자 존재
		if ($result) {
			return FALSE;	
		} else {
			$query = @mysqli_query($this->conn, "INSERT INTO `depol_member` VALUES (NULL, '{$email}', '{$name}', MD5('{$password}'), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL)");
			return $query;
		}
	}
	
	
	//로그인
	public function Login($email, $password) {
		
		$result = $this->CheckEmail($email);
		if (!$result) {
			$json = ExceptionJson::jsonNonExistentFail();
			return $json;	
		}
		
		$result = $this->getUserId($email, $password);
		if ($result) {
			return $result;
		} else {
			$json = ExceptionJson::jsonPasswordFail();
			return $json;
		}	
	}
	
	//사용자 ID 얻어옴
	private function getUserId($email, $password) {
		
		$query = "SELECT `user_id` FROM `depol_member` WHERE `user_email` = '{$email}' AND `user_pass` = MD5('{$password}')";
		$result = mysqli_query($this->conn, $query);
		$rows = @mysqli_fetch_array($result, MYSQLI_NUM);
    	@mysqli_free_result($result);
		return (int)$rows[0];
	}
	
	//페이스북 존재 검사
	private function CheckFacebookId($userId) {
		
		$query = "SELECT * FROM `depol_member` WHERE `facebook_id` = {$userId}";

		$result = mysqli_query($this->conn, $query);
		$row_cnt = mysqli_num_rows($result);
    	@mysqli_free_result($result);
		return $row_cnt;
	}
	
	//카카오 존재 검사
	private function CheckKakaoId($kakao_id) {
		
		$query = "SELECT * FROM `depol_member` WHERE `kakao_id` = {$kakao_id}";

		$result = mysqli_query($this->conn, $query);
		$row_cnt = @mysqli_num_rows($result);
    	@mysqli_free_result($result);
		return $row_cnt;
	}
		
	//이메일 존재 검사
	public function CheckEmail($email) {
		
		$query = "SELECT * FROM `depol_member` WHERE `user_email` = '{$email}'";
		$result = mysqli_query($this->conn, $query);
		$row_cnt = mysqli_num_rows($result);
    	@mysqli_free_result($result);
		return $row_cnt;
	}
	
	//사용자 정보
	public function getUserInfo($userId, $type) {
		
		if ($type == Consts::REQUEST_FACEBOOK_LOGIN) {
			$result = @mysqli_query($this->conn, "SELECT * FROM `depol_member` WHERE `facebook_id` = {$userId}");
		}
		else if ($type == Consts::REQUEST_LOGIN) {
			$result = @mysqli_query($this->conn, "SELECT * FROM `depol_member` WHERE `user_id` = {$userId}");
		}
		else if ($type == Consts::REQUEST_KAKAO_LOGIN) {
			$result = @mysqli_query($this->conn, "SELECT * FROM `depol_member` WHERE `kakao_id` = {$userId}");
		}		
		
		$rows = @mysqli_fetch_array($result, MYSQLI_ASSOC);		
		if ($rows['user_url'] == NULL) {
			$rows['user_url'] = Config::EMPTY_IMAGE_PATH;			
		}
		$arr = array(
					"result" => "SUCCESS",
					"userId" => (int)$rows['user_id'], 
					"userEmail" => $rows['user_email'],
					"userName" => $rows['user_name'],
					"userRecruitStatus" => (int)$rows['user_status'],
					"userPropicUri" => Config::SERVER_DOMAIN."/".$rows['user_url'],
					"userPosition" => $rows['user_job_position']				
		);
	
		
		@mysqli_free_result($result);
		return $arr;	
	}
	
	
	//포트폴리오 리스트
	public function getPortfolioList($userId, $tag, $page) {
		
		switch ($tag) {
			case Consts::REQUEST_PORTFOLIO_NEW:
			$query = "SELECT	M.`user_name`, P.`portfolio_id`, P.`title`, P.`thumbnail_url`, 
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS likeNum,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS commentNum,
								IF((SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id` AND `user_id` = {$userId}) > 0 , 1, 0) AS isLiked
						FROM	`depol_portfolio` AS P LEFT JOIN `depol_member` AS M ON P.`user_id` = M.`user_id`
					ORDER BY	P.`creation_time` DESC";
				break;	
			case Consts::REQUEST_PORTFOLIO_BEST:
			$query = "SELECT	M.`user_name`, P.`portfolio_id`, P.`title`, P.`thumbnail_url`, 
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS likeNum,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS commentNum,
								IF((SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id` AND `user_id` = {$userId}) > 0 , 1, 0) AS isLiked
						FROM	`depol_portfolio` AS P LEFT JOIN `depol_member` AS M ON P.`user_id` = M.`user_id`
					ORDER BY	likeNum DESC";
				break;
			case Consts::REQUEST_PORTFOLIO_FOLLOW:
			$query = "SELECT	M.`user_name`, P.`portfolio_id`, P.`title`, P.`thumbnail_url`,
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS likeNum,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS commentNum,
								IF((SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id` AND `user_id` = {$userId}) > 0 , 1, 0) AS isLiked
						FROM	`depol_follow` AS U LEFT JOIN  `depol_portfolio` AS P ON U.`follow_user_id` = P.`user_id` LEFT JOIN `depol_member` AS M ON U.`follow_user_id` = M.`user_id`
						WHERE	U.`user_id` = {$userId}";
				break;
			default: 
				return FALSE;
		}
		

		$result = @mysqli_query($this->conn, $query);
		
		if (mysqli_num_rows($result)) {
			while ($rows = @mysqli_fetch_array($result))
			{
				$arr[] = array(
					"userName" => $rows['user_name'],
					"thumbPofolId" => (int)$rows['portfolio_id'],
					"pofolTitle" => $rows['title'],
					"thumbImgUri" => Config::SERVER_DOMAIN."/".$rows['thumbnail_url'],
					"likeNum" => (int)$rows['likeNum'],	
					"commentNum" => (int)$rows['commentNum'],
					"isLiked" => (int)$rows['isLiked']
				);
			}
			return $arr;
		} else {
			return FALSE;	
		}					
	}
	

	
	//포트폴리오 정보 (단일)
	public function getPortfolioInfo($userId, $portfolioId) {
		
		$query = "SELECT	P.`portfolio_id`, P.`user_id`, M.`user_name`, M.`user_url`, M.`user_status`, P.`title`, P.`context`, P.`thumbnail_url`,
							(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS likeNum,
							(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS commentNum,
							IF((SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id` AND `user_id` = {$userId}) > 0 , 1, 0) AS isLiked
					FROM	`depol_member` AS M LEFT JOIN `depol_portfolio` AS P ON M.`user_id` = P.`user_id`  
					WHERE	P.`portfolio_id` = {$portfolioId}";	

		$result = @mysqli_query($this->conn, $query);
		
		if (mysqli_num_rows($result)) {
			$rows = @mysqli_fetch_array($result, MYSQLI_ASSOC);
			if ($rows['user_url'] == NULL) {
				$rows['user_url'] = Config::EMPTY_IMAGE_PATH;			
			}
			return $rows;			
		} else {
			return FALSE;	
		}
	}
	
	
	//포트폴리오 리스트
	public function getUserPortfolioList($userId) {
		
		$query = "SELECT	P.`portfolio_id`, P.`user_id`, M.`user_name`, P.`title`, P.`thumbnail_url`, P.`web_thumbnail_url`,
							(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS like_count,
							(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS comment_count,
							IF((SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id` AND `user_id` = {$userId}) > 0 , 1, 0) AS isLiked
					FROM	`depol_member` AS M LEFT JOIN `depol_portfolio` AS P ON M.`user_id` = P.`user_id`  
					WHERE	P.`user_id` = {$userId}";	
					
		$result = @mysqli_query($this->conn, $query);
		
		if (mysqli_num_rows($result)) {
			while ($rows = @mysqli_fetch_array($result, MYSQLI_ASSOC))
			{
				$arr[] = array(
					"userName" => $rows['user_name'],
					"thumbPofolId" => (int)$rows['portfolio_id'],
					"thumbImgUri" => Config::SERVER_DOMAIN."/".$rows['thumbnail_url'],
					"webThumbImgUri" => $rows['web_thumbnail_url'],
					"pofolTitle" => $rows['title'],	
					"likeNum" => (int)$rows['like_count'],	
					"commentNum" => (int)$rows['comment_count'],
					"isLiked" => (int)$rows['isLiked']
				);
			}
			return $arr;
		} else {
			return FALSE;	
		}	
	}
	
	//포트폴리오 이미지 리스트
	public function getPortfolioImageList($portfolioId) {
		
		$query = "SELECT `image_url` FROM `depol_portfolio_images` WHERE `portfolio_id` = {$portfolioId} ORDER BY `image_id` ASC";	
		$result = @mysqli_query($this->conn, $query);
		
		if (mysqli_num_rows($result)) {
			$arr = array();
			while ($rows = @mysqli_fetch_array($result, MYSQLI_ASSOC))
			{
				array_push($arr, Config::SERVER_DOMAIN."/".$rows['image_url']);
			}
			return $arr;		
		} else {
			return FALSE;	
		}
	}
	
	//라이크
	public function addLike($user_id, $portfolio_id) {
		
		$result = $this->checkLike($user_id, $portfolio_id);
		if ($result) {
			return FALSE;
		}
		
		$query = "INSERT INTO `depol_like` VALUES (NULL, '{$user_id}', '{$portfolio_id}', '0')";
		$result = @mysqli_query($this->conn, $query);
    	@mysqli_free_result($result);
		return $result;
	}
	
	
	//언라이크
	public function unLike($user_id, $portfolio_id) {
		
		$result = $this->checkLike($user_id, $portfolio_id);
		if (!$result) {
			return FALSE;
		}
		
		$query = "DELETE FROM `depol_like` WHERE `user_id` = {$user_id} AND `portfolio_id` = {$portfolio_id}";
		$result = @mysqli_query($this->conn, $query);
		return $result;
	}
		
	//라이크 체크
	public function checkLike($userId, $portfolioId) {		
		$query = "SELECT * FROM `depol_like` WHERE `user_id` = {$userId} AND `portfolio_id` = {$portfolioId}";
		$result = mysqli_query($this->conn, $query);
		$row_cnt = mysqli_num_rows($result);
    	@mysqli_free_result($result);
		return $row_cnt;
	}
	
	//라이크 히스토리 리스트
	public function getLikeHistoryList($id, $type) {
		if ($type == Consts::REQUEST_USER_LIKE_HISTORY) {
			
			$query = "SELECT M.`user_id`,
							 M.`user_name`,
							 M.`user_url`,
							 M.`user_status`,
							 M.`user_job_position`,
							 L.`like_id`,
							 L.`is_read`,
							 L.`portfolio_id`,
							 P.`thumbnail_url` 
						FROM `depol_member` AS M 
							LEFT JOIN `depol_like` AS L ON M.`user_id` = L.`user_id` 
							LEFT JOIN `depol_portfolio` AS P ON P.`portfolio_id` = L.`portfolio_id` 
						WHERE L.`like_id` IS NOT NULL AND P.`portfolio_id` = {$id}";
						
		} else if ($type == Consts::REQUEST_LIKE_HISTORY) {
			$query = "SELECT M.`user_id`,
							 M.`user_name`,
							 M.`user_url`,
							 M.`user_status`,
							 M.`user_job_position`,
							 L.`like_id`,
							 L.`is_read`,
							 L.`portfolio_id`,
							 P.`thumbnail_url`
						FROM `depol_member` AS M
						    LEFT JOIN `depol_like` AS L ON M.`user_id` = L.`user_id` 
							LEFT JOIN `depol_portfolio` AS P ON P.`portfolio_id` = L.`portfolio_id`
						WHERE L.`like_id` IS NOT NULL AND P.`user_id` = {$id}";
		}
			
		$result = @mysqli_query($this->conn, $query);
		
		if (mysqli_num_rows($result)) {
			while ($rows = @mysqli_fetch_array($result, MYSQLI_ASSOC))
			{
				
				if ($rows['user_url'] == NULL) {
					$rows['user_url'] = Config::EMPTY_IMAGE_PATH;			
				}
				
				$arr[] = array(
					"userId" => (int)$rows['user_id'], 
					"likeId" => (int)$rows['like_id'],
					"pofolId" => (int)$rows['portfolio_id'], 
					"userName" => $rows['user_name'],
					"userPosition" => $rows['user_job_position'], 
					"userPropicUri" => Config::SERVER_DOMAIN."/".$rows['user_url'], 
					"userRecruitStatus" => (int)$rows['user_status'], 
					"thumbImgUri" => Config::SERVER_DOMAIN."/".$rows['thumbnail_url'],
					"isRead" => (int)$rows['is_read']
				);
			}
			return $arr;		
		} else {
			return FALSE;
		}
	
	}	
	
	//라이크 읽음
	public function readLike($like_id) {		
		$query = "UPDATE `depol_like` SET `is_read` = '1' WHERE `like_id` = {$like_id}";
		$result = mysqli_query($this->conn, $query);
		return $result;
	}
	
	//댓글 리스트
	public function getCommentList($portfolioId) {
		
		$query = "SELECT	C.`comment_id`, M.`user_id`, M.`user_name`, M.`user_url`, M.`user_status`,C.`context`, C.`creation_time`
					FROM	`depol_member` AS M LEFT JOIN `depol_comments` AS C ON M.`user_id` = C.`user_id`
					WHERE	C.`comment_id` IS NOT NULL AND C.`portfolio_id` = {$portfolioId}";
					
		$result = @mysqli_query($this->conn, $query);
		
		if (mysqli_num_rows($result)) {
			
			while ($rows = @mysqli_fetch_array($result, MYSQLI_ASSOC))
			{
				if ($rows['user_url'] == NULL) {
					$rows['user_url'] = Config::EMPTY_IMAGE_PATH;			
				}
				$arr[] = array(
					"commentId" => (int)$rows['comment_id'],
					"userId" => (int)$rows['user_id'],
					"userName" => $rows['user_name'],
					"userPropicUri" => Config::SERVER_DOMAIN."/".$rows['user_url'],
					"userRecruitStatus" => (int)$rows['user_status'],	
					"text" => $rows['context'],
					"timeStamp" => $rows['creation_time']
				);
			}
    		@mysqli_free_result($result);
			return $arr;		
		} else {
    		@mysqli_free_result($result);
			return FALSE;	
		}
	}


	//댓글 추가
	public function commentWrite($userId, $portfolioId, $context) {
		
		$query = "INSERT INTO `depol_comments` VALUES (NULL , '{$userId}', '{$portfolioId}', '{$context}', NOW())";
		$result = @mysqli_query($this->conn, $query);
    	@mysqli_free_result($result);
    	return $query;
	}
	
	
	//댓글 삭제
	public function commentRemove($commentId) {
		
		$result = $this->checkComment($commentId);
		if (!$result) {
			return FALSE;	
		}
		
		$query = "DELETE FROM `depol_comments` WHERE `comment_id` = {$commentId}";
		$result = @mysqli_query($this->conn, $query);
    	@mysqli_free_result($result);
    	return $query;
	}
	
	
	//리플 체크
	public function checkComment($commentId) {
		
		$query = "SELECT * FROM `depol_comments` WHERE `comment_id` = {$commentId}";
		$result = mysqli_query($this->conn, $query);
		$row_cnt = mysqli_num_rows($result);
    	@mysqli_free_result($result);
		return $row_cnt;
	}
		
	//마이페이지
	public function getMyPageUserInfo($userId) {
		
		$query = "SELECT	M.`user_id`, M.`user_name`, M.`user_job_position`, M.`user_url`, M.`user_status`, 
							(SELECT COUNT(*) FROM `depol_follow` WHERE `follow_user_id` = M.`user_id`) AS follower_count,
							(SELECT COUNT(*) FROM `depol_follow` WHERE `user_id` = M.`user_id`) AS following_count,
							(SELECT IF(COUNT(`follow_id`) > 0, 1, 0) FROM `depol_follow` WHERE `follow_user_id` = {$userId}) AS isFollow
					FROM	`depol_member` AS M
					WHERE	M.`user_id` = {$userId}";
		
		$result = @mysqli_query($this->conn, $query);
		
		if (mysqli_num_rows($result)) {
			$rows = @mysqli_fetch_array($result, MYSQLI_ASSOC);
			if ($rows['user_url'] == NULL) {
				$rows['user_url'] = Config::EMPTY_IMAGE_PATH;			
			}
			return $rows;			
		} else {
			return FALSE;
		}
	}	
	
	//포트폴리오 삭제
	public function removePortfolio($id) {
		$query = "DELETE FROM `depol_portfolio` WHERE `portfolio_id` = {$id}";		
		$result = @mysqli_query($this->conn, $query);
		return $result;
	}
	
	
	//기술능력 정보
	public function getAbility($userId) {
		
		$query = "SELECT * FROM `depol_member_abilitys` WHERE `user_id` = {$userId}";
		
		$result = @mysqli_query($this->conn, $query);
		
		if (mysqli_num_rows($result)) {
			$arr = array(array());
			$i = 0;
			while ($rows = @mysqli_fetch_array($result, MYSQLI_ASSOC))
			{
				$arr[$i] = array(
					"pk" => (int)$rows['ability_id'],
					"program" => $rows['context'],	
					"level" => (int)$rows['level']
				);
				$i++;
			}
			return $arr;		
		} else {
			return FALSE;
		}
	}
	
	
	//커리어 정보
	public function getCareer($userId) {
		
		$query = "SELECT * FROM `depol_member_careers` WHERE `user_id` = {$userId}";
		
		$result = @mysqli_query($this->conn, $query);
		
		if (mysqli_num_rows($result)) {
			$arr = array(array());
			$i = 0;
			while ($rows = @mysqli_fetch_array($result, MYSQLI_ASSOC))
			{
				$startDate = explode('-', $rows['start_date']);
				$endDate = explode('-', $rows['end_date']);
				$arr[$i] = array(
					"pk" => (int)$rows['career_id'],				
					"startYear" => (int)$startDate[0],
					"startMonth" => (int)$startDate[1],
					"endYear" => (int)$endDate[0],
					"endMonth" => (int)$endDate[1],
					"text" => $rows['context'],	
					"subText" => $rows['sub_context']
				);
				$i++;
			}
			return $arr;
		} else {
			return FALSE;
		}
	}
		
	//자격증 정보
	public function getCertification($user_id) {
		
		$query = "SELECT * FROM `depol_member_certifications` WHERE `user_id` = {$user_id}";
		
		$result = @mysqli_query($this->conn, $query);
		
		if (mysqli_num_rows($result)) {
			while ($rows = @mysqli_fetch_array($result, MYSQLI_ASSOC))
			{
				$arr[] = array(
					"pk" => (int)$rows['certification_id'],
					"text" => $rows['context']
				);
			}
			return $arr;			
		} else {
			return FALSE;
		}
	}
	
	
	//학력 정보
	public function getEducation($user_id) {
		
		$query = "SELECT * FROM `depol_member_educations` WHERE `user_id` = {$user_id}";
		
		$result = @mysqli_query($this->conn, $query);
		
		if (mysqli_num_rows($result)) {
			while ($rows = @mysqli_fetch_array($result, MYSQLI_ASSOC))
			{
				$startDate = explode('-', $rows['start_date']);
				$endDate = explode('-', $rows['end_date']);
				$arr[] = array(
					"pk" => (int)$rows['education_id'],				
					"startYear" => (int)$startDate[0],
					"startMonth" => (int)$startDate[1],
					"endYear" => (int)$endDate[0],
					"endMonth" => (int)$endDate[1],
					"text" => $rows['context'],	
					"subText" => $rows['sub_context']
				);
			}
			return $arr;		
		} else {
			return FALSE;
		}
	}
	
	//수상 정보
	public function getAward($user_id) {
		
		$query = "SELECT * FROM `depol_member_awards` WHERE `user_id` = {$user_id}";
		
		$result = @mysqli_query($this->conn, $query);
		
		if (mysqli_num_rows($result)) {
			while ($rows = @mysqli_fetch_array($result, MYSQLI_ASSOC))
			{
				$arr[] = array(
					"pk" => (int)$rows['award_id'],				
					"year" => (int)$rows['award_year'],
					"text" => $rows['context'],	
					"subText" => $rows['sub_context']
				);
			}
			return $arr;		
		} else {
			return FALSE;
		}
	}
	
	//사용자 부가 정보
	public function getUserSubInfo($userId) {
		
		$query = "SELECT	`user_address`, `user_birth_date`, `user_sub_email`, `user_website`
					FROM	`depol_member` 
					WHERE	`user_id` = {$userId}";
		
		$result = @mysqli_query($this->conn, $query);
		
		if (mysqli_num_rows($result)) {
			$rows = @mysqli_fetch_array($result, MYSQLI_ASSOC);			
			$arr = array(
					"email" => $rows['user_sub_email'],				
					"website" => $rows['user_website'],
					"location" => $rows['user_address'],	
					"userName" => $rows['user_name'],	
					"userPosition" => $rows['user_job_position'],	
					"birth" => $rows['user_birth_date']
			);
			return $arr;		
		} else {
			return FALSE;
		}
	}
	
	
	//팔로우
	public function addFollow($userId, $followUserId) {
		$result = $this->checkFollow($userId, $followUserId);
		if ($result) {
			return FALSE;	
		}			
		$query = "INSERT INTO `depol_follow` VALUES (NULL , '{$userId}', '{$followUserId}')";
		$result = @mysqli_query($this->conn, $query);
		return $result;
	}	
	
	//언팔로우
	public function unFollow($userId, $followUserId) {
		$query = "DELETE FROM `depol_follow` WHERE `user_id` = {$userId} AND `follow_user_id` = {$followUserId}";
		$result = @mysqli_query($this->conn, $query);
		return $result;
	}
	
	//팔로우 체크
	public function checkFollow($userId, $followUserId) {		
		$query = "SELECT `user_id` FROM `depol_follow` WHERE `user_id` = {$userId} AND `follow_user_id` = {$followUserId}";
		$result = mysqli_query($this->conn, $query);
		$row_cnt = mysqli_num_rows($result);
    	@mysqli_free_result($result);
		return $row_cnt;
	}
	
	//팔로워 리스트
	public function getFollowList($userId, $type) {
		
		if ($type == Consts::REQUEST_FOLLOWER) {
			$query = "SELECT	M.`user_id` , M.`user_name` , M.`user_status` , M.`user_job_position`, M.`user_url`
						FROM	`depol_follow` AS F LEFT JOIN `depol_member` AS M ON F.`user_id` = M.`user_id`
						WHERE	`follow_user_id` = {$userId}";			
		} else if ($type == Consts::REQUEST_FOLLOWING) {
			$query = "SELECT	M.`user_id` , M.`user_name` , M.`user_status` , M.`user_job_position` , M.`user_url` 
						FROM	`depol_follow` AS F LEFT JOIN `depol_member` AS M ON F.`follow_user_id` = M.`user_id` 
						WHERE	F.`user_id` = {$userId}";
		}
		
		$result = @mysqli_query($this->conn, $query);
		
		if (mysqli_num_rows($result)) {
			while ($rows = @mysqli_fetch_array($result, MYSQLI_ASSOC))
			{
				if ($rows['user_url'] == NULL) {
					$rows['user_url'] = Config::EMPTY_IMAGE_PATH;			
				}
				$arr[] = array(
					"userId" => (int)$rows['user_id'], 
					"userRecruitStatus" => (int)$rows['user_status'],
					"userName" => $rows['user_name'],
					"userPosition" => $rows['user_job_position'], 
					"userPropicUri" => Config::SERVER_DOMAIN."/".$rows['user_url']
				);
			}
			return $arr;		
		} else {
			return FALSE;
		}
	}	
	
	//포트폴리오 추가
	public function addPortfolio($userId, $title, $context, $thumbnail_path, $web_thumbnail_path, $image_path, $category) {
								
		mysqli_autocommit($this->conn, FALSE);
		$query = "INSERT INTO `depol_portfolio` VALUES (NULL , '{$userId}', '{$title}', '{$context}', NOW(), '{$thumbnail_path}', '{$web_thumbnail_path}')";
		
		$result = @mysqli_query($this->conn, $query);
		$id = mysqli_insert_id($this->conn);
		
		if ($result) {
			
			foreach($category as $value) {
				$result = $this->addPortfolioCategory($id, $value);
				if (!$result) {
					mysqli_rollback($this->conn);	
					return FALSE;
				}			
			}
			
			foreach($image_path as $path) {	
				$result = $this->addPortfolioImage($id, $path);
				if (!$result) {
					mysqli_rollback($this->conn);	
					return FALSE;
				}
			}			
			
			mysqli_commit($this->conn);	
			mysqli_autocommit($this->conn, TRUE);
			return TRUE;		
		}
		return FALSE;
	}	
	
	
	//포트폴리오 카테고리 추가
	public function addPortfolioCategory($portfolio_id, $category_id) {
		$query = "INSERT INTO `depol_portfolio_categorys` VALUES (NULL, '{$portfolio_id}', '{$category_id}')";
		$result = @mysqli_query($this->conn, $query);
		return $result;
	}	
	
	//포트폴리오 이미지 추가
	public function addPortfolioImage($portfolio_id, $image_path) {
		$query = "INSERT INTO `depol_portfolio_images` VALUES (NULL, '{$portfolio_id}', '{$image_path}')";
		$result = @mysqli_query($this->conn, $query);
		return $result;
	}
	
	
	//포트폴리오 수정
	public function modifyPortfolio($portfolio_id, $title, $context, $thumbnail_path, $web_thumbnail_path, $image_path, $category) {
		mysqli_autocommit($this->conn, FALSE);
		$query = "UPDATE `depol_portfolio` SET `title` = '{$title}', `context` = '{$context}', `thumbnail_url` = '{$thumbnail_path}', `web_thumbnail_url` = '{$web_thumbnail_path}' WHERE `portfolio_id` = {$portfolio_id}";
		$result = @mysqli_query($this->conn, $query);
		
		if ($result) {	
			$result = $this->removeCategorysImages($portfolio_id);
			if (!$result) {
				mysqli_rollback($this->conn);	
				return FALSE;
			}			
					
			foreach($category as $value) {				
				$result = $this->addPortfolioCategory($portfolio_id, $value);
				if (!$result) {
					mysqli_rollback($this->conn);	
					return FALSE;
				}			
			}
			
			foreach($image_path as $path) {	
				$result = $this->addPortfolioImage($portfolio_id, $path);
				if (!$result) {
					mysqli_rollback($this->conn);	
					return FALSE;
				}
			}
			mysqli_commit($this->conn);	
			mysqli_autocommit($this->conn, TRUE);
			return TRUE;		
		}
		return FALSE;
	}
	
	//이미지, 카테고리 삭제
	public function removeCategorysImages($portfolio_id) {
		$query = "DELETE	`depol_portfolio_categorys`, `depol_portfolio_images`
					FROM	`depol_portfolio` LEFT JOIN `depol_portfolio_categorys` ON `depol_portfolio`.`portfolio_id` = `depol_portfolio_categorys`.`portfolio_id`
							LEFT JOIN `depol_portfolio_images` ON `depol_portfolio`.`portfolio_id` = `depol_portfolio_images`.`portfolio_id`
					WHERE `depol_portfolio`.`portfolio_id` = {$portfolio_id}";	
		$result = @mysqli_query($this->conn, $query);
		return $result;
	}
	
	//이미지 패스 경로
	public function getImagesPath($portfolio_id) {
		
		$query = "SELECT `image_url` FROM `depol_portfolio_images` WHERE `portfolio_id` = {$portfolio_id}";
		$result = @mysqli_query($this->conn, $query);
		
		$arr = array();
		if (mysqli_num_rows($result)) {
			while ($rows = @mysqli_fetch_array($result, MYSQLI_ASSOC))
			{
				array_push($arr, $rows['image_url']);
			}		
		}		
		
		$query = "SELECT `thumbnail_url`, `web_thumbnail_url` FROM `depol_portfolio` WHERE `portfolio_id` = {$portfolio_id}";
		$result = @mysqli_query($this->conn, $query);		
		if (mysqli_num_rows($result)) {
			$rows = @mysqli_fetch_array($result, MYSQLI_ASSOC);
			array_push($arr, $rows['thumbnail_url']);
			array_push($arr, $rows['web_thumbnail_url']);
		}	
		
		if (COUNT($arr) <= 0) {
			return FALSE;
		}			
		
		return $arr;
	}	
	
	public function getSearchList($user_id, $keyword, $category) {
			
		foreach($category as $key => $value) {
			if ($key == 0) {
				$category_append = $value;						
			} else {
				$category_append = $category_append.",".$value;					
			}									
		}
 
		if (empty($keyword) && $category[0] == 0) {
			//모든카테고리
			$query = "SELECT	M.`user_name`, P.`portfolio_id`, P.`title`, P.`thumbnail_url`, 
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS likeNum,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS commentNum,
								IF((SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id` AND `user_id` = {$user_id}) > 0 , 1, 0) AS isLiked
						FROM	`depol_portfolio` AS P LEFT JOIN `depol_member` AS M ON P.`user_id` = M.`user_id`";			
		} else if (!empty($keyword) && $category[0] == 0) {	
			//키워드 + 모든 카테고리
			$query = "SELECT	M.`user_name`, P.`portfolio_id`, P.`title`, P.`thumbnail_url`, 
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS likeNum,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS commentNum,
								IF((SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id` AND `user_id` = {$user_id}) > 0 , 1, 0) AS isLiked
						FROM	`depol_portfolio` AS P LEFT JOIN `depol_member` AS M ON P.`user_id` = M.`user_id` 
						WHERE	P.`title` LIKE '%{$keyword}%'";					
		} else if (!empty($keyword) && !empty($category)) {
			//키워드 + 특정 카테고리
			$query = "SELECT	M.`user_name`, P.`portfolio_id`, P.`title`, P.`thumbnail_url`, 
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS likeNum,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS commentNum,
								IF((SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id` AND `user_id` = {$user_id}) > 0 , 1, 0) AS isLiked
						FROM	`depol_portfolio` AS P LEFT JOIN `depol_member` AS M ON P.`user_id` = M.`user_id` 
						WHERE	P.`title` LIKE '%{$keyword}%' AND P.`portfolio_id` IN (SELECT `portfolio_id` FROM depol_portfolio_categorys WHERE `category_id` IN ({$category_append}))";
		} else if (empty($keyword) && !empty($category)) {
			//특정 카테고리
			$query = "SELECT	M.`user_name`, P.`portfolio_id`, P.`title`, P.`thumbnail_url`, 
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS likeNum,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS commentNum,
								IF((SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id` AND `user_id` = {$user_id}) > 0 , 1, 0) AS isLiked
						FROM	`depol_portfolio` AS P LEFT JOIN `depol_member` AS M ON P.`user_id` = M.`user_id` 
						WHERE	P.`portfolio_id` IN (SELECT `portfolio_id` FROM depol_portfolio_categorys WHERE `category_id` IN ({$category_append}))";				
		}				
					
					
		$result = @mysqli_query($this->conn, $query);
		if ($result) {
			while ($rows = @mysqli_fetch_array($result))
			{
				$arr[] = array(
					"userName" => $rows['user_name'],
					"thumbPofolId" => (int)$rows['portfolio_id'],
					"pofolTitle" => $rows['title'],
					"thumbImgUri" => Config::SERVER_DOMAIN."/".$rows['thumbnail_url'],
					"likeNum" => (int)$rows['likeNum'],	
					"commentNum" => (int)$rows['commentNum'],
					"isLiked" => (int)$rows['isLiked']
				);
			}
			return $arr;			
		} 
			return FALSE;			
					
	}
		
	public function getMessageList($type, $user_id) {
		
		if ($type == Consts::REQUEST_INBOX) {
			$query =  "SELECT	UM.`user_id`, UM.`user_name`, UM.`user_url`,UM.`user_job_position`, UM.`user_status`, M.`message_id`, M.`context`, M.`creation_time`, M.`is_read`,
								IF((SELECT COUNT(*) FROM `depol_follow` WHERE `follow_user_id` = UM.`user_id`) > 0 , 1, 0) AS is_followed
						FROM	`depol_member` AS UM LEFT JOIN `depol_message` AS M ON `user_id` = `send_user_id`
						WHERE	`received_user_id` = {$user_id}";					
		} else if ($type == Consts::REQUEST_OUTBOX) {
			$query =  "SELECT	UM.`user_id`, UM.`user_name`, UM.`user_url`,UM.`user_job_position`, UM.`user_status`, M.`message_id`, M.`context`, M.`creation_time`, M.`is_read`,
								IF((SELECT COUNT(*) FROM `depol_follow` WHERE `follow_user_id` = UM.`user_id`) > 0 , 1, 0) AS is_followed
						FROM	`depol_member` AS UM LEFT JOIN `depol_message` AS M ON `user_id` = `send_user_id`
						WHERE	`send_user_id` = {$user_id}";				
		}
		
		$result = @mysqli_query($this->conn, $query);
		
		if (mysqli_num_rows($result)) {
			while ($rows = @mysqli_fetch_array($result, MYSQLI_ASSOC))
			{
				if ($rows['user_url'] == NULL) {
					$rows['user_url'] = Config::EMPTY_IMAGE_PATH;			
				}
				
				$arr[] = array(
					"userId" => (int)$rows['user_id'], 
					"messageId" => (int)$rows['message_id'],
					"userName" => $rows['user_name'],
					"userPosition" => $rows['user_job_position'], 
					"userPropicUri" => Config::SERVER_DOMAIN."/".$rows['user_url'], 
					"userRecruitStatus" => (int)$rows['user_status'], 
					"text" => $rows['context'], 
					"timeStamp" => $rows['creation_time'], 
					"isRead" => (int)$rows['is_read'], 
					"isFollowed" => (int)$rows['is_followed']
				);
			}
			return $arr;		
		} else {
			return FALSE;
		}
	}
	
	
	//메세지 읽음
	public function readMessage($message_id) {
		$query = "UPDATE `depol_message` SET `is_read` = '1' WHERE `message_id` = {$message_id}";
		$result = @mysqli_query($this->conn, $query);
		return $result;
	}
	
	//메세지 쓰기
	public function writeMessage($user_id, $receive_user_id, $context) {
		$query = "INSERT INTO `depol_message` VALUES (NULL, '{$user_id}', '{$receive_user_id}', '{$context}', NOW(), '0')";
		$result = @mysqli_query($this->conn, $query);
		return $result;
	}
	
	//메세지 삭제
	public function removeMessage($message_id) {
		$query = "DELETE FROM `depol_message` WHERE `message_id` = {$message_id}";
		$result = @mysqli_query($this->conn, $query);
		return $result;
	}
	
	//사용자 정보 수정
	public function setMemberInfo($user_id, $name, $position, $location, $email, $birth, $website) {
		$query = "UPDATE `depol_member`
					 SET `user_name` = '{$name}',
					 	 `user_job_position` = '{$position}',
					 	 `user_address` = '{$location}',
					 	 `user_birth_date` = '{$birth}',
					 	 `user_sub_email` = '{$email}',
					 	 `user_website` = '{$website}'
				   WHERE `user_id` = {$user_id}";
		$result = @mysqli_query($this->conn, $query);
		return $result;	
	}
	
	//학력 정보 추가
	public function addEducation($user_id, $start_date, $end_date, $context, $sub_text) {
		$query = "INSERT INTO `depol_member_educations` VALUES (NULL, '{$user_id}', '{$start_date}', '{$end_date}', '{$context}', '{$sub_text}')";
		$result = @mysqli_query($this->conn, $query);
		return mysqli_insert_id($this->conn);	
	}
	
	//학력 정보 수정
	public function setEducation($user_id, $education_id, $start_date, $end_date, $context, $sub_text) {
		$query = "UPDATE `depol_member_educations`
					 SET `start_date` = '{$start_date}',
					 	 `end_date` = '{$end_date}',
					 	 `context` = '{$context}',
					 	 `sub_context` = '{$sub_text}'
				   WHERE `education_id` = '{$education_id}'";
		$result = @mysqli_query($this->conn, $query);
		return $result;	
	}

	//학력 정보 삭제
	public function deleteEducation($user_id, $ids) {
		$query = "DELETE FROM `depol_member_educations`
						WHERE `user_id` = {$user_id} AND `education_id` NOT IN ({$ids})";
		$result = @mysqli_query($this->conn, $query);
		return $result;	
	}
	
	//자격증 정보 추가
	public function addCertification($user_id, $context) {
		$query = "INSERT INTO `depol_member_certifications` VALUES (NULL, '{$user_id}', '{$context}')";
		$result = @mysqli_query($this->conn, $query);
		return mysqli_insert_id($this->conn);	
	}	
	
	//자격증 정보 수정
	public function setCertification($certification_id, $context) {
		$query = "UPDATE `depol_member_certifications`
					 SET `context` = '{$context}'
				   WHERE `certification_id` = '{$certification_id}'";
		$result = @mysqli_query($this->conn, $query);
		return $result;	
	}
	
	//자격증 정보 삭제
	public function deleteCertification($user_id, $ids) {
		$query = "DELETE FROM `depol_member_certifications`
						WHERE `user_id` = {$user_id} AND `certification_id` NOT IN ({$ids})";
		$result = @mysqli_query($this->conn, $query);
		return $result;	
	}

	//경력 정보 추가
	public function addCareer($user_id, $start_date, $end_date, $context, $sub_context) {
		$query = "INSERT INTO `depol_member_careers` VALUES (NULL, '{$user_id}', '{$start_date}', '{$end_date}', '{$context}', '{$sub_context}')";
		$result = @mysqli_query($this->conn, $query);
		return mysqli_insert_id($this->conn);	
	}	
	
	//경력 정보 수정
	public function setCareer($user_id, $career_id, $start_date, $end_date, $context, $sub_context) {
		$query = "UPDATE `depol_member_careers`
					 SET `start_date` = '{$start_date}',
					 	 `end_date` = '{$end_date}',
					 	 `context` = '{$context}',
					 	 `sub_context` = '{$sub_context}'
				   WHERE `career_id` = '{$career_id}'";
		$result = @mysqli_query($this->conn, $query);
		return $result;	
	}
	
	//경력 정보 삭제
	public function deleteCareer($user_id, $ids) {
		$query = "DELETE FROM `depol_member_careers`
						WHERE `user_id` = {$user_id} AND `career_id` NOT IN ({$ids})";
		$result = @mysqli_query($this->conn, $query);
		return $result;	
	}
	
	//수상 정보 추가
	public function addAward($user_id, $award_year, $context, $sub_context) {
		$query = "INSERT INTO `depol_member_awards` VALUES (NULL, '{$user_id}', '{$award_year}', '{$context}', '{$sub_context}')";
		$result = @mysqli_query($this->conn, $query);
		return mysqli_insert_id($this->conn);	
	}
	
	//수상 정보 수정
	public function setAward($user_id, $award_id, $award_year, $context, $sub_context) {
		$query = "UPDATE `depol_member_awards`
					 SET `award_year` = '{$award_year}',
					 	 `context` = '{$context}',
					 	 `sub_context` = '{$sub_context}'
				   WHERE `award_id` = '{$award_id}'";
		$result = @mysqli_query($this->conn, $query);
		return $result;	
	}	
	
	//경력 정보 삭제
	public function deleteAward($user_id, $ids) {
		$query = "DELETE FROM `depol_member_awards`
						WHERE `user_id` = {$user_id} AND `award_id` NOT IN ({$ids})";
		$result = @mysqli_query($this->conn, $query);
		return $result;	
	}
	
	
	//스킬 정보 추가
	public function addAbility($user_id, $context, $level) {
		$query = "INSERT INTO `depol_member_abilitys` VALUES (NULL, '{$user_id}', '{$context}', '{$level}')";
		$result = @mysqli_query($this->conn, $query);
		return mysqli_insert_id($this->conn);	
	}
	
	//스킬 정보 수정
	public function setAbility($user_id, $ability_id, $context, $level) {
		$query = "UPDATE `depol_member_abilitys`
					 SET `context` = '{$context}',
					 	 `level` = '{$level}'
				   WHERE `ability_id` = '{$ability_id}'";
		$result = @mysqli_query($this->conn, $query);
		return $result;	
	}	
	
	//스킬 정보 삭제
	public function deleteAbility($user_id, $ids) {
		$query = "DELETE FROM `depol_member_abilitys`
						WHERE `user_id` = {$user_id} AND `ability_id` NOT IN ({$ids})";
		$result = @mysqli_query($this->conn, $query);
		return $result;	
	}
	
	
	//공지사항 리스트
	public function getNoticeList() {
		
		$query = "SELECT * FROM `depol_notice`";
					
		$result = @mysqli_query($this->conn, $query);		
		if (mysqli_num_rows($result)) {
			while ($rows = @mysqli_fetch_array($result, MYSQLI_ASSOC))
			{
				$arr[] = array(
					"noticeId" => (int)$rows['notice_id'],
					"title" => $rows['notice_title'],
					"subTitle" => $rows['notice_sub_title'],
					"text" => $rows['notice_context'],	
					"period" => $rows['notice_create_time']
				);
			}
    		@mysqli_free_result($result);
			return $arr;		
		} else {
    		@mysqli_free_result($result);
			return FALSE;	
		}
	}


	//사용자 프로필 사진 수정
	public function updateProfileImage($user_id, $image_path) {
		$query = "UPDATE `depol_member`
					 SET `user_url` = '{$image_path}'
				   WHERE `user_id` = {$user_id}";
		
		$result = @mysqli_query($this->conn, $query);
		return $result;
	}	
	
	//사용자 프로필 수정
	public function updateProfile($user_id, $user_name, $status, $position) {
		$query = "UPDATE `depol_member`
					 SET `user_name` = '{$user_name}',
					     `user_status` = '{$status}',
					     `user_job_position` = '{$position}'
				   WHERE `user_id` = {$user_id}";
		
		$result = @mysqli_query($this->conn, $query);
		return $result;
	}
		
	//사용자 프로필 수정
	public function updateEmail($user_id, $email) {
		$query = "UPDATE `depol_member`
					 SET `user_email` = '{$email}'
				   WHERE `user_id` = {$user_id}";
		
		$result = @mysqli_query($this->conn, $query);
		return $result;
	}	
	
	//사용자 비밀번호 수정
	public function updatePassword($user_id, $password) {
		$query = "UPDATE `depol_member`
					 SET `user_pass` = MD5('{$password}')
				   WHERE `user_id` = {$user_id}";
		
		$result = @mysqli_query($this->conn, $query);
		return $result;
	}
	
	//사용자 비밀번호 체크
	public function checkPassword($user_id, $password) {
		$query = "SELECT * FROM `depol_member` WHERE `user_id` = {$user_id} AND `user_pass` = MD5('{$password}')";
		$result = mysqli_query($this->conn, $query);
		$row_cnt = mysqli_num_rows($result);
    	@mysqli_free_result($result);
		return $row_cnt;
	}
	
	
}//class 끝
?>