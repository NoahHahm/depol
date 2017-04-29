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
	
	$category_arr = $db->getCategoryList();
	
	
	
	
	//단일 소트 (초기 화면)
	if (empty($type) || !empty($type) && empty($keyword) && empty($category)) {
		if (!$type) {
			$type = Consts::REQUEST_PORTFOLIO_NEW;
		}
		$arr = $db->getPortfolioList($type, $last_id, $_SESSION['ID'], null);		
	}
	else if (!empty($type) && !empty($category) && empty($keyword)) { //키워드 없을때 검색
		$arr = $db->getSearchList($keyword, $category, $type, $_SESSION['ID']);	
	}
	
	//단일 검색
	if (is_numeric($type) && is_numeric($category) && !empty($keyword)) {
		$arr = $db->getSearchList($keyword, $category, $type, $_SESSION['ID']);
	}
	 
	
	
	
?>
<!DOCTYPE html>
<html>
<head>
	<title><?=TITLE?></title>
    <? include_once("./base_script.html"); ?>	
	  
    <script src="./js/imagesloaded.pkgd.min.js"></script>
    <script src="./js/masonry.pkgd.min.js"></script>
	<script src="./js/purl.js"></script>
    <script src="./js/bootstrap.min.js"></script>
 
 
	<script type="text/javascript">

	$(window).load(function(){

				
			var $container = $('#masonry').masonry({
			  columnWidth: 110,
			  itemSelector: '.item',
			  isFitWidth: true
			});
			
			
			$.appendDocument = function(page_count) {
			
				var id = $(".item:last").attr("id");
				if (id == null) return;
				$.get("./scroll.php", {page : page_count, last_id : id, type : "<?=$type?>", category : "<?=$category?>", keyword : "<?=$keyword?>"}, function(data){
					if(data != "") {
						var html = $.parseHTML(data);
						//var aa = $(data).filter(".item:last").attr("id");
						$container.append(html).masonry('appended', html);
						//$container.layout();
						$container.masonry();
		            }
		        });
				
			}
			
			var page_count = 1;
			$(window).scroll(function() {
				var scrollHeight = $(window).scrollTop() + $(window).height();
				var documentHeight = $(document).height();
				if (scrollHeight == documentHeight) {
					$.appendDocument(page_count);
					page_count++;
				}
			});		
			
			
			
	});
	$(document).ready(function () {	
	
				

			var link = "./portfolio_item.php?id=";
			var creator_link = "./creator.php?uid=";
			var NEW = "1";
			var POPULAR = "2";
			var FOLLOW = "3";
			
			//rest API
			var SEARCH_API = "../Portfolio/Search.php";
		
			
			$.link = function(id) {
				window.location = link + id;
			}
			
			$.creator_link = function(uid) {
				window.location = creator_link + uid;
			}			
				
						
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
			
			
			
			//버튼 상태 변경
			var sort_value = $.url().param('type');
			var category_value = $.url().param('category');
						
			//카테고리 버튼 변경
			$('#category a').click(function() {
				var text = $(this).text();
				$('#btn-category').text(text);
				category_value = $(this).attr('value');
				
				if (sort_value == null) 
					sort_value = NEW; //최신순서
										
				window.location = "?type=" + sort_value +"&category="+category_value;	
			});
			
			
			//정렬순서 버튼 변경
			$('#sort a').click(function() {	
						
				var text = $(this).text();
				var temp = sort_value;
				sort_value = $(this).attr('value');
				
				if (sort_value == FOLLOW) {
					<?
						if (empty($_SESSION['ID'])) {
							?>
							alert('로그인을 하신 후 이용해주시기 바랍니다.'); 
							sort_value = temp;
							return;							
							<?
						}					
					?>
				}
				$('#btn-sort').text(text);
				
				if (category_value == null) 
					category_value = 0; //All Category
				
				window.location = "?type=" + sort_value +"&category="+category_value;				
			});
			
			//검색
			$('.btn-search').click(function() {
				var keyword = $('.search-bar').val();
				if (keyword == null || keyword == '') {
					alert('검색어 입력 해주세요.');
					return;
				}
				
				if (category_value == null) 
					category_value = 0; //All Category
										
				if (sort_value == null) 
					sort_value = NEW; //최신순서
				
				window.location = "?type=" + sort_value +"&category="+category_value+"&keyword="+keyword;											
			});
			
			//sort
			$.init_btn_sort = function(type) {
				
				if (type == null) //param null
					return;
					
				var val = $('#sort a[value='+type+']');
				$('#btn-sort').text(val.text());			
			}
			
			//category
			$.init_btn_category = function(category) {
			
				if (category == null) //param null
					return; //All Category
				
				var val = $('#category a[value='+category+']');
				$('#btn-category').text(val.text());
				
								
			}
			$.init_btn_sort(sort_value);
			$.init_btn_category(category_value);
					
										
	});	
	</script> 
