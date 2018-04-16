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

#print the parsed info
echo '[context_id] '.$context_id . "</br>";
echo '[launch_presentation_return_url] '. $return_url . "</br>";
echo '[lis_person_sourcedid] '. $personal_email . "</br>";
echo '[ext_sakai_role] '. $role . "</br>";
echo '[context_title]' . $context_title. "</br>";
echo '[ext_sakai_launch_presentation_css_url_list]' . $ext_url_list. "</br>";
echo '[launch_presentation_css]' . $launch_css. "</br>";
echo '[ext_ims_lti_tool_setting_url]' . $ext_setting_url. "</br>";
echo '[ext_ims_lti_memberships_url]' . $ext_mem_url. "</br>";

# execute JAVA file
echo exec("java HelloWorld") . "</br>";

/*
print "**********";
print "https://duke.app.box.com/folder/0" . "</br>";

print "print out the entire post pack </br>";
print_r($_POST);
print "</br>";

$instructor1 = $_POST['context_id'];
echo htmlspecialchars($instructor1) . "</br>";
*/

#echo $output;

#$ch = curl_init("https://duke.app.box.com/folder/0");
#curl_exec($ch);

header("Location:" . $return_url); 
?>
