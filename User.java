
public class User {

		//Member variables
	    protected String name;
	    protected String type;
	    protected int id;
	    protected boolean access;
	    
	    //Overloaded constructor
	    public User(String name, int id, boolean access, String type){
	    	this.name = name;
	        this.id = id;
	        this.access = access;
	        this.type = type; 
	    }
	    
}
