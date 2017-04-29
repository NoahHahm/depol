<?
	session_start();
	include_once('./class/common.php');
	require_once ("../class/Config.php");
	require_once ("../class/DataBase.php");	
	require_once ("../class/WebDataBase.php");		
		
	$db = DataBase::global_instance();
	$user_data = $db->getUserPortfolioList($_GET['uid']);
	$user_info = $db->getMyPageUserInfo($_GET['uid']);

	if ($user_info['user_url'] == null) {
		$user_info['user_url'] = DOMAIN."image/empty.jpg";
	}
	
?>
<!DOCTYPE html>
<html>
<head>
	<title><?=TITLE?></title>
    <? include_once("./base_script.html") ?>
    <script src="./js/holder.js"></script>
    <script src="http://masonry.desandro.com/masonry.pkgd.min.js"></script>
    
    <link href="./css/upload/jquery.fileupload.css" rel="stylesheet" />  
    <link href="./css/upload/jquery.fileupload-ui.css" rel="stylesheet" />  
	<script src="./js/bootstrap.min.js"></script>
	<script src="./js/jquery.form.min.js"></script>	
	
	
    <script>   
    $(document).ready(function () {
    
		var link = "./portfolio_item.php?id=";
		
		
		var MODIFY_PROFILE_URL = "./api/profile_modify.php";
		var FOLLOW_API = "../Follow/Follow.php";
		var UNFOLLOW_API = "../Follow/UnFollow.php";
		
		$.link = function(pid) {
			window.location = link + pid;
		}	
				
		$('#form-upload').ajaxForm({	        
		       dataType : 'json', 
			beforeSerialize: function() {
			
			},
			beforeSubmit : function() {
			
			},
			success : function(data) {						
				$('#upload-modal').modal('hide');					
				location.reload();
			}
		});
		
		$.updateName = function() {
					
			var name = $('#m-name').val();
			
			$.ajax({
            	url : MODIFY_PROFILE_URL, 
            	type: "GET",
            	dataType : "json",
            	data: {mname:name},
            	success: function(data) {
					if (data.result == "SUCCESS") {
						$('#name-modify-modal').modal('hide');
						location.reload();
					} else {
						alert('변경 실패! 다시 시도해주세요.');
					}
				}
			}).fail(function(jqXHR, textStatus, errorThrown) {
			    alert(textStatus);
			});
			
		}
		
		$.updateJob = function() {
		
			var job = $('#m-job').val();
			
			$.ajax({
            	url : MODIFY_PROFILE_URL, 
            	type: "GET",
            	dataType : "json",
            	data: {mjob:job},
            	success: function(data) {
					if (data.result == "SUCCESS") {
						$('#job-modify-modal').modal('hide');
						location.reload();
					} else {
						alert('변경 실패! 다시 시도해주세요.');
					}
				}
			}).fail(function(jqXHR, textStatus, errorThrown) {
			    alert(textStatus);			    
			});
		}
		
		$('.upload').click(function(){
			<?
				if ($_SESSION['ID']) {
					?>$('#upload-modal').modal('show');<?
				} else {
					?>
						alert('로그인 후 이용해주시기 바랍니다.');
					<?
				}
			?>
		});	
		
		$.follow = function(fid) {
				
            $.ajax({
            	url : FOLLOW_API, 
            	type: "GET",
            	dataType : "json",
            	data: {fid:fid},
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
		
		
		$.unfollow = function(fid) {
				
            $.ajax({
            	url : UNFOLLOW_API, 
            	type: "GET",
            	dataType : "json",
            	data: {fid:fid},
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
		
		var container = document.querySelector('#creator-portfolio-list');
			var msnry = new Masonry(container, {
			  itemSelector: '.creator-portfolio',
			  animationOptions: {
				  duration: 400
			  }
		});  
		
		msnry.layout();
	});
    </script>
</head>
<body>
	<? include_once("./header_mypage.html") ?>  
	
      <div class="creator">
          <img class="img-circle creator-img" src="<?= DOMAIN.$user_info['user_url']?>" data-src="holder.js/140x140" alt="<?=$user_info['user_name']?>">
          
          <!-- Follow -->
          <? 
	          if ($user_info['isFollow'] && ($_SESSION['ID'] != $user_info['user_id'])) {
		          ?><a href="javascript::void(0)" onclick="$.unfollow(<?=$user_info['user_id']?>)"><img class="creator-follow" src="./images/following.png" /></a><?
	          } else if(!$user_info['isFollow'] && ($_SESSION['ID'] != $user_info['user_id'])) {
		          ?><a href="javascript::void(0)" onclick="$.follow(<?=$user_info['user_id']?>)"><img class="creator-follow" src="./images/follow.png" /></a><?
	          }        
          ?>          
      	  
      	  
      	  
          <div class="creator-name"><h4><?=$user_info['user_name']?></h4></div>
          <h5><?=$user_info['user_job_position']?></h5><br>	
          
		  <?
		  	//사용자 변경 활성화
		  	if ($_SESSION['ID'] == $user_info['user_id']) {
			  	?>
	          <div class="creator-modify">
				  <ul class="nav nav-pills">
				    <li class="dropdown">
				      <a class="btn btn-default" id="drop5" role="button" data-toggle="dropdown" href="#">프로필 변경<b class="caret"></b></a>
				      <ul id="menu2" class="dropdown-menu" role="menu" aria-labelledby="drop5">
				        <li role="presentation"><a role="menuitem" tabindex="-1" data-toggle="modal" data-target="#name-modify-modal">이름 변경</a></li>
				        <li role="presentation" class="divider"></li>
				        <li role="presentation"><a role="menuitem" tabindex="-1" data-toggle="modal" data-target="#job-modify-modal">직업정보 변경</a></li>
				        <li role="presentation"><a role="menuitem" tabindex="-1" data-toggle="modal" data-target="#photo-modify-modal">프로필 사진 변경</a></li>
				      </ul>
				    </li>
				  </ul> <!-- /tabs -->
	          </div>
			  	<?
		  	}
		  ?>
	  
			  
	  </div>
	  
	  <?
	  	//업로드 활성화
	  	if ($_SESSION['ID'] == $user_info['user_id']) {
		  	?>
		  	<div class="upload-btn">
		  		<button class="btn btn-primary btn-lg" data-toggle="modal" data-target="#upload-modal">Portfolio Upload</button>
		  	</div>
		  	<?
	  	}
	  ?>	  		  
	  
	  
	  <div id="creator-portfolio-list">    
      	<div class="creator-portfolio">
	  <?
		  if ($user_data) {
			
			foreach($user_data as $key => $data) {
				if ($data['webThumbImgUri'] != NULL) {
					
						//URL 재정의
						$data['webThumbImgUri'] = "http://".$_SERVER['SERVER_NAME']."/depol/".$data['webThumbImgUri'];
						
					?>
					<img onclick="$.link(<?=$data['thumbPofolId']?>);" src="<?=$data['webThumbImgUri']?>" width="250" height="250" data-src="holder.js/250x250" alt="<?=$data['pofolTitle']?>">
					<?
				} else {
					?>
					<img onclick="$.link(<?=$data['thumbPofolId']?>);" src="<?=$data['thumbImgUri']?>" width="250" height="250" data-src="holder.js/250x250" alt="<?=$data['pofolTitle']?>">
					<?					
				}
			} //END Foreach
			 
		  } //END IF		
	  ?>
	   </div>
	  </div>  

	 
	<!-- Modal -->
	<div class="modal fade" id="upload-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	        <h4 class="modal-title">포트폴리오 업로드</h4>
	      </div>
	      <div class="modal-body">
				<form enctype="multipart/form-data" method="post" id="form-upload" action="../Portfolio/UploadTest.php">
					<input type="text" class="form-control" placeholder="제목" name="title" required/>
					<textarea class="form-control" rows="9" placeholder="내용" name="context" required></textarea>
					<br><h2>포트폴리오 선택</h2><input type="file" name="images[]" id="file" min="1" max="10" multiple required/>
					<br><h2>카테고리 선택</h2>
						<?
							$db = WebDataBase::global_instance();
							$arr = $db->getCategoryList();
							foreach($arr as $key => $data) {
								?><input type="checkbox" class="btn btn-default" value=<?=$data['category_id']?> name="category[]"/><?=$data['context']?><br><?
							}
						
						?>
					  <br><button type="submit" class="btn btn-success btn-lg" id="send">Upload</button>
					
				</form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
	
	<!-- 이름 변경 모달 -->
	<div class="modal fade" id="name-modify-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog modal-sm">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	        <h4 class="modal-title">이름 변경</h4>
	      </div>
	      <div class="modal-body">
				<input type="text" class="form-control" placeholder="변경 이름" id="m-name" required/>	
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-primary" onclick="$.updateName();">Save</button>
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
	<!-- 직업 변경 모달 -->
	<div class="modal fade" id="job-modify-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog modal-sm">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	        <h4 class="modal-title">직업 변경</h4>
	      </div>
	      <div class="modal-body">
				<input type="text" class="form-control" placeholder="변경 직업" id="m-job" required/>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-primary" id="save-modify-job" onclick="$.updateJob();">Save</button>
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
	
	<!-- 사진 변경 모달 -->
	<div class="modal fade" id="photo-modify-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog modal-sm">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	        <h4 class="modal-title">프로필 사진 변경</h4>
	      </div>
	      <div class="modal-body">
				<form enctype="multipart/form-data" method="post" id="form-upload" action="../MyPage/Upload_Profile_Image.php">
					<input type="file" name="images" id="file" min="1" max="1" multiple required/>			
					<br><button type="submit" class="btn btn-success btn-lg" id="send">변경</button>	
				</form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
               
	<? include_once("./footer.html"); ?>
</body>
</html>