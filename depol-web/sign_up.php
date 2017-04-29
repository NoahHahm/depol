<?
	session_start();
	include_once('./class/common.php');	
?>
<!DOCTYPE html>
<html>
<head>
	<title><?=TITLE?></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link href="./css/bootstrap.css" rel="stylesheet"> 
    <link href="./css/depol.css" rel="stylesheet">  
	<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
    <script src="./js/bootstrap.min.js"></script>
    <script src="./js/bootstrapValidator.min.js"></script>
    <script src="./js/validator/identical.js"></script>
    <script type="text/javascript">
	$(document).ready(function() {
		
	    $('#registerForm').bootstrapValidator({
	        feedbackIcons: {
	            valid: 'glyphicon glyphicon-ok',
	            invalid: 'glyphicon glyphicon-remove',
	            validating: 'glyphicon glyphicon-refresh'
	        },
	        fields: {
	            password: {
	                validators: {
	                    identical: {
	                        field: 'confirmPassword',
	                        message: 'The password and its confirm are not the same'
	                    }
	                }
	            },
	            confirmPassword: {
	                validators: {
	                    identical: {
	                        field: 'password',
	                        message: 'The password and its confirm are not the same'
	                    }
	                }
	            }
	        }
	    });	    
		
		
		$("#registerForm").submit(function(e) {
		
			var REGISTER_API = "../Register.php";     
			       
            $.ajax({
            	url : REGISTER_API, 
            	type: "POST",
            	dataType : "json",
            	data: $('#registerForm').serialize(),
            	success: function(data) {
					if (data.result == "success") {
						alert_success.style.display = "block";
						setTimeout(function() {
							window.location.href = "./index.php";
						}, 1000);
					} else if (data.result == "fail") {
						alert_danger.style.display = "block";						
					} else if (data.result == "REGISTER_DUPLICATION") {
						alert_duplication.style.display = "block";						
					}
					
				}
			}).fail(function(jqXHR, textStatus, errorThrown) {
			    alert_danger.style.display = "block";
			});
			
		});
			
		
	});
    </script>
</head>
<body>
    <div class="container">          
      <form id="registerForm" class="form-sign form-horizontal" method="post">
      	<div class="form-group">
	        <label class="col-lg-3 control-label">Email</label>
	            <input type="email" class="form-control" placeholder="Email address" name="email" data-bv-emailaddress-message="The value is not a valid email address" required />	        
	    </div>		    
	    <div class="form-group">
	        <label class="col-lg-3 control-label">User Name</label>	        
	            <input type="text" class="form-control" name="name" data-bv-notempty-message="The full name is required" required />	        
	    </div>		    
	    <div class="form-group">
	        <label class="col-lg-3 control-label">Password</label>	        
	            <input type="password" class="form-control" name="password" required/>	        
	    </div>	
	    <div class="form-group">
	        <label class="col-lg-3 control-label">Retype password</label>	        
	            <input type="password" class="form-control" name="confirmPassword" required/>	        
	    </div>	
	    <button class="btn btn-lg btn-primary btn-block" id="submit">Sign up</button>  
	</form>
	
   <div id="alert_success" class="alert alert-success alert-dismissable"> <button type="button" class="close" data-dismiss="alert" aria-hidden="true"> &times;</button>회원가입 성공! 1초후 메인으로 이동 합니다.</div>
   <div id="alert_danger" class="alert alert-danger">회원 가입 실패! 에러를 확인 해주세요.!<button type="button" class="close" data-dismiss="alert" aria-hidden="true"> &times;</button></div>
   <div id="alert_duplication" class="alert alert-danger">중복 회원 존재 합니다! 다시 가입해주세요!<button type="button" class="close" data-dismiss="alert" aria-hidden="true"> &times;</button></div>

    </div> <!-- /container -->
</body>
</html>