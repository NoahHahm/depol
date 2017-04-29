<?php

class Facebook {
	
	public function getUserJson($access_token) {
				
		$url = "https://graph.facebook.com/me?fields=id,name,email&access_token={$access_token}";
		
		$ch = curl_init();
		curl_setopt($ch, CURLOPT_URL, $url);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
		curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false);
		$result = curl_exec($ch);
		curl_close($ch);
		return json_decode($result);		
	}
	
}

?>