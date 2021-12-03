package Server;

import java.net.*;

public class Server {
	
	public static int LISTENING_PORT = 3001;
	
	public static void main(String[] args) {
		ServerSocket listener = null;
		try {
			listener = new ServerSocket(LISTENING_PORT);
			while(true) {
				Socket client1 = listener.accept();
				Socket client2 = listener.accept();
				ServerThread serverCreate = new ServerThread(client1, client2);
				serverCreate.start();
			}
		} catch(Exception e) {
			System.out.println("SERVER ERROR: " + e);
		}
	}
}
