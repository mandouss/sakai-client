import java.sql.*;

public class Professor_Click {

    static final String USER = "root";
    static final String PASS = "123456";

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/RUNOOB";

    public static void Other_Click(String Course_ID){

    }

    public static void main(String []args){
        LTI_message msg= new LTI_message("123456",
                "sakai.duke.edu","Instructor",
                "yd94@duke.edu","ECE_651");

        Box_Helper=box();
        Connection conn=null;
        Statement stmt=null;

        try {

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String search;
            search = "SELECT FOLDER_ID FROM COURSE_FOLDER WHERE COURSE_ID='" + msg.course_id + "'";

            stmt = conn.createStatement();
            ResultSet rs =stmt.executeQuery(search);

            if(rs.next()) {
                String Folder_ID = rs.getString("FOlDER_ID");
                Other_Click(Folder_ID);
            }else {
                //not find
                String folder_id=box.Create_New_Folder(msg.course_id,msg.Course_Name);
                String insert;
                insert="INSERT INTO COURSE_FOLDER VALUES ('"+msg.course_id+"',"+folder_id+")";
                stmt.executeUpdate(insert);
            }


        }catch (SQLException se){
            se.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
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
        }
    }
}
