//Author Jack
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import javafx.scene.paint.Color;

public class changeUsernameScene extends SceneBasic {
	private Label newText = new Label("New Username");
	private TextField newField = new TextField();
	private Button button = new Button("Change Username");
	Button cancelButton = new Button("Back to GameSetting");
	VBox root = new VBox();
	public changeUsernameScene() {
		super("Change Username");
		// text message
		Label message = new Label(titleText);
		message.setFont(new Font(40));
		// Creating Grid Pane
		GridPane gridPane = new GridPane();
		gridPane.setMinSize(400, 200);
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		gridPane.setVgap(5);
		gridPane.setHgap(5);
		gridPane.add(newText, 0, 0);
		gridPane.add(newField, 1, 0);
		gridPane.add(button, 1, 1);
		gridPane.add(cancelButton, 1, 2);
		gridPane.setAlignment(Pos.TOP_CENTER);
		root.getChildren().addAll(gridPane);
		VBox container = new VBox(10, message, root);
		container.setAlignment(Pos.CENTER);
		button.setOnAction(e -> change());
		cancelButton.setOnAction(e -> SceneManager.setGameSettingScene());
		scene = new Scene(container, 450, 250);
	}

	private void change() {
		try {
			Socket connection = SceneManager.getSocket(); // Server socket
			PrintWriter outgoing; // Stream for sending data.
			outgoing = new PrintWriter(connection.getOutputStream());
			System.out.println("Sending... CHANGE_USERNAME");
			outgoing.println("CHANGE_USERNAME");
			outgoing.flush();
			// outgoing.close(); // CAUSES SOCKET TO CLOSE?

			String newInput = newField.getText();
			outgoing.println(newInput);
			outgoing.flush(); // Make sure the data is actually sent!
			System.out.println("Sent username info"); // For debugging
//            outgoing.close();

			SceneManager.setGameSettingScene();
		} catch (Exception e) {
			System.out.println("Error:  " + e);
		}
	}
}