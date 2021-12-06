//Author : Jack Peng
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Test_Changepassword_Helper {

	public static void main(String[] args) {

		PrintWriter outgoing;
		BufferedReader incoming;
		HashMap<String, Account> account;
		account = AccountsReader.readFile("accounts.xml");
		Account userAccount = account.get("12");
		int LISTENING_PORT = 32007;
		ServerSocket listener;
		Socket connection;

		try {
			listener = new ServerSocket(LISTENING_PORT);
			connection = listener.accept();
			outgoing = new PrintWriter(connection.getOutputStream());
			incoming = new BufferedReader(new InputStreamReader(connection.getInputStream())); 
			ServerThread.changePassword(userAccount,incoming,outgoing);
			//ServerThread.changePassword(userAccount1,incoming,outgoing);
			connection.close();
			listener.close();
		} catch (Exception e) {
			System.out.println("Error: " + e);
			return;
		}
	}
}
