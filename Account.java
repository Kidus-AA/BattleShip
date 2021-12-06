package Server;
// Author: Kidus Asmare Ayele. Abstract Account class which other account types inherit from.

public class Account {
	private int id;			// Account id
	private String username;	// Account username
	private String password;	// Account password
	private int level;
	private String backgroundColor;

	public Account(int id, String username, String password, int level, String backgroundColor) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.level = level;
		this.backgroundColor = backgroundColor;
	}
	
	public String getBackgroundColor() {
		return this.backgroundColor;
	}
	
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
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
	
	public void setUsername(String newUsername) {
		this.username = newUsername;
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
