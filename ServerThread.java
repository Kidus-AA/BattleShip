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
				} else if(command.equals("NEXT")) {
					nextMove(player1Input, player1Output, player2Input, player2Output);
				} else if(command.equals("SAVE")) {
					saveGame();
				} else if(command.equals("LOAD")) {
					loadGame();
				} else if(command.equals("CHANGEP")) {
					changePassword(player1Input, player1Output, player2Input, player2Output);
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
			if(currentRound == 0) {
				System.out.println("HERE");
				outgoingP1.println("NOMOVE");
				outgoingP1.flush();
			} 
			
			if(currentRound % 2 == 0) {
				String[] coordinates = incomingP1.readLine().split(",");
				System.out.println("HERE");
				if(player2Board[Integer.parseInt(coordinates[0])][Integer.parseInt(coordinates[1])] == 1) {
					outgoingP1.println("HIT");
					outgoingP2.println("HIT");
					outgoingP2.println(coordinates[0] + "," + coordinates[1]);
				} else {
					outgoingP1.println("MISS");
					outgoingP2.println("MISS");
					outgoingP2.println(coordinates[0] + "," + coordinates[1]);
				}
			} else {
				String[] coordinates = incomingP2.readLine().split(",");
				if(player1Board[Integer.parseInt(coordinates[0])][Integer.parseInt(coordinates[1])] == 1) {
					outgoingP1.println("HIT");
					outgoingP2.println("HIT");
					outgoingP1.println(coordinates[0] + "," + coordinates[1]);
				} else {
					outgoingP1.println("MISS");
					outgoingP2.println("MISS");
					outgoingP1.println(coordinates[0] + "," + coordinates[1]);
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
		
		for(int i = 0; i < 2; i++) {
//			int[][] copyArray = (i == 0) ? player1Board : player2Board;
			for(int j = 0; j < 3; j++) { // to draw three ships
				int direction = (int)(Math.random() * 2);
				int xPos = (int)(Math.random() * 5);
				int yPos = (int)(Math.random() * 5);
				if(i == 0) {
					player1Board[xPos][yPos] = 1;
				} else {
					player2Board[xPos][yPos] = 1;
				}				
			}
		}
	}
	
	public void saveGame() {
		try {
			FileOutputStream writeFile = new FileOutputStream("gameData");
			writeFile.write(currentRound);
			for(int i = 0; i < player1Board.length; i++) {
				for(int j = 0; j < player1Board[i].length; j++) {
					writeFile.write(player1Board[i][j]);
				}
			}
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
	
	public void loadGame() {
		try {
			FileInputStream readFile = new FileInputStream("gameData");
			int savedCurrRound = readFile.read();
			currentRound = savedCurrRound;
			for(int i = 0; i < player1Board.length; i++) {
				for(int j = 0; j < player1Board[i].length; j++) {
					int savedP1Board = readFile.read();
					player1Board[i][j] = savedP1Board;
				}
			}
			
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
	
	public void changePassword(BufferedReader incomingP1, PrintWriter outgoingP1, BufferedReader incomingP2, PrintWriter outgoingP2) {
		if(currentPlayer == 0) {
			
		} else if(currentPlayer == 1) {
			
		}
	}
//	
//	private boolean checkSpace(int[][] board, int direction, int xPos, int yPos, int sign) {
//		if(direction == 0) {
//			if(sign == 0) {
//				if(board[xPos][yPos] == 1 || board[xPos + 1][yPos] == 1 || board[xPos + 2][yPos] == 1) {
//					return false;
//				}
//			} else if (sign == 1) {
//				if(board[xPos][yPos] == 1 || board[xPos - 1][yPos] == 1 || board[xPos - 2][yPos] == 1) {
//					return false;
//				}
//			}
//		} else {
//			if(sign == 0) {
//				if(board[xPos][yPos] == 1 || board[xPos][yPos + 1] == 1 || board[xPos][yPos + 2] == 1) {
//					return false;
//				}
//			} else if (sign == 1) {
//				if(board[xPos][yPos] == 1 || board[xPos][yPos - 1] == 1 || board[xPos][yPos - 2] == 1) {
//					return false;
//				}
//			}
//		}
//		return true;
//	}
//	
//	private void inputShipRows(int[][] board, int xPos, int yPos, int sign) {
//		if(sign == 0) {	// moving in the positive direction of the row
//			board[xPos][yPos] = 1;
//			board[xPos + 1][yPos] = 1;
//			board[xPos + 2][yPos] = 1;
//		} else if(sign == 1) {	// moving the negative direction of the row
//			board[xPos][yPos] = 1;
//			board[xPos - 1][yPos] = 1;
//			board[xPos - 2][yPos] = 1;
//		}
//	}
//	
//	private void inputShipColumns(int[][] board, int xPos, int yPos, int sign) {
//		if(sign == 0) {	// moving in the positive direction of the row
//			board[xPos][yPos] = 1;
//			board[xPos][yPos + 1] = 1;
//			board[xPos][yPos + 2] = 1;
//		} else if(sign == 1) {	// moving the negative direction of the row
//			board[xPos][yPos] = 1;
//			board[xPos][yPos + 1] = 1;
//			board[xPos][yPos + 2] = 1;
//		}
//	}
	
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
