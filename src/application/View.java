/*
 * TODO
 *  
 *  add ImageView attributes to champions and covers  
 *  make a few cover images depending on cover's health points // khaled
 *  ----no?---- make champions and cover images fade a little when they become hit 
 *  
 * marvel video intro //home
 * improve landing page and update scene1 background to be a random photo
 * update scene2 original background
 * update scene 3 background
 * when selecting champion to view details, background changes to be a big photo of champion
 * buttons should not translate when choosing leader
 * be creative with pop-ups and make sure they pop in the center of screen
 * 
 * attack & cast ability animation
 * 
 * Clean code
 * 
 * Fantastic GUI
 * Network Mode
 * Computer Player
 */


// 0 move up, 1 move down, 2 move right, 3 move left, 4   atk up,5   atk down, 6   atk right, 7   atk left
//8   a1
//9   a2
//10  a3
//11  a4
//12  leader
//13  manual  
//14  end turn 
package application;
	
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.GroupLayout.Alignment;

import engine.Game;
import engine.Player;
import engine.PriorityQueue;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Disarm;
import model.effects.Effect;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Condition;
import model.world.Cover;
import model.world.Direction;
import model.world.Hero;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;

public class View extends Application implements Initializable {
	static Game game;
	static Player player1, player2;
	static Scene modePage, homepage, begin, gameview;
	static GridPane boardView;
	static HBox gameStatus;
	static VBox turnOrderStatus, currentInformation, currentControls;
	static HashMap<Champion,Boolean> chosenMap;
	static HashMap<Champion, String> aliveMap;
	static HashMap<Champion, String> deadMap;
	static HashMap<Champion, String> backgroundMap;
	static ArrayList<Button> chooseLeaderButtons;
	static ArrayList<Button> championsButtons = new ArrayList<>();
	static ArrayList<Button> actions = new ArrayList<Button>();
	static ArrayList<Champion> champions;
	static ArrayList<Champion> player1Champions = new ArrayList<>();
	static ArrayList<Champion> player2Champions = new ArrayList<>();
	static ArrayList<Boolean> memo = new ArrayList<>(15);
	static PriorityQueue q;
	static PriorityQueue tmp;
	static Object[][] board;
	static Button[][] boardButtons = new Button[5][5];
	static Stage primaryStage;
	static boolean full = false;
	static boolean punch = false;
	static boolean twoPlayerMode;
	static int randomBackgroundModePage = (int)(Math.random() * 7) + 1; // 1 to 7
	static ImageView fire;
	static TranslateTransition translateAttack;
	static boolean picked;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Marvel - Ultimate War");
		primaryStage.setFullScreen(true);
		primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		Image icon = new Image("./application/media/gameIcon.jpeg");
		primaryStage.getIcons().add(icon);

