
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

public class GameSettingScene extends SceneBasic {
	Button cancelButton = new Button("Back to GameLobby");
	Button changeUsernameButton = new Button("Change Username");
	Button changePasswordButton = new Button("Change Password");
	Button changeBackGroundButton = new Button("Change BackGround");

	public GameSettingScene() {
		super("User Settings");
		Label message = new Label(titleText);
		message.setFont(new Font(40));
		GridPane grid = new GridPane();

		int WIDTH = 150;
		// 3 buttons
		cancelButton.setMinWidth(WIDTH);
		changePasswordButton.setMinWidth(WIDTH);
		changeUsernameButton.setMinWidth(WIDTH);
		changeBackGroundButton.setMinWidth(WIDTH);

		cancelButton.setOnAction(e -> SceneManager.setGameLobbyScene());
		changePasswordButton.setOnAction(e -> SceneManager.setChangePasswordScene());
		changeUsernameButton.setOnAction(e -> SceneManager.setChangeUsernameScene());
		changeBackGroundButton.setOnAction(e -> SceneManager.setBackgroundScene());
		grid.add(changeUsernameButton, 0, 0);
		grid.add(changePasswordButton, 0, 1);
		grid.add(changeBackGroundButton, 0, 2);
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

}
