import static org.junit.jupiter.api.Assertions.*;

import java.net.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
//Author:Jack
class Test_initalze_JUnit {
	private static int LISTENING_PORT = 32007;
    private static BufferedReader incoming,incoming1;   
    private static PrintWriter outgoing,outgoing1 ;  
    private static Socket client,client1;

	@BeforeEach
	void setUp() throws Exception {
		client = new Socket("localhost", LISTENING_PORT);
		client1 = new Socket("localhost", LISTENING_PORT);
        incoming = new BufferedReader(new InputStreamReader(client.getInputStream()));
        incoming1 = new BufferedReader(new InputStreamReader(client1.getInputStream()));
        outgoing = new PrintWriter( client.getOutputStream() );
        outgoing1 = new PrintWriter( client1.getOutputStream() );
	}

	@Test
	void test() {
		String reply1 = "",reply2 = "";
		
		try {
			outgoing.println("INITG");
			outgoing.flush();
	        reply1 = incoming.readLine();
	        reply2 = incoming.readLine();
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
		incoming1.close();
		outgoing.close();
		outgoing1.close();
		client.close();
		client1.close();
	}
}