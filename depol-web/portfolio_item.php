<?
	session_start();
	include_once('./class/common.php');
	require_once ("../class/Config.php");
	require_once ("../class/WebDataBase.php");
	require_once ("../class/Consts.php");
		
	if (empty($_GET['id'])) {
		echo "올바른 접근이 아닙니다.";
		exit();	
	}
	
	//데이터베이스 연결
	$db = WebDataBase::global_instance();
	$images = $db->getPortfolioImageList($id);
	$replay = $db->getCommentList($_GET['id']);
	$arr = $db->getMyPageUserInfo($id);
	$info = $db->getPortfolioWebInfo($_GET['id'], $_SESSION['ID']);
?>
<!DOCTYPE html>
<html>
<head>
	<title><?=TITLE?></title>
    <? include_once("./base_script.html") ?>
    
    <script src="./js/masonry.pkgd.min.js"></script>
    <link href="./js/fotorama.css" rel="stylesheet">
	<script src="./js/fotorama.js"></script>
	
	<script type="text/javascript">
	$(document).ready(function() {
	
		var creator_link = "./creator.php?uid=";
		
		
		//REST API
		var LIKE_API = "../Portfolio/Like.php";
		var UNLIKE_API = "../Portfolio/UnLike.php";
		
		
		
		
		$.creator_link = function(uid) {
				window.location = creator_link + uid;
		}
	
	
		$('#replay_input').click(function() {
			<?
				if (empty($_SESSION['ID'])) {
					?> alert('로그인을 하신 후 이용해주시기 바랍니다.'); <?
				}
			?>
		});
		
		$('#mypage').click(function() {
				
			<?
				
				if (empty($_SESSION['ID'])) {
					?> alert('로그인을 하신 후 이용해주시기 바랍니다.'); <?
				} else {
					?> 
						window.location = creator_link + "<?=$_SESSION['ID']?>";
					<?
				}
				
			?>
		});
		
		$('.btn').click(function() {
			
			var COMMENT_WRITE_API = "../Portfolio/Comment_Write.php";     
			var replay_context = $('#replay_input').val();
			var uid = <?=$info['user_id']?>;
			var pid = <?=$info['portfolio_id']?>;
			  
            $.ajax({
            	url : COMMENT_WRITE_API, 
            	type: "POST",
            	dataType : "json",
            	data: {id:pid, targer_user:uid, context:replay_context},
            	success: function(data) {
					if (data.result == "SUCCESS") {
						$('html, body').animate({
							scrollTop: $('#replay-last').offset().top
						}, 1000, function(){						
							location.reload();
							this.reset();
						});
					}
				}
			}).fail(function(jqXHR, textStatus, errorThrown) {
            	alert(textStatus);			    
			});
			
		});
		
		
		$.removeComment = function(pid, cid) {			
			
			var COMMENT_REMOVE_API = "../Portfolio/Comment_Remove.php";
			  
            $.ajax({
            	url : COMMENT_REMOVE_API, 
            	type: "GET",
            	dataType : "json",
            	data: {id:pid, comment_id:cid},
            	success: function(data) {
            	alert(data.result);
					if (data.result == "SUCCESS") {
						location.reload();
					}
				}
			}).fail(function(jqXHR, textStatus, errorThrown) {
            	alert(textStatus);			    			    
			});
					
		}
		
		$.like = function(pid) {
				
            $.ajax({
            	url : LIKE_API, 
            	type: "GET",
            	dataType : "json",
            	data: {pid:pid},
            	success: function(data) {
					if (data.result == "SUCCESS") {
						location.reload();
					} else if (data.result == "LOGIN_SESSION_FAIL") {
						alert('로그인 후 이용해주세요.');
					}
				}
			}).fail(function(jqXHR, textStatus, errorThrown) {
            	alert(textStatus);			    
			});
		}
		
		$.unlike = function(pid) {
		
            $.ajax({
            	url : UNLIKE_API, 
            	type: "GET",
            	dataType : "json",
            	data: {pid:pid},
            	success: function(data) {
					if (data.result == "SUCCESS") {
						location.reload();
					} else if (data.result == "LOGIN_SESSION_FAIL") {
						alert('로그인 후 이용해주세요.');
					}
				}
			}).fail(function(jqXHR, textStatus, errorThrown) {
            	alert(textStatus);			    
			});		
		}
		
	});
	</script>
</head>
<body>
	
	<? include_once("./header_portfolio.html") ?>
	<div class="profile">
	<img onclick="$.creator_link(<?=$info['user_id']?>);" class="pimage" src="<?= DOMAIN.$info['user_url']?>" alt="<?=$info['title']?>" />
	<div class="ptitle"><?=$info['title']?></div>
	<div class="pname">by. <?=$info['user_name']?></div>
	</div>
	
	  <div id="container">
		  <div class="fotorama" data-width="1920" data-ratio="1920/850" data-max-width="100%"> 
				  <?
				  	foreach($images as $key => $data) {
				  		?>
				  			<img src="<?=$data?>" alt="<?=$key?>"/>
				  		<?
				  	}
				  ?>
				  
		  </div>		  
		  <div class="context"> 
		  <p class="title"><?=$info['title']?></p>		  
		  <?=nl2br($info['context'])?>
		  </div>
		  <br>
		  <div class="copyright"> 
		  <h6>&copy; 본 콘텐츠의 저작권은 제공처에 있으며 이를 무단 사용시 저작권법 등에 따라 법적 책임을 질 수 있습니다.</h6>
		  </div>
		  
		  <div class="like"> <!-- Like -->
		  	<?
		  		//좋아요 상태
		  		if ($info['is_liked']) {
			  		?><a href="javascript::void(0)" onclick="$.unlike(<?=$info['portfolio_id']?>)"><img src="./images/unlike_it.png"/></a><?
		  		} else {
			  		?><a href="javascript::void(0)" onclick="$.like(<?=$info['portfolio_id']?>)"><img src="./images/like_it.png"></a><?
		  		}
		  	
		  	?>
		  </div> <!-- End Like -->
		  
		  <div class="replay"> 
			  <div class="input-group">
			      <input type="text" class="form-control" id="replay_input">
			      <span class="input-group-btn">
			        <button class="btn btn-default" type="button">댓글 달기</button>
			      </span>
			  </div>
			  <div class="replay-list">
			  <?
				  if ($replay != null) {
					  	foreach($replay as $key => $data) {
						  	?>	
						  		<div class="replay-list-data">
						  		<img onclick="$.creator_link(<?=$data['userId']?>);" class="image-wrap" src="<?= $data['userPropicUri']?>" alt="<?= $data['userName']?>"/>
						  		<div class="replay-list-name"><?= $data['userName']?></div>
						  		<div class="replay-list-time"><?= $data['timeStamp']?> <?
						  			if ($_SESSION['ID'] == $data['userId']) {
							  			?><a onclick="$.removeComment(<?=$_GET['id']?>, <?=$data['commentId']?>);" class ="replay-remove">삭제</a><?
						  			}							  		
						  		?></div>
						  		<div class="replay-list-context"><?= nl2br($data['text'])?></div>
						  		</div>
						  	<?
					  	}
				  }			  
			  ?>
			  	<div id="replay-last"></div>
			  </div>
		  </div>
		    
	</div>	
	        	
		  
	<? include_once("./footer.html") ?>
</body>
</html>