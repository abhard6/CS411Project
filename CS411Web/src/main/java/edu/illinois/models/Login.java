/**
 * 
 */
package edu.illinois.models;

/**
 * @author ashutoshbhardwaj
 *
 */
public class Login {
	
	private String userName;
	private String password;
	private String message;
	private boolean flag;
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setmessage(String message){
		
		this.message = message;
	}
	
	public void setValidUser(boolean flag){
		this.flag = flag;
	}

}
