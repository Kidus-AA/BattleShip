//Author : Jack Peng
public class Account {
	private String username;
	private String password;
	private String backGroundColor;
	private int id;

	// Constructor for account
	public Account(int id, String username, String password, String backGroundColor) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.backGroundColor = backGroundColor;
	}

	public String getID() {
		return id + "";
	}

	// username getter
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	// verify password
	public Boolean verifyPassword(String password) {
		if (this.password.equals(password)) {
			return true;
		} else {
			return false;
		}

	}

	// print username and class type
	public String toString() {
		String result = "Username: " + username + ", " + this.getClass();
		return result;
	}

	// set a new password
	public void setPassword(String newPassword) {
		password = newPassword;

	}

	public void setUsername(String newUsername) {
		username = newUsername;
	}

	public String getBackGroundColor() {
		return backGroundColor;
	}

	public void setNewColor(String newColor) {
		backGroundColor = newColor;
	}

}
