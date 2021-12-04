import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameScene extends SceneBasic {
    protected Canvas canvas;
    protected Canvas canvas2;
    private Button logoutButton = new Button("Logout");
    private Button attackSendButton = new Button("sendattack");
    private Button initlizeButton = new Button("initlize");
    private GridPane gridPane = new GridPane();
    private String buttonnumber = ("");
    private Socket connection;
    //	private String hostName = "localhost";
    private String hostName = "127.0.0.1";
    private int LISTENING_PORT = 32008;
    private Label errorMessage = new Label();
    private int[][] ships = new int[5][5];
    private Label pointMessage = new Label();
    private Label totalpoints = new Label();
    private int points = 0;
    private int canvaHeight = 0;
    private int canvasWidth = 0;
    protected HBox root = new HBox();

    public GameScene() {
        super("Game Board");
        canvaHeight = 400;
        canvasWidth = 400;
        root.setAlignment(Pos.TOP_LEFT);
        scene = new Scene(root, 1800, 1000);
        gridPane.setMinSize(400, 400);
        gridPane.setMaxSize(500, 500);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(2);
        gridPane.setHgap(2);
        gridPane.add(errorMessage, 1, 3);
        gridPane.add(pointMessage, 1, 4);
        gridPane.add(totalpoints, 1, 5);
        canvas = new Canvas(canvaHeight, canvasWidth);
        canvas2 = new Canvas(canvaHeight, canvasWidth);
        root.getChildren().addAll(canvas, canvas2, gridPane);
        root.setSpacing(10);
        initlizeBoard();
        drawbuttons();
    }

    private void initlizeBoard() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        GraphicsContext gc2 = canvas2.getGraphicsContext2D();
        int gaplength = canvaHeight / 5;
        gc.strokeLine(0, 0, canvasWidth, 0);
        gc.strokeLine(0, gaplength, canvasWidth, gaplength);
        gc.strokeLine(0, gaplength * 2, canvasWidth, gaplength * 2);
        gc.strokeLine(0, gaplength * 3, canvasWidth, gaplength * 3);
        gc.strokeLine(0, gaplength * 4, canvasWidth, gaplength * 4);
        gc.strokeLine(0, gaplength * 5, canvasWidth, gaplength * 5);


        gc.strokeLine(0, 0, 0, canvaHeight);
        gc.strokeLine(gaplength, 0, gaplength, canvaHeight);
        gc.strokeLine(gaplength * 2, 0, gaplength * 2, canvaHeight);
        gc.strokeLine(gaplength * 3, 0, gaplength * 3, canvaHeight);
        gc.strokeLine(gaplength * 4, 0, gaplength * 4, canvaHeight);
        gc.strokeLine(gaplength * 5, 0, gaplength * 5, canvaHeight);

        gc2.strokeLine(0, 0, canvasWidth, 0);
        gc2.strokeLine(0, gaplength, canvasWidth, gaplength);
        gc2.strokeLine(0, gaplength * 2, canvasWidth, gaplength * 2);
        gc2.strokeLine(0, gaplength * 3, canvasWidth, gaplength * 3);
        gc2.strokeLine(0, gaplength * 4, canvasWidth, gaplength * 4);
        gc2.strokeLine(0, gaplength * 5, canvasWidth, gaplength * 5);

        gc2.strokeLine(0, 0, 0, canvaHeight);
        gc2.strokeLine(gaplength, 0, gaplength, canvaHeight);
        gc2.strokeLine(gaplength * 2, 0, gaplength * 2, canvaHeight);
        gc2.strokeLine(gaplength * 3, 0, gaplength * 3, canvaHeight);
        gc2.strokeLine(gaplength * 4, 0, gaplength * 4, canvaHeight);
        gc2.strokeLine(gaplength * 5, 0, gaplength * 5, canvaHeight);
    }

    private void drawships(int coloumn, int row) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        int gaplengthfordraw = canvaHeight / 5;
        gc.fillOval(gaplengthfordraw * coloumn, gaplengthfordraw * row, 80, 80);
    }

    private void drawattack(int coloumn, int row, Boolean hitornot) {
        GraphicsContext gc = canvas2.getGraphicsContext2D();
        int gaplengthfordraw = canvaHeight / 5;
        if (hitornot == true ) {
            gc.setFill(Color.ORANGE);
            gc.fillOval(gaplengthfordraw * coloumn, gaplengthfordraw * row, 80, 80);
        } else
            gc.setFill(Color.BLUE);
        	gc.fillOval(gaplengthfordraw * coloumn, gaplengthfordraw * row, 80, 80);
    }

    private void drawbuttons() {
        int numRows = 5;
        int numColumns = 5;
        for (int row = 0; row < numRows; row++) {
            RowConstraints rc = new RowConstraints();
            rc.setFillHeight(true);
            rc.setVgrow(Priority.ALWAYS);
            gridPane.getRowConstraints().add(rc);
        }
        for (int col = 0; col < numColumns; col++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setFillWidth(true);
            cc.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(cc);
        }
        for (int i = 0; i < 25; i++) {
            Button button = createButton(Integer.toString(i + 1));
            gridPane.add(button, i % 5, i / 5);
        }
        gridPane.add(logoutButton, 0, 6);
        gridPane.add(attackSendButton, 1, 6);
        gridPane.add(initlizeButton, 2, 6);
        logoutButton.setOnAction(e -> logout());
        attackSendButton.setOnAction(e -> hitBoard());
        initlizeButton.setOnAction(e -> receiveinfo());
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button.setOnAction(e -> buttonnumber = text);
        System.out.println(buttonnumber);
        return button;
    }
    
    public void hitBoard() {
    	boolean hitornot = true;
    	try {
			//Read other player move then hit
			//Check if there is any move for the first player first move
			connection = SceneManager.getSocket();
			
			PrintWriter outgoing = new PrintWriter( new OutputStreamWriter(connection.getOutputStream()));
			outgoing.println("NEXT");		// Request to login that will be sent to the server
			outgoing.flush();
			BufferedReader incoming = new BufferedReader( new InputStreamReader(connection.getInputStream()));
			String moveRes = incoming.readLine();
			
			if(moveRes.equals("HIT") || moveRes.equals("MISS")) {
				String coordinates = incoming.readLine();
				System.out.println(coordinates);
			} else if(moveRes.equals("NOMOVE")) {
				
			}
			int calbuttonnumber = Integer.parseInt(buttonnumber) - 1;
	        int coloumn = calbuttonnumber % 5;
	        int row = calbuttonnumber / 5;
	        
			outgoing.println( row + "," + coloumn);
			outgoing.flush();				// Information sent to the server
			
			String res = incoming.readLine();
			if(res.equals("MISS")) {
				hitornot = false;
			}else {
				hitornot = true;
			}
			drawattack(coloumn, row,hitornot);
			System.out.println(res);
		} catch(Exception e) {
			System.out.println("PLAYER READY ERROR: " + e);
		}
	}

    private void receiveinfo() {
        try {
            connection = SceneManager.getSocket();
            PrintWriter outgoing = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
            outgoing.println("INITB"); // Request to login that will be sent to the server
            outgoing.flush();
            errorMessage.setText("");
            BufferedReader incoming = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
            System.out.println("Waiting for position info...");
            String reply = incoming.readLine();
            System.out.println("info is " + reply);
            String[] strArray = reply.split(",");
            int[] intArray = new int[strArray.length];
            for (int i = 0; i < strArray.length; i++) {
                intArray[i] = Integer.parseInt(strArray[i]);
            }
            for (int c = 0; c < intArray.length; c++) {
                int receivecoloumn = c % 5;
                int receiverow = c / 5 ;
                ships[receivecoloumn][receiverow] = intArray[c];
            }
        } catch (Exception e) {
            errorMessage.setText("Error trying to connect to server.");
            System.out.println("Error:  " + e);
        }
        for (int z = 0; z < ships.length; z++) {
            for (int x = 0; x < ships[z].length; x++) {
                if (ships[z][x] == 1) {
                    drawships(z, x);
                }
            }
        }
        initlizeButton.setDisable(true);
    }
}