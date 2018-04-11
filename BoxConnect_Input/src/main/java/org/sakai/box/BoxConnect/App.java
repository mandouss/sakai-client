package org.sakai.box.BoxConnect;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxAPIException;
import com.box.sdk.BoxCollaboration;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem;

public class App {
	private static BoxAPIConnection connection;
	private BoxFolder root;
	static String ID ="zz1xp9c8r10msudlhmf8up4gwbt7s19s";
	static String secret = "5QN6jcJyXffvfXReHeG0lPYEj0bxboXk";
	static String access;
	static String refresh;
	

//Connect with Access Token, Client ID, Client Secret
public void Boxconnect() throws MalformedURLException {
	connection = new BoxAPIConnection(access);
	//set root
	BoxFolder rootFolder = BoxFolder.getRootFolder(connection);
	root = rootFolder;
}
//CODE BUILT FROM INSTRUCTION BY: https://www.tutorialspoint.com/postgresql/postgresql_java.htm
//Updates my Refresh and Access Tokens after POST request
public void PSQLupdate(String access, String refresh) {
    Connection c = null;
    Statement stmt = null;
    try {
    		c = DriverManager
          .getConnection("jdbc:postgresql://localhost:5432/db",
          "postgres", "passw0rd");
    		System.out.println("Opened database successfully");
       c.setAutoCommit(true);
       
       //UPDATE statement
       stmt = c.createStatement();
       String sql = "UPDATE TOKEN SET ACCESS_TOKEN = ?, REFRESH_TOKEN = ? WHERE ID=1;";
       PreparedStatement update = null;
       update = c.prepareStatement(sql);
       update.setString(1, access);
       update.setString(2, refresh);
       update.executeUpdate();

       
    } catch (Exception e) {
       e.printStackTrace();
       System.err.println(e.getClass().getName()+": "+e.getMessage());
       System.exit(0);
    }
 }
	
//Brings access and refresh tokens into memory from database for use in program
public void PSQL_GetToken() {
    Connection c = null;
    Statement state = null;
    ResultSet rs;
	try {
		c = DriverManager
		.getConnection("jdbc:postgresql://localhost:5432/db",
		"postgres", "passw0rd");
		System.out.println("Opened database successfully");
		c.setAutoCommit(true);
		state = c.createStatement();
		rs = state.executeQuery( "SELECT * FROM TOKEN;" );
	    while ( rs.next() ) {
	        int id = rs.getInt("ID");
	        //Update globals
	        	access = rs.getString("ACCESS_TOKEN");
	        	refresh = rs.getString("REFRESH_TOKEN");
	     }
	    	System.out.println("MY access token is: "+access);
	    	System.out.println("MY refresh token is: "+refresh);
	     //CLEANUP
	     rs.close();
	     state.close();
	     
	} catch (SQLException e) {
		e.printStackTrace();
	    System.err.println(e.getClass().getName()+": "+e.getMessage());
	    System.exit(0);
	}
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
//Connects to Box through POST method to get new Refresh and Access Tokens
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
		//Read the Response of this POST
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		while ((inputLine = in.readLine()) != null) {
		    content.append(inputLine);
		}
		String mystring = content.toString();
		String quote = "\"";
		int quote_index = mystring.indexOf(quote);
		while (quote_index >= 0) {
			indices.add(quote_index);
			quote_index = mystring.indexOf(quote, quote_index+1);
		}
		String myaccess = mystring.substring(indices.get(2)+1, indices.get(3));
		String myrefresh = mystring.substring(indices.get(10)+1, indices.get(11));
		System.out.println("Access found is: "+myaccess);
		System.out.println("Refresh found is: "+myrefresh);
		PSQLupdate(myaccess, myrefresh);
		in.close();
		//Print contents of Connection
//	    System.out.println("contents = " + mystring);
		
	} catch(MalformedURLException malformed) {
		System.out.println("Malformed URL: EXIT!");
		System.exit(0);
	} catch (IOException e) {
		System.out.println("Connection Object can't be made: EXIT!");
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.exit(0);
	}
}
	 
//Connect to root folder in account: KEEP
public void getroot() {
	root = BoxFolder.getRootFolder(connection);
}

//Print all Box folders in account
public void Boxprint() {
	for (BoxItem.Info itemInfo : root) {
	    System.out.format("[%s] %s\n", itemInfo.getID(), itemInfo.getName());
	}
}

