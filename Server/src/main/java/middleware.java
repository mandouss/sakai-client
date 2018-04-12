import java.util.*;

public class middleware {
    //HashMap<String, String> course_id_folder_id_map;

    boolean connect() { //Fangyuan Sun
        return true;
    }

    //Please realize the three functions in Box_helper class
    /*boolean Check_Course_id(String course_id) {  //Fangyuan Sun  return true;  }

        String Get_Folder_id(String course_id){return "";}

        String Create_New_Folder(String course_id, String course_name) {  //Sihao Yao        return "";    }

            String Get_Folder_Info(String folder_id) {  //Luoyi Li        return "";    }*/

    public static void main(String[] args) {
        middleware my_middleware = new middleware();
        LTI_message test_msg = new LTI_message("ECE651_ID_12345", "127.0.0.1", "professor", "wz88@duke.edu", "ECE_651");

        Box_Helper helper = new Box_Helper();
        String folder_id;
        if (test_msg.roles.equals("professor")) {
            Professor professor=new Professor();
            Professor.Click(test_msg,helper);
            /*if (!my_middleware.check_course_id_exist(test_msg.course_id)) {
                folder_id = my_middleware.box_create_folder(test_msg.Course_Name);
                my_middleware.course_id_folder_id_map.put(test_msg.course_id, folder_id);
            } else {
                // course_id exists, then folder_id must exist
                folder_id = my_middleware.course_id_folder_id_map.get(test_msg.course_id);
            }
            my_middleware.box_get_folder_info(folder_id);*/
        }else (test_msg.roles.equals("student")){
            Student student=new Student();
            student.Click(test_msg,helper);
        }
    }
}
