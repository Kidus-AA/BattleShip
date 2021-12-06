
// Author: Jack P
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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

public class changeBackGroundColor extends SceneBasic {
	Button cancelButton = new Button("Back to GameSetting");
	Button redButton = new Button("Red");
	Button blueButton = new Button("Blue");
	Button yellowButton = new Button("Yellow");

	public changeBackGroundColor() {
		super("Choose one you like!");
		Label message = new Label(titleText);
		message.setFont(new Font(40));
		GridPane grid = new GridPane();

		int WIDTH = 150;
		// 3 buttons
		cancelButton.setMinWidth(WIDTH);
		redButton.setMinWidth(WIDTH);
		blueButton.setMinWidth(WIDTH);
		yellowButton.setMinWidth(WIDTH);

		cancelButton.setOnAction(e -> SceneManager.setGameSettingScene());
		redButton.setOnAction(e -> redChange());
		blueButton.setOnAction(e -> blueChange());
		yellowButton.setOnAction(e -> yellowChange());
		grid.add(redButton, 0, 0);
		grid.add(blueButton, 0, 1);
		grid.add(yellowButton, 0, 2);
		grid.add(cancelButton, 0, 3);
		// set pos
		grid.setVgap(10);
		grid.setAlignment(Pos.CENTER);
		// container to contain message
		VBox container = new VBox(10, message, grid);
		container.setAlignment(Pos.CENTER);
		// set scene

		scene = new Scene(container, 450, 250);
	}

	public void redChange() {
		try {
			Socket connection = SceneManager.getSocket(); // Server socket
			PrintWriter outgoing; // Stream for sending data.
			outgoing = new PrintWriter(connection.getOutputStream());
			outgoing.println("STORE_COLOR");
			outgoing.flush();
			// change
			outgoing.println("Red");
			outgoing.flush();
			SceneManager.setGameLobbyScene();
		} catch (Exception e) {
			System.out.println("Error:  " + e);
		}
	}

	public void blueChange() {
		try {
			Socket connection = SceneManager.getSocket(); // Server socket
			PrintWriter outgoing; // Stream for sending data.
			outgoing = new PrintWriter(connection.getOutputStream());
			outgoing.println("STORE_COLOR");
			outgoing.flush();
			// change
			outgoing.println("Blue");
			outgoing.flush();
			SceneManager.setGameLobbyScene();
		} catch (Exception e) {
			System.out.println("Error:  " + e);
		}
	}

	public void yellowChange() {
		try {
			Socket connection = SceneManager.getSocket(); // Server socket
			PrintWriter outgoing; // Stream for sending data.
			outgoing = new PrintWriter(connection.getOutputStream());
			outgoing.println("STORE_COLOR");
			outgoing.flush();
			// change
			outgoing.println("Yellow");
			outgoing.flush();
			SceneManager.setGameLobbyScene();
		} catch (Exception e) {
			System.out.println("Error:  " + e);
		}
	}

}
