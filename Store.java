// Author: Kidus Asmare Ayele. Class extending application that does the launch.
package Client;

import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class Store extends Application {
	private static SceneManager sceneManager; // Scene changer
	private static Socket connection;
	
	public void start(Stage stage) {
		sceneManager = new SceneManager();
		
		stage.setTitle("Store Application");
		sceneManager.setStage(stage);	// Sets the stage
		SceneManager.setLoginScene();
		stage.show();
		stage.setResizable(false);
	}
	
	public void stop() {
		try {
			connection = SceneManager.getSocket();
			if(connection != null) {
				PrintWriter outgoing = new PrintWriter( new OutputStreamWriter(connection.getOutputStream()));
				outgoing.println("QUIT");
				outgoing.flush();
			}
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		}
	}
	
	public static void main(String[] args) {
		launch();
	}

}
