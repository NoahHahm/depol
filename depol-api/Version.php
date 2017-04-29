<?PHP
	require_once ("./class/Consts.php");
	$arr = array(Consts::RESULT => Consts::SUCCESS, "version" => "v 1.0.0");	
	$json = json_encode($arr);
	echo $json;	
?>