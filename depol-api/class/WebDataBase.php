<?
	require_once("../class/DataBase.php");

class WebDataBase extends DataBase {


	private function __construct() {			
		$this->conn = mysqli_connect($this->host_name, $this->user_name, $this->user_password);
		mysqli_select_db($this->conn, $this->db_name);
		mysqli_set_charset($this->conn, "utf8");		
    }
     
    public static function global_instance() {
        if($instance === null) {
            $instance = new WebDataBase();
        }         
        return $instance;
    }
    
    //포트폴리오 리스트
	public function getPortfolioList($tag, $last_id, $user_id, $page) {
	
		$max_count = 20;
		if (empty($last_id)) {
			$last_id = 0;
		}

		if (empty($page)) {
			$page = 0;
			$start_limit = 0;
		}
		else {
			$start_limit = ($page*$max_count);
		}
		
		//($page*$max_count)-1 : END
		//($page*$max_count)-$max_count START
		//$start = ($page*$max_count)-$max_count;
		//$end = ($page*$max_count)-1;
		
		// 1 (10, 10)
		// 2 (20, 10)
		// 3 (30, 10)
		// 4 (40, 10)
		// 5 (50, 10)
		// ($page*10) - 10;
	
		switch ($tag) {
			case Consts::REQUEST_PORTFOLIO_NEW:
			$query = "SELECT	M.`user_id`,
								M.`user_url`, 
								M.`user_name`, 
								M.`user_job_position`,
								P.`portfolio_id`, 
								P.`title`, 
								P.`thumbnail_url`, 
								P.`web_thumbnail_url`, 
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS like_count,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS comment_count
						FROM	`depol_portfolio` AS P LEFT JOIN `depol_member` AS M ON P.`user_id` = M.`user_id`
						WHERE	P.`portfolio_id` < IF({$last_id} = 0, (SELECT MAX(`portfolio_id`) FROM `depol_portfolio`), {$last_id})
					ORDER BY	P.`creation_time` DESC LIMIT {$max_count}";
					
				break;	
			case Consts::REQUEST_PORTFOLIO_BEST:
			$query = "SELECT	M.`user_id`,
								M.`user_url`, 
								M.`user_name`, 
								M.`user_job_position`,
								P.`portfolio_id`, 
								P.`title`, 
								P.`thumbnail_url`, 
								P.`web_thumbnail_url`, 
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS like_count,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS comment_count
						FROM	`depol_portfolio` AS P LEFT JOIN `depol_member` AS M ON P.`user_id` = M.`user_id`
					ORDER BY	like_count DESC, P.`creation_time` DESC LIMIT {$start_limit}, {$max_count}";
				break;
			case Consts::REQUEST_PORTFOLIO_FOLLOW:
			$query = "SELECT	M.`user_id`,
								M.`user_url`, 
								M.`user_name`, 
								M.`user_job_position`,
								P.`portfolio_id`, 
								P.`title`, 
								P.`thumbnail_url`, 
								P.`web_thumbnail_url`,
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS like_count,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS comment_count
						FROM	`depol_follow` AS U LEFT JOIN  
								`depol_portfolio` AS P ON U.`follow_user_id` = P.`user_id` LEFT JOIN
								`depol_member` AS M ON U.`follow_user_id` = M.`user_id`
						WHERE	U.`user_id` = {$user_id} AND P.`portfolio_id` < IF({$last_id} = 0, (SELECT MAX(`portfolio_id`) FROM `depol_portfolio`), {$last_id}) LIMIT {$max_count}";
				break;
			default: 
				return FALSE;
		}	
		
		$result = @mysqli_query($this->conn, $query);
		if (mysqli_num_rows($result)) {
			while ($rows = @mysqli_fetch_array($result))
			{
				$arr[] = array(
					"user_id" => (int)$rows['user_id'],
					"userName" => $rows['user_name'],
					"userJobPosition" => $rows['user_job_position'],
					"userUrl" => $rows['user_url'],
					"thumbPofolId" => (int)$rows['portfolio_id'],
					"pofolTitle" => $rows['title'],
					"thumbImgUri" => Config::SERVER_DOMAIN."/".$rows['thumbnail_url'],
					"webThumbImgUri" => Config::SERVER_DOMAIN."/".$rows['web_thumbnail_url'],
					"likeNum" => (int)$rows['like_count'],	
					"commentNum" => (int)$rows['comment_count']
				);
			}
			return $arr;
		} else {
			return FALSE;	
		}	
	}	
	
	
	//웹 포트폴리오 정보 (단일)
	public function getPortfolioWebInfo($portfolio_id, $user_id) {
		
		if ($user_id == NULL) { 
			$query = "SELECT	P.`portfolio_id`, P.`user_id`, M.`user_name`, M.`user_url`, M.`user_status`, P.`title`, P.`context`, P.`thumbnail_url`,
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS likeNum,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS commentNum
						FROM	`depol_member` AS M LEFT JOIN `depol_portfolio` AS P ON M.`user_id` = P.`user_id`  
						WHERE	P.`portfolio_id` = {$portfolio_id}";	
		} else {
			$query = "SELECT	P.`portfolio_id`, P.`user_id`, M.`user_name`, M.`user_url`, M.`user_status`, P.`title`, P.`context`, P.`thumbnail_url`,
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS like_count,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS comment_count,
								IF((SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id` AND `user_id` = {$user_id}) > 0 , 1, 0) AS is_liked
						FROM	`depol_member` AS M LEFT JOIN `depol_portfolio` AS P ON M.`user_id` = P.`user_id`  
						WHERE	P.`portfolio_id` = {$portfolio_id}";				
		}

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
	
	public function getCategoryList() {
		$query = "SELECT * FROM `depol_portfolio_category`";
		$result = @mysqli_query($this->conn, $query);
		if (mysqli_num_rows($result)) {
			while ($rows = @mysqli_fetch_array($result))
			{
				$arr[] = array("category_id" => (int)$rows['category_id'], "context" => $rows['context']);
			}
			return $arr;
		} else {
			return FALSE;
		}
	}
	
	//검색
	public function getSearchList($keyword, $category, $sort, $user_id) {
				
		//모든 카테고리 + 특정 키워드 + 인기
		//모든 카테고리 + 특정 키워드 + New		
		//특정 카테고리 + 특정 키워드 + New
		//특정 카테고리 + 특정 키워드 + 인기
		//단일 카테고리
		//단일 소트
		if ($category == 0) $category = null; // 모든 카테고리
		
		
		if (empty($keyword) && $category == null && $sort == Consts::REQUEST_PORTFOLIO_NEW) {	
			//모든 카테고리 + 최신순
			$query = "SELECT	M.`user_name`,
								P.`portfolio_id`, 
								P.`title`, 
								P.`thumbnail_url`, 
								M.`user_job_position`,
								P.`web_thumbnail_url`,
								M.`user_url`,
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS like_count,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS comment_count
						FROM	`depol_portfolio` AS P LEFT JOIN `depol_member` AS M ON P.`user_id` = M.`user_id`
					 ORDER BY	P.`creation_time` DESC";					
		}
		else if (empty($keyword) && $category == null && $sort == Consts::REQUEST_PORTFOLIO_BEST) {	
			//모든 카테고리 + 인기순
			$query = "SELECT	M.`user_name`,
								P.`portfolio_id`, 
								P.`title`, 
								P.`thumbnail_url`, 
								M.`user_job_position`,
								P.`web_thumbnail_url`,
								M.`user_url`,
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS like_count,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS comment_count
						FROM	`depol_portfolio` AS P LEFT JOIN `depol_member` AS M ON P.`user_id` = M.`user_id`
					 ORDER BY	like_count DESC";					
		}
		else if (!empty($keyword) && $category == null && $sort == Consts::REQUEST_PORTFOLIO_FOLLOW) {	
			//모든 카테고리 + 팔로우
			$query = "SELECT	M.`user_id`,
								M.`user_url`, 
								M.`user_name`, 
								M.`user_job_position`,
								P.`portfolio_id`, 
								P.`title`, 
								P.`thumbnail_url`, 
								P.`web_thumbnail_url`,
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS like_count,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS comment_count
						FROM	`depol_follow` AS U LEFT JOIN  
								`depol_portfolio` AS P ON U.`follow_user_id` = P.`user_id` LEFT JOIN
								`depol_member` AS M ON U.`follow_user_id` = M.`user_id`
						WHERE	U.`user_id` = {$user_id} AND P.`title` LIKE '%{$keyword}%'";					
		}
		else if (empty($keyword) && is_numeric($category) && $sort == Consts::REQUEST_PORTFOLIO_FOLLOW) {	
			//특정 카테고리 + 팔로우
			$query = "SELECT	M.`user_id`,
								M.`user_url`, 
								M.`user_name`, 
								M.`user_job_position`,
								P.`portfolio_id`, 
								P.`title`, 
								P.`thumbnail_url`, 
								P.`web_thumbnail_url`,
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS like_count,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS comment_count
						FROM	`depol_follow` AS U LEFT JOIN  
								`depol_portfolio` AS P ON U.`follow_user_id` = P.`user_id` LEFT JOIN
								`depol_member` AS M ON U.`follow_user_id` = M.`user_id`
						WHERE	U.`user_id` = {$user_id} AND P.`portfolio_id` IN (SELECT `portfolio_id` FROM depol_portfolio_categorys WHERE `category_id` = {$category})";					
		}
		else if (empty($keyword) && is_numeric($category) && $sort == Consts::REQUEST_PORTFOLIO_NEW) {	
			//특정 카테고리  + 최신순
			$query = "SELECT	M.`user_name`,
								P.`portfolio_id`, 
								P.`title`, 
								P.`thumbnail_url`, 
								M.`user_job_position`,
								P.`web_thumbnail_url`,
								M.`user_url`,
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS like_count,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS comment_count
						FROM	`depol_portfolio` AS P LEFT JOIN `depol_member` AS M ON P.`user_id` = M.`user_id` 
						WHERE	P.`portfolio_id` IN (SELECT `portfolio_id` FROM depol_portfolio_categorys WHERE `category_id` = {$category})
					 ORDER BY	P.`creation_time` DESC";					
		} 
		else if (empty($keyword) && !empty($category) && $sort == Consts::REQUEST_PORTFOLIO_BEST) {	
			//단일 카테고리
			$query = "SELECT	M.`user_name`,
								P.`portfolio_id`, 
								P.`title`, 
								P.`thumbnail_url`, 
								M.`user_job_position`,
								P.`web_thumbnail_url`,
								M.`user_url`,
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS like_count,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS comment_count
						FROM	`depol_portfolio` AS P LEFT JOIN `depol_member` AS M ON P.`user_id` = M.`user_id` 
						WHERE	P.`portfolio_id` IN (SELECT `portfolio_id` FROM depol_portfolio_categorys WHERE `category_id` = {$category})
					 ORDER BY	like_count DESC";					
		}
		else if (!empty($keyword) && $category == null && $sort == Consts::REQUEST_PORTFOLIO_NEW) {	
			//모든 카테고리 + 특정 키워드 + New
			$query = "SELECT	M.`user_name`,
								P.`portfolio_id`, 
								P.`title`, 
								P.`thumbnail_url`, 
								M.`user_job_position`,
								P.`web_thumbnail_url`,
								M.`user_url`,
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS like_count,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS comment_count
						FROM	`depol_portfolio` AS P LEFT JOIN `depol_member` AS M ON P.`user_id` = M.`user_id` 
						WHERE	P.`title` LIKE '%{$keyword}%' 
					 ORDER BY	P.`creation_time` DESC";					
		} else if (!empty($keyword) && $category == null && $sort == Consts::REQUEST_PORTFOLIO_BEST) {
			//모든 카테고리 + 특정 키워드 + 인기
			$query = "SELECT	M.`user_name`,
								P.`portfolio_id`, 
								P.`title`, 
								P.`thumbnail_url`, 
								M.`user_job_position`,
								P.`web_thumbnail_url`,
								M.`user_url`,
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS like_count,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS comment_count
						FROM	`depol_portfolio` AS P LEFT JOIN `depol_member` AS M ON P.`user_id` = M.`user_id` 
						WHERE	P.`title` LIKE '%{$keyword}%' 
					 ORDER BY	like_count DESC";
		} else if (!empty($keyword) && !empty($category) && $sort == Consts::REQUEST_PORTFOLIO_NEW) {
			//특정 카테고리 + 특정 키워드 + New
			$query = "SELECT	M.`user_name`,
								P.`portfolio_id`, 
								P.`title`, 
								P.`thumbnail_url`, 
								M.`user_job_position`,
								P.`web_thumbnail_url`,
								M.`user_url`,
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS like_count,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS comment_count
						FROM	`depol_portfolio` AS P LEFT JOIN `depol_member` AS M ON P.`user_id` = M.`user_id` 
						WHERE	P.`title` LIKE '%{$keyword}%' AND 
								P.`portfolio_id` IN (SELECT `portfolio_id` FROM depol_portfolio_categorys WHERE `category_id` = {$category})
					 ORDER BY	P.`creation_time` DESC";
		} else if (!empty($keyword) && !empty($category) && $sort == Consts::REQUEST_PORTFOLIO_BEST) {
			//특정 카테고리 + 특정 키워드 + 인기
			$query = "SELECT	M.`user_name`,
								M.`user_job_position`,
								P.`portfolio_id`, 
								P.`title`, 
								P.`thumbnail_url`, 
								P.`web_thumbnail_url`,
								M.`user_url`,
								(SELECT COUNT(*) FROM `depol_like` WHERE `portfolio_id` = P.`portfolio_id`) AS like_count,
								(SELECT COUNT(*) FROM `depol_comments` WHERE `portfolio_id` = P.`portfolio_id`) AS comment_count
						FROM	`depol_portfolio` AS P LEFT JOIN `depol_member` AS M ON P.`user_id` = M.`user_id` 
						WHERE	P.`title` LIKE '%{$keyword}%' AND 
								P.`portfolio_id` IN (SELECT `portfolio_id` FROM depol_portfolio_categorys WHERE `category_id` = {$category})
					 ORDER BY	like_count DESC";
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
					"userUrl" => $rows['user_url'],
					"commentNum" => (int)$rows['commentNum'],
					"userJobPosition" => $rows['user_job_position'],
					"webThumbImgUri" => Config::SERVER_DOMAIN."/".$rows['web_thumbnail_url'],
				);
			}
			return $arr;			
		} 
		return FALSE;			
					
	}
	
}
?>