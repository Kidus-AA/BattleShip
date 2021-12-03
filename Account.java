//Author : Jack Peng
public class Account {
	private String username;
	private String password;
	private String nickName;
	private String backGroundColor;
	private int	id;

	// Constructor for account
	public Account(int id, String username, String password,String nickName,String backGroundColor) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.nickName = nickName;
		this.backGroundColor = backGroundColor;
	}

	public String getID() {
		return id+"";
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
	public String getNickName() {
		return nickName;
	}
	
	public String getBackGroundColor() {
		return backGroundColor;
	}

}
