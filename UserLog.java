
public class UserLog {

		//Member variables
	    protected String name;
	    protected String in_out;
	    protected String timestamp;
	    protected int id;
	    
	    //Overloaded constructor
	    public UserLog(String name, String in_out, String timestamp, int id){
	    	this.name = name;
	        this.id = id;
	        this.timestamp = timestamp;
	        this.in_out = in_out; 
	    }
	    
}
