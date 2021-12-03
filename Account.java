package Server;
// Author: Kidus Asmare Ayele. Abstract Account class which other account types inherit from.

public class Account {
	private int id;			// Account id
	private String username;	// Account username
	private String password;	// Account password
	private int level;

	public Account(int id, String username, String password, int level) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.level = level;
	}
	
	public String getLevel() {
		return Integer.toString(this.level);
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	// Getter method for account's id
	public String getID() {
		return Integer.toString(this.id);
	}

	// Getter method for account's username
	public String getUsername() {
		return this.username;
	}
	
	// Getter method for account's password
	public String getPassword() {
		return this.password;
	}

	// Verifies an account's password
	public boolean verifyPassword(String password) {
		return this.password.equals(password);
	}
	
	// Changes an account's password
	public void setPassword(String newPassword) {
		this.password = newPassword;
	}
	
	// Prints the account's username and type of account
	public String toString() {
		return "username: " + this.username + ", " + this.getClass();
	}
}
