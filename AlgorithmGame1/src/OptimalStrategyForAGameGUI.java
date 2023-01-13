
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class OptimalStrategyForAGameGUI extends Application {
	final static Font font3 = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 25);
	final static Font font4 = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 18);
	// Since we want to save the coins and their values i used a global variable to
	// store them since I couldn't get it back out of the scoop of the action button
	// of the label
	static int[] coinsarr;// coins values saved

	static PlayersChoices[][] DP;// Dynamic Programming table

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.initStyle(StageStyle.UTILITY);
		primaryStage.setTitle("Optimal Strategy for A game");
		primaryStage.setResizable(false);
		mainScreen(primaryStage);
	}

	public void mainScreen(Stage primaryStage) {
		Button startGame = new Button("Play Game");
		startGame.setFont(font3);
		Button showSteps = new Button("Show Steps");
		showSteps.setFont(font3);
		Button showRelation = new Button("Show Relation");
		Label relation = new Label(
				"T(n)--> DP[i][j].FirstVal ={MAX(DP[i+1][j].SecondVal+coin[i] , DP[i][j-1].SecondVal+coin[j])}\n"
						+ "DP[i][j].SecondVal = {DP[i+1][j].FirstVal if DP[i+1][j]+coin[i] was max OR\n"
						+ "DP[i][j-1].First if DP[i][j-1]+coin[j] was max }\n"
						+ "Time Coplexity =O(n^2)\nSpace Complexity =O(n^2)");
		showRelation.setFont(font3);
		startGame.setPrefSize(200, 50);
		showSteps.setPrefSize(200, 50);
		showRelation.setPrefSize(200, 50);
		startGame.setTranslateX(40);
		startGame.setTranslateY(100);
		showSteps.setTranslateX(40);
		showSteps.setTranslateY(260);
//		showSteps.setDisable(true);
		showRelation.setTranslateX(40);
		showRelation.setTranslateY(420);
		relation.setFont(font4);
		relation.setTranslateX(40);
		relation.setTranslateY(520);
		relation.setVisible(false);
		Group root = new Group();
		root.setStyle("-fx-backfround-color:blue");
		root.getChildren().addAll(startGame, showSteps, showRelation, relation);
		Scene scene1 = new Scene(root, 850, 650);
		scene1.setFill(Color.SKYBLUE);
		primaryStage.setScene(scene1);
		primaryStage.show();
		// -----------------------------------------------------------
		scene1.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				System.out.println(event.getSceneX());
				System.out.println(event.getSceneY());
			}
		});
		// ----------------------------------------------------------

		startGame.setOnAction(e -> {
			primaryStage.hide();
			playGame(primaryStage);
		});
		showSteps.setOnAction(e -> {
			try {
				showSteps();
			} catch (Exception ex) {
				warning_Message("Should enter a game first");
			}
		});
		showRelation.setOnAction(e -> {
			relation.setVisible(true);
		});
	}

	// A scene that starts play game
	// where user have to enter input (coins and their values)
	// Table is created
	private void playGame(Stage primaryStage) {
		TextField coins = new TextField();
		coins.setPromptText("Enter Even number of coins with spaces between each value");
		coins.setPrefSize(550, 60);
		coins.setFont(font3);
		Button start = new Button("Start Game");
		Button backBtn = new Button("Back");
		start.setFont(font3);
		start.setPrefSize(200, 50);
		backBtn.setFont(font3);
		backBtn.setPrefSize(200, 50);
		coins.setTranslateX(40);
		coins.setTranslateY(60);
		start.setTranslateX(40);
		start.setTranslateY(170);
		backBtn.setTranslateX(280);
		backBtn.setTranslateY(170);
		Group root = new Group();
		root.getChildren().addAll(coins, start, backBtn);
		Scene playGameScene = new Scene(root, 850, 250);
		playGameScene.setFill(Color.SKYBLUE);
		primaryStage.setScene(playGameScene);
		primaryStage.show();
		start.setOnAction(e -> {
			try {
				String s = coins.getText().strip();// gets values of coins entered
				String[] tok = s.split(" ");
				coinsarr = new int[tok.length];
				if (tok.length % 2 != 0) {// if number of coins isn't even then wrong input (constrain that we work only
											// with even numbered coins)
					warning_Message("Error(Number of coins must be even)");
				} else {// if input format is correct(only integers) and number of coins is even then
					for (int i = 0; i < tok.length; i++) {// fill in coins array
						coinsarr[i] = Integer.parseInt(tok[i]);
						if (coinsarr[i] <= 0)
							throw new IllegalArgumentException();
					}
					DP = findMovesToOptimalSolution();
					createTable(coinsarr.length);// create DPTable according to coins number
					confirmation_Message("First Player -Second player best results" + DP[0][DP.length - 1].toString());
				}
			} catch (IllegalArgumentException ex) {
				warning_Message("Error(Number of coins must be even) And must be positive values only");
			}
		});
		backBtn.setOnAction(e -> {
			primaryStage.hide();
			mainScreen(primaryStage);
		});
	}

	private void showSteps() {
		Label user1 = new Label("Player 1:");
		Label user2 = new Label("Player 2:");
		user1.setFont(font4);
		user2.setFont(font4);
		user1.setTextFill(Color.CYAN);
		user2.setTextFill(Color.CYAN);
		user1.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		user2.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		Label total1 = new Label("Player one total:" + DP[0][DP.length - 1].getP1Choice() + "");
		Label total2 = new Label("Player two total:" + DP[0][DP.length - 1].getP2Choice() + "");
		total1.setFont(font3);
		total2.setFont(font3);
		total1.setTextFill(Color.CYAN);
		total2.setTextFill(Color.CYAN);
		total1.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		total2.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		total1.setLayoutX(500);
		total1.setLayoutY(480);
		total2.setLayoutX(740);
		total2.setLayoutY(480);
		total1.setVisible(false);
		total2.setVisible(false);
		int length = coinsarr.length;
		Circle[] coinsval = new Circle[length];
		StackPane[] st = new StackPane[length];
		Text[] text = new Text[length];
		Group root = new Group();
		user1.setLayoutX(25);
		user1.setLayoutY(65);
		user2.setLayoutX(25);
		user2.setLayoutY(330);
		root.getChildren().addAll(user1, user2, total1, total2);
		TranslateTransition[] translate = new TranslateTransition[length];
		for (int i = 0; i < length; i++) {
			st[i] = new StackPane();
			text[i] = new Text();
			coinsval[i] = new Circle();
			translate[i] = new TranslateTransition();
		}
		for (int i = 0; i < length; i++) {
			coinsval[i].setRadius(40);
			coinsval[i].setFill(Color.CYAN);
			coinsval[i].setStroke(Color.BLACK);
			text[i].setText("" + coinsarr[i]);
			text[i].setFont(font3);
			st[i].getChildren().addAll(coinsval[i], text[i]);
			st[i].setLayoutX(100 + (100 * i));
			st[i].setLayoutY(170);
			root.getChildren().add(st[i]);
		}
		int[] moves = printSteps();
		PauseTransition pt = new PauseTransition(Duration.seconds(3));// pause Between transitions
		SequentialTransition seqTrans = new SequentialTransition();// To add transition to it and work with pauses
																	// between each play
		seqTrans.getChildren().add(pt);
		// goes through the move of each player and moves his choice next to his name
		for (int i = 1; i <= length; i++) {
			translate[i - 1].setDuration(Duration.millis(1000));
			translate[i - 1].setNode(st[moves[i - 1]]);
			if (i % 2 != 0) {// first player move
				translate[i - 1].setByY(-145);

			} else {// second player move
				translate[i - 1].setByY(135);

			}
			seqTrans.getChildren().add(translate[i - 1]);
		}
		seqTrans.play();
		// When sequential transition is finished show total for each player
		seqTrans.setOnFinished(e -> {
			total1.setVisible(true);
			total2.setVisible(true);
		});
		Scene scene = new Scene(root, 850, 450);
		scene.setFill(Color.SKYBLUE);
		Stage stage0 = new Stage();
		stage0.setMaximized(true);
		stage0.setTitle("Moves Scene");
		stage0.setScene(scene);
		stage0.show();
	}

	private void createTable(int coinsNumber) {
		BackgroundFill background_fill = new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY);
		Background background = new Background(background_fill);
		Label table = new Label("   ");
		table.setBackground(background);
		table.setFont(font4);
