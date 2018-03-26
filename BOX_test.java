import java.io.FileReader;
import java.io.Reader;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxConfig;
import com.box.sdk.BoxDeveloperEditionAPIConnection;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem;
import com.box.sdk.BoxUser;

public class BOX_test {
	private BoxAPIConnection connection;
	private BoxFolder root;
	
	
	
//connect to Box Developer Token
public void Boxconnect(String dev_token) {
	BoxAPIConnection api = new BoxAPIConnection(dev_token);
	connection = api;
}

//connect to root folder in account
public void getroot() {
	root = BoxFolder.getRootFolder(connection);
}

//print all Box folders in account
public void Boxprint() {
	for (BoxItem.Info itemInfo : root) {
	    System.out.format("[%s] %s\n", itemInfo.getID(), itemInfo.getName());
	}
}

//create and return new folder in root directory
public BoxFolder.Info Box_newfolder(String name) {
	BoxFolder.Info childFolder = root.createFolder(name);
	return childFolder;
}
	
	//main method 
	public static void main(String[] args) {
		//Section for use with Developer Token: testing
		
////		create Box_test object
		BOX_test myaccount = new BOX_test();
////		connect using Developer Token
		myaccount.Boxconnect("7wFi6vkySSZbHrGBWyvdQrjwC5shC3Sv");
////		connect to root
		myaccount.getroot();
////		print all folders from root Folder
		myaccount.Boxprint();
//		
////		create a new folder in root
//		BoxFolder.Info child = myaccount.Box_newfolder("New Sakai Folder 3");
//		
//		
////		print all contents again
		System.out.println("Printing folders after making a new one");
		myaccount.Boxprint();
		

	}
}
	

