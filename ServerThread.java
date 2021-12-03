package Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread extends Thread {
	private HashMap<String, Account> accounts;
	private Socket player1;
	private Socket player2;
	private BufferedReader player1Input;
	private PrintWriter player1Output;
	private BufferedReader player2Input;
	private PrintWriter player2Output;
	private int[][] player1Board;
	private int[][] player2Board;
	private final int rowDimension = 5;
	private final int columnDimension = 5;
	private int currentRound;
	private int currentPlayer = 0;
	
	public ServerThread(Socket player1, Socket player2) {
		accounts = AccountsReader.readFile("accounts.xml");
		this.player1 = player1;
		this.player2 = player2;
		player1Board = new int[rowDimension][columnDimension];
		player2Board = new int[rowDimension][columnDimension];
	}
	
	public void run() {		
		try {
			player1Input = new BufferedReader( new InputStreamReader( player1.getInputStream()));
			player1Output = new PrintWriter( new OutputStreamWriter( player1.getOutputStream()));
			player2Input = new BufferedReader( new InputStreamReader( player2.getInputStream()));
			player2Output = new PrintWriter( new OutputStreamWriter( player2.getOutputStream()));
			
			while(true) {
				String command = "";
				if(player1Input.ready()) {
					command = player1Input.readLine();
					currentPlayer = 0;
				} else if(player2Input.ready()) {
					command = player2Input.readLine();
					currentPlayer = 1;
				}
				
				if(command.equals("QUIT")) {
					break;
				} else if(command.equals("LOGIN")) {
					if(currentPlayer == 0) {
						login(player1Input, player1Output);
					} else if(currentPlayer == 1) {
						login(player2Input, player2Output);
					}
				} else if(command.equals("INITG")) {
					initializeGame(player1Input, player1Output, player2Input, player2Output);
				} else if(command.equals("INITB")) {
					initializeBoard(player1Output, player2Output);
				}
				
				else if(command.equals("NEXT")) {
					nextMove(player1Input, player1Output, player2Input, player2Output);
				}
				
				// Get setting changes
				// Save game
			}
		} catch(Exception e) {
			System.out.println("SERVER THREAD ERROR: " + e);
			e.printStackTrace();
		}
	}	

	public void login(BufferedReader incoming, PrintWriter outgoing) {
		try {
            String username = incoming.readLine();
            String password = incoming.readLine();
            boolean passError = false;

            for(String id : accounts.keySet()) {
            	Account account = accounts.get(id);
            	if (account.getUsername().equals(username)) {
                    if (account.verifyPassword(password) == true) {
                    	outgoing.println("LOGGEDIN");
                    	outgoing.flush();
                        passError = false;
                        return;
                    } else {
                    	passError = true;
                    }
                }
            }
			
			if (passError == true) {
                outgoing.println("PASS_ERROR");
            } else {
                outgoing.println("USER_ERROR");
            }
			
            outgoing.flush(); 
		} catch (Exception e) {
            System.out.println("LOGIN ERROR: " + e);
        }
	}
	
	public void initializeGame(BufferedReader incomingP1, PrintWriter outgoingP1, BufferedReader incomingP2, PrintWriter outgoingP2) {
		try {
			startGame();
			String command = "";
			if(currentPlayer == 0) {
				command = incomingP2.readLine();
			} else if(currentPlayer == 1) {
				command = incomingP1.readLine();
			}
			
			if(command.equals("INITG")) {
				outgoingP1.println("READY");
				outgoingP2.println("READY");
				outgoingP1.flush();
				outgoingP2.flush();
			}
		} catch(Exception e) {
			System.out.println("INITIALIZE GAME ERROR: " + e);
		}
	}
	
	public void initializeBoard(PrintWriter outgoingP1, PrintWriter outgoingP2) {
		try {
			outgoingP1.println(convertBoard(player1Board));
			outgoingP2.println(convertBoard(player1Board));
			outgoingP1.flush();
			outgoingP2.flush();
		} catch(Exception e) {
			System.out.println("INITIALIZE BOARD ERROR: " + e);
		}
	}
	
	public void nextMove(BufferedReader incomingP1, PrintWriter outgoingP1, BufferedReader incomingP2, PrintWriter outgoingP2) {
		try {
			if(currentRound % 2 == 0) {
				String[] coordinates = incomingP1.readLine().split(",");
				if(player2Board[Integer.parseInt(coordinates[0])][Integer.parseInt(coordinates[1])] == 1) {
					outgoingP1.println("HIT");
					outgoingP2.println("HIT");
				} else {
					outgoingP1.println("MISS");
					outgoingP2.println("MISS");
				}
			} else {
				String[] coordinates = incomingP2.readLine().split(",");
				if(player1Board[Integer.parseInt(coordinates[0])][Integer.parseInt(coordinates[1])] == 1) {
					outgoingP1.println("HIT");
					outgoingP2.println("HIT");
				} else {
					outgoingP1.println("MISS");
					outgoingP2.println("MISS");
				}
			}
			currentRound++;
			outgoingP1.flush();
			outgoingP2.flush();
		} catch(Exception e) {
			System.out.println("NEXT MOVE ERROR: " + e);
		}
	}
	
	// Randomize and place the ships here
	private void startGame() {
		currentRound = 0;
		for(int i = 0; i < rowDimension; i++) {
			for(int j = 0; j < columnDimension; j++) {
				player1Board[i][j] = 0;
				player2Board[i][j] = 0;
			}
		}
	}
	
	private String convertBoard(int[][] board) {
		String boardStr = "";
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				boardStr = boardStr + board[i][j] + ",";
			}
		}
		return boardStr;
	}
}
