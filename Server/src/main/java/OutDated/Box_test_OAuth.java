package OutDated;

import com.box.sdk.*;

public class Box_test_OAuth {
    public static void main(String []args){
        BoxAPIConnection api = new BoxAPIConnection("2NK8YZw8tkbPKhcsf4RIMteXXL2PKb7Y");
        BoxFolder rootFolder = BoxFolder.getRootFolder(api);
        for (BoxItem.Info itemInfo : rootFolder) {
            System.out.format("[%s] %s\n", itemInfo.getID(), itemInfo.getName());
        }
    }
}
