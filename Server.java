// Author: Kidus Asmare Ayele: Initialize the server

package Server;

import java.net.*;

public class Server {
	
	public static int LISTENING_PORT = 3001;
	
	public static void main(String[] args) {
		ServerSocket listener = null;
		try {
			listener = new ServerSocket(LISTENING_PORT);
			while(true) {
				Socket client1 = listener.accept();	// Accept the first client
				System.out.println(client1.getRemoteSocketAddress().toString());
				Socket client2 = listener.accept();	// Accept the second client
				System.out.println(client2.getRemoteSocketAddress().toString());
				ServerThread serverCreate = new ServerThread(client1, client2);
				serverCreate.start();	// Start the thread
			}
		} catch(Exception e) {
			System.out.println("SERVER ERROR: " + e);
		}
	}
}
