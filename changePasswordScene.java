
//Author:JackP
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

public class changePasswordScene extends SceneBasic {
	private Label oldText = new Label("Old password");
	private PasswordField oldField = new PasswordField();
	private Label newText = new Label("New password");
	private PasswordField newField = new PasswordField();
	private Button button = new Button("Change password");
	private Button cancelButton = new Button("Back to GameSetting");
	private Label errorMessage = new Label();
	VBox root = new VBox();
	
	public changePasswordScene() {
		super("Change Password");
		// text message
		Label message = new Label(titleText);
		message.setFont(new Font(40));
		// Creating Grid Pane
		errorMessage.setTextFill(Color.RED);
		GridPane gridPane = new GridPane();
		gridPane.setMinSize(400, 200);
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		gridPane.setVgap(5);
		gridPane.setHgap(5);
		gridPane.add(oldText, 0, 0);
		gridPane.add(oldField, 1, 0);
		gridPane.add(newText, 0, 1);
		gridPane.add(newField, 1, 1);
		gridPane.add(button, 1, 2);
		gridPane.add(cancelButton, 1, 3);
		gridPane.add(errorMessage, 1, 3);
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
			System.out.println("Sending... CHANGE_PASSWORD");
			outgoing.println("CHANGEP");
			outgoing.flush();
			// outgoing.close(); // CAUSES SOCKET TO CLOSE?

			String oldInput = oldField.getText();
			String newInput = newField.getText();
			outgoing.println(oldInput);
			outgoing.println(newInput);
			outgoing.flush(); // Make sure the data is actually sent!
			System.out.println("Sent password info"); // For debugging
//            outgoing.close();

			BufferedReader incoming = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			System.out.println("Waiting for result...");
			String reply = incoming.readLine();

			if (reply.equals("PLAYER")) {
				errorMessage.setText("");
				SceneManager.setGameSettingScene();
			} else
				errorMessage.setText(reply);
		} catch (Exception e) {
			System.out.println("Error:  " + e);
		}
	}
}