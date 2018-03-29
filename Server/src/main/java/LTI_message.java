
public class LTI_message{
	//this is the class that save the message from LTI, which contains information about the people and the course and the way to communicate with LTI
	String context_id;	//unique id for account and each course
	String return_url;	//send message to this url(Sakai)
	String roles;		//Instructor, Student
	String person_email;//email of this account

	public LTI_message(String context_id,String return_url,String roles,String person_email){
		//constructor
		this.context_id=context_id;
		this.return_url=return_url;
		this.roles=roles;
		this.person_email=person_email;
	}
}