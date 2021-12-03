package Client;
// Author: Kidus Asmare Ayele. Abstract class inherited by all scenes

import java.io.*;
import java.net.Socket;

import javafx.scene.Scene;

public abstract class SceneBasic {
	protected Scene scene;			// Scene that is used by SceneManager
	protected String titleText;		// Title for all scenes
	
	public SceneBasic(String titleText) {
		this.titleText = titleText;
	}
	
	// Allows scenes to log out
	public void logout() {
		try {
			Socket connection = SceneManager.getSocket();
			PrintWriter outgoing = new PrintWriter( new OutputStreamWriter(connection.getOutputStream()));
			outgoing.println("QUIT");
			outgoing.flush();
			SceneManager.setSocket(null);
			SceneManager.setLoginScene();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		}
	}
	
	// Returns the scene
	public Scene getScene() {
		return scene;
	}
}