//Create and return new folder in root directory: CATCHES EXCEPTION WHEN ATTEMPT TO CREATE EXISTING FOLDER
public BoxFolder.Info Box_newfolder(String name, String email, String Role) {
	BoxFolder empty = new BoxFolder(null, name); 
	try {
		BoxFolder.Info childFolder = root.createFolder(name);
		return childFolder;  		
	}catch (BoxAPIException e)	{
//		e.printStackTrace();
		System.out.println("Attempted to create a folder that already exists: TRY TO SHARE WITH PROFESSOR!");
		FindandShare(name, email, Role);
	}
	return empty.getInfo();
}

//Share folder with professor: Make Editor: KEEP
public void Editor_Share(BoxFolder contents, String email) {
	//give editor privileges
	contents.collaborate(email, BoxCollaboration.Role.EDITOR);
}

//FIND FOLDER AND SHARE IT
public void FindandShare(String name, String EMAIL, String ROLE) {
	for (BoxItem.Info itemInfo : root) {
		if (itemInfo.getName().equals(name)) {
		   System.out.println("We have found the correct folder: SHARE HERE!");
		   String ID = itemInfo.getID();
		   System.out.println("The name of my found folder is:"+itemInfo.getName());
		   BoxFolder SHARE = new BoxFolder(connection, ID);
    			try {
    				if (ROLE.equals("Student")) {
    					System.out.println("Student Sharing!");
	    				Viewer_Share(SHARE, EMAIL);				
    				}
    				else if (ROLE.equals("Instructor")) {
    					System.out.println("Instructor Sharing!");
    					Editor_Share(SHARE, EMAIL);
    				}
    			}catch (BoxAPIException e)	{
//        				e.printStackTrace();
        			System.out.println("Attempted to Add a Collaborator Whose Already There: LETS EXIT!");
        			System.exit(0);
        		}
		}
	}
}

//Share folder with 1 student: Make Viewer --> KEEP
public void Viewer_Share(BoxFolder contents, String email) {
	//give editor privileges
	contents.collaborate(email, BoxCollaboration.Role.VIEWER);
}

//Creates Professor's Folder in Box and Makes him Editor: KEEP
public void Professor_Create(String name, String email, String role) {

		//create a new folder under root
		BoxFolder.Info myInfo = Box_newfolder(name, email, role);
			//get info of folder
			String ID = myInfo.getID();
		
			//connect to our new folder using its ID
			BoxFolder newFolder = new BoxFolder(connection, ID);
		
			//share folder with professor's Box account
			Editor_Share(newFolder, email);		
	}	

	public static void main(String[] args) throws MalformedURLException {
		//String 1: Role String 2: Class String 3: Email
	     String Role = args[0];
	     String Class = args[1];
	     String Email = args[2];
		
//		create Box_test object
		App myaccount = new App();
		//Set access and refresh token global variables from database values
		myaccount.PSQL_GetToken();
		
//		Connect to BOX w/ Access Token
		 myaccount.Boxconnect();
		 


	     //Exceptions handled below --> Box allows you to connect with expired tokens
	     //The Box API will throw a connection if we attempt to access files using expired tokens
	     //When that exception is thrown we: 1. catch it 2.send POST to Token endpoint 3. update our
	     //refresh and access tokens 4. Connect with our new tokens 5. attempt to create a folder again
	     
      	//If an Instructor is sending POST --> Create folder
  		if (Role.equals("Instructor")) {
  			try {
  			myaccount.Professor_Create(Class, Email, Role);
  			} catch (BoxAPIException expired) {
  				System.out.println("My token is expired! --> I need to refresh!");
  				//create new instance and connection
  				App errorcase = new App();
  				//gets old access and refresh tokens
  				errorcase.PSQL_GetToken();
  				//sends POST request to refresh
  				errorcase.Send_Post();
  				//pull new tokens in database back into memory
  				errorcase.PSQL_GetToken();
  				//makes connections
  				errorcase.Boxconnect();
  				//attempt this function one more time
  	  			errorcase.Professor_Create(Class, Email, Role);
  			}
  		}
  		//If a Student is sending POST --> Share folder
  		else if(Role.equals("Student")) {
  			try {
  			myaccount.FindandShare(Class, Email, Role);
  			} catch (BoxAPIException expired) {
  				System.out.println("My token is expired! --> I need to refresh!");
  				//create new instance and connection
  				App errorcase = new App();
  				//gets old access and refresh tokens
  				errorcase.PSQL_GetToken();
  				//sends POST request to refresh
  				errorcase.Send_Post();
  				//pull new tokens in database back into memory
  				errorcase.PSQL_GetToken();
  				//makes connections
  				errorcase.Boxconnect();
  				//attempt this function one more time
  	  			errorcase.FindandShare(Class, Email, Role);
  			}
  		}	
	}
}
	

