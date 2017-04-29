<?
	session_start();
	include_once('./class/common.php');
?>
<!DOCTYPE html>
<html>
<head>
	<title><?=TITLE?></title>
    <? include_once("./base_script.html") ?>
    
    <script>
    $(document).ready(function () {
    
    	//REST API
		var FACEBOOK_LOGIN_API = "../FacebookLogin.php";
		var KAKAO_LOGIN_API = "../KakaoLogin.php";
		
		//RINK PAGE
		var creator_link = "./creator.php?uid=";
		
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
    
		$.topMove = function() {
			$('html, body').animate({
				scrollTop: $('.header').offset().top
			}, 1000);
		}
		
	    $.sign_up_link = function() {
	    	window.location = "./sign_up.php";
		}
		
		$.sign_in_link = function() {
	    	window.location = "./sign_in.php";
		}
		
		$.logout_link = function() {
		
			//SNS Logout
            $.logout_kakao(); 
            $.logout_facebook();
            		
			//Session Destory
			var LOGOUT_API = "./logout.php";
		 	
		 	$.ajax({
            	url : LOGOUT_API,
            	success: function() {            		
            		//refresh
	            	location.reload();
            	}
            });            
                           
		}	
		
		
		//페이스북
		$.ajaxSetup({ cache: true });		
		$.getScript('http://connect.facebook.net/en_US/all.js', function() {
		
		    FB.init({
		      appId: '1498303817126899',
			  version: 'v2.3'
		    });     
		    
		});	
			
		$.logout_facebook = function() {
			$.getScript('http://connect.facebook.net/en_US/all.js', function() {
				 
		        //페이스북 로그아웃
		        FB.getLoginStatus(function(response) { 
					 if (response.status == 'connected') {
					    FB.logout(function(response) {
					    	
						});				 	
					 }
				});  
				
			});
		}
		
		$.facebookCheckLoginStatus = function() {
			$.getScript('http://connect.facebook.net/en_US/all.js', function() {
				
				FB.getLoginStatus(function(response) {
					 if (response.status == 'connected') {
						    var uid = response.authResponse.userID;
						    var accessToken = response.authResponse.accessToken;
						    
						    <? 
							    if (!$_SESSION['ID']) {
								    ?>
								    	$.facebook_login(accessToken);
								    <?
							    }
						    ?>
					 } else {
					 	FB.login(function(response) {		 	
							
							$.ajax({
					            url : FACEBOOK_LOGIN_API,
					            type: "POST",
					            dataType : "json",
					            data: {access_token: response.authResponse.accessToken},
					            success: function() {
						           	location.reload();
					            }
							});
								
						}, {scope: 'public_profile, email'});
					 }
				});				
			});
			
		}
		
		$.facebook_login = function(accessToken) {
									
				$.ajax({
				        url : FACEBOOK_LOGIN_API,
				        type: "POST",
				        dataType : "json",
				        data: {access_token: accessToken},
				        success: function() {
					         	location.reload();
				        }
				});
		}
		
		
		
		//카카오톡
		Kakao.init('439a25520936ee68153c949a5904629f');
		$.kakaoCheckLoginStatus = function() {
			Kakao.Auth.getStatus(function(statusObj) {
				if (statusObj.status == 'not_connected') {
				
					Kakao.Auth.login({
						success: function(authObj) {
														
							$.ajax({
					            url : KAKAO_LOGIN_API,
					            type: "GET",
					            dataType : "json",
					            data: {access_token: authObj.access_token},
					            success: function(obj) {
					            	if (obj.result == "SUCCESS") {
						           		location.reload();
									}
					            }
							});
							
						}
					});				
				}
				
			});
		}
		
		$.logout_kakao = function() {
			Kakao.Auth.logout(function() {
			});
		}
		
		<?
			if (!$_SESSION['ID']) {
				?>$.logout_kakao();<?
			}
		?>
		
	});
	
    </script>
