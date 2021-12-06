import static org.junit.jupiter.api.Assertions.*;
//Author:JAck
import java.net.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class Test_Changepassword_JUnit {
	private static int LISTENING_PORT = 32007;
    private static BufferedReader incoming;   
    private static PrintWriter outgoing;  
    private static Socket client;

	@BeforeEach
	void setUp() throws Exception {
		client = new Socket("localhost", LISTENING_PORT);
        incoming = new BufferedReader(new InputStreamReader(client.getInputStream()));
        outgoing = new PrintWriter( client.getOutputStream() );
	}

	@Test
	void test() {
		String reply1 = "";

		try {
			outgoing.println("3");
			outgoing.println("2");
			outgoing.flush();
	        reply1 = incoming.readLine();
	        System.out.println(reply1);
		}
        catch (Exception e) {
            System.out.println("Error:  " + e);
        }
	        
        assert reply1.equals("ERROR: Invalid password") ;         
	}

	// Close everything
	@AfterEach
	void shutDown() throws Exception {
		incoming.close();
		outgoing.close();
		client.close();
	}
}