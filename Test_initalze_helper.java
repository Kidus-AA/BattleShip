//Author : Jack Peng
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Test_initalze_helper {

	public static void main(String[] args) {

		PrintWriter outgoing,outgoing1;
		BufferedReader incoming,incoming1;
		int LISTENING_PORT = 32007;
		ServerSocket listener,listener1;
		Socket connection,connection1;

		try {
			listener = new ServerSocket(LISTENING_PORT);
			connection = listener.accept();
			listener1 = new ServerSocket(LISTENING_PORT);
			connection1 = listener1.accept();
			outgoing = new PrintWriter(connection.getOutputStream());
			outgoing1 = new PrintWriter(connection1.getOutputStream());
			incoming = new BufferedReader(new InputStreamReader(connection.getInputStream())); 
			incoming1 = new BufferedReader(new InputStreamReader(connection1.getInputStream())); 
			
			ServerThread.initializeGame(incoming,outgoing,incoming1,outgoing1);
			
			connection.close();
			connection1.close();
			listener.close();
			listener1.close();

		} catch (Exception e) {
			System.out.println("Error: " + e);
			return;
		}
	}
}
