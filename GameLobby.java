
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

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

public class GameLobby extends SceneBasic{
	private String username;				// Stores username of account trying to log in
	private String password;				// Stores password of account trying to log in
	private GridPane grid;					// Grid container for layout
	private Label waitingLabel;				// Error label on the scene
	private Socket connection;				// Socket connection to server
	
	public GameLobby() {
		super("GameLobby");
		
		// Set the title of the scene
		Label title = new Label(super.titleText);
		title.setFont(new Font(40));
		
		Button startButton = new Button("START");
		// Onclick set the username and password variables to inputs and call the login method
		startButton.setOnAction(e -> playerReady());
		
		Button gameSettingsButton = new Button("Game Settings");
//				 Onclick redirect the user to the settings scene
		gameSettingsButton.setOnAction(e -> SceneManager.setGameSettingScene()); //edit by Jack
		
		waitingLabel = new Label("");
		waitingLabel.setFont(new Font(30));
		
		grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10);	// Vertical margin between rows
		grid.setHgap(5);	// Horizontal margin between columns
		
		HBox buttonContain = new HBox(10, startButton, gameSettingsButton);
		
		// Sets the layout in the grid
		grid.add(buttonContain, 1, 2);
		grid.add(waitingLabel, 1, 3);

		// Vertically contains all items within the scene
		VBox container = new VBox(30, title, grid);
		container.setAlignment(Pos.CENTER);

		// Positions the container at the center of the screen
		BorderPane root = new BorderPane(container);
		BorderPane.setAlignment(container, Pos.CENTER);
		
		super.scene = new Scene(root, 400, 300);	// Sets the scene
	}
	
	public void playerReady() {
		try {
			connection = SceneManager.getSocket();
			PrintWriter outgoing = new PrintWriter( new OutputStreamWriter(connection.getOutputStream()));
			outgoing.println("INITG");		// Request to login that will be sent to the server
			outgoing.flush();				// Information sent to the server
			waitingLabel.setText("WAITING FOR OTHER PLAYERS...");
			BufferedReader incoming = new BufferedReader( new InputStreamReader(connection.getInputStream()));
			String runGame = incoming.readLine();
			if(runGame.equals("READY")) {
				SceneManager.setGameScene();
			}
		} catch(Exception e) {
			System.out.println("PLAYER READY ERROR: " + e);
		}
	}
	
}
