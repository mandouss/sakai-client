import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxCollaboration;
import com.box.sdk.BoxConfig;
import com.box.sdk.BoxDeveloperEditionAPIConnection;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem;
import com.box.sdk.BoxUser;

public class BOX_test {
	private static BoxAPIConnection connection;
	private BoxFolder root;
	
	
//Connect to Box Account with Developer Token
public void Boxconnect(String dev_token) {
	BoxAPIConnection api = new BoxAPIConnection(dev_token);
	connection = api;
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

	//main method 
	public static void main(String[] args) {
//		Section for use with Developer Token
		
//		create Box_test object
		BOX_test myaccount = new BOX_test();
		
//		REPLACE DEVELOPER TOKEN: LINE 105
		myaccount.Boxconnect("1tn7X8bQQXK4hIHtWCEvzg9sAyX6O6gz");
//		connect to root
		myaccount.getroot();
//		print all folders from root Folder
		myaccount.Boxprint();
		
//		create Folder for Professor, share with Professor giving admin rights Test: WORKS
		BoxFolder folder = myaccount.Professor_Create("Sigma", "nrd10@duke.edu");
		
		//share folder with list of student Test: WORKS 
		ArrayList<String> mystudents = new ArrayList<String>();
		mystudents.add("zw73@duke.edu");
		myaccount.Share_With_Students(folder, mystudents);
		
		String shared = "https://app.box.com/s/u7hjt2y1cpbr69p3oeyrtin8rz1x9rql";
		
		//Share Existing Folder Test: WORKS
		myaccount.Share_Existing_Folder(shared, mystudents);
		
//		print all contents again
		System.out.println("Printing folders after new one made ");
		myaccount.Boxprint();

	}
}
	

