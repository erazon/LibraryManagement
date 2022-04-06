package dataaccess;

import java.io.Serializable;

final public class User implements Serializable {
	
	private static final long serialVersionUID = 5147265048973262104L;

	private String id;
	
	private String password;
	private Auth authorization;
	public User(String id, String pass, Auth  auth) {
		this.id = id;
		this.password = pass;
		this.authorization = auth;
	}
	
	public String getId() {
		return id;
	}
	public String getPassword() {
		return password;
	}
	public Auth getAuthorization() {
		return authorization;
	}
	public boolean equals(Object ob) {
		if(ob == null) return false;
		if(!(ob instanceof User)) return false;
		User u = (User)ob;
		return id.equals(u.id) && password.equals(u.password); 
	}
	@Override
	public String toString() {
		return "[" + id + ":" + password + ", " + authorization.toString() + "]";
	}
	
}
