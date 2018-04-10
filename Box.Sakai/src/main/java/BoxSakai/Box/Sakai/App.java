package BoxSakai.Box.Sakai;
import com.box.sdk.*;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * Hello world!
 *
 */
public class App {
	private static BoxDeveloperEditionAPIConnection connection;
	private BoxFolder root;
	
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
//    			e.printStackTrace();
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
//    	        				e.printStackTrace();
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
    	
    
    	public void Authenticate() {
    	      // Open a reader to read and dispose of the automatically created Box configuration file.
         	try(Reader reader = new FileReader("src/main/java/myconfig.json")) {
          			// Initialize the SDK with the Box configuration file and create a client that uses the Service Account.
    				BoxConfig boxConfig = BoxConfig.readFrom(reader);
    				connection = BoxDeveloperEditionAPIConnection.getAppEnterpriseConnection(boxConfig);
    				BoxUser serviceAccountUser = BoxUser.getCurrentUser(connection);
    				BoxUser.Info serviceAccountUserInfo = serviceAccountUser.getInfo();              
    				System.out.println("My App Name is: "+serviceAccountUserInfo.getName());
    				BoxFolder rootFolder = BoxFolder.getRootFolder(connection);
    				root = rootFolder;
                   
         	} catch (java.io.IOException e) {
              // Log any errors for debugging 
         		e.printStackTrace();
         	}
    	}	
    	
    public static void main( String[] args ) {
     	App myapp = new App();
     	//JSON Authentication
     	myapp.Authenticate();
     	//parameters passed to JAR: 1. ROLE 2. CLASS 3. EMAIL
//     	String Role = args[0];	
//     	String Class = args[1];
//     	String Email = args[2];
     	String Role = "Instructor";
     	String Class = "BETA";
     	String Email = "nrd10@duke.edu";
     	//Create:
     	
         	//If an Instructor is sending POST --> Create folder
     		if (Role.equals("Instructor")) { 
     			myapp.Professor_Create(Class, Email, Role);
     		}
     		//If a Student is sending POST --> Share folder
     		else if(Role.equals("Student")) {
     			myapp.FindandShare(Class, Email, Role);
     		}	     		
    }
}
