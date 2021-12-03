package Client;
// Author: Kidus Asmare Ayele. Scene to login as a client or admin.

import java.net.*;
import java.io.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginScene extends SceneBasic {
	private String username;				// Stores username of account trying to log in
	private String password;				// Stores password of account trying to log in
	private GridPane grid;					// Grid container for layout
	private Label errorLabel;				// Error label on the scene
	private Socket connection;				// Socket connection to server
	
	public LoginScene() {
		super("Login Menu");
		
		// Set the title of the scene
		Label title = new Label(super.titleText);
		title.setFont(new Font(40));
		
		Label usernameLabel = new Label("Username");
		usernameLabel.setFont(new Font(15));
		// Input field for the username
		TextField usernameInput = new TextField();
		
		Label passwordLabel = new Label("Password");
		passwordLabel.setFont(new Font(15));
		// Input field for the password
		PasswordField passwordInput = new PasswordField();
		
		Button loginButton = new Button("Login");
		// Onclick set the username and password variables to inputs and call the login method
		loginButton.setOnAction(e -> {this.username = usernameInput.getText();
									  this.password = passwordInput.getText();
									  login();});
		
		Button settingsButton = new Button("Setting");
//		 Onclick redirect the user to the settings scene
		settingsButton.setOnAction(e -> SceneManager.setSettingsScene());
		
		errorLabel = new Label("");
		errorLabel.setFont(new Font(10));
		errorLabel.setTextFill(Color.RED);
		
		grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10);	// Vertical margin between rows
		grid.setHgap(5);	// Horizontal margin between columns
		
		HBox buttonContain = new HBox(10,loginButton, settingsButton);
		
		// Sets the layout in the grid
		grid.add(usernameLabel, 0, 0);
		grid.add(usernameInput, 1, 0);
		grid.add(passwordLabel, 0, 1);
		grid.add(passwordInput, 1, 1);
		grid.add(buttonContain, 1, 2);
		grid.add(errorLabel, 1, 3);

		// Vertically contains all items within the scene
		VBox container = new VBox(30, title, grid);
		container.setAlignment(Pos.CENTER);

		// Positions the container at the center of the screen
		BorderPane root = new BorderPane(container);
		BorderPane.setAlignment(container, Pos.CENTER);
		
		super.scene = new Scene(root, 400, 300);	// Sets the scene
	}
	
	public void login() {
		try {
			if(SceneManager.getSocket() == null) {
				connection = new Socket("localhost", 3001);
				SceneManager.setSocket(connection);	
			}
			connection = SceneManager.getSocket();
			PrintWriter outgoing = new PrintWriter( new OutputStreamWriter(connection.getOutputStream()));
			outgoing.println("LOGIN");		// Request to login that will be sent to the server
			outgoing.println(username);		// The entered username
			outgoing.println(password);		// The entered password
			outgoing.flush();				// Information sent to the server
			BufferedReader incoming = new BufferedReader( new InputStreamReader(connection.getInputStream()));
			String loginRes = incoming.readLine();
			if(loginRes.equals("LOGGEDIN")) {
				SceneManager.setGameLobbyScene();
			} else if(loginRes.equals("PASS_ERROR")) {		// If there is a password error
				errorLabel.setText("Incorrect password");
			} else if(loginRes.equals("USER_ERROR")) {		// If there is a username error
				errorLabel.setText("Incorrect username");
			}
		} catch(Exception e) {
			errorLabel.setText("Error trying to connect to server");
		}
	}
}
