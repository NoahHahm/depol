<?
	session_start();	
	include_once('./class/common.php');
	require_once ("../class/Config.php");
	require_once ("../class/WebDataBase.php");
	require_once ("../class/Consts.php");
		
	//데이터베이스 연결
	$db = WebDataBase::global_instance();
	$type = $_GET['type'];
	$category = $_GET['category'];
	$keyword = $_GET['keyword'];
	$last_id = $_GET['last_id'];
	$page = $_GET['page'];

	$arr = $db->getPortfolioList($type, $last_id, $_SESSION['ID'], $page);
	
	if ($arr != null) {
	  	foreach($arr as $key => $data) {
		  	
	 		//이미지 널
	  		if ($data['userUrl'] == null) {
		  		$data['userUrl'] = DOMAIN."image/empty.jpg";
	  		} else {
		  		$data['userUrl'] = DOMAIN.$data['userUrl'];
	  		}
	  		?><div class="item" id="<?=$data['thumbPofolId']?>"><img onclick="$.link(<?=$data['thumbPofolId']?>);" class="image_thumbnail" src="<?=$data['thumbImgUri']?>" alt="<?=$data['pofolTitle']?>" /><div class="item_sub1"><h5 class="title"><?=$data['pofolTitle']?></h5></div><div class="item_sub2"><img onclick="$.creator_link(<?=$data['user_id']?>);" class="image-wrap" src="<?= $data['userUrl']?>" alt="<?=$data['userName']?>" /><div class="name"><?=$data['userName']?></div><div class="job"><?=$data['userJobPosition']?></div></div></div><?
	  		
	  		
		}
	}
?>