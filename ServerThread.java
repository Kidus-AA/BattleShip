// Author: Kidus Asmare Ayele. Server commands for client communication

package Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread extends Thread {
	private HashMap<String, Account> accounts;
	private Account account1;
	private Socket player1;
	private Account account2;
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
	private int p1WinCheck = 0;
	private boolean p1Winner = false;
	private int p2WinCheck = 0;
	private boolean p2Winner = false;
	
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
				if(player1Input.ready()) {	// If client 1 has sent a command
					command = player1Input.readLine();
					currentPlayer = 0;
				} else if(player2Input.ready()) { // If client 2 has sent a command
					command = player2Input.readLine();
					currentPlayer = 1;
				}
				
				if(command.equals("QUIT")) {
					break;
				} else if(command.equals("LOGIN")) {	// Determins which player sent the command and calls method
					if(currentPlayer == 0) {
						login(player1Input, player1Output);
					} else if(currentPlayer == 1) {
						login(player2Input, player2Output);
					}
				} else if(command.equals("INITG")) {	// Initalizes the game
					initializeGame(player1Input, player1Output, player2Input, player2Output);
				} else if(command.equals("INITB")) {	// Initializes the board 
					initializeBoard(player1Output, player2Output);
				} else if(command.equals("NEXT")) {	// Computes the next move for the clients
					nextMove(player1Input, player1Output, player2Input, player2Output);
				} else if(command.equals("SAVE")) {	// Saves the game information
					saveGame();
				} else if(command.equals("LOAD")) {	// Loads the game information
					loadGame();
				} else if(command.equals("CHANGEP")) {	// Changes the player's passwords
					if(currentPlayer == 0) {
						changePassword(player1Input, player1Output);
					} else if(currentPlayer == 1) {
						changePassword(player2Input, player2Output);
					}
				}
				// change password
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
                    	// If the user and password are correct
                    	if(currentPlayer == 0) {
                    		account1 = account;
                    	} else if(currentPlayer == 1) {
                    		account2 = account;
                    	}
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
			
			// If one of the clients are ready it waits for the other client to get ready
			// When both are ready it sends a command to both clients
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
			// Sends the current board information of the players to the clients
			if(currentPlayer == 0) {
				outgoingP1.println(convertBoard(player1Board));
				outgoingP1.flush();
			} else if (currentPlayer == 1) {
				outgoingP2.println(convertBoard(player2Board));
				outgoingP2.flush();
			}
		} catch(Exception e) {
			System.out.println("INITIALIZE BOARD ERROR: " + e);
		}
	}
	
	public void nextMove(BufferedReader incomingP1, PrintWriter outgoingP1, BufferedReader incomingP2, PrintWriter outgoingP2) {
		try {
			// For the first client calling NEXT
			// It should not expect coordinates of a previous move
			if(currentRound == 0) {
				outgoingP1.println("NOMOVE");
				outgoingP1.flush();
			} 
			
			// While there is no winner
			if(!p1Winner && !p2Winner) {
				if(currentRound % 2 == 0) {	// Client 1 turn
					String[] coordinates = incomingP1.readLine().split(",");
					// If it hits the board
					// Sends the result of and the coordinates of where they hit
					if(player2Board[Integer.parseInt(coordinates[0])][Integer.parseInt(coordinates[1])] == 1) {
						p1WinCheck++;
						if(p1WinCheck == 3 && p2WinCheck < 3) {	// If the client has won
							p1Winner = true;
							outgoingP1.println("WIN");
							outgoingP2.println("LOSE");
							outgoingP2.println(coordinates[0] + "," + coordinates[1]);
						} else if(p1WinCheck == 3 && p2WinCheck == 3) {
							p1Winner = true;
							p2Winner = true;
							outgoingP1.println("DRAW");
							outgoingP2.println("DRAW");
							outgoingP2.println(coordinates[0] + "," + coordinates[1]);
						} else {
							outgoingP1.println("HIT");
							outgoingP2.println("HIT");
							outgoingP2.println(coordinates[0] + "," + coordinates[1]);
						}
					} else {
						outgoingP1.println("MISS");
						outgoingP2.println("MISS");
						outgoingP2.println(coordinates[0] + "," + coordinates[1]);
					}
				} else {	// Client 2 turn
					String[] coordinates = incomingP2.readLine().split(",");
					if(player1Board[Integer.parseInt(coordinates[0])][Integer.parseInt(coordinates[1])] == 1) {
						p2WinCheck++;
						if(p2WinCheck == 3 && p1WinCheck < 3) {
							p2Winner = true;
							outgoingP1.println("LOSE");
							outgoingP2.println("WIN");
							outgoingP1.println(coordinates[0] + "," + coordinates[1]);
						} else if(p2WinCheck == 3 && p1WinCheck == 3) {
							p1Winner = true;
							p2Winner = true;
							outgoingP1.println("DRAW");
							outgoingP2.println("DRAW");
							outgoingP1.println(coordinates[0] + "," + coordinates[1]);
						} else {
							outgoingP1.println("HIT");
							outgoingP2.println("HIT");
							outgoingP1.println(coordinates[0] + "," + coordinates[1]);
						}
					} else {
						outgoingP1.println("MISS");
						outgoingP2.println("MISS");
						outgoingP1.println(coordinates[0] + "," + coordinates[1]);
					}
				}
				currentRound++;
				outgoingP1.flush();
				outgoingP2.flush();
			}
		} catch(Exception e) {
			System.out.println("NEXT MOVE ERROR: " + e);
		}
	}
	
	// Randomizes and places ships on the player's boards
	private void startGame() {
		currentRound = 0;
		p1Winner = false;
		p2Winner = false;
		// Initially all filled up with 0
		for(int i = 0; i < rowDimension; i++) {
			for(int j = 0; j < columnDimension; j++) {
				player1Board[i][j] = 0;
				player2Board[i][j] = 0;
			}
		}
		
		// Generates random x and y positions to place ships on the board
		for(int i = 0; i < 2; i++) {
			int[][] copyArray = (i == 0) ? player1Board : player2Board;
			for(int j = 0; j < 3; j++) { // Three ships per player
				int xPos = (int)(Math.random() * 5);
				int yPos = (int)(Math.random() * 5);
				while(copyArray[xPos][yPos] == 1) {
					xPos = (int)(Math.random() * 5);
					yPos = (int)(Math.random() * 5);
				}
				copyArray[xPos][yPos] = 1;
			}
		}
	}
	
	synchronized public void saveGame() {
		try {
			FileOutputStream writeFile = new FileOutputStream("gameData");
			writeFile.write(currentRound);	// Writes the round they are on
			// Writes client 1 board
			for(int i = 0; i < player1Board.length; i++) {
				for(int j = 0; j < player1Board[i].length; j++) {
					writeFile.write(player1Board[i][j]);
				}
			}
			
			// Writes client 2 board
			for(int i = 0; i < player2Board.length; i++) {
				for(int j = 0; j < player2Board[i].length; j++) {
					writeFile.write(player2Board[i][j]);
				}
			}
			writeFile.close();
		} catch(Exception e) {
			System.out.println("SAVE GAME ERROR: " + e);
		}	
	}
	
	synchronized public void loadGame() {
		try {
			FileInputStream readFile = new FileInputStream("gameData");
			int savedCurrRound = readFile.read();
			currentRound = savedCurrRound;	// Reads the current round
			// Reads board date for client 1
			for(int i = 0; i < player1Board.length; i++) {
				for(int j = 0; j < player1Board[i].length; j++) {
					int savedP1Board = readFile.read();
					player1Board[i][j] = savedP1Board;
				}
			}
			
			// Reads board data for client 2
			for(int i = 0; i < player2Board.length; i++) {
				for(int j = 0; j < player2Board[i].length; j++) {
					int savedP2Board = readFile.read();
					player2Board[i][j] = savedP2Board;
				}
			}
			readFile.close();
		} catch(Exception e) {
			System.out.println("LOAD GAME ERROR: " + e);
		}
	}
	
	synchronized public void changePassword(BufferedReader incoming, PrintWriter outgoing) {
		try {
			Account userAccount;
			String oldPass = incoming.readLine();
            String newPass = incoming.readLine();
			if(currentPlayer == 0) {
				userAccount = account1;
			} else {
				userAccount = account2;
			}
			
			if (!userAccount.verifyPassword(oldPass)) {
                // Send error message for an invalid password
                outgoing.println("Old Password is incorrect");
                outgoing.flush();
                return;
            } else if (newPass.length() == 0) {
                // Send error message for an invalid password
                outgoing.println("New Password can not be empty");
                outgoing.flush();
                return;
            }
			
			// Change userAccount's password
            userAccount.setPassword(newPass);
            
            PrintWriter writeFile = new PrintWriter(new File("accounts.xml"));
            writeFile.println("<ACCOUNTS>");
            for (String key : accounts.keySet()) {
            	 writeFile.println("<PLAYER>");
                 writeFile.println("<id>" + userAccount.getID() +"</id>");
                 writeFile.println("<username>" + userAccount.getUsername() + "</username>");
                 writeFile.println("<password>" + userAccount.getPassword() + "</password>");
                 writeFile.println("<level>" + userAccount.getLevel() + "</level>");
                 writeFile.println("</PLAYER>");
            }
            
            writeFile.println("</ACCOUNTS>");
            writeFile.flush();
            writeFile.close();
            
		} catch(Exception e) {
			System.out.println("CHANGE PASSWORD ERROR: " + e);
		}
	}
	
	// Converts a player's board into a string
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
