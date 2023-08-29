
public class User {

		//Member variables need to be public in order to deserialize data from FireBase
	    public String name;
	    public String type;
	    public int id;
	    public boolean access;
	    
	    
	    public User() {}
	    
	    public User(String name, int id, boolean access, String type){
	    	this.name = name;
	        this.id = id;
	        this.access = access;
	        this.type = type; 
	    }
	    
	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getType() {
	        return type;
	    }

	    public void setType(String type) {
	        this.type = type;
	    }
	    
	    public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }

	    public boolean getAccess() {
	        return this.access;
	    }

	    public void setAccess(boolean access) {
	        this.access = access;
	    }
	    
	    public int compareTo(User other) {
	        return Integer.compare(this.id, other.id);
	    }
}
