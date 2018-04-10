
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxAPIException;
import com.box.sdk.BoxCollaboration;
import com.box.sdk.BoxConfig;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem;
import com.box.sdk.BoxAPIConnection;

public class BOX_test {
	private static BoxAPIConnection connection;
	private BoxFolder root;
	static String ID ="zz1xp9c8r10msudlhmf8up4gwbt7s19s";
	static String secret = "5QN6jcJyXffvfXReHeG0lPYEj0bxboXk";
	static String access = "yjsUBlkqhkqB2mB2RGtjrY6eTQEFjN4h";
	static String refresh = "u1UWOpJQZWA0mIVzp1JLhe1620luGgc66ESorzi6xihWJBvYrvQcY6QEI5axZZuf";
	

//Connect with Access Token, Client ID, Client Secret
public void Boxconnect(String accessToken) {
	try {
	BoxAPIConnection api = new BoxAPIConnection(accessToken);
	connection = api;
	} catch(BoxAPIException expired) {
		System.out.println("The current Access token is expired! We need to refresh!");
		//CALL REFRESH
	}
}

//Connect to root folder in account
public void getroot() {
	root = BoxFolder.getRootFolder(connection);
}

//Print all Box folders in account
public void Boxprint() {
	for (BoxItem.Info itemInfo : root) {
	    System.out.format("[%s] %s\n", itemInfo.getID(), itemInfo.getName());
	}
}

//Create and return new folder in root directory
public BoxFolder.Info Box_newfolder(String name) {
	BoxFolder.Info childFolder = root.createFolder(name);
	return childFolder;
}

//Share folder with professor: Make Editor
public void Editor_Share(BoxFolder contents, String email) {
	//give editor privileges
	contents.collaborate(email, BoxCollaboration.Role.EDITOR);
}

//Share folder with students: Make Viewer
public void Viewer_Share(BoxFolder contents, String email) {
	//give editor privileges
	contents.collaborate(email, BoxCollaboration.Role.VIEWER);
}

//Creates Professor's Folder in Box and Makes him Editor
public BoxFolder Professor_Create(String name, String email) {
	//create a new folder under root
	BoxFolder.Info myInfo = Box_newfolder(name);
	
	//get info of folder
	String ID = myInfo.getID();
	
	//connect to our new folder using its ID
	BoxFolder newFolder = new BoxFolder(connection, ID);
	
	//share folder with professor's Box account
	Editor_Share(newFolder, email);
	
	return newFolder;
}

//Share A Created Folder with Students as Viewers
public void Share_With_Students(BoxFolder classfolder, ArrayList<String> names) {
	for (String student: names) {
		Viewer_Share(classfolder, student);
	}
}


//Share Existing Folder with Students as Viewers using Shared Link
public void Share_Existing_Folder(String link, ArrayList<String> students) {
	
	//get info of folder using shared link
	BoxItem.Info itemInfo = BoxItem.getSharedItem(connection, link);
	String ID = itemInfo.getID();
	
	//print out folder name
	System.out.println("The folder we are sharing through a Shared Link is: "+itemInfo.getName());
	
	//connect to existing folder using ID
	BoxFolder existing = new BoxFolder(connection, ID);
	
	//Share with students
	Share_With_Students(existing, students);
}


//METHOD OBTAINED FROM following URL: http://www.baeldung.com/java-http-request
public static String getParamsString(Map<String, String> params) 
	      throws UnsupportedEncodingException{
	        StringBuilder result = new StringBuilder();
	 
	        for (Map.Entry<String, String> entry : params.entrySet()) {
	          result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
	          result.append("=");
	          result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
	          result.append("&");
	        }
	 
	        String resultString = result.toString();
	        return resultString.length() > 0
	          ? resultString.substring(0, resultString.length() - 1)
	          : resultString;
	    }

//METHOD CODE built with help from the following URL: http://www.baeldung.com/java-http-request
//SEND POST Request to Get Access and Refresh
public void Send_Post() throws MalformedURLException {
	try {
		//set token URL
		URL url = new URL("https://api.box.com/oauth2/token");
		//open connection and set method to POST
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		//Set Request Header: Content-Type
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);
		
		//Add Request Parameters
		Map<String, String> parameters = new HashMap<>();
		//1. Grant Type
		parameters.put("grant_type","refresh_token");
		//2. Refresh Token
		parameters.put("refresh_token", refresh);
		//3. Client ID
		parameters.put("client_id", ID);
		//4. Client Secret
		parameters.put("client_secret", secret);
		con.setDoOutput(true);
		DataOutputStream out = new DataOutputStream(con.getOutputStream());
		out.writeBytes(getParamsString(parameters));
		out.flush();
		out.close();
		
		

		//Execute Request
		int status = con.getResponseCode();
		System.out.println("My response code is:"+status);
		String key1;
		String val1;
		String key2;
		String val2;
		String key3;
		String val3;
		String key4;
		String val4;
		//Read the Response of this POST
		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
		    content.append(inputLine);
		}
		String mystring = content.toString();
		String quote = "\"";
		int quote_index = mystring.indexOf(quote);
		while (quote_index >= 0) {
			System.out.println("My index is:"+quote_index);
			quote_index = mystring.indexOf(quote, quote_index+1);
		}
		in.close();
	    System.out.println("contents = " + content);
		System.out.println("Contents as a string is:"+mystring);
		//Print contents of Connection
		
		
	} catch(MalformedURLException malformed) {
		System.out.println("Malformed URL: EXIT!");
		System.exit(0);
	} catch (IOException e) {
		System.out.println("Connection Object can't be made: EXIT!");
		System.exit(0);
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

	//main method 
	public static void main(String[] args) throws MalformedURLException {
//		Section for use with Developer Token
		
//		create Box_test object
		BOX_test myaccount = new BOX_test();

		
		
//		////	Use of Access Token for Box Connect: Valid for 60 minutes --> IF exception --> 
//		//SEND POST REQUEST TO REFRESH TO RECEIVE NEW REFRESH AND ACCESS TOKEN
//		myaccount.Boxconnect(access);
//		
//		//connect to root
//		myaccount.getroot();
////		print all folders from root Folder
//		myaccount.Boxprint();
//		
		myaccount.Send_Post();
		
	}
}
	