//		table.setPrefSize(400, 400);
		table.setTranslateY(0);
		for (int i = 1; i <= coinsNumber; i++) {
			table.setText(table.getText() + i + "\t\t");
		}
		table.setText(table.getText() + "\n");
		// Fill in the DPTable for the interface with DP
		for (int i = 0; i < DP.length; i++) {
			table.setText(table.getText() + (i + 1) + "|");
			for (int j = 0; j < DP[i].length; j++) {
				table.setText(table.getText() + DP[i][j].toString() + "|\t");
			}
			table.setText(table.getText() + "\n");
		}
		StackPane root = new StackPane();
		root.getChildren().add(table);
		root.setBackground(background);
		Stage newStage = new Stage();
		Scene s = new Scene(root, 650, 450);
		s.setFill(Color.SKYBLUE);
		newStage.setScene(s);
		newStage.setTitle("Dynamic Programming Table");
		newStage.show();
	}

	public static void confirmation_Message(String x) {
		Alert alert = new Alert(AlertType.NONE);
		alert.setAlertType(AlertType.CONFIRMATION);
		alert.setContentText(x);
		alert.show();
	}

	public static void warning_Message(String x) {
		Alert alert = new Alert(AlertType.NONE);
		alert.setAlertType(AlertType.WARNING);
		alert.setContentText(x);
		alert.show();
	}

	// method to build DP table In a Bottom up approach
	// Each turn In DP has two values first player choice which is the best choice
	// for that current index and second player choice
	// Eg: for DP[1,1] first p1 choice = value[0], p2 choice is =0 since we only
	// have one value at those indexes
	private PlayersChoices[][] findMovesToOptimalSolution() {
		PlayersChoices[][] DP = new PlayersChoices[coinsarr.length][coinsarr.length];
		for (int i = 0; i < DP.length; i++) {// Initially definition ofDynamic programming table
			for (int j = 0; j < DP[i].length; j++) {
				DP[i][j] = new PlayersChoices();
			}
		}
		// Diagonal of DP is equal to the index at i=j
		// since it represent a game of only one value where first player would get
		// coin[i] and
		// second gets value of zero since no more coins are left
		for (int i = 0; i < coinsarr.length; i++) {
			DP[i][i].setP1Choice(coinsarr[i]);
			// index at which value was taken from
			DP[i][i].setIndex(i);
		}
		// looping diagonally through DP
		// where k is the columns setter as to move to next cell through diagonal
		// we increment rows counter, and increment columns counter each by one
		// and each time the number of cells we need to loop through is decreased
		// so k is to keep us in range for the length of diagonal
		for (int k = 2; k <= coinsarr.length; k++) {
			for (int rows = 0; rows <= coinsarr.length - k; rows++) {// holds rows counter of current diagonal
				int column = rows + k - 1;// holds columns counter of current diagonal
				if (coinsarr[rows] + DP[rows + 1][column].getP2Choice() > coinsarr[column]
						+ DP[rows][column - 1].getP2Choice()) {
					DP[rows][column].setP1Choice(coinsarr[rows] + DP[rows + 1][column].getP2Choice());
					DP[rows][column].setP2Choice(DP[rows + 1][column].getP1Choice());
					DP[rows][column].setIndex(rows);
				} else {
					DP[rows][column].setP1Choice(coinsarr[column] + DP[rows][column - 1].getP2Choice());
					DP[rows][column].setP2Choice(DP[rows][column - 1].getP1Choice());
					DP[rows][column].setIndex(column);
				}
			}
		}
		return DP;
	}

	private int[] printSteps() {
		int rows = 0;
		int columns = coinsarr.length - 1;
		int step;
		int[] movesByPlayer = new int[coinsarr.length];
		for (int k = 0; k < coinsarr.length; k++) {// loops through all coins which is equal to rows length of DP
			step = DP[rows][columns].getIndex();
			// this is the value of pick and its index
			movesByPlayer[k] = step;
			if (step <= rows) {// coin was taken from left side of array
				rows = rows + 1;
			} else {// coin was taken from right side of array
				columns = columns - 1;
			}
		}
		return movesByPlayer;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
