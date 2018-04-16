import java.sql.*;

public class Professor {

    public static void Other_Click(String Course_ID){

    }

    public static void Click(LTI_message msg, Box_Helper box){
        /*LTI_message msg= new LTI_message("123456",
                "sakai.duke.edu","Instructor",
                "yd94@duke.edu","ECE_651");
        */

        //Box_Helper=box();
        Connection conn=null;
        Statement stmt=null;

        try {

            if(box.Check_Course_id(msg.course_id)) {
                //found
                String Folder_ID = box.Get_Folder_id(msg.course_id);
                Other_Click(Folder_ID);
            }else {
                //not find
                String folder_id=box.Create_New_Folder(msg.course_id,msg.Course_Name);
                /*String insert;
                insert="INSERT INTO COURSE_FOLDER VALUES ('"+msg.course_id+"',"+folder_id+")";
                stmt.executeUpdate(insert);*/
            }
        }catch (Exception e){
            e.printStackTrace();
        }/*finally {
            // close resource
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// do nothing
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }*/
    }
}