</head>
<body>
	<? include_once("./header_index.html") ?>  

      <div class="jumbotron">
        <h1>DEPOL</h1>
        <p class="lead">디자이너를 위한 포트폴리오 관리 서비스!</p>
        <h5>본 페이지는 웹표준을 준수하며 HTML5, CSS3, Ajax, PHP, JQuery 기술을 기반으로 제작하였습니다. </h5>
		<?
			if ($_SESSION['ID']) {
				?>
				<p><a class="btn btn-lg btn-success" onclick="$.logout_link();">Logout</a></p>
				<?
			} else {
				?>
				<p><a class="btn btn-lg btn-success" onclick="$.sign_up_link();">Sign up</a>
				   <a class="btn btn-lg btn-success" onclick="$.sign_in_link();">Sign in</a> 
				   <a class="btn btn-lg btn-success" onclick="$.facebookCheckLoginStatus();">Facebook Sign in</a>
				   <!--<a class="btn btn-lg btn-success" onclick="$.kakaoCheckLoginStatus();">Kakao Sign in</a>-->
				</p>
				
				<?
			}
		?>       
     </div>
      
      
    <div class="container marketing">

      <div class="row">
        <div class="col-lg-4"></div>
        <div class="col-lg-4">
          <img class="img-circle" src="./images/depol_icon.jpg" data-src="holder.js/140x140" alt="Generic placeholder image">
          <h2>DEPOL</h2>
          <p>바로 지금 안드로이드 마켓에서 확인하세요!</p><br>
		  
          <p><a class="btn btn-default" href="https://play.google.com/store/apps/details?id=com.noah.depol&hl=ko" target="_blank">구글 플레이 이동 &raquo;</a></p>
        </div><!-- /.col-lg-4 -->
        <center><iframe width="640" height="390" src="//www.youtube.com/embed/61u9cKzwYEs" frameborder="0" allowfullscreen></iframe><br><br></center>
      </div><!-- /.row -->

      <!-- START THE FEATURETTES -->

      <hr class="featurette-divider">

      <div class="row featurette">
        <div class="col-md-7">
          <h2 class="featurette-heading"><span class="text-muted">당신은 최고의 디자이너!</span></h2>
          <p class="lead">DEPOL은 당신을 조금더 업그레이드 시켜 줍니다. <br>다양한 디자이너들의 포트폴리오를 직접 눈으로 보고 영감을 얻고,<br>내가 좋아하는 포트폴리오를 찾아보세요.
	          	          
          </p>
        </div>
        <div class="col-md-5">
          <img class="featurette-image img-responsive" src="./images/main-0.png" width="283" height="508" data-src="holder.js/283x508/auto" alt="Generic placeholder image">
        </div>
      </div>

      <hr class="featurette-divider">

      <div class="row featurette">
        <div class="col-md-5">
          <img class="featurette-image img-responsive" src="./images/main-1.png" width="283" height="508" data-src="holder.js/283x508/auto" alt="Generic placeholder image">
        </div>
        <div class="col-md-7">
          <h2 class="featurette-heading"><span class="text-muted">나만의 기회를 찾다.</span></h2>
          <p class="lead">DEPOL의 다양한 기능을 직접 체험하세요.<br> 그리고 나만의 기회를 찾으세요!</p>
        </div>
      </div>

      <hr class="featurette-divider">

      <div class="row featurette">
        <div class="col-md-7">
          <h2 class="featurette-heading"><span class="text-muted">전문적인 포트폴리오 관리</span></h2>
          <p class="lead">자신을 다른사람들에게 드러내는 최고의 방법!<br>내가 만든 작품을 다양한 사람들과 나누고 추천하고 공유하세요!</p>
        </div>
        <div class="col-md-5">
          <img class="featurette-image img-responsive" src="./images/main-2.png" width="283" height="508" data-src="holder.js/283x508/auto" alt="Generic placeholder image">
        </div>
      </div>

      <hr class="featurette-divider">

      <!-- /END THE FEATURETTES -->

        <p class="pull-right"><a class="top" href="javascript::void(0)" onclick="$.topMove();">Back to top</a></p>
    </div><!-- /.container -->      
      
         
	<? include_once("./footer.html"); ?>
</body>
</html>