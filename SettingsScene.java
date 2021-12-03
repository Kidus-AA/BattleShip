package Client;
// Author: Kidus Asmare Ayele. Scene to connect client to the server.

import java.net.Socket;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SettingsScene extends SceneBasic {
	private String host;				// Stores the entered host name
	private String port;				// Stores the entered port number
	private GridPane grid;					// Grid container for layout
	private Label errorLabel;
	private TextField hostInput;
	private TextField portInput;	
	
	public SettingsScene() {
		super("Connection Settings");
		
		// Set the title of the scene
		Label title = new Label(super.titleText);
		title.setFont(new Font(40));
		
		Label hostLabel = new Label("Host");
		hostLabel.setFont(new Font(15));
		// Input field for the host name
		hostInput = new TextField();
		
		Label portLabel = new Label("Port");
		portLabel.setFont(new Font(15));
		// Input field for the port number
		portInput = new TextField();
		
		Button applyButton = new Button("Apply");
		// Onclick set the host and port variables to inputs and call the login method
		applyButton.setOnAction(e -> {this.host = hostInput.getText();
									  this.port = portInput.getText();
									  apply();});
		
		Button clearButton = new Button("Clear");
		// Onclick resets all inputs and error message
		clearButton.setOnAction(e -> cancel());
		
		errorLabel = new Label("");
		errorLabel.setFont(new Font(10));
		errorLabel.setTextFill(Color.RED);
		
		grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10);	// Vertical margin between rows
		grid.setHgap(5);	// Horizontal margin between columns
		
		HBox buttonContain = new HBox(10,applyButton, clearButton);
		
		// Sets the layout in the grid
		grid.add(hostLabel, 0, 0);
		grid.add(hostInput, 1, 0);
		grid.add(portLabel, 0, 1);
		grid.add(portInput, 1, 1);
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
	
	// Sets the socket connection for the client to the server
	public void apply() {
		try {
			Socket connection = new Socket(host, Integer.parseInt(port));
			SceneManager.setSocket(connection);		// Sets the socket connection
			errorLabel.setText("");
			SceneManager.setLoginScene();
		} catch (Exception e) {
			errorLabel.setText("Error trying to connect to server");
		}
	}
	
	// Resets all inputs and error and redirects to the login scene
	public void cancel() {
		hostInput.setText("");
		portInput.setText("");
		errorLabel.setText("");
		SceneManager.setLoginScene();
	}
}
