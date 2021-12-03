//Jack Peng
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane; 
import javafx.geometry.Insets; 
import java.util.*;
import javafx.scene.paint.Color;
import java.net.*;
import java.io.*;

// Since using SceneManager, I disable some functions first.
public class SettingsScene{
	
	static HashMap<String, Account> dataSet = new HashMap<String, Account>();
	
	public static void main(String[] args) {
		readFile("accounts.xml");
	}
		
	public static HashMap<String,Account> readFile(String filename){
		File dataFile = new File(filename);
		if ( ! dataFile.exists() ) {
				System.out.println("No data file found.");
				System.exit(1);
		}
		try( Scanner scanner = new Scanner(dataFile) ) {
			while (scanner.hasNextLine()) {
				String input = scanner.nextLine();
				int separatorPosition = input.indexOf('<');
				int separatorPosition2 = input.indexOf('>');
				String next = input.substring(separatorPosition + 1, separatorPosition2);
				if(next.equals("PLAYER")) {
					String input1 = "";
					while(scanner.hasNextLine() && !input1.equals("/PLAYER")){
						input1 = scanner.nextLine();
						String currentLine = input1;
						int separatorPosition3 = input1.indexOf('<');
						int separatorPosition4 = input1.indexOf('>');
						int separatorPosition5 = input1.indexOf('<',separatorPosition4+1);
						input1 = input1.substring(separatorPosition3 + 1, separatorPosition4);
						if(input1.equals("id")){
    						String id = currentLine.substring(separatorPosition4 + 1, separatorPosition5);
    						//line of username
    						String usernameLine = scanner.nextLine();
    						int separatorPosition6 = usernameLine.indexOf('>');
							int separatorPosition7 = usernameLine.indexOf('<',separatorPosition6+1);
    						String username = usernameLine.substring(separatorPosition6 + 1, separatorPosition7);
    						//line of passowrd
    						String passwordLine = scanner.nextLine();
    						int separatorPosition8 = passwordLine.indexOf('>');
							int separatorPosition9 = passwordLine.indexOf('<',separatorPosition8+1);
    						String password = passwordLine.substring(separatorPosition8 + 1, separatorPosition9);
    						//line of profile
    						String backLine = scanner.nextLine();
    						int separatorPositionA = backLine.indexOf('>');
							int separatorPositionB = backLine.indexOf('<',separatorPositionA+1);
    						String backGround= backLine.substring(separatorPositionA + 1, separatorPositionB);
    						
    						String nickLine = scanner.nextLine();
    						int separatorPositionC = nickLine.indexOf('>');
							int separatorPositionD = nickLine.indexOf('<',separatorPositionC+1);
    						String nickname= nickLine.substring(separatorPositionC + 1, separatorPositionD );
    						
    						Account player = new Account(Integer.parseInt(id),username, password, backGround,nickname);
    						dataSet.put(id, player);	
    					}
    					
					}
				}
					
				
				}
			 for (String key : dataSet.keySet()) {
			        System.out.print("Key = " + key + ", ");
			        System.out.println("Data = " + dataSet.get(key));
			        }
				
		}
		catch (IOException e) {
			System.out.println("Error in data file.");
			System.exit(1);
		}
		return dataSet;
	}
}
