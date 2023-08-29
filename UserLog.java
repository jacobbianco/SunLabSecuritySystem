import com.google.cloud.Timestamp;

public class UserLog {

		//Member variables
	    protected String name;
	    protected String in_out;
	    protected Timestamp timestamp;
	    protected int id;
	    
	    public UserLog() {}
	    
	    //Overloaded constructor
	    public UserLog(String name, String in_out, Timestamp timestamp, int id){
	    	this.name = name;
	        this.id = id;
	        this.timestamp = timestamp;
	        this.in_out = in_out; 
	    }
	    
	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getIn_out() {
	        return in_out;
	    }

	    public void setIn_out(String in_out) {
	        this.in_out = in_out;
	    }

	    public Timestamp getTimestamp() {
	        return timestamp;
	    }

	    public void setTimestamp(Timestamp timestamp) {
	        this.timestamp = timestamp;
	    }

	    public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }
}
