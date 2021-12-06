package Server;
import java.io.*;
import java.util.*;

public class AccountsReader {
	
	public static HashMap<String,Account> readFile(String file) {
		String id = "";
		String username = "";
		String password = "";
		String level = "";
		String backgroundColor = "";
		
		HashMap<String,Account> accountList = new HashMap<String,Account>();
		try {
			Scanner readFile = new Scanner( new File(file));
			readFile.nextLine();
			while(readFile.hasNextLine()) {
				String accountType = readFile.nextLine();
				if(accountType.contains("PLAYER")) {
					id = stripTags(readFile.nextLine());
					username = stripTags(readFile.nextLine());
					password = stripTags(readFile.nextLine());
					level = stripTags(readFile.nextLine());
					backgroundColor = stripTags(readFile.nextLine());
					Account account = new Account(Integer.parseInt(id), username, password, Integer.parseInt(level), backgroundColor);
					accountList.put(id, account);
					readFile.nextLine();
				}				
			}	
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("READ ACCOUNTS ERROR: " + e);
		}	
		return accountList;
	}
	
	private static String stripTags(String line) {
		String cleanLine = "";
		boolean read = true;
		for(int i = 0; i < line.length(); i++) {
			if(line.charAt(i) == '<') {
				read = false;
			}
			
			if(read) {
				cleanLine = cleanLine + line.charAt(i);
			}
			
			if (line.charAt(i) == '>') {
				read = true;
			}
		}
		return cleanLine.trim();
	}
}