</head>
<body>
	<? include_once("./header_portfolio.html") ?>	

	<!-- 사용자 정의 메뉴 -->
	<div id="user-defined" class="row">	
		<div class="btn-group"> 
		    <button type="button" id="btn-category" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
		    카테고리
		      <span class="caret"></span>
		    </button>
		    <ul class="dropdown-menu" id="category">
		    	<li><a href="javascript::void(0)" onclick="$.portfolio_category_link(0)" value="0">모든 카테고리</a></li>
		    <?		    	
				foreach($category_arr as $key => $data) {
					/* ?><li><a href="javascript::void(0)" onclick="$.portfolio_category_link(<?=$data['category_id']?>)" value="<?=$data['category_id']?>"><?=$data['context']?></a></li><? */
					?><li><a href="#" value="<?=$data['category_id']?>"><?=$data['context']?></a></li><?
				}						
			?>
		    </ul>	    
		</div>
			
		<div class="btn-group"> 
		    <button type="button" id="btn-sort" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
		      정렬 순서
		      <span class="caret"></span>
		    </button>
		    <ul class="dropdown-menu" id="sort">
		      <li><a href="#" value="1">최신순</a></li>
		      <li><a href="#" value="2">인기순</a></li>
		      <li><a href="#" value="3">팔로우</a></li>
		    </ul>
		    
			<div class="input-group">
				<input type="text" class="form-control search-bar" name="keyword">
					<span class="input-group-btn">
					<button class="btn btn-default btn-search" type="button">검색</button>
					</span> 		   	    
			</div>
		</div>		    
		    			
			
	</div>

	
	<!-- 포트폴리오 리스트 -->
	<div id="masonry">
			 <?
			 if ($arr != null) {
				  	foreach($arr as $key => $data) {
				  		//이미지 널
				  		if ($data['userUrl'] == null) {
					  		$data['userUrl'] = DOMAIN."image/empty.jpg";
				  		} else {
					  		$data['userUrl'] = DOMAIN.$data['userUrl'];
				  		}
				  		?>
				  		<div class="item" id="<?=$data['thumbPofolId']?>">
							<img onclick="$.link(<?=$data['thumbPofolId']?>);" class="image_thumbnail" src="<?=$data['thumbImgUri']?>" alt="<?=$data['pofolTitle']?>" />
							<div class="item_sub1">
								<h5 class="title"><?=$data['pofolTitle']?></h5>
							</div>
							<div class="item_sub2">
								<img onclick="$.creator_link(<?=$data['user_id']?>);" class="image-wrap" src="<?= $data['userUrl']?>" alt="<?=$data['userName']?>" />
								<div class="name"><?=$data['userName']?></div>	
								<div class="job"><?=$data['userJobPosition']?></div>
							</div>
						</div>
						<?
					}
				}
			  ?>
	</div>

		
	
	
	<!-- <? include_once("./footer.html") ?> -->
	
</body>
</html>