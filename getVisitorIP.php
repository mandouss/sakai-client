<html>
<link rel="stylesheet" href="https://cdn01.boxcdn.net/webapp_assets/css/enduser-embed-widget-fa993393e8.css">

<?PHP
function getUserIP()
{
    $client  = @$_SERVER['HTTP_CLIENT_IP'];
    $forward = @$_SERVER['HTTP_X_FORWARDED_FOR'];
    $remote  = $_SERVER['REMOTE_ADDR'];

    if(filter_var($client, FILTER_VALIDATE_IP))
    {
        echo "1.";
        $ip = $client;
    }
    elseif(filter_var($forward, FILTER_VALIDATE_IP))
    {
        echo "2.";
        $ip = $forward;
    }
    else
    { 
        echo "3.";
        $ip = $remote;
    }

    return $ip;
}


$user_ip = getUserIP();

echo "Welcome to Ziyi's Web Page, ";
print "your IP is: ";
echo $user_ip . "</br>"; 

# parse important info from the POST request
$context_id = $_POST['context_id'];
$return_url = $_POST['launch_presentation_return_url'];
$personal_email = $_POST['lis_person_sourcedid'];
$role = $_POST['ext_sakai_role'];
$context_title = $_POST['context_title'];
$ext_url_list = $_POST['ext_sakai_launch_presentation_css_url_list'];
$launch_css = $_POST['launch_presentation_css_url'];
$ext_setting_url = $_POST['ext_ims_lti_tool_setting_url'];
$ext_mem_url = $_POST['ext_ims_lis_memberships_url'];
$lti_message_type = $_POST['lti_message_type'];

#print the parsed info
echo '[context_id] '. $context_id . "</br>";
echo '[launch_presentation_return_url] '. $return_url . "</br>";
echo '[lis_person_sourcedid] '. $personal_email . "</br>";
echo '[ext_sakai_role] '. $role . "</br>";
echo '[context_title]' . $context_title. "</br>";
echo '[ext_sakai_launch_presentation_css_url_list]' . $ext_url_list. "</br>";
echo '[launch_presentation_css]' . $launch_css. "</br>";
echo '[ext_ims_lti_tool_setting_url]' . $ext_setting_url. "</br>";
echo '[ext_ims_lti_memberships_url]' . $ext_mem_url. "</br>";

$context_title = str_replace(' ','_',$context_title);
echo '[context_title]' . $context_title."</br>";

# execute JAVA file
$java_return = exec("java -jar BoxInputv2.jar " .$role. " ".$context_title." ".$personal_email)."</br>";

# remove the last 5 digits "</br>"
$java_return = substr($java_return,0,-5); 

if(empty($java_return)){
 header('Location: https://duke.app.box.com/embed_widget/files/0/f/0');
 exit;
}
else{
 header("Location: ".$java_return);
 exit; 
}

/*
# send a post request to somewhere
echo "Try sending a POST request to the return url". "</br>";
$ch = curl_init($return_url);

$content = array(
   "lti_message_type"=>"ContentItemSelectionRequest",   
   "lti_version" =>"LTI-1p0",
   "accept_media_types"=> "image/*",
   "accept_presentation_document_targets"=>"iframe",
   "content_item_return_url"=>$return_url,
);


$content = http_build_query($content);
$context_options = array(
   'http' => array(
      'method'=> 'POST',
      'header' => "Content-type: application/x-www-form-urlencoded\r\n" . "Content-Length: ".strlen($content)."\r\n",
      'content' => $content,
  )
);
$context = stream_context_create($context_options);

curl_setopt($ch, CURLOPT_POST, 1);
curl_setopt($ch, CURLOPT_POSTFIELDS, $content);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

$response = curl_exec($ch);

curl_close($ch);

echo $response . "</br>";
*/
?>


</html>
