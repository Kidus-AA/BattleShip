import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//Author : Jack
class Test_ChangeColor_Junit {
	private static int LISTENING_PORT = 32007;
	private static BufferedReader incoming;
	private static PrintWriter outgoing;
	private static Socket client;

	@BeforeEach
	void setUp() throws Exception {
		client = new Socket("localhost", LISTENING_PORT);
		incoming = new BufferedReader(new InputStreamReader(client.getInputStream()));
		outgoing = new PrintWriter(client.getOutputStream());
	}

	@Test
	void test() {
		String reply1 = "";
		try {
			reply1 = incoming.readLine();
		} catch (Exception e) {
			System.out.println("Error:  " + e);
		}
		assert reply1.equals("Yellow");
	}

	// Close everything
	@AfterEach
	void shutDown() throws Exception {
		incoming.close();
		outgoing.close();
		client.close();
	}
}
