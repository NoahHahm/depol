<?
	session_start();
	include_once('./class/common.php');
?>
<!DOCTYPE html>
<html>
<head>
	<title><?=TITLE?></title>
    <? include_once("./base_script.html") ?>
    <script src="./js/bootstrap.min.js"></script>
    
    <script type="text/javascript">
    $(document).ready(function() {
        
    	$(".btn").click(function(e) {
    	
			var LOGIN_API = "../Login.php";     
			       
            $.ajax({
            	url : LOGIN_API, 
            	type: "POST",
            	dataType : "json",
            	data: $('#form-sign-in').serialize(),
            	success: function(data) {
            		if (data.result == "SUCCESS") {
	            		alert_success_login.style.display = "block";
		            	setTimeout(function() {
							window.location.href = "./index.php";
						}, 1000);
            		}
            		else if (data.result == "LOGIN_PASSWORD_FAIL") {
	            		alert_danger_password.style.display = "block";
            		} else if (data.result == "EMAIL_NONEXISTENT") {
	            		alert_danger_email.style.display = "block";
            		}
				}
			}).fail(function(jqXHR, textStatus, errorThrown) {
			    
			});
		});
		
		
		
    });
    </script>
       
</head>
<body>
    <div class="container">
      <form class="form-sign" id="form-sign-in" method="post">
        <h2 class="form-sign-heading">로그인</h2>
        
        <input type="email" class="form-control" placeholder="Email address" name="email" autofocus required />
        <input type="password" class="form-control" placeholder="Password" name="password" required />
        <button class="btn btn-lg btn-primary btn-block" type="button">Sign in</button>
      </form>
      
	  <div id="alert_success_login" class="alert alert-success alert-dismissable">로그인 성공! 1초후 메인으로 이동 합니다.<button type="button" class="close" data-dismiss="alert" aria-hidden="true"> &times;</button></div>
	  <div id="alert_danger_email" class="alert alert-danger">이메일이 존재하지 않습니다.<button type="button" class="close" data-dismiss="alert" aria-hidden="true"> &times;</button></div>
	  <div id="alert_danger_password" class="alert alert-danger">패스워드가 잘못 되었습니다.<button type="button" class="close" data-dismiss="alert" aria-hidden="true"> &times;</button></div>

    </div> <!-- /container -->
</body>
</html>