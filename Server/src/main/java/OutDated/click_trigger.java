package OutDated;

import com.sun.corba.se.spi.activation.Server.LTI_message;	//change it before files are moved to new package
import com.sun.corba.se.spi.activation.Server.Box_helper;
import com.sun.deploy.net.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class click_trigger{
	//This is the function triggered when the Box button is clicked on Sakai
	//and our server receives a message from LTI
    public static void Redirect(String url, LTI_message msg){
		//Send the folder url back to sakai
		Map<String,String> map = new HashMap<>();

		//use the format that LTI accept
		map.put("url",url);

		HttpRequest httpRequest = HttpRequest.post(msg.return_url,map,Boolean.TRUE);
		String result = httpRequest.body();
    }


	public static void main(String []args){
		LTI_message msg=new LTI_message(args);

		//interact with box(like creating folder)
		Box_Helper Box=new Box_Helper(msg);	//can use Nisarg's Box_test
        String Folder_url=Box.folderurl();

		//Send the folder url back to sakai
        Redirect(Folder_url,msg);
	}
}