package com.boxplayground;
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
	
    //function to input "Create" or "Share"
    	public  String Create_or_Share(Scanner sc) {
    		System.out.println("Do you want to create a new folder, or share an existing folder?");
    		System.out.println("Enter your choice: ");
    		String choice=sc.next();  
    
    		while(true) {
    			if ((!choice.equals("Create")||(!choice.equals("Share")))) {
    				if (choice.equals("Create")||(choice.equals("create"))) {
    					break;
    				}
    				if (choice.equals("Share")||(choice.equals("share"))) {
    					break;
    				}
    				System.out.println("Enter a valid option: ");
    				choice = sc.next();
    			}
    		}
    		return choice;
    	}
    
    	//function to add emails to specify who to share with
    	public ArrayList<String> Email_List(Scanner scan) {
    		ArrayList<String> roster = new ArrayList<String>();
    		String name = "hello";
    		System.out.println("Enter emails of who you would like to share this folder with separated by newlines: ");
    		name = scan.nextLine();
    		while (true) {
    			if (name.equals("exit")) {
    				break;
    			}
    			if (name.length() != 0) {
        			roster.add(name);
    			}
        		name = scan.nextLine();

    		}
//    		for (String s: roster) {
//    			System.out.println("My arraylist entry is: "+s);
//    			System.out.println("My arraylist entry length is: "+s.length());
//    		}
    		
    		return roster;
    	}
    //function to get shareable link
    	public String Share_Link(Scanner scan) {
    		System.out.println("Enter shared link: ");
    		String link = scan.next();
    		System.out.println("Your link is: "+link);
    		return link;
    	}

    //function to get email  
    	public String Get_Email(Scanner scan) {
    		System.out.println("Enter professor email: ");
    		String email = scan.next();
    		System.out.println("Your email is: "+email);
    		return email;
    	}   	
    	
    //function to get folder name  
    	public String Get_Name(Scanner scan) {
    		System.out.println("Enter folder name: ");
    		String folder = scan.next();
    		System.out.println("Your folder is: "+folder);
    		return folder;
    	} 
    	
    	
    	//Connect to Box Account with JSON Web Toekn
    	public void Boxconnect(BoxConfig myconfig) {
        connection = BoxDeveloperEditionAPIConnection.getAppEnterpriseConnection(myconfig);
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
    		System.out.println("The name of the folder we are sharing through a Shared Link is: "+itemInfo.getName());
    			
    		
 
    		//connect to existing folder using ID
//    		BoxFolder existing = new BoxFolder(connection, ID);
    		
    		//Share with students
//    		Share_With_Students(existing, students);
    	}
    
    	
    public static void main( String[] args ) {
     	App myapp = new App();

      // Open a reader to read and dispose of the automatically created Box configuration file.
     	try(Reader reader = new FileReader("src/main/java/myconfig.json")) {
      			// Initialize the SDK with the Box configuration file and create a client that uses the Service Account.
				BoxConfig boxConfig = BoxConfig.readFrom(reader);
				myapp.connection = BoxDeveloperEditionAPIConnection.getAppEnterpriseConnection(boxConfig);
				
				// Use the getCurrentUser method to retrieve current user's information.
				// Since this client uses the Service Account, this will return the Service Account's information.
				BoxUser serviceAccountUser = BoxUser.getCurrentUser(connection);
				BoxUser.Info serviceAccountUserInfo = serviceAccountUser.getInfo();
				// Log the Service Account's login value which should contain "AutomationUser". 
				System.out.println("My login value is: "+serviceAccountUserInfo.getLogin());
          
				System.out.println("My name is: "+serviceAccountUserInfo.getName());

				BoxFolder rootFolder = BoxFolder.getRootFolder(connection);
				myapp.root = rootFolder;
				for (BoxItem.Info itemInfo : rootFolder) {
          			System.out.format("[%s] %s\n", itemInfo.getID(), itemInfo.getName());
      			}
               
     	} catch (java.io.IOException e) {
          // Log any errors for debugging 
     		e.printStackTrace();
     	}
    	
     	Scanner scanner = new Scanner(System.in);
     	ArrayList<String> roster = new ArrayList<String>();
     	String myentry = myapp.Create_or_Share(scanner);
     	System.out.println("Your choice is: "+myentry);
	
     	//Ask whether Create or Share
     	
     	//Share:
     	if ((myentry.equals("Share"))||(myentry.equals("share"))) {
     		// Ask for a shareable link
     		String mylink = myapp.Share_Link(scanner);
     		//Ask for list of students to share folder with
     		roster = myapp.Email_List(scanner);
		
     		//Share folder with those students 
     		myapp.Share_Existing_Folder(mylink, roster);
     	}
	
     	//Create:
     	else if ((myentry.equals("Create"))||(myentry.equals("create"))) {
     		// Show current list of folders and prompt that a folder with the same name cannot be created
     		myapp.Boxprint();
     		System.out.println("Do not choose any of the names above as two folders in Box cannot have the same name!");
     		
         	//Get Professor email
         	String myemail = myapp.Get_Email(scanner);
         	
         	//Get Folder name
         	String myfolder = myapp.Get_Name(scanner);
         	
         	//Ask for list of students to share folder with
         	ArrayList<String> students = myapp.Email_List(scanner);
         	
         	//Create folder
         	BoxFolder newfolder = myapp.Professor_Create(myfolder, myemail);
         	
         	//Share folder with list of students
         	myapp.Share_With_Students(newfolder, students);
     	}
     	
    }
}