		checkPlayingMode(primaryStage);
//		scene(primaryStage);
		primaryStage.show();
	}
	
	public static void checkPlayingMode(Stage primaryStage) {
		Image background = new Image("application/media/backgrounds/back1.jpeg");
		ImageView backgroundIV = new ImageView(background);
		backgroundIV.fitHeightProperty().bind(primaryStage.heightProperty());
		backgroundIV.fitWidthProperty().bind(primaryStage.widthProperty());
		
		// Scene Organisation
		BorderPane root1 = new BorderPane();
		root1.getChildren().add(backgroundIV);
		HBox chooseMode = new HBox(10);
		HBox welcomeBox = new HBox(10);
		Label welcome = new Label("WELCOME TO WAR!");
		welcome.setFont(new Font("Didot.",70));
		welcomeBox.setAlignment(Pos.CENTER);
		welcomeBox.getChildren().add(welcome);
		chooseMode.setPadding(new Insets(10, 10, 110, 10));
		chooseMode.setAlignment(Pos.CENTER);
		modePage = new Scene(root1,400,400);
		Button onePlayer = new Button("1 Player");
		onePlayer.setMinHeight(90);
		onePlayer.setMinWidth(230);
		onePlayer.setOnAction(e -> {
			twoPlayerMode = false;
			scene1(primaryStage);
		});
		Button twoPlayers = new Button("2 Players");
		twoPlayers.setMinHeight(90);
		twoPlayers.setMinWidth(230);
		twoPlayers.setOnAction(e -> {
			twoPlayerMode = true;
			scene1(primaryStage);
		});
		
		Region region = new Region();
		region.setMinWidth(30);
		chooseMode.getChildren().addAll(onePlayer,region,twoPlayers);
		root1.setBottom(chooseMode);
		root1.setTop(welcomeBox);
		primaryStage.setScene(modePage);
		
	}
	
	// Enter Players' Names
	public static void scene1(Stage primaryStage) {
		Image background = new Image("application/media/backgrounds/back1.jpeg");
		ImageView backgroundIV = new ImageView(background);
		backgroundIV.fitHeightProperty().bind(primaryStage.heightProperty());
		backgroundIV.fitWidthProperty().bind(primaryStage.widthProperty());
		
		// Scene Organisation
		BorderPane root = new BorderPane();
		root.getChildren().add(backgroundIV);
		
		VBox root1 = new VBox(10);
		root1.setAlignment(Pos.CENTER);
		root1.setPadding(new Insets(10, 10, 10, 10));
		homepage = new Scene(root,400,400);
		
		// First Player Enter Name
		Label enterFirstPlayerNameLabel = new Label();
		if(twoPlayerMode) {
			enterFirstPlayerNameLabel.setText("First Player Name:     ");
		}
		
		else{
			enterFirstPlayerNameLabel.setText("Enter You Name: ");
		}
		
		enterFirstPlayerNameLabel.setFont(new Font("Didot.",14));
		TextField name1TextField = new TextField();
		HBox firstPlayerHBox = new HBox();
		firstPlayerHBox.setAlignment(Pos.CENTER);
		firstPlayerHBox.getChildren().addAll(enterFirstPlayerNameLabel, name1TextField);
		// Second Player Enter Name
		
		Label enterSecondPlayerNameLabel = new Label("Second Player Name: ");
		enterSecondPlayerNameLabel.setFont(new Font("Didot.",14));
		TextField name2TextField = new TextField();
		HBox secondPlayerHBox = new HBox();
		secondPlayerHBox.setAlignment(Pos.CENTER);
		secondPlayerHBox.getChildren().addAll(enterSecondPlayerNameLabel, name2TextField);
		// Begin Game Button
		Button startBtn = new Button("Begin Game!");
		startBtn.setOnAction(e -> {
			player1 = new Player(name1TextField.getText());
			if(twoPlayerMode)
				player2 = new Player(name2TextField.getText());
			else player2 = new Player("Computer");
			
			try {
				game = new Game(player1, player2);
				champions = Game.getAvailableChampions();
				q = game.getTurnOrder();
			} catch (IOException e1) {
				throwException(e1.getMessage());;
			}
			scene2(primaryStage);
		});
		// Configuring Nodes
		HBox buttonHBox = new HBox();
		buttonHBox.setAlignment(Pos.CENTER);
		buttonHBox.getChildren().add(startBtn);
		if(twoPlayerMode)
			root1.getChildren().addAll(firstPlayerHBox, secondPlayerHBox, buttonHBox);
		else root1.getChildren().addAll(firstPlayerHBox, buttonHBox);
		
		root.setCenter(root1);
		primaryStage.setScene(homepage);
		primaryStage.setFullScreen(true);
	}
	
	// Choose Champions
	public static void scene2(Stage primaryStage){ 
		Image background = new Image("application/media/backgrounds/chooseView.jpeg");
		ImageView backgroundIV = new ImageView(background);
		backgroundIV.fitHeightProperty().bind(primaryStage.heightProperty());
		backgroundIV.fitWidthProperty().bind(primaryStage.widthProperty());
		
		// Map Champions with their Images
		aliveMap = new HashMap<Champion,String>();
		deadMap = new HashMap<Champion, String>();
		for (int i = 1; i <= 15; i++) {
			aliveMap.put(Game.getAvailableChampions().get(i-1), "./application/media/" + i + ".jpeg");
			deadMap.put(Game.getAvailableChampions().get(i-1), "./application/media/" + i + "d.jpeg");
		}
		
		// Scene Organisation
		BorderPane root2 = new BorderPane();
		root2.getChildren().add(backgroundIV);
		begin = new Scene(root2);
		primaryStage.setScene(begin);
		primaryStage.setFullScreen(true);
		HBox chosenChampions = new HBox();
		VBox detailsVBox = new VBox();
		GridPane champsgrid = new GridPane();

		root2.setTop(chosenChampions);
		root2.setCenter(detailsVBox);
		root2.setBottom(champsgrid);
		
		
		// Chosen Champions Bar
		// First Player Label and Selected Champions ImageViews
		Label player1LabelScene2 = new Label(player1.getName());
		Image notYetSelected = new Image("application/media/gameIcon.jpeg");
		ImageView chosen1_1 = new ImageView(notYetSelected);
		chosen1_1.setFitWidth(50);
		chosen1_1.setFitHeight(50);
		ImageView chosen1_2 = new ImageView(notYetSelected);
		chosen1_2.setFitWidth(50);
		chosen1_2.setFitHeight(50);
		ImageView chosen1_3 = new ImageView(notYetSelected);
		chosen1_3.setFitWidth(50);
		chosen1_3.setFitHeight(50);
		Region region = new Region();
		region.setMinWidth(100);
		// Second Player Label and Selected Champions ImageViews
		ImageView chosen2_1 = new ImageView(notYetSelected);
		chosen2_1.setFitWidth(50);
		chosen2_1.setFitHeight(50);
		ImageView chosen2_2 = new ImageView(notYetSelected);
		chosen2_2.setFitWidth(50);
		chosen2_2.setFitHeight(50);
		ImageView chosen2_3 = new ImageView(notYetSelected);
		chosen2_3.setFitWidth(50);
		chosen2_3.setFitHeight(50);
		Label player2LabelScene2 = new Label(player2.getName());
		// Configuring Nodes
		chosenChampions.getChildren().addAll(player1LabelScene2, chosen1_1, chosen1_2, chosen1_3, region, chosen2_1, chosen2_2, chosen2_3, player2LabelScene2);
		chosenChampions.setAlignment(Pos.CENTER);
		chosenChampions.setSpacing(10);
		chosenChampions.setPadding(new Insets(15, 15, 15, 15));
		
		
		// Show Details Box
		// Click to show details Message
		Label clickMsg = new Label("Click on a champion to show details.");
		// Configuring Nodes
		detailsVBox.getChildren().add(clickMsg);
		detailsVBox.setPadding(new Insets(10, 10, 10, 10));
		detailsVBox.setAlignment(Pos.CENTER);
		
		
	    // Champions Buttons GridPane
		chosenMap = new HashMap<Champion,Boolean>();
		int a = 0; int b = 0;
		for(Champion c : champions) {
			chosenMap.put(c, false);
			Image ch = new Image(aliveMap.get(c));
			ImageView iv = new ImageView(ch);
			Button btn = new Button();
			btn.setMinHeight(110);
			btn.setMinWidth(110);
			BackgroundImage bImage = new BackgroundImage(ch, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(btn.getWidth(), btn.getHeight(), true, true, true, false));
			Background backGround = new Background(bImage);
			btn.setBackground(backGround);
		    btn.setOnAction((e) -> {
		    	picked = false;
	    		show(c, root2, chosenChampions,ch, btn, primaryStage);
	    		if(picked)
	    			btn.setDisable(true);
		    });
		    
		    champsgrid.add(btn, a, b);
		    a++;
		    if (a == 5) {
		    	a = 0;
		    	b++;
		    }
		    
		    championsButtons.add(btn);
		}
		
		// Configuring Nodes
		champsgrid.setPadding(new Insets(10, 10, 10, 10));
//		champsgrid.setStyle("-fx-background-color: #222;");
		champsgrid.setAlignment(Pos.CENTER);
	}

	
	// Show Pressed Champion's Details
	public static void show(Champion champion, BorderPane root2, HBox chosenChampions, Image ch, Button btn, Stage primaryStage) {
		// Organisation
		VBox details = new VBox(15);
		
		// Configuring Nodes
    	details.setPadding(new Insets(10, 10, 10, 10));
    	details.setAlignment(Pos.CENTER);
		root2.setCenter(details);

    	// Information Labels about Pressed Champion
    	String type = "";
    	if (champion instanceof AntiHero)
    		type = "AntiHero";
    	else if (champion instanceof Hero)
    		type = "Hero";
    	else
    		type = "Villain";
//    	Label championType = new Label("Type: " + type);
//    	championType.setFont(new Font("Impact",20));
		Label championNameAndType = new Label(champion.getName() + "(" + type + ")");
		championNameAndType.setFont(new Font("Impact",20));
		Label championMaxHP = new Label("Maximum HP: " + champion.getMaxHP() + "");
		championMaxHP.setFont(new Font("Impact",20));
		Label championMana = new Label("Mana: " + champion.getMana() + "");
		championMana.setFont(new Font("Impact",20));
		Label championActions = new Label("Maximum Actions Points per Turn: " + champion.getMaxActionPointsPerTurn() + "");
		championActions.setFont(new Font("Impact",20));
		Label championSpeed = new Label ("Speed: " + champion.getSpeed() + "");
		championSpeed.setFont(new Font("Impact",20));
		Label championRange = new Label ("Attack Range: " + champion.getAttackRange() + "");
		championRange.setFont(new Font("Impact",20));
		Label championDamage = new Label ("Attack Damage: " + champion.getAttackDamage() + "");
		championDamage.setFont(new Font("Impact",20));
		Label championAbilities = new Label ("Abilities: " + champion.getAbilities().get(0).getName() + ", " +
				champion.getAbilities().get(1).getName() + ", " + champion.getAbilities().get(2).getName() + ".");
		championAbilities.setFont(new Font("Impact",20));
		// Choose Button
		Button choose = new Button("Pick");
		boolean chosen = chosenMap.get(champion);
		if(chosen) choose.setDisable(true);
		choose.setOnAction(e -> {
			picked = true;
			// Putting the Chosen Champion's Image in Status Bar
			if(player1.getTeam().size() < 3) {
				player1.getTeam().add(champion);
				if(player1.getTeam().size() == 1) {
					ImageView img = (ImageView)(chosenChampions.getChildren().get(1));
					img.setImage(ch);
				}
				
				else if(player1.getTeam().size() == 2) {
					ImageView img = (ImageView)(chosenChampions.getChildren().get(2));
					img.setImage(ch);
				}
				
				else if(player1.getTeam().size() == 3) {
					ImageView img = (ImageView)(chosenChampions.getChildren().get(3));
					img.setImage(ch);
					
					if(!twoPlayerMode) {
						
						do {
							int i = (int)(Math.random() * champions.size());
							Champion cc = champions.get(i);
							
							Image cch = new Image(aliveMap.get(cc));
							ImageView iv = new ImageView(cch);
							iv.setFitHeight(80);
							iv.setFitWidth(80);
							if(!player1.getTeam().contains(cc) && !player2.getTeam().contains(cc)) {
								player2.getTeam().add(cc);
								if(player2.getTeam().size() == 1) {
									ImageView imgg = (ImageView)(chosenChampions.getChildren().get(5));
									imgg.setImage(cch);
								}
								
								else if(player2.getTeam().size() == 2) {
									ImageView imgg = (ImageView)(chosenChampions.getChildren().get(6));
									imgg.setImage(cch);
								}
								
								else if(player2.getTeam().size() == 3) {
									ImageView imgg = (ImageView)(chosenChampions.getChildren().get(7));
									imgg.setImage(cch);					
								}
								
							}
						}while(player2.getTeam().size()<3);
						
					}
				}
			}
			
			else {
				if(twoPlayerMode) {
					player2.getTeam().add(champion);
					if(player2.getTeam().size() == 1) {
						ImageView img = (ImageView)(chosenChampions.getChildren().get(5));
						img.setImage(ch);
					}
					
					else if(player2.getTeam().size() == 2) {
						ImageView img = (ImageView)(chosenChampions.getChildren().get(6));
						img.setImage(ch);
					}
					
					else if(player2.getTeam().size() == 3) {
						ImageView img = (ImageView)(chosenChampions.getChildren().get(7));
						img.setImage(ch);					
					}
				}
			}			
			
			// Disabling Choose Button and Setting the Chosen Champion to true in chosenMap
			choose.setDisable(true);
			for(Map.Entry<Champion,Boolean> m : chosenMap.entrySet()) {
				if(m.getKey() == champion) {
					chosenMap.put(m.getKey(),true);
				}
			}
			
			// Disable All Champions Buttons When Teams Full and Ask to Choose Leaders
			if (player1.getTeam().size() + player2.getTeam().size() == 6) {
				for(Button b : championsButtons) {
					b.setDisable(true);
				}
					
				full = true;
				details.getChildren().clear();
				
				
				Label chooseLeaderLabel1 = new Label("Choose a leader for the first team");
				chooseLeaderLabel1.setFont(new Font("Didot.",14));
				details.getChildren().add(chooseLeaderLabel1);
				
				for (int i = 1; i <= 3; i++) {
					Button button = new Button();
					int a = i - 1;
					button.setOnAction(event -> {
						chooseLeader(player1, player1.getTeam().get(a), details, primaryStage);
					});
					button.setPrefSize(50, 50);
					ImageView img = (ImageView)(chosenChampions.getChildren().get(i));
				    button.setGraphic(img);
				    img.setFitHeight(80);
				    img.setFitWidth(80);
					details.getChildren().add(button);
				}
				
				Label chooseLeaderLabel2 = new Label("Choose a leader for the second team");
				chooseLeaderLabel2.setFont(new Font("Didot.",14));
				details.getChildren().add(chooseLeaderLabel2); 
				
				for (int i = 5; i <= 7; i++) {
					Button button = new Button();
					int a = i-5;
					button.setOnAction(event -> {
						chooseLeader(player2,player2.getTeam().get(a), details, primaryStage);
					});
					button.setPrefSize(50, 50);
					ImageView img = (ImageView)(chosenChampions.getChildren().get(i));
				    button.setGraphic(img);
				    img.setFitHeight(60);
				    img.setFitWidth(60);
					details.getChildren().add(button);
				}
				if(!twoPlayerMode) {
					details.getChildren().get(5).setDisable(true);
					details.getChildren().get(6).setDisable(true);
					details.getChildren().get(7).setDisable(true);
				}
				
			    chosenChampions.getChildren().clear();
			}
		});
		
		if(player1.getTeam().size()==3 && !twoPlayerMode) choose.fire();
		
		if (!full) {
				details.getChildren().addAll(championNameAndType, championMaxHP, championMana, championActions,
				championSpeed, championRange, championDamage,championAbilities, choose);
		}
	}
	
	// Set Leader and Disable Choosing Another Leader
	public static void chooseLeader(Player player, Champion c, VBox details, Stage primaryStage) {
		
		if (player == player1) {			
			player1.setLeader(c);
			details.getChildren().get(1).setDisable(true);
			details.getChildren().get(2).setDisable(true);
			details.getChildren().get(3).setDisable(true);
			
			if(!twoPlayerMode) {
				int i = (int)(Math.random() * player2.getTeam().size());
				player2.setLeader(player2.getTeam().get(i));
				details.getChildren().get(5).setDisable(true);
				details.getChildren().get(6).setDisable(true);
				details.getChildren().get(7).setDisable(true);
			}
		}

		else if (player == player2) {
			player2.setLeader(c);
			details.getChildren().get(5).setDisable(true);
			details.getChildren().get(6).setDisable(true);
			details.getChildren().get(7).setDisable(true);
		}
		
		if(player1.getLeader()!=null && player2.getLeader()!=null) {
			Button play = new Button("Play");
			play.setOnAction(e -> {
				try {
					scene3(primaryStage);
				} catch (IOException e1) { }
			});
			details.getChildren().add(play);
		}
	}

	// Open Board Game View
	public static void scene3(Stage primaryStage) throws IOException {
		Image backgroundBoard = new Image("application/media/backgrounds/gameplay-1.jpeg");
		ImageView backgroundBoardIV = new ImageView(backgroundBoard);
		backgroundBoardIV.fitHeightProperty().bind(primaryStage.heightProperty());
		backgroundBoardIV.fitWidthProperty().bind(primaryStage.widthProperty());
		
		// Assign players' champions team arrays for image views
		for (Champion c : player1.getTeam()) {
			player1Champions.add(c);
		}
		for (Champion c : player2.getTeam()) {
			player2Champions.add(c);
		}
		board = game.getBoard();
		game.placeChampions();
		game.prepareChampionTurns();
		
		for (int i = 0; i < 15; i++) {
			memo.add(true);
		}
		
		// Scene organisation     
		BorderPane root3 = new BorderPane();
		root3.getChildren().add(backgroundBoardIV);
		gameview = new Scene(root3);
		keyMoved();
		primaryStage.setScene(gameview);
		primaryStage.setFullScreen(true);
		
		gameStatus = new HBox(10);
		turnOrderStatus = new VBox(15);
		currentControls = new VBox(5);
		boardView = new GridPane();
		currentInformation = new VBox(5);
		root3.setTop(gameStatus);
		root3.setRight(turnOrderStatus);
		root3.setBottom(currentControls);
		root3.setLeft(currentInformation);
		root3.setCenter(boardView);
		                                                       
		gameStatus.setPadding(new Insets(10,10,10,10));
		gameStatus.setAlignment(Pos.CENTER);
		turnOrderStatus.setPadding(new Insets(15,15,15,15));
		turnOrderStatus.setAlignment(Pos.TOP_RIGHT);
		turnOrderStatus.setMaxWidth(400);
		turnOrderStatus.setMinWidth(400);
		currentControls.setAlignment(Pos.CENTER);
		currentControls.setPadding(new Insets(10,10,30,10));
		boardView.setAlignment(Pos.CENTER);	
		currentInformation.setMaxWidth(400);
		currentInformation.setMinWidth(400);
		currentInformation.setAlignment(Pos.TOP_LEFT);
		currentInformation.setPadding(new Insets(10, 10, 10, 30));
		
		showControls();
		updateStatusBar();
		prepareTurns();
		updateCurrentInformation();
		updateBoard();
			
		if(!twoPlayerMode && player2.getTeam().contains(game.getCurrentChampion())) {
			computerAction();
		}
	}	
	
	// Update the Turn Order Status
	public static void prepareTurns() {
		turnOrderStatus.getChildren().clear();
		tmp = new PriorityQueue(q.size());
		Label turnLabel = new Label("Next in Turn: ");
		turnOrderStatus.getChildren().add(turnLabel);
		while(!q.isEmpty()){
			Image img = new Image(aliveMap.get((Champion)q.peekMin()));
			ImageView iv = new ImageView(img);
			iv.setFitHeight(75);
			iv.setFitWidth(75);
			
			turnOrderStatus.getChildren().add(iv);
			tmp.insert((Champion)q.remove());
		}
		while (!tmp.isEmpty()) {
			q.insert((Champion)tmp.remove());
		}
	}
	
	// Update Current Champion's Information
	public static void updateCurrentInformation() {
		currentInformation.getChildren().clear();
		// Get Current Champion
		Champion champion = game.getCurrentChampion();
		
		// Get Attributes of Current Champion
		String type="";
		if (champion instanceof AntiHero)
    		type = "AntiHero";
    	else if (champion instanceof Hero)
    		type = "Hero";
    	else
    		type = "Villain";
		String championEffects = "";
		for (Effect e : champion.getAppliedEffects()) {
			championEffects += e.getName() + "(" + e.getDuration() + " turns)" + ", ";
		}
		if (championEffects.length() >= 2)
			championEffects = championEffects.substring(0,championEffects.length()-2) + ".";
		Label championName = new Label(champion.getName().toUpperCase());
		championName.setFont(new Font("Didot.",15));
		championName.setTextFill(Color.color(1, 1, 1));
		Label championType = new Label("Type: " + type);
		championType.setFont(new Font("Didot.",15));
		Label championMaxHP = new Label("HP: " + champion.getCurrentHP() + "/" + champion.getMaxHP());
		championMaxHP.setFont(new Font("Didot.",15));
		Label championMana = new Label("Mana: " + champion.getMana() + "");
		championMana.setFont(new Font("Didot.",15));
		Label championActions = new Label("Actions Points: " + champion.getCurrentActionPoints() + "/" + champion.getMaxActionPointsPerTurn());
		championActions.setFont(new Font("Didot.",15));
		Label championSpeed = new Label ("Speed: " + champion.getSpeed() + "");
		championSpeed.setFont(new Font("Didot.",15));
		Label championRange = new Label ("Attack Range: " + champion.getAttackRange() + "");
		championRange.setFont(new Font("Didot.",15));
		Label championDamage = new Label ("Attack Damage: " + champion.getAttackDamage() + "");
		championDamage.setFont(new Font("Didot.",15));
		Label championAppliedEffects = new Label ("Applied Effects: " + championEffects);
		championAppliedEffects.setFont(new Font("Didot.",15));
		Label championCondition = new Label ("Condition: " + champion.getCondition());
		championCondition.setFont(new Font("Didot.",15));
		Region region1 = new Region();
		region1.setMinHeight(15);
		// Get Current Champion's Abilities
		Ability a1 = champion.getAbilities().get(0);
		Ability a2 = champion.getAbilities().get(1);
		Ability a3 = champion.getAbilities().get(2);

		VBox temp = new VBox(5);
		Button a1Button = actions.get(8);
		Button a2Button = actions.get(9);
		Button a3Button = actions.get(10);
				
		
		// First Ability's Attributes
		
		a1Button.setOnMouseEntered(e -> {
			Label a1Name = new Label ("First Ability: " + a1.getName());
			a1Name.setFont(new Font("Didot.",15));
			String abilityType1 = "";
			String abilityAmount1 = "";
			if (a1 instanceof DamagingAbility) {
				abilityType1 = "Damaging Ability";
				abilityAmount1 = "Damaging amount: " + ((DamagingAbility)a1).getDamageAmount();
			}
			if (a1 instanceof HealingAbility) {
				abilityType1 = "Healing Ability";
				abilityAmount1 = "Healing amount: " + ((HealingAbility)a1).getHealAmount();
			}
			else if (a1 instanceof CrowdControlAbility) {
				abilityType1 = "Crowd Control Ability";
				abilityAmount1 = "Casted effect: " + ((CrowdControlAbility)a1).getEffect().getName() + 
						"(" + ((CrowdControlAbility)a1).getEffect().getDuration() + " turns)";
			}
			Label a1Type = new Label ("Type: " + abilityType1);
			a1Type.setFont(new Font("Didot.",15));
			Label a1Amount = new Label (abilityAmount1);
			a1Amount.setFont(new Font("Didot.",15));
			Label a1Mana = new Label ("Mana Cost: " + a1.getManaCost());
			a1Mana.setFont(new Font("Didot.",15));
			Label a1Cool = new Label ("Cooldown: " + a1.getCurrentCooldown() + "/" + a1.getBaseCooldown());
			a1Cool.setFont(new Font("Didot.",15));
			Label a1Range = new Label ("Range: " + a1.getCastRange());
			a1Range.setFont(new Font("Didot.",15));
			Label a1Area = new Label ("Cast Area: " + a1.getCastArea());
			a1Area.setFont(new Font("Didot.",15));
			Label a1Action = new Label ("Required Action Points: " + a1.getRequiredActionPoints());
			a1Action.setFont(new Font("Didot.",15));
			temp.getChildren().addAll(a1Name, a1Type, a1Amount, a1Mana, a1Cool, a1Range, a1Area, a1Action);
			for (Node n : temp.getChildren()) {
				if (n instanceof Label)
					((Label)n).setTextFill(Color.color(1, 1, 1));
			}
		});
		
		// Second Ability's Attributes
		a2Button.setOnMouseEntered(e -> {
			Label a2Name = new Label ("Second Ability: " + a2.getName());
			a2Name.setFont(new Font("Didot.",15));
			String abilityType2 = "";
			String abilityAmount2 = "";
			if (a2 instanceof DamagingAbility) {
				abilityType2 = "Damaging Ability";
				abilityAmount2 = "Damaging amount: " + ((DamagingAbility)a2).getDamageAmount();
			}
			if (a2 instanceof HealingAbility) {
				abilityType2 = "Healing Ability";
				abilityAmount2 = "Healing amount: " + ((HealingAbility)a2).getHealAmount();
			}
			else if (a2 instanceof CrowdControlAbility) {
				abilityType2 = "Crowd Control Ability";
				abilityAmount2 = "Casted effect: " + ((CrowdControlAbility)a2).getEffect().getName() + 
						"(" + ((CrowdControlAbility)a2).getEffect().getDuration() + " turns)";
			}
			Label a2Type = new Label ("Type: " + abilityType2);
			a2Type.setFont(new Font("Didot.",15));
			Label a2Amount = new Label (abilityAmount2);
			a2Amount.setFont(new Font("Didot.",15));
			Label a2Mana = new Label ("Mana Cost: " + a2.getManaCost());
			a2Mana.setFont(new Font("Didot.",15));
			Label a2Cool = new Label ("Cooldown: " + a2.getCurrentCooldown() + "/" + a2.getBaseCooldown());
			a2Cool.setFont(new Font("Didot.",15));
			Label a2Range = new Label ("Range: " + a2.getCastRange());
			a2Range.setFont(new Font("Didot.",15));
			Label a2Area = new Label ("Cast Area: " + a2.getCastArea());
			a2Area.setFont(new Font("Didot.",15));
			Label a2Action = new Label ("Required Action Points: " + a2.getRequiredActionPoints());	
			a2Action.setFont(new Font("Didot.",15));
			temp.getChildren().addAll(a2Name, a2Type, a2Amount, a2Mana, a2Cool, a2Range, a2Area, a2Action);
			for (Node n : temp.getChildren()) {
				if (n instanceof Label)
					((Label)n).setTextFill(Color.color(1, 1, 1));
			}
		});
		
		// Third Ability's Attributes
		a3Button.setOnMouseEntered(e -> {
			Label a3Name = new Label ("Third Ability: " + a3.getName());
			a3Name.setFont(new Font("Didot.",15));
			String abilityType3 = "";
			String abilityAmount3 = "";
			if (a3 instanceof DamagingAbility) {
				abilityType3 = "Damaging Ability";
				abilityAmount3 = "Damaging amount: " + ((DamagingAbility)a3).getDamageAmount();
			}
			if (a3 instanceof HealingAbility) {
				abilityType3 = "Healing Ability";
				abilityAmount3 = "Healing amount: " + ((HealingAbility)a3).getHealAmount();
			}
			else if (a3 instanceof CrowdControlAbility) {
				abilityType3 = "Crowd Control Ability";
				abilityAmount3 = "Casted effect: " + ((CrowdControlAbility)a3).getEffect().getName() + 
						"(" + ((CrowdControlAbility)a3).getEffect().getDuration() + " turns)";
			}
			Label a3Type = new Label ("Type: " + abilityType3);
			a3Type.setFont(new Font("Didot.",15));
			Label a3Amount = new Label (abilityAmount3);
			a3Amount.setFont(new Font("Didot.",15));
			Label a3Mana = new Label ("Mana Cost: " + a3.getManaCost());
			a3Mana.setFont(new Font("Didot.",15));
			Label a3Cool = new Label ("Cooldown: " + a3.getCurrentCooldown() + "/" + a3.getBaseCooldown());
			a3Cool.setFont(new Font("Didot.",15));
			Label a3Range = new Label ("Range: " + a3.getCastRange());
			a3Range.setFont(new Font("Didot.",15));
			Label a3Area = new Label ("Cast Area: " + a3.getCastArea());
			a3Area.setFont(new Font("Didot.",15));
			Label a3Action = new Label ("Required Action Points: " + a3.getRequiredActionPoints());
			a3Action.setFont(new Font("Didot.",15));
			temp.getChildren().addAll(a3Name, a3Type, a3Amount, a3Mana, a3Cool, a3Range, a3Area, a3Action);
			for (Node n : temp.getChildren()) {
				if (n instanceof Label)
					((Label)n).setTextFill(Color.color(1, 1, 1));
			}
		});
		
		a1Button.setOnMouseExited(e -> {
			temp.getChildren().clear();
		});

		a2Button.setOnMouseExited(e -> {
			temp.getChildren().clear();
		});
		
		a3Button.setOnMouseExited(e -> {
			temp.getChildren().clear();
		});
		
		if (punch && game.getCurrentChampion().getAbilities().size() > 3) {
			Button a4Button = actions.get(11);
			Ability a4 = game.getCurrentChampion().getAbilities().get(3);
			a4Button.setOnMouseEntered(e -> {
				Label a4Name = new Label ("Fourth Ability: " + a4.getName());
				a4Name.setFont(new Font("Didot.",15));
				String abilityType4 = "";
				String abilityAmount4 = "";
				if (a4 instanceof DamagingAbility) {
					abilityType4 = "Damaging Ability";
					abilityAmount4 = "Damaging amount: " + ((DamagingAbility)a4).getDamageAmount();
				}
				if (a4 instanceof HealingAbility) {
					abilityType4 = "Healing Ability";
					abilityAmount4 = "Healing amount: " + ((HealingAbility)a4).getHealAmount();
				}
				else if (a4 instanceof CrowdControlAbility) {
					abilityType4 = "Crowd Control Ability";
					abilityAmount4 = "Casted effect: " + ((CrowdControlAbility)a4).getEffect().getName() + 
							"(" + ((CrowdControlAbility)a4).getEffect().getDuration() + " turns)";
				}
				Label a4Type = new Label ("Type: " + abilityType4);
				a4Type.setFont(new Font("Didot.",15));
				Label a4Amount = new Label (abilityAmount4);
				a4Amount.setFont(new Font("Didot.",15));
				Label a4Mana = new Label ("Mana Cost: " + a4.getManaCost());
				a4Mana.setFont(new Font("Didot.",15));
				Label a4Cool = new Label ("Cooldown: " + a4.getCurrentCooldown() + "/" + a4.getBaseCooldown());
				a4Cool.setFont(new Font("Didot.",15));
				Label a4Range = new Label ("Range: " + a4.getCastRange());
				a4Range.setFont(new Font("Didot.",15));
				Label a4Area = new Label ("Cast Area: " + a4.getCastArea());
				a4Area.setFont(new Font("Didot.",15));
				Label a4Action = new Label ("Required Action Points: " + a4.getRequiredActionPoints());
				a4Action.setFont(new Font("Didot.",15));
				temp.getChildren().addAll(a4Name, a4Type, a4Amount, a4Mana, a4Cool, a4Range, a4Area, a4Action);
				for (Node n : temp.getChildren()) {
					if (n instanceof Label)
						((Label)n).setTextFill(Color.color(1, 1, 1));
				}
			});
			
			a4Button.setOnMouseExited(e -> {
				temp.getChildren().clear();
			});
		}
		
		// Configuring Nodec
		currentInformation.getChildren().addAll(championName,championType,championMaxHP,championMana,championActions,
				championSpeed, championRange, championDamage, championAppliedEffects, championCondition, 
				region1, temp);
		for (Node n : currentInformation.getChildren()) {
			if (n instanceof Label)
				((Label)n).setTextFill(Color.color(1, 1, 1));
		}
		
	}
		
	// Update the Status of Players' Champions and Leader Ability
	public static void updateStatusBar() {
		gameStatus.getChildren().clear();
		Label player1Name = new Label(player1.getName());
		player1Name.setFont(new Font("Didot.",16));
		gameStatus.getChildren().add(player1Name);
		
		for (Champion c : player1Champions) {
			Image image = new Image(aliveMap.get(c));
			if (c.getCurrentHP() == 0)
				image = new Image(deadMap.get(c));
			ImageView iv = new ImageView(image);
			iv.setFitHeight(80);
			iv.setFitWidth(80);
			gameStatus.getChildren().add(iv);
		}
		
		Image LeaderAbilityNotUsed = new Image("./application/media/pow.jpeg");
		Image LeaderAbilityUsed = new Image ("./application/media/powd.jpeg");
		ImageView firstLeaderAbility = new ImageView();
		ImageView secondLeaderAbility = new ImageView();
		if (!game.isFirstLeaderAbilityUsed()) firstLeaderAbility = new ImageView(LeaderAbilityNotUsed);
		if (game.isFirstLeaderAbilityUsed()) firstLeaderAbility = new ImageView(LeaderAbilityUsed);
		if (!game.isSecondLeaderAbilityUsed()) secondLeaderAbility = new ImageView(LeaderAbilityNotUsed);
		if (game.isSecondLeaderAbilityUsed()) secondLeaderAbility = new ImageView(LeaderAbilityUsed);

		Region r = new Region();
		r.setMinWidth(100);
		firstLeaderAbility.setFitHeight(80);
		firstLeaderAbility.setFitWidth(80);
		secondLeaderAbility.setFitHeight(80);
		secondLeaderAbility.setFitWidth(80);
		gameStatus.getChildren().addAll(firstLeaderAbility, r, secondLeaderAbility);
		
		for (Champion c : player2Champions) {
			Image image = new Image(aliveMap.get(c));
			if (c.getCurrentHP() == 0)
				image = new Image(deadMap.get(c));
			ImageView iv = new ImageView(image);
			iv.setFitHeight(80);
			iv.setFitWidth(80);
			gameStatus.getChildren().add(iv);
		}
		Label player2Name = new Label(player2.getName());
		player2Name.setFont(new Font("Didot.",16));
		player1Name.setTextFill(Color.color(1, 1, 1));
		player2Name.setTextFill(Color.color(1, 1, 1));
		gameStatus.getChildren().add(player2Name);
	}
	
	public static void updateBoard() {		
		for(int i=0;i<5;i++) {
			for(int j=0;j<5;j++) {
				boolean isCurrent1 = false;
				boolean isCurrent2 = false;
				Button btn = new Button();
				btn.setMinHeight(100);
				btn.setMinWidth(100);
				btn.setMaxHeight(100);
				btn.setMaxWidth(100);
				boardButtons[j][4-i] = btn;
				
				if(board[i][j] instanceof Cover) {
					Image img = new Image("./application/media/cover.jpeg");
					ImageView iv = new ImageView(img);
					iv.setFitHeight(90);
					iv.setFitWidth(90);
					btn.setGraphic(iv);
					int a = i;
					int b = j;
					btn.setOnAction(e -> {
						Stage currentHealth = new Stage();
						currentHealth.setTitle("Cover");
						VBox window = new VBox(10);
						window.setAlignment(Pos.CENTER);
						Scene scene = new Scene(window);
						Button OK = new Button("OK");
						OK.setOnAction( ee -> currentHealth.close());
						
						currentHealth.setScene(scene);
						currentHealth.setMinWidth(400);
						currentHealth.setMinHeight(200);
						Text msgText =new Text("Cover's health: " + ((Cover)(board[a][b])).getCurrentHP());
						window.getChildren().addAll(msgText, OK);
						window.setPadding(new Insets(10,10,10,10));
						currentHealth.show();
					});
				}
				
				else if(board[i][j] instanceof Champion){
					Champion c = (Champion)board[i][j];
					Image img = new Image(aliveMap.get(c));
					ImageView iv = new ImageView(img);
					iv.setFitHeight(90);
					iv.setFitWidth(90);
					btn.setGraphic(iv);
					Champion current = game.getCurrentChampion();
					if (c == current && player1.getTeam().contains(current)) {
//						btn.setStyle("-fx-background-color: #010098;");
						isCurrent1 = true;
					}
					else if (c == current && player2.getTeam().contains(current)) {
//						btn.setStyle("-fx-background-color: #9a0000; ");
						isCurrent2 = true;
					}
					btn.setOnAction(e -> {
						Stage currentHealth = new Stage();
						String type = "";
						if (c instanceof Hero) type = "Hero";
						else if (c instanceof AntiHero) type = "AntiHero";
						else type = "Villain";
						currentHealth.setTitle(c.getName() + " (" + type + ")");
						VBox window = new VBox(10);
						window.setAlignment(Pos.CENTER);
						Scene scene = new Scene(window);
						Button OK = new Button("OK");
						OK.setOnAction( ee -> currentHealth.close());
						currentHealth.setScene(scene);
						currentHealth.setMinWidth(400);
						currentHealth.setMinHeight(200);
						Text teamText;
						if (player1.getTeam().contains(c))
							teamText = new Text("Belonging to first team");
						else 
							teamText = new Text("Belonging to second team");
						Text healthText =new Text("Champion's health: " + c.getCurrentHP() + "/" + c.getMaxHP());
						Text conditionText =new Text("Champion's condition: " + c.getCondition());
						String effects = "";
						for (Effect effect : c.getAppliedEffects()) {
							effects += effect.getName() + ", ";
						}
						if (effects.length() >= 2)
							effects = effects.substring(0, effects.length()-2);
						Text effectsText =new Text("Effects on Champion: " + effects);
						Text otherText = new Text("Mana: " + c.getMana() + ", " +
								"Speed: " + c.getSpeed() + ", \n" +
								"Max Actions per Turn: " + c.getMaxActionPointsPerTurn() + ", \n" +
								"Attack Range: " + c.getAttackRange() + ", " +
								"Attack Damage: " + c.getAttackDamage() + ".");
						otherText.setTextAlignment(TextAlignment.CENTER);
						Text leaderText = new Text("Champion is NOT a leader.");
						if (player1.getLeader() == c || player2.getLeader() == c)
							leaderText = new Text("Champion is a leader");
						window.getChildren().addAll(teamText, healthText, conditionText, effectsText, otherText, leaderText, OK);
						window.setPadding(new Insets(10,10,10,10));
						currentHealth.show();
					});
				}
				
				if(!isCurrent1&&!isCurrent2)
					btn.setStyle("-fx-border-color: #313135 ; -fx-background-color: #ccccff");
				else if(isCurrent1) {
					btn.setStyle("-fx-border-color: #313135 ; -fx-background-color: #010098");
				}
				else if(isCurrent2) {
					btn.setStyle("-fx-border-color: #313135 ; -fx-background-color: #9a0000");
				}
				boardView.add(btn,j,4-i);
			}
		}
	}
	
	public static void throwException(String msg) {
		Stage exception = new Stage();
		exception.setTitle("Error");
		VBox window = new VBox(10);
		window.setAlignment(Pos.CENTER);
		Scene scene = new Scene(window);
		Button OK = new Button("OK");
		OK.setOnAction( e -> exception.close());
		exception.setScene(scene);
		exception.setMinWidth(400);
		exception.setMinHeight(200);
		Text msgText =new Text(msg);
		window.getChildren().addAll(msgText, OK);
		window.setPadding(new Insets(10,10,10,10));
		exception.show();
	}
	
	public static void showControls()  {
		actions.clear();
		currentControls.getChildren().clear();
		HBox move = new HBox(10);
		move.setAlignment(Pos.CENTER);
		Region region1 = new Region();
		region1.setMinWidth(5);
		move.getChildren().addAll(moveUp(), moveDown(), moveRight(), moveLeft(), region1, attackUp(), attackDown(), attackRight(), attackLeft());
		HBox abilities = new HBox(10);
		abilities.setAlignment(Pos.CENTER);
		Region region2 = new Region();
		region2.setMinWidth(5);
		abilities.getChildren().addAll(castAbility(0, 8), castAbility(1, 9), castAbility(2, 10));
//		if (punch) {
		punch  = false;
		for (Effect effect : game.getCurrentChampion().getAppliedEffects()) {
			if (effect instanceof Disarm) {
				punch = true;
				break;
			}
		}
		
		
		abilities.getChildren().addAll(castAbility(3, 11), useLeaderAbility(), region2, manualButton(), endTurn());
		currentControls.getChildren().addAll(move, abilities);
}
	
	public static Button manualButton() {
		Button manual = new Button("Open Game Manual");
		manual.setMinHeight(30);
		manual.setMinWidth(30);
		actions.add(manual);
		manual.setOnAction(e -> {
			Stage manualStage = new Stage();
			manualStage.setTitle("Game Manual");
			VBox mainBox = new VBox(10);
			mainBox.setAlignment(Pos.CENTER);
			VBox tmpBox = new VBox(10);
			tmpBox.setAlignment(Pos.CENTER);
			VBox effectBox = new VBox(10);
			effectBox.setAlignment(Pos.CENTER);
			Scene mainScene = new Scene(mainBox);
			Scene tmpScene = new Scene(tmpBox);
			Scene effectScene = new Scene(effectBox);
			// Abilities Manual Button
				Button abilities = new Button("Abilities");
				abilities.setOnAction(ee -> {
					tmpBox.getChildren().clear();
					manualStage.setTitle("Abilities Manual");
					Text info = new Text("There are 3 types of abilities:\n "
							+ "Damaging Ability, Healing Ability, Crowd Control Ability.\n"
							+ "Each Ability requires a ceratin amount of actions and mana to be casted.\n"
							+ "Each Ability has a specific Casting Area.");
					info.setTextAlignment(TextAlignment.CENTER);
					Text DA = new Text("Damaging Abilities deal a certain amount\n"
							+ "of damage to enemies within Casting Area.");
					Text HA = new Text("Healing Abilities add a certain amount\n"
							+ "of health to friends within Casting Area.");
					Text CCA = new Text("Crowd Control Abilities activate a certain\n"
							+ "effect on targets within Casting Area.");
					Button back = new Button("Go Back");
					back.setOnAction(eee -> {
						manualStage.setTitle("Game Manual");
						manualStage.setScene(mainScene);
					});
					tmpBox.getChildren().addAll(info, DA, HA, CCA, back);
					manualStage.setScene(tmpScene);
				});
				
				
				// Effects Manual Button
				Button effects = new Button("Effects");
				effects.setOnAction(ee -> {
					tmpBox.getChildren().clear();
					manualStage.setTitle("Effects Manual");
					
					Label selectEffect = new Label("Select an Effect");
					
					Button disarm = new Button("Disarm");
					disarm.setOnAction(eee -> {
						effectBox.getChildren().clear();
						manualStage.setTitle("Disarm Effect");
						Text disarmText = new Text("Target cannot use normal attacks.\n" + 
								"Gain a SINGLETARGET damaging ability");
						disarmText.setTextAlignment(TextAlignment.CENTER);
						Button backToTmp = new Button("Back to Effects List");
						backToTmp.setOnAction(eeee -> {
							manualStage.setTitle("Effects Manual");
							manualStage.setScene(tmpScene);
						});
						effectBox.getChildren().addAll(disarmText, backToTmp);
						manualStage.setScene(effectScene);
					});
					
					Button dodge = new Button("Dodge");
					dodge.setOnAction(eee -> {
						effectBox.getChildren().clear();
						manualStage.setTitle("Dodge Effect");
						Text dodgeText = new Text("Target has a 50% chance of dodging normal attacks.\n" + 
								"Increase speed by 5%");
						dodgeText.setTextAlignment(TextAlignment.CENTER);
						Button backToTmp = new Button("Back to Effects List");
						backToTmp.setOnAction(eeee -> {
							manualStage.setTitle("Effects Manual");
							manualStage.setScene(tmpScene);
						});
						effectBox.getChildren().addAll(dodgeText, backToTmp);
						manualStage.setScene(effectScene);
					});
					
					Button embrace = new Button("Embrace");
					embrace.setOnAction(eee -> {
						effectBox.getChildren().clear();
						manualStage.setTitle("Embrace Effect");
						Text embraceText = new Text("Permanently add 20% from maxHP to currentHP,\n" + 
								"Permanently increase mana by 20%,\n" + 
								"Increase speed and attackDamage by 20%.");
						embraceText.setTextAlignment(TextAlignment.CENTER);
						Button backToTmp = new Button("Back to Effects List");
						backToTmp.setOnAction(eeee -> {
							manualStage.setTitle("Effects Manual");
							manualStage.setScene(tmpScene);
						});
						effectBox.getChildren().addAll(embraceText, backToTmp);
						manualStage.setScene(effectScene);
					});
					
					Button powerup = new Button("PowerUp");
					powerup.setOnAction(eee -> {
						effectBox.getChildren().clear();
						manualStage.setTitle("PowerUp Effect");
						Text powerupText = new Text("Increase damageAmount and healAmount of all damaging\n"
								+ "and healing abilities of the target by 20%");
						powerupText.setTextAlignment(TextAlignment.CENTER);
						Button backToTmp = new Button("Back to Effects List");
						backToTmp.setOnAction(eeee -> {
							manualStage.setTitle("Effects Manual");
							manualStage.setScene(tmpScene);
						});
						effectBox.getChildren().addAll(powerupText, backToTmp);
						manualStage.setScene(effectScene);
					});
					
					Button root	= new Button("Root");
					root.setOnAction(eee -> {
						effectBox.getChildren().clear();
						manualStage.setTitle("Root Effect");
						Text rootText = new Text("Target cannot move.");
						rootText.setTextAlignment(TextAlignment.CENTER);
						Button backToTmp = new Button("Back to Effects List");
						backToTmp.setOnAction(eeee -> {
							manualStage.setTitle("Effects Manual");
							manualStage.setScene(tmpScene);
						});
						effectBox.getChildren().addAll(rootText, backToTmp);
						manualStage.setScene(effectScene);
					});
					
					Button shield = new Button("Shield");
					shield.setOnAction(eee -> {
						effectBox.getChildren().clear();
						manualStage.setTitle("Shield Effect");
						Text shieldText = new Text("Block the next attack or damaging ability cast on target.\n" + 
								"Once an attack or ability is blocked, the effect will be removed.\n" + 
								"Increase speed by 2%.");
						shieldText.setTextAlignment(TextAlignment.CENTER);
						Button backToTmp = new Button("Back to Effects List");
						backToTmp.setOnAction(eeee -> {
							manualStage.setTitle("Effects Manual");
							manualStage.setScene(tmpScene);
						});
						effectBox.getChildren().addAll(shieldText, backToTmp);
						manualStage.setScene(effectScene);
					});
					
					Button shock = new Button("Shock");
					shock.setOnAction(eee -> {
						effectBox.getChildren().clear();
						manualStage.setTitle("Shock Effect");
						Text shockText = new Text("Decrease target speed by 10%\n" + 
								"Decrease the target’s normal attack damage by 10%\n" + 
								"Decrease max action points per turn and current action points by 1.");
						shockText.setTextAlignment(TextAlignment.CENTER);
						Button backToTmp = new Button("Back to Effects List");
						backToTmp.setOnAction(eeee -> {
							manualStage.setTitle("Effects Manual");
							manualStage.setScene(tmpScene);
						});
						effectBox.getChildren().addAll(shockText, backToTmp);
						manualStage.setScene(effectScene);
					});
					
					Button silence = new Button("Silence");
					silence.setOnAction(eee -> {
						effectBox.getChildren().clear();
						manualStage.setTitle("Silence Effect");
						Text silenceText = new Text("Target cannot use abilities.\n" + 
								"Increase max action points per turn and current action points by 2");
						silenceText.setTextAlignment(TextAlignment.CENTER);
						Button backToTmp = new Button("Back to Effects List");
						backToTmp.setOnAction(eeee -> {
							manualStage.setTitle("Effects Manual");
							manualStage.setScene(tmpScene);
						});
						effectBox.getChildren().addAll(silenceText, backToTmp);
						manualStage.setScene(effectScene);
					});
					
					Button speedup = new Button("SpeedUp");
					speedup.setOnAction(eee -> {
						effectBox.getChildren().clear();
						manualStage.setTitle("SpeedUp Effect");
						Text speedupText = new Text("Increase speed by 15%.\n" + 
								"Increase max action points per turn and current action points by 1.");
						speedupText.setTextAlignment(TextAlignment.CENTER);
						Button backToTmp = new Button("Back to Effects List");
						backToTmp.setOnAction(eeee -> {
							manualStage.setTitle("Effects Manual");
							manualStage.setScene(tmpScene);
						});
						effectBox.getChildren().addAll(speedupText, backToTmp);
						manualStage.setScene(effectScene);
					});
					
					Button stun	= new Button("Stun");
					stun.setOnAction(eee -> {
						effectBox.getChildren().clear();
						manualStage.setTitle("Stun Effect");
						Text stunText = new Text("Set target to INACTIVE.\n" + 
								"Target is not allowed to play their turn for the duration.");
						stunText.setTextAlignment(TextAlignment.CENTER);
						Button backToTmp = new Button("Back to Effects List");
						backToTmp.setOnAction(eeee -> {
							manualStage.setTitle("Effects Manual");
							manualStage.setScene(tmpScene);
						});
						effectBox.getChildren().addAll(stunText, backToTmp);
						manualStage.setScene(effectScene);
					});
					
					Button back = new Button("Go Back");
					back.setOnAction(eee -> {
						manualStage.setScene(mainScene);
					});
					
					disarm.setMinWidth(100);
					dodge.setMinWidth(100);
					embrace.setMinWidth(100);
					powerup.setMinWidth(100);
					root.setMinWidth(100);
					shield.setMinWidth(100);
					shock.setMinWidth(100);
					silence.setMinWidth(100);
					speedup.setMinWidth(100);
					stun.setMinWidth(100);
					tmpBox.getChildren().addAll(selectEffect, disarm, dodge, embrace, powerup, root, shield,
							shock, silence, speedup, stun, back);
					manualStage.setScene(tmpScene);
				});
							
				Button close = new Button("Close");
				close.setOnAction(ee -> manualStage.close());
				
				manualStage.setScene(mainScene);
				manualStage.setMinWidth(400);
				manualStage.setMinHeight(200);
				mainBox.getChildren().addAll(abilities, effects, close);
				mainBox.setPadding(new Insets(10,10,10,10));
				tmpBox.setPadding(new Insets(10,10,10,10));
				effectBox.setPadding(new Insets(10,10,10,10));
				manualStage.centerOnScreen();
				manualStage.show();
		});
		return manual;
	}
	
	public static Button moveUp() {
		Champion current = game.getCurrentChampion();
		Button moveUpButton = new Button("Move Up");
		actions.add(moveUpButton);
		moveUpButton.setOnAction(e -> {
			try {
				game.move(Direction.UP);
				showControls();
				updateCurrentInformation();
				updateStatusBar();
				prepareTurns();
				updateBoard();
				checkWinner();
			} catch (NotEnoughResourcesException | UnallowedMovementException e1) {
				if (!twoPlayerMode && player2.getTeam().contains(current)) {
					memo.set(0, false);				}
				else {
					throwException(e1.getMessage());
				}
			}
		});
		return moveUpButton;
	}
	
	public static Button moveDown() {
//		primaryStage.setScene(gameview);
//		System.out.println("New height: " + primaryStage.getScene().getHeight());
//		System.out.println("New width: " + primaryStage.getScene().getWidth());
		Champion current = game.getCurrentChampion();
		Button moveDownButton = new Button("Move Down");
		actions.add(moveDownButton);
		moveDownButton.setOnAction(e -> {
			try {
				game.move(Direction.DOWN);
				showControls();
				updateCurrentInformation();
				updateStatusBar();
				prepareTurns();
				updateBoard();
				checkWinner();
			} catch (NotEnoughResourcesException | UnallowedMovementException e1) {
				if (!twoPlayerMode && player2.getTeam().contains(current)) {
					memo.set(1, false);
					e1.printStackTrace();
				}
				else {
					throwException(e1.getMessage());
				}
			}
		});
		return moveDownButton;
	}
	
	public static Button moveRight() {
		Champion current = game.getCurrentChampion();
		Button moveRightButton = new Button("Move Right");
		actions.add(moveRightButton);
		moveRightButton.setOnAction(e -> {
			try {
				game.move(Direction.RIGHT);
				showControls();
				updateCurrentInformation();
				updateStatusBar();
				prepareTurns();
				updateBoard();
				checkWinner();
			} catch (NotEnoughResourcesException | UnallowedMovementException e1) {
				if (!twoPlayerMode && player2.getTeam().contains(current)) {
					memo.set(2, false);
					e1.printStackTrace();
				}
				else {
					throwException(e1.getMessage());
				}
			}
		});
		return moveRightButton;
	}
	
	public static Button moveLeft() {
		Champion current = game.getCurrentChampion();
		Button moveLeftButton = new Button("Move Left");
		actions.add(moveLeftButton);
		moveLeftButton.setOnAction(e -> {
			try {
				game.move(Direction.LEFT);
				showControls();
				updateCurrentInformation();
				updateStatusBar();
				prepareTurns();
				updateBoard();
				checkWinner();
			} catch (NotEnoughResourcesException | UnallowedMovementException e1) {
				if (!twoPlayerMode && player2.getTeam().contains(current)) {
					memo.set(3, false);
					e1.printStackTrace();
				}
				else {
					throwException(e1.getMessage());
				}
			}
		});
		return moveLeftButton;
	}
	
	public static Button attackUp() {
		Champion current = game.getCurrentChampion();
		Button attackUpButton = new Button("Attack Up");
		actions.add(attackUpButton);
		
		Image img = new Image("./application/animations/fire.jpeg");
		fire = new ImageView(img);
		fire.setFitWidth(30);
		fire.setFitHeight(30);
		translateAttack = new TranslateTransition(Duration.seconds(0.7), fire);
		translateAttack.setNode(fire);
		translateAttack.setByX(200);
		
		attackUpButton.setOnAction(e -> {
			try {
				game.attack(Direction.UP);
//				translateAttack.play();				
				showControls();
				updateCurrentInformation();
				updateStatusBar();
				prepareTurns();
				updateBoard();
				checkWinner();
			} catch (Exception e1) {
				if (!twoPlayerMode && player2.getTeam().contains(current)) {
					memo.set(4, false);
					e1.printStackTrace();
				}
				else {
					throwException(e1.getMessage());
				}
			}
		});
		return attackUpButton;
	}
	
	public static Button attackDown() {
		Champion current = game.getCurrentChampion();
		Button attackDownButton = new Button("Attack Down");
		actions.add(attackDownButton);
		attackDownButton.setOnAction(e -> {
			try {
				game.attack(Direction.DOWN);
				
				Image img = new Image("./application/animations/fire.jpeg");
				ImageView fireView = new ImageView(img);
				fireView.setFitWidth(30);
				fireView.setFitHeight(30);
				translateAttack = new TranslateTransition();
				translateAttack.setNode(fireView);
				translateAttack.setByY(200);
				translateAttack.play();
				
				showControls();
				updateCurrentInformation();
				updateStatusBar();
				prepareTurns();
				updateBoard();
				checkWinner();
			} catch (Exception e1) {
				if (!twoPlayerMode && player2.getTeam().contains(current)) {
					memo.set(5, false);
					e1.printStackTrace();
				}
				else {
					throwException(e1.getMessage());
				}
			}
		});
		return attackDownButton;
	}
	
	public static Button attackRight() {
		Champion current = game.getCurrentChampion();
		Button attackRightButton = new Button("Attack Right");
		actions.add(attackRightButton);
		attackRightButton.setOnAction(e -> {
			try {
				game.attack(Direction.RIGHT);
				
				Image img = new Image("./application/animations/fire.jpeg");
				ImageView fireView = new ImageView(img);
				fireView.setFitWidth(30);
				fireView.setFitHeight(30);
				translateAttack = new TranslateTransition();
				translateAttack.setNode(fireView);
				translateAttack.setByY(200);
				translateAttack.play();
				
				showControls();
				updateCurrentInformation();
				updateStatusBar();
				prepareTurns();
				updateBoard();
				checkWinner();
			} catch (Exception e1) {
				if (!twoPlayerMode && player2.getTeam().contains(current)) {
					memo.set(6, false);
					e1.printStackTrace();
				}
				else {
					throwException(e1.getMessage());
				}
			}
		});
		return attackRightButton;
	}
	
	public static Button attackLeft() {
		Champion current = game.getCurrentChampion();
		Button attackLeftButton = new Button("Attack Left");
		actions.add(attackLeftButton);
		attackLeftButton.setOnAction(e -> {
			try {
				game.attack(Direction.LEFT);
				
				Image img = new Image("./application/animations/fire.jpeg");
				ImageView fireView = new ImageView(img);
				fireView.setFitWidth(30);
				fireView.setFitHeight(30);
				translateAttack = new TranslateTransition();
				translateAttack.setNode(fireView);
//				translateAttack.setByY(200);
				translateAttack.play();
				
				showControls();
				updateCurrentInformation();
				updateStatusBar();
				prepareTurns();
				updateBoard();
				checkWinner();
			} catch (Exception e1) {
				if (!twoPlayerMode && player2.getTeam().contains(current)) {
					memo.set(7, false);
					e1.printStackTrace();
				}
				else {
					throwException(e1.getMessage());
				}
			}
		});
		return attackLeftButton;
	}
	
	public static Button castAbility(int abilityIndex, int buttonIndex) {
		Champion current = game.getCurrentChampion();
		Button useless = new Button();
		useless.setVisible(false);
		if (abilityIndex == 3 && !punch) {
			actions.add(useless);
			return useless;
		}
		
		Ability ability = game.getCurrentChampion().getAbilities().get(abilityIndex);
		AreaOfEffect area = ability.getCastArea();
		Button castAbilityButton = new Button("Cast " + ability.getName());
		actions.add(castAbilityButton);
		castAbilityButton.setOnAction(e -> {
			if (area == AreaOfEffect.SELFTARGET || area == AreaOfEffect.TEAMTARGET || area == AreaOfEffect.SURROUND) {
				try {
					game.castAbility(ability);
					showControls();
					updateCurrentInformation();
					updateStatusBar();
					prepareTurns();
					updateBoard();
					checkWinner();
				}
				catch (Exception e1) {
					if (!twoPlayerMode && player2.getTeam().contains(current)) {
						memo.set(buttonIndex, false);
						e1.printStackTrace();
					}
					else {
						throwException(e1.getMessage());
					}
				}
			}
			
			else if (area == AreaOfEffect.DIRECTIONAL) {
				if (twoPlayerMode || !twoPlayerMode && player1.getTeam().contains(game.getCurrentChampion())) {
					Stage chooseDirection = new Stage();
					chooseDirection.setTitle("Choose a Direction to Cast Ability");
					VBox window = new VBox(10);
					window.setAlignment(Pos.CENTER);
					Scene scene = new Scene(window);
					Button up = new Button("UP");
					up.setOnAction(ee-> {
						chooseDirection.close();
						try {
							game.castAbility(ability, Direction.UP);
							showControls();
							updateCurrentInformation();
							updateStatusBar();
							prepareTurns();
							updateBoard();
							checkWinner();
						}
						catch (Exception e1) {
							throwException(e1.getMessage());	
						}
					});
					Button down = new Button("DOWN");
					down.setOnAction(ee-> {
						chooseDirection.close();
						try {
							game.castAbility(ability, Direction.DOWN);
							showControls();
							updateCurrentInformation();
							updateStatusBar();
							prepareTurns();
							updateBoard();
							checkWinner();
						}
						catch (Exception e1) {
							throwException(e1.getMessage());
						}
					});
					Button right = new Button("RIGHT");
					right.setOnAction(ee-> {
						chooseDirection.close();
						try {
							game.castAbility(ability, Direction.RIGHT);
							showControls();
							updateCurrentInformation();
							updateStatusBar();
							prepareTurns();
							updateBoard();
							checkWinner();
						}
						catch (Exception e1) {
							throwException(e1.getMessage());
						}
					});
					Button left = new Button("LEFT");
					left.setOnAction(ee-> {
						chooseDirection.close();
						try {
							game.castAbility(ability, Direction.LEFT);
							showControls();
							updateCurrentInformation();
							updateStatusBar();
							prepareTurns();
							updateBoard();
							checkWinner();
						}
						catch (Exception e1) {
							throwException(e1.getMessage());
						}
					});
					chooseDirection.setScene(scene);
					chooseDirection.setMinWidth(400);
					chooseDirection.setMinHeight(200);
					window.getChildren().addAll(up, down, right, left);
					window.setPadding(new Insets(10,10,10,10));
					chooseDirection.show();
				}
				
				else if (!twoPlayerMode && player2.getTeam().contains(current)) {
					memo.set(buttonIndex, false);
				}
			}
			
			else if (area == AreaOfEffect.SINGLETARGET) {
				if (twoPlayerMode || !twoPlayerMode && player1.getTeam().contains(game.getCurrentChampion())) {
					Stage chooseCell = new Stage();
					chooseCell.setTitle("Choose a Cell to Cast Ability On");
					VBox window = new VBox(10);
					window.setAlignment(Pos.CENTER);
					Scene scene = new Scene(window);
					TextField xField = new TextField("X co-ordinate (0 is bottom)");
					TextField yField = new TextField("Y co-ordinate (0 is left)");
					Button confirm = new Button("Confirm");
					confirm.setOnAction(ee-> {
						chooseCell.close();
						try {
							game.castAbility(ability, Integer.parseInt(xField.getText()), Integer.parseInt(yField.getText()));
							showControls();
							updateCurrentInformation();
							updateStatusBar();
							prepareTurns();
							updateBoard();
							checkWinner();
						}
						catch (Exception e1) {
							throwException(e1.getMessage());	
						}
					});
					chooseCell.setScene(scene);
					chooseCell.setMinWidth(400);
					chooseCell.setMinHeight(200);
					window.getChildren().addAll(xField, yField, confirm);
					window.setPadding(new Insets(10,10,10,10));
					chooseCell.show();
				}
				
				else if (!twoPlayerMode && player2.getTeam().contains(current)) {
					memo.set(buttonIndex, false);
				}
			}
		});
		return castAbilityButton;
	}
	
	public static Button useLeaderAbility() {
		Champion current = game.getCurrentChampion();
		Button useless = new Button();
		useless.setVisible(false);
		if (current != player1.getLeader() && current != player2.getLeader()) {
			actions.add(useless);
			return useless;
		}
		Button useLeaderAbility = new Button("Use Leader Ability");
		actions.add(useLeaderAbility);
		useLeaderAbility.setOnAction(e -> {
			try {
				game.useLeaderAbility();
				showControls();
				updateCurrentInformation();
				updateStatusBar();
				prepareTurns();
				updateBoard();
				checkWinner();
			}
			catch (Exception e1) {
				if (!twoPlayerMode && player2.getTeam().contains(current)) {
					memo.set(11, false);
				}
				else {
					throwException(e1.getMessage());
				}
			}
		});
		return useLeaderAbility;
	}
	
	public static Button endTurn() {
		Button endCurrentTurnButton = new Button("End Turn");
		actions.add(endCurrentTurnButton);
		endCurrentTurnButton.setOnAction(e -> {
			game.endTurn();
			
			showControls();
			updateCurrentInformation();
			updateStatusBar();
			prepareTurns();
			updateBoard();
			checkWinner();
			if((!twoPlayerMode) && player2.getTeam().contains(game.getCurrentChampion())) {
				computerAction();
			}	
		});
		return endCurrentTurnButton;
	}
	
	public static void computerAction() {
	for (int i = 0; i < 15; i++) {
		if (i == 13)
			memo.set(13, false);
		else
			memo.set(i, true);                                                                               
	}
	for (Button b : actions) {
		b.setVisible(false);
	}
		PauseTransition pause = new PauseTransition(Duration.seconds(3));
		pause.setOnFinished(event -> {
			int random = 0;
			for (int i = 0; i < 100; i++) {
				random = (int)(Math.random() * 13);
				System.out.println("Random: " + random);
				if (game.getCurrentChampion().getCurrentActionPoints() < 2) 
					break;
				if (memo.get(random) == true) {
					System.out.println("trying to do");
					actions.get(random).fire();
					System.out.println("Current actions: " + game.getCurrentChampion().getCurrentActionPoints());
				}
			}
			actions.get(14).fire();
		});
		pause.play();
	}
	
	public static void checkWinner() {
		Player winner = game.checkGameOver();
		if(winner != null) {
			for (Button b : actions) {
				b.setDisable(true);
			}
			Stage gameOver = new Stage();
			gameOver.setTitle("Game Over");
			VBox window = new VBox(10);
			window.setAlignment(Pos.CENTER);
			Scene scene = new Scene(window);
			Button exitGame = new Button("Exit Game");
			exitGame.setOnAction(k -> {
				gameOver.close();
				primaryStage.close();
			});
			gameOver.setScene(scene);
			gameOver.setMinWidth(400);
			gameOver.setMinHeight(200);
			Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
			window.getChildren().addAll(msgText, exitGame);
			window.setPadding(new Insets(10,10,10,10));
			gameOver.show();
		}
		
		
	}

	public static  void keyMoved() {
		gameview.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.NUMPAD8) {
				actions.get(0).fire();
			}
			if (e.getCode() == KeyCode.NUMPAD2) {
				actions.get(1).fire();
			}
			if (e.getCode() == KeyCode.NUMPAD6) {
				actions.get(2).fire();
			}
			if (e.getCode() == KeyCode.NUMPAD4) {
				actions.get(3).fire();
			}
			if (e.getCode() == KeyCode.W) {
				actions.get(4).fire();
			}
			if (e.getCode() == KeyCode.S) {
				actions.get(5).fire();
			}
			if (e.getCode() == KeyCode.D) {
				actions.get(6).fire();
			}
			if (e.getCode() == KeyCode.A) {
				actions.get(7).fire();
			}
			if (e.getCode() == KeyCode.DIGIT1) {
				actions.get(8).fire();
			}
			if (e.getCode() == KeyCode.DIGIT2) {
				actions.get(9).fire();
			}
			if (e.getCode() == KeyCode.DIGIT3) {
				actions.get(10).fire();
			}
			if (e.getCode() == KeyCode.DIGIT4) {
				actions.get(11).fire();
			}
			if (e.getCode() == KeyCode.DIGIT5) {
				actions.get(12).fire();
			}
			if (e.getCode() == KeyCode.H) {
				actions.get(13).fire();
			}
			if (e.getCode() == KeyCode.E) {
				actions.get(14).fire();
			}
//			switch(e.getCode()) {
//			case KeyCode.UP: 
//			}
		});
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
