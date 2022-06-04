/*
 * TODO
 * 
 * cast ability
 * use leader ability
 * attack animation
 * 
 * Clean code
 * 
 * Fantastic GUI
 * Network Mode
 * Computer Player
 */

package application;
	
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import engine.Game;
import engine.Player;
import engine.PriorityQueue;
import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.InvalidTargetException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Effect;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Condition;
import model.world.Cover;
import model.world.Direction;
import model.world.Hero;
import model.world.Villain;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class View extends Application {
	static Game game;
	static Player player1, player2;
	static Scene homepage, begin, gameview;
	static GridPane boardView;
	static HBox gameStatus, currentControls;
	static VBox turnOrderStatus, currentInformation;
	static HashMap<Champion,Boolean> chosenMap;
	static HashMap<Champion, String> aliveMap;
	static HashMap<Champion, String> deadMap;
	static ArrayList<Button> chooseLeaderButtons;
	static ArrayList<Button> championsButtons = new ArrayList<>();
	static ArrayList<Button> actions = new ArrayList<Button>();
	static ArrayList<Champion> champions;
	static PriorityQueue q;
	static PriorityQueue tmp;
	static boolean full = false;
	static Object[][] board;
	static Button[][] boardButtons = new Button[5][5];
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Marvel - Ultimate War");
		primaryStage.setFullScreen(true);
//		primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		Image icon = new Image("./application/media/icon.png");
		primaryStage.getIcons().add(icon);
		scene1(primaryStage);
		primaryStage.show();
	}
	
	// Enter Players' Names
	public static void scene1(Stage primaryStage) {
		// Scene Organisation
		VBox root1 = new VBox(10);
		root1.setAlignment(Pos.CENTER);
		root1.setPadding(new Insets(10, 10, 10, 10));
		homepage = new Scene(root1,400,400);
		
		
		// TODO: add background
		
		
		// First Player Enter Name
		Label enterFirstPlayerNameLabel = new Label("First Player Name: ");
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
			player2 = new Player(name2TextField.getText());
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
		root1.getChildren().addAll(firstPlayerHBox, secondPlayerHBox, buttonHBox);
		primaryStage.setScene(homepage);
	}
	
	// Choose Champions
	public static void scene2(Stage primaryStage){ 
		// Map Champions with their Images
		aliveMap = new HashMap<Champion,String>();
		deadMap = new HashMap<Champion, String>();
		for (int i = 1; i <= 15; i++) {
			aliveMap.put(Game.getAvailableChampions().get(i-1), "./application/media/" + i + ".jpeg");
			deadMap.put(Game.getAvailableChampions().get(i-1), "./application/media/" + i + "d.jpeg");
		}
		
		
		// Scene Organisation
		BorderPane root2 = new BorderPane();
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
		Image notYetSelected = new Image("./application/media/icon.png");
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
			iv.setFitHeight(80);
			iv.setFitWidth(80);
			Button btn = new Button();
			btn.setPrefSize(70, 70);
		    btn.setGraphic(iv);
		    btn.setOnAction((e) -> {
		    	show(c, root2, chosenChampions,ch, btn, primaryStage);
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
		champsgrid.setStyle("-fx-background-color: #222;");
		champsgrid.setAlignment(Pos.CENTER);
	}
	
	// Show Pressed Champion's Details
	public static void show(Champion champion, BorderPane root2, HBox chosenChampions, Image ch, Button btn, Stage primaryStage) {
		// Organisation
		VBox details = new VBox(5);
		
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
    	Label championType = new Label("Champion's Type: " + type);
		Label championName = new Label("Champion's Name: " + champion.getName());
		Label championMaxHP = new Label("Champion's Maximum HP: " + champion.getMaxHP() + "");
		Label championMana = new Label("Champion's Mana: " + champion.getMana() + "");
		Label championActions = new Label("Champion's Maximum Actions Points per Turn: " + champion.getMaxActionPointsPerTurn() + "");
		Label championSpeed = new Label ("Champion's Speed: " + champion.getSpeed() + "");
		Label championRange = new Label ("Champion's Attack Range: " + champion.getAttackRange() + "");
		Label championDamage = new Label ("Champion's Attack Damage: " + champion.getAttackDamage() + "");
		Label championAbilities = new Label ("Abilities: " + champion.getAbilities().get(0).getName() + ", " +
		champion.getAbilities().get(1).getName() + ", " + champion.getAbilities().get(2).getName() + ".");
		// Choose Button
		Button choose = new Button("Confirm Champion");
		boolean chosen = chosenMap.get(champion);
		if(chosen) choose.setDisable(true);
		choose.setOnAction(e -> {
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
				}
			}
			
			else {
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
				    img.setFitHeight(60);
				    img.setFitWidth(60);
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
			    chosenChampions.getChildren().clear();
			}
		});
		
		if (!full)
				details.getChildren().addAll(championType, championName, championMaxHP, championMana, championActions,
				championSpeed, championRange, championDamage,championAbilities, choose);		
	}
	
	// Set Leader and Disable Choosing Another Leader
	public static void chooseLeader(Player player, Champion c, VBox details, Stage primaryStage) {
		
		if (player == player1) {
			player1.setLeader(c);
			details.getChildren().get(1).setDisable(true);
			details.getChildren().get(2).setDisable(true);
			details.getChildren().get(3).setDisable(true);
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
		board = game.getBoard();
		game.placeChampions();
		game.prepareChampionTurns();
		// Scene organisation     
		BorderPane root3 = new BorderPane();
		gameview = new Scene(root3);
		primaryStage.setScene(gameview);
		primaryStage.setFullScreen(true);
		gameStatus = new HBox(10);
		turnOrderStatus = new VBox(15);
		currentControls = new HBox();
		boardView = new GridPane();
		currentInformation = new VBox(2);
		root3.setTop(gameStatus);
		root3.setRight(turnOrderStatus);
		root3.setBottom(currentControls);
		root3.setLeft(currentInformation);
		root3.setCenter(boardView);
		
		// Game Status Bar
		updateStatusBar();
		gameStatus.setPadding(new Insets(10,10,10,10));
		gameStatus.setAlignment(Pos.CENTER);
		
		
		// Turn Order Status Bar
		prepareTurns();
		turnOrderStatus.setPadding(new Insets(15,15,15,15));
		turnOrderStatus.setAlignment(Pos.TOP_RIGHT);
		turnOrderStatus.setMaxWidth(250);
		turnOrderStatus.setMinWidth(250);
		
			
		// Current Information
		updateCurrentInformation();
			
		// Board View
		updateBoard();
		boardView.setAlignment(Pos.CENTER);
		
		// Controls
		// ATTACK
		Button attack = new Button("Attack");
		attack.setMinHeight(30);
		attack.setMinWidth(30);
		actions.add(attack);
		attack.setOnAction(e-> {
			Button up = new Button("Attack Up");
			up.setOnAction(ee -> {
				try {
					game.attack(Direction.UP);
					Player winner = game.checkGameOver();
					if(winner != null) {
						Stage gameOver = new Stage();
						gameOver.setTitle("Game Over");
						VBox window = new VBox(10);
						window.setAlignment(Pos.CENTER);
						Scene scene = new Scene(window);
						Button exitGame = new Button("Exit Game");
						exitGame.setOnAction( k -> gameOver.close());
						gameOver.setScene(scene);
						gameOver.setMinWidth(400);
						gameOver.setMinHeight(200);
						Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
						window.getChildren().addAll(msgText, exitGame);
						window.setPadding(new Insets(10,10,10,10));
						gameOver.show();
						
					}
					updateCurrentInformation();
					updateStatusBar();
					prepareTurns();
					updateBoard();
				} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException e1) {
					throwException(e1.getMessage());
				}				
			});
			
			Button down = new Button("Attack Down");
			down.setOnAction(ee -> {
				try {
					game.attack(Direction.DOWN);
					Player winner = game.checkGameOver();
					if(winner != null) {
						Stage gameOver = new Stage();
						gameOver.setTitle("Game Over");
						VBox window = new VBox(10);
						window.setAlignment(Pos.CENTER);
						Scene scene = new Scene(window);
						Button exitGame = new Button("Exit Game");
						exitGame.setOnAction( k -> gameOver.close());
						gameOver.setScene(scene);
						gameOver.setMinWidth(400);
						gameOver.setMinHeight(200);
						Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
						window.getChildren().addAll(msgText, exitGame);
						window.setPadding(new Insets(10,10,10,10));
						gameOver.show();
						
					}
					updateCurrentInformation();
					updateStatusBar();
					prepareTurns();
					updateBoard();
				} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException e1) {
					throwException(e1.getMessage());
				}								
			});
			
			Button left = new Button("Attack Left");
			left.setOnAction(ee -> {
				try {
					game.attack(Direction.LEFT);
					Player winner = game.checkGameOver();
					if(winner != null) {
						Stage gameOver = new Stage();
						gameOver.setTitle("Game Over");
						VBox window = new VBox(10);
						window.setAlignment(Pos.CENTER);
						Scene scene = new Scene(window);
						Button exitGame = new Button("Exit Game");
						exitGame.setOnAction( k -> gameOver.close());
						gameOver.setScene(scene);
						gameOver.setMinWidth(400);
						gameOver.setMinHeight(200);
						Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
						window.getChildren().addAll(msgText, exitGame);
						window.setPadding(new Insets(10,10,10,10));
						gameOver.show();
						
					}
					updateCurrentInformation();
					updateStatusBar();
					prepareTurns();
					updateBoard();
				} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException e1) {
					throwException(e1.getMessage());
				}
			});
			
			Button right = new Button("Attack Right");
			right.setOnAction(ee -> {
				try {
					game.attack(Direction.RIGHT);
					Player winner = game.checkGameOver();
					if(winner != null) {
						Stage gameOver = new Stage();
						gameOver.setTitle("Game Over");
						VBox window = new VBox(10);
						window.setAlignment(Pos.CENTER);
						Scene scene = new Scene(window);
						Button exitGame = new Button("Exit Game");
						exitGame.setOnAction( k -> gameOver.close());
						gameOver.setScene(scene);
						gameOver.setMinWidth(400);
						gameOver.setMinHeight(200);
						Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
						window.getChildren().addAll(msgText, exitGame);
						window.setPadding(new Insets(10,10,10,10));
						gameOver.show();
						
					}
					updateCurrentInformation();
					updateStatusBar();
					prepareTurns();
					updateBoard();
				} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException e1) {
					throwException(e1.getMessage());
				}				
			});

			while(currentControls.getChildren().size() > 5) {
				currentControls.getChildren().remove(currentControls.getChildren().size() - 1);
			}
			
			Region region = new Region();
			region.setMinWidth(10);
			
			currentControls.getChildren().addAll(region,up,down,left,right);
		});
		
		
		// MOVE	
		Button move = new Button("Move");
		move.setMinHeight(30);
		move.setMinWidth(30);
		actions.add(move);
		move.setOnAction(e -> {
			Button up = new Button("UP");
			up.setOnAction(ee -> {
				try {
					game.move(Direction.UP);
					updateCurrentInformation();
					updateStatusBar();
					prepareTurns();
					updateBoard();
				} catch (NotEnoughResourcesException | UnallowedMovementException e1) {
					throwException(e1.getMessage());
				}				
			});
			
			Button down = new Button("DOWN");
			down.setOnAction(ee -> {
				try {
					game.move(Direction.DOWN);
					updateCurrentInformation();
					updateStatusBar();
					prepareTurns();
					updateBoard();
				} catch (NotEnoughResourcesException | UnallowedMovementException e1) {
					throwException(e1.getMessage());
				}
			});
			
			Button left = new Button("LEFT");
			left.setOnAction(ee -> {
				try {
					game.move(Direction.LEFT);
					updateCurrentInformation();
					updateStatusBar();
					prepareTurns();
					updateBoard();
				} catch (NotEnoughResourcesException | UnallowedMovementException e1) {
					throwException(e1.getMessage());
				}
			});
			
			Button right = new Button("RIGHT");
			right.setOnAction(ee -> {
				try {
					game.move(Direction.RIGHT);
					updateCurrentInformation();
					updateStatusBar();
					prepareTurns();
					updateBoard();
				} catch (NotEnoughResourcesException | UnallowedMovementException e1) {
					throwException(e1.getMessage());
				}
			});
			
			while(currentControls.getChildren().size() > 5) {
				currentControls.getChildren().remove(currentControls.getChildren().size() - 1);
			}
			Region region = new Region();
			region.setMinWidth(10);
			currentControls.getChildren().addAll(region,up,down,left,right);
		});
			
			
		
		// CAST ABILITY
		
		Button castAbility = new Button("Cast Ability");
		castAbility.setMinHeight(30);
		castAbility.setMinWidth(30);
		actions.add(castAbility);
		
		
		
//		System.out.println(abilities.get(0) +" " + abilities.get(1) + " "+abilities.get(2));
		castAbility.setOnAction(e -> {
			
			System.out.println(game.getCurrentChampion().getLocation());
			ArrayList<Ability> abilities = game.getCurrentChampion().getAbilities();
			Button ability1 = new Button("First Ability");
			
			ability1.setOnAction(ee-> {
				Ability a1 = abilities.get(0);
				AreaOfEffect area = a1.getCastArea();
				if(area == AreaOfEffect.SELFTARGET || area == AreaOfEffect.TEAMTARGET || area == AreaOfEffect.SURROUND) {
					try {
						game.castAbility(a1);
						Player winner = game.checkGameOver();
						if(winner != null) {
							Stage gameOver = new Stage();
							gameOver.setTitle("Game Over");
							VBox window = new VBox(10);
							window.setAlignment(Pos.CENTER);
							Scene scene = new Scene(window);
							Button exitGame = new Button("Exit Game");
							exitGame.setOnAction( k -> primaryStage.close());
							gameOver.setScene(scene);
							gameOver.setMinWidth(400);
							gameOver.setMinHeight(200);
							Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
							window.getChildren().addAll(msgText, exitGame);
							window.setPadding(new Insets(10,10,10,10));
							gameOver.show();
							
						}
						updateCurrentInformation();
						updateStatusBar();
						prepareTurns();
						updateBoard();
					} catch (NotEnoughResourcesException | AbilityUseException | InvalidTargetException e1) {
						throwException(e1.getMessage());
					}
				}
				
				else if(area == AreaOfEffect.DIRECTIONAL) {
					Button up = new Button("UP");
					
					up.setOnAction(eee ->{
						try {
							game.castAbility(a1, Direction.UP);
							Player winner = game.checkGameOver();
							if(winner != null) {
								Stage gameOver = new Stage();
								gameOver.setTitle("Game Over");
								VBox window = new VBox(10);
								window.setAlignment(Pos.CENTER);
								Scene scene = new Scene(window);
								Button exitGame = new Button("Exit Game");
								exitGame.setOnAction( k -> primaryStage.close());
								gameOver.setScene(scene);
								gameOver.setMinWidth(400);
								gameOver.setMinHeight(200);
								Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
								window.getChildren().addAll(msgText, exitGame);
								window.setPadding(new Insets(10,10,10,10));
								gameOver.show();
								
							}
							updateCurrentInformation();
							updateStatusBar();
							prepareTurns();
							updateBoard();
						} catch (NotEnoughResourcesException | AbilityUseException |InvalidTargetException e1) {
							throwException(e1.getMessage());
						}
						
					});
					
					Button down = new Button("DOWN");
					
					down.setOnAction(eee ->{
						try {
							game.castAbility(a1, Direction.DOWN);
							Player winner = game.checkGameOver();
							if(winner != null) {
								Stage gameOver = new Stage();
								gameOver.setTitle("Game Over");
								VBox window = new VBox(10);
								window.setAlignment(Pos.CENTER);
								Scene scene = new Scene(window);
								Button exitGame = new Button("Exit Game");
								exitGame.setOnAction( k -> primaryStage.close());
								gameOver.setScene(scene);
								gameOver.setMinWidth(400);
								gameOver.setMinHeight(200);
								Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
								window.getChildren().addAll(msgText, exitGame);
								window.setPadding(new Insets(10,10,10,10));
								gameOver.show();
								
							}
							updateCurrentInformation();
							updateStatusBar();
							prepareTurns();
							updateBoard();
						} catch (NotEnoughResourcesException | AbilityUseException | InvalidTargetException e1) {
							throwException(e1.getMessage());
						}
						
					});
					
					Button left = new Button("LEFT");
					
					left.setOnAction(eee ->{
						try {
							game.castAbility(a1, Direction.LEFT);
							Player winner = game.checkGameOver();
							if(winner != null) {
								Stage gameOver = new Stage();
								gameOver.setTitle("Game Over");
								VBox window = new VBox(10);
								window.setAlignment(Pos.CENTER);
								Scene scene = new Scene(window);
								Button exitGame = new Button("Exit Game");
								exitGame.setOnAction( k -> primaryStage.close());
								gameOver.setScene(scene);
								gameOver.setMinWidth(400);
								gameOver.setMinHeight(200);
								Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
								window.getChildren().addAll(msgText, exitGame);
								window.setPadding(new Insets(10,10,10,10));
								gameOver.show();
								
							}
							updateCurrentInformation();
							updateStatusBar();
							prepareTurns();
							updateBoard();
						} catch (NotEnoughResourcesException | AbilityUseException | InvalidTargetException e1) {
							throwException(e1.getMessage());
						}
						
					});
					
					
					Button right = new Button("RIGHT");
					
					right.setOnAction(eee ->{
						try {
							game.castAbility(a1, Direction.RIGHT);
							Player winner = game.checkGameOver();
							if(winner != null) {
								Stage gameOver = new Stage();
								gameOver.setTitle("Game Over");
								VBox window = new VBox(10);
								window.setAlignment(Pos.CENTER);
								Scene scene = new Scene(window);
								Button exitGame = new Button("Exit Game");
								exitGame.setOnAction( k -> primaryStage.close());
								gameOver.setScene(scene);
								gameOver.setMinWidth(400);
								gameOver.setMinHeight(200);
								Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
								window.getChildren().addAll(msgText, exitGame);
								window.setPadding(new Insets(10,10,10,10));
								gameOver.show();
								
							}
							updateCurrentInformation();
							updateStatusBar();
							prepareTurns();
							updateBoard();
						} catch (NotEnoughResourcesException | AbilityUseException
								|  InvalidTargetException e1) {
							throwException(e1.getMessage());
						}
						
					});
					
					while(currentControls.getChildren().size() > 5) {
						currentControls.getChildren().remove(currentControls.getChildren().size() - 1);
					}
					Region region = new Region();
					region.setMinWidth(10);
					currentControls.getChildren().addAll(region,up,down,left,right);
					
				}
				
				
				else if(area == AreaOfEffect.SINGLETARGET) {
					Label x = new Label("X");
					TextField getX = new TextField();
					Label y = new Label("Y");
					TextField getY = new TextField();
					
					while(currentControls.getChildren().size() > 5) {
						currentControls.getChildren().remove(currentControls.getChildren().size() - 1);
					}
					Region region1 = new Region();
					region1.setMinWidth(10);
					Region region2 = new Region();
					region2.setMinWidth(10);
					currentControls.getChildren().addAll(region1,x,getX,region2,y,getY);
					
					Button confirm = new Button("Confirm");
					confirm.setOnAction(l -> {
						String xPos = getX.getText();
						String yPos = getY.getText();
						System.out.println(xPos + " " + yPos);
						try {
							game.castAbility(a1, Integer.parseInt(xPos), Integer.parseInt(yPos));
							Player winner = game.checkGameOver();
							if(winner != null) {
								Stage gameOver = new Stage();
								gameOver.setTitle("Game Over");
								VBox window = new VBox(10);
								window.setAlignment(Pos.CENTER);
								Scene scene = new Scene(window);
								Button exitGame = new Button("Exit Game");
								exitGame.setOnAction( k -> primaryStage.close());
								gameOver.setScene(scene);
								gameOver.setMinWidth(400);
								gameOver.setMinHeight(200);
								Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
								window.getChildren().addAll(msgText, exitGame);
								window.setPadding(new Insets(10,10,10,10));
								gameOver.show();
								
							}
							updateCurrentInformation();
							updateStatusBar();
							prepareTurns();
							updateBoard();
						} catch (NotEnoughResourcesException | AbilityUseException | InvalidTargetException e1) {
							throwException(e1.getMessage());;
						}
					});
				
					currentControls.getChildren().add(confirm);
				}
				
			});
			
			
			
			Button ability2 = new Button("Second Ability");
			
			ability2.setOnAction(ee-> {
				Ability a2 = abilities.get(1);
				AreaOfEffect area = a2.getCastArea();
				if(area == AreaOfEffect.SELFTARGET || area == AreaOfEffect.TEAMTARGET || area == AreaOfEffect.SURROUND) {
					try {
						game.castAbility(a2);
						Player winner = game.checkGameOver();
						if(winner != null) {
							Stage gameOver = new Stage();
							gameOver.setTitle("Game Over");
							VBox window = new VBox(10);
							window.setAlignment(Pos.CENTER);
							Scene scene = new Scene(window);
							Button exitGame = new Button("Exit Game");
							exitGame.setOnAction( k -> primaryStage.close());
							gameOver.setScene(scene);
							gameOver.setMinWidth(400);
							gameOver.setMinHeight(200);
							Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
							window.getChildren().addAll(msgText, exitGame);
							window.setPadding(new Insets(10,10,10,10));
							gameOver.show();
							
						}
						updateCurrentInformation();
						updateStatusBar();
						prepareTurns();
						updateBoard();
					} catch (NotEnoughResourcesException | AbilityUseException | InvalidTargetException e1) {
						throwException(e1.getMessage());;
					}
				}
				
				else if(area == AreaOfEffect.DIRECTIONAL) {
					Button up = new Button("UP");
					
					up.setOnAction(eee ->{
						try {
							game.castAbility(a2, Direction.UP);
							Player winner = game.checkGameOver();
							if(winner != null) {
								Stage gameOver = new Stage();
								gameOver.setTitle("Game Over");
								VBox window = new VBox(10);
								window.setAlignment(Pos.CENTER);
								Scene scene = new Scene(window);
								Button exitGame = new Button("Exit Game");
								exitGame.setOnAction( k -> primaryStage.close());
								gameOver.setScene(scene);
								gameOver.setMinWidth(400);
								gameOver.setMinHeight(200);
								Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
								window.getChildren().addAll(msgText, exitGame);
								window.setPadding(new Insets(10,10,10,10));
								gameOver.show();
								
							}
							updateCurrentInformation();
							updateStatusBar();
							prepareTurns();
							updateBoard();
						} catch (NotEnoughResourcesException | AbilityUseException | InvalidTargetException e1) {
							throwException(e1.getMessage());;
						}
						
					});
					
					Button down = new Button("DOWN");
					
					down.setOnAction(eee ->{
						try {
							game.castAbility(a2, Direction.DOWN);
							Player winner = game.checkGameOver();
							if(winner != null) {
								Stage gameOver = new Stage();
								gameOver.setTitle("Game Over");
								VBox window = new VBox(10);
								window.setAlignment(Pos.CENTER);
								Scene scene = new Scene(window);
								Button exitGame = new Button("Exit Game");
								exitGame.setOnAction( k -> primaryStage.close());
								gameOver.setScene(scene);
								gameOver.setMinWidth(400);
								gameOver.setMinHeight(200);
								Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
								window.getChildren().addAll(msgText, exitGame);
								window.setPadding(new Insets(10,10,10,10));
								gameOver.show();
								
							}
							updateCurrentInformation();
							updateStatusBar();
							prepareTurns();
							updateBoard();
						} catch (NotEnoughResourcesException | AbilityUseException | InvalidTargetException e1) {
							throwException(e1.getMessage());;
						}
						
					});
					
					Button left = new Button("LEFT");
					
					left.setOnAction(eee ->{
						try {
							game.castAbility(a2, Direction.LEFT);
							Player winner = game.checkGameOver();
							if(winner != null) {
								Stage gameOver = new Stage();
								gameOver.setTitle("Game Over");
								VBox window = new VBox(10);
								window.setAlignment(Pos.CENTER);
								Scene scene = new Scene(window);
								Button exitGame = new Button("Exit Game");
								exitGame.setOnAction( k -> primaryStage.close());
								gameOver.setScene(scene);
								gameOver.setMinWidth(400);
								gameOver.setMinHeight(200);
								Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
								window.getChildren().addAll(msgText, exitGame);
								window.setPadding(new Insets(10,10,10,10));
								gameOver.show();
								
							}
							updateCurrentInformation();
							updateStatusBar();
							prepareTurns();
							updateBoard();
						} catch (NotEnoughResourcesException | AbilityUseException | InvalidTargetException e1) {
							throwException(e1.getMessage());;
						}
						
					});
					
					
					Button right = new Button("RIGHT");
					
					right.setOnAction(eee ->{
						try {
							game.castAbility(a2, Direction.RIGHT);
							Player winner = game.checkGameOver();
							if(winner != null) {
								Stage gameOver = new Stage();
								gameOver.setTitle("Game Over");
								VBox window = new VBox(10);
								window.setAlignment(Pos.CENTER);
								Scene scene = new Scene(window);
								Button exitGame = new Button("Exit Game");
								exitGame.setOnAction( k -> primaryStage.close());
								gameOver.setScene(scene);
								gameOver.setMinWidth(400);
								gameOver.setMinHeight(200);
								Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
								window.getChildren().addAll(msgText, exitGame);
								window.setPadding(new Insets(10,10,10,10));
								gameOver.show();
								
							}
							updateCurrentInformation();
							updateStatusBar();
							prepareTurns();
							updateBoard();
						} catch (NotEnoughResourcesException | AbilityUseException | InvalidTargetException e1) {
							throwException(e1.getMessage());;
						}
						
					});
					
					
					while(currentControls.getChildren().size() > 5) {
						currentControls.getChildren().remove(currentControls.getChildren().size() - 1);
					}
					Region region = new Region();
					region.setMinWidth(10);
					currentControls.getChildren().addAll(region,up,down,left,right);
					
				}
				
				
				else if(area == AreaOfEffect.SINGLETARGET) {
					Label x = new Label("X");
					TextField getX = new TextField();
					Label y = new Label("Y");
					TextField getY = new TextField();
					
					while(currentControls.getChildren().size() > 5) {
						currentControls.getChildren().remove(currentControls.getChildren().size() - 1);
					}
					Region region1 = new Region();
					region1.setMinWidth(10);
					Region region2 = new Region();
					region2.setMinWidth(10);
					currentControls.getChildren().addAll(region1,x,getX,region2,y,getY);
					
					
					Button confirm = new Button("Confirm");
					confirm.setOnAction(l -> {
						String xPos = getX.getText();
						String yPos = getY.getText();
						System.out.println(xPos + " " + yPos);
						try {
							game.castAbility(a2, Integer.parseInt(xPos), Integer.parseInt(yPos));
							Player winner = game.checkGameOver();
							if(winner != null) {
								Stage gameOver = new Stage();
								gameOver.setTitle("Game Over");
								VBox window = new VBox(10);
								window.setAlignment(Pos.CENTER);
								Scene scene = new Scene(window);
								Button exitGame = new Button("Exit Game");
								exitGame.setOnAction( k -> primaryStage.close());
								gameOver.setScene(scene);
								gameOver.setMinWidth(400);
								gameOver.setMinHeight(200);
								Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
								window.getChildren().addAll(msgText, exitGame);
								window.setPadding(new Insets(10,10,10,10));
								gameOver.show();
								
							}
							updateCurrentInformation();
							updateStatusBar();
							prepareTurns();
							updateBoard();
						} catch (NotEnoughResourcesException | AbilityUseException | InvalidTargetException e1) {
							throwException(e1.getMessage());;
						}
					});
				
					currentControls.getChildren().add(confirm);	
				}
				
			});
			
			
			
			
			Button ability3 = new Button("Third Ability");
			
			ability3.setOnAction(ee-> {
				Ability a3 = abilities.get(2);
				AreaOfEffect area = a3.getCastArea();
				if(area == AreaOfEffect.SELFTARGET || area == AreaOfEffect.TEAMTARGET || area == AreaOfEffect.SURROUND) {
					try {
						game.castAbility(a3);
						Player winner = game.checkGameOver();
						if(winner != null) {
							Stage gameOver = new Stage();
							gameOver.setTitle("Game Over");
							VBox window = new VBox(10);
							window.setAlignment(Pos.CENTER);
							Scene scene = new Scene(window);
							Button exitGame = new Button("Exit Game");
							exitGame.setOnAction( k -> primaryStage.close());
							gameOver.setScene(scene);
							gameOver.setMinWidth(400);
							gameOver.setMinHeight(200);
							Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
							window.getChildren().addAll(msgText, exitGame);
							window.setPadding(new Insets(10,10,10,10));
							gameOver.show();
							
						}
						updateCurrentInformation();
						updateStatusBar();
						prepareTurns();
						updateBoard();
					} catch (NotEnoughResourcesException | AbilityUseException | InvalidTargetException e1) {
						throwException(e1.getMessage());;
					}
				}
				
				else if(area == AreaOfEffect.DIRECTIONAL) {
					Button up = new Button("UP");
					
					up.setOnAction(eee ->{
						try {
							game.castAbility(a3, Direction.UP);
							Player winner = game.checkGameOver();
							if(winner != null) {
								Stage gameOver = new Stage();
								gameOver.setTitle("Game Over");
								VBox window = new VBox(10);
								window.setAlignment(Pos.CENTER);
								Scene scene = new Scene(window);
								Button exitGame = new Button("Exit Game");
								exitGame.setOnAction( k -> primaryStage.close());
								gameOver.setScene(scene);
								gameOver.setMinWidth(400);
								gameOver.setMinHeight(200);
								Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
								window.getChildren().addAll(msgText, exitGame);
								window.setPadding(new Insets(10,10,10,10));
								gameOver.show();
								
							}
							updateCurrentInformation();
							updateStatusBar();
							prepareTurns();
							updateBoard();
						} catch (NotEnoughResourcesException | AbilityUseException | InvalidTargetException e1) {
							throwException(e1.getMessage());;
						}
						
					});
					
					Button down = new Button("DOWN");
					
					down.setOnAction(eee ->{
						try {
							game.castAbility(a3, Direction.DOWN);
							Player winner = game.checkGameOver();
							if(winner != null) {
								Stage gameOver = new Stage();
								gameOver.setTitle("Game Over");
								VBox window = new VBox(10);
								window.setAlignment(Pos.CENTER);
								Scene scene = new Scene(window);
								Button exitGame = new Button("Exit Game");
								exitGame.setOnAction( k -> primaryStage.close());
								gameOver.setScene(scene);
								gameOver.setMinWidth(400);
								gameOver.setMinHeight(200);
								Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
								window.getChildren().addAll(msgText, exitGame);
								window.setPadding(new Insets(10,10,10,10));
								gameOver.show();
								
							}
							updateCurrentInformation();
							updateStatusBar();
							prepareTurns();
							updateBoard();
						} catch (NotEnoughResourcesException | AbilityUseException | InvalidTargetException e1) {
							throwException(e1.getMessage());;
						}
						
					});
					
					Button left = new Button("LEFT");
					
					left.setOnAction(eee ->{
						try {
							game.castAbility(a3, Direction.LEFT);
							Player winner = game.checkGameOver();
							if(winner != null) {
								Stage gameOver = new Stage();
								gameOver.setTitle("Game Over");
								VBox window = new VBox(10);
								window.setAlignment(Pos.CENTER);
								Scene scene = new Scene(window);
								Button exitGame = new Button("Exit Game");
								exitGame.setOnAction( k -> primaryStage.close());
								gameOver.setScene(scene);
								gameOver.setMinWidth(400);
								gameOver.setMinHeight(200);
								Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
								window.getChildren().addAll(msgText, exitGame);
								window.setPadding(new Insets(10,10,10,10));
								gameOver.show();
								
							}
							updateCurrentInformation();
							updateStatusBar();
							prepareTurns();
							updateBoard();
						} catch (NotEnoughResourcesException | AbilityUseException | InvalidTargetException e1) {
							throwException(e1.getMessage());;
						}
						
					});
					
					
					Button right = new Button("RIGHT");
					
					right.setOnAction(eee ->{
						try {
							game.castAbility(a3, Direction.RIGHT);
							Player winner = game.checkGameOver();
							if(winner != null) {
								Stage gameOver = new Stage();
								gameOver.setTitle("Game Over");
								VBox window = new VBox(10);
								window.setAlignment(Pos.CENTER);
								Scene scene = new Scene(window);
								Button exitGame = new Button("Exit Game");
								exitGame.setOnAction( k -> primaryStage.close());
								gameOver.setScene(scene);
								gameOver.setMinWidth(400);
								gameOver.setMinHeight(200);
								Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
								window.getChildren().addAll(msgText, exitGame);
								window.setPadding(new Insets(10,10,10,10));
								gameOver.show();
								
							}
							updateCurrentInformation();
							updateStatusBar();
							prepareTurns();
							updateBoard();
						} catch (NotEnoughResourcesException | AbilityUseException | InvalidTargetException e1) {
							throwException(e1.getMessage());;
						}
						
					});
					
					while(currentControls.getChildren().size() > 5) {
						currentControls.getChildren().remove(currentControls.getChildren().size() - 1);
					}
					Region region = new Region();
					region.setMinWidth(10);
					currentControls.getChildren().addAll(region,up,down,left,right);
					
				}
				
				else if(area == AreaOfEffect.SINGLETARGET) {
					Label x = new Label("X");
					TextField getX = new TextField();
					Label y = new Label("Y");
					TextField getY = new TextField();
					
					while(currentControls.getChildren().size() > 5) {
						currentControls.getChildren().remove(currentControls.getChildren().size() - 1);
					}
					Region region1 = new Region();
					region1.setMinWidth(10);
					Region region2 = new Region();
					region2.setMinWidth(10);
					currentControls.getChildren().addAll(region1,x,getX,region2,y,getY);
					
				
					Button confirm = new Button("Confirm");	
					confirm.setOnAction(l -> {
						String xPos = getX.getText();
						String yPos = getY.getText();
						System.out.println(xPos + " " + yPos);
						try {
							game.castAbility(a3, Integer.parseInt(xPos), Integer.parseInt(yPos));
							Player winner = game.checkGameOver();
							if(winner != null) {
								Stage gameOver = new Stage();
								gameOver.setTitle("Game Over");
								VBox window = new VBox(10);
								window.setAlignment(Pos.CENTER);
								Scene scene = new Scene(window);
								Button exitGame = new Button("Exit Game");
								exitGame.setOnAction( k -> primaryStage.close());
								gameOver.setScene(scene);
								gameOver.setMinWidth(400);
								gameOver.setMinHeight(200);
								Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
								window.getChildren().addAll(msgText, exitGame);
								window.setPadding(new Insets(10,10,10,10));
								gameOver.show();
								
							}
							updateCurrentInformation();
							updateStatusBar();
							prepareTurns();
							updateBoard();
						} catch (NotEnoughResourcesException | AbilityUseException | InvalidTargetException e1) {
							throwException(e1.getMessage());;
						}
					});
				
					currentControls.getChildren().add(confirm);
					
				}
				
			});
			
			
			while(currentControls.getChildren().size() > 5) {
				currentControls.getChildren().remove(currentControls.getChildren().size() - 1);
			}
			Region region = new Region();
			region.setMinWidth(10);
			currentControls.getChildren().addAll(region,ability1,ability2,ability3);
			
		});
			
			
			
		// LEADER ABILITY
		Button useLeaderAbility = new Button("Use Leader Ability");
		useLeaderAbility.setMinHeight(30);
		useLeaderAbility.setMinWidth(30);
		actions.add(useLeaderAbility);
		
			
		useLeaderAbility.setOnAction(e -> {
//<<<<<<< Updated upstream
			String type = "";
			String msg = "";
			if (q.peekMin() instanceof Hero) {
				type = "Hero";
				msg = "Removes all negative effects from the player’s entire team and adds an Embrace effect to them which lasts for 2 turns.";
//=======
			
			try {
				game.useLeaderAbility();
				Player winner = game.checkGameOver();
				if(winner != null) {
					Stage gameOver = new Stage();
					gameOver.setTitle("Game Over");
					VBox window = new VBox(10);
					window.setAlignment(Pos.CENTER);
					Scene scene = new Scene(window);
					Button exitGame = new Button("Exit Game");
					exitGame.setOnAction( k -> primaryStage.close());
					gameOver.setScene(scene);
					gameOver.setMinWidth(400);
					gameOver.setMinHeight(200);
					Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
					window.getChildren().addAll(msgText, exitGame);
					window.setPadding(new Insets(10,10,10,10));
					gameOver.show();
					
				}
				updateCurrentInformation();
				updateStatusBar();
				prepareTurns();
				updateBoard();
			} catch (LeaderNotCurrentException | LeaderAbilityAlreadyUsedException e1) {
				// TODO Auto-generated catch block
				throwException(e1.getMessage());
//>>>>>>> Stashed changes
			}
			}
			else if (q.peekMin() instanceof AntiHero) {
				type = "AntiHero";
				msg =  "All champions on the board except for the leaders of each team will be stunned for 2 turns.";
			}
			else if (q.peekMin() instanceof Villain) {
				type = "Villain";
				msg = "Immediately eliminates all enemy champions with less than 30% health points.";
			}
			Stage message = new Stage();
			message.setTitle("Confirm to Use "  + type + " Leader Ability");
			VBox window = new VBox(10);
			window.setAlignment(Pos.CENTER);
			Scene scene = new Scene(window);
			Button confirm = new Button("Confirm");
			confirm.setOnAction(ee -> {
				try {
					game.useLeaderAbility();
					Player winner = game.checkGameOver();
					if(winner != null) {
						Stage gameOver = new Stage();
						gameOver.setTitle("Game Over");
						VBox windoww = new VBox(10);
						window.setAlignment(Pos.CENTER);
						Scene scenee = new Scene(windoww);
						Button exitGame = new Button("Exit Game");
						exitGame.setOnAction( k -> primaryStage.close());
						gameOver.setScene(scenee);
						gameOver.setMinWidth(400);
						gameOver.setMinHeight(200);
						Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
						windoww.getChildren().addAll(msgText, exitGame);
						windoww.setPadding(new Insets(10,10,10,10));
						gameOver.show();
						
					}
					updateCurrentInformation();
					updateStatusBar();
					prepareTurns();
					updateBoard();
					message.close();
				} catch (LeaderNotCurrentException | LeaderAbilityAlreadyUsedException e1) {
					throwException(e1.getMessage());
				}
			});
			message.setScene(scene);
			message.setMinWidth(400);
			message.setMinHeight(200);
			Text msgText =new Text(msg);
			window.getChildren().addAll(msgText, confirm);
			window.setPadding(new Insets(10,10,10,10));
			message.show();
		});
		
		
	
			
		// END TURN
		Button endCurrentTurn = new Button("End Turn");
		endCurrentTurn.setMinHeight(30);
		endCurrentTurn.setMinWidth(30);
		actions.add(endCurrentTurn);
		endCurrentTurn.setOnAction(e -> {
			
			try {
				game.endTurn();
				Player winner = game.checkGameOver();
				if(winner != null) {
					Stage gameOver = new Stage();
					gameOver.setTitle("Game Over");
					VBox window = new VBox(10);
					window.setAlignment(Pos.CENTER);
					Scene scene = new Scene(window);
					Button exitGame = new Button("Exit Game");
					exitGame.setOnAction( k -> primaryStage.close());
					gameOver.setScene(scene);
					gameOver.setMinWidth(400);
					gameOver.setMinHeight(200);
					Text msgText =new Text("Congratulations! " + winner.getName() + " is the WINNER");
					window.getChildren().addAll(msgText, exitGame);
					window.setPadding(new Insets(10,10,10,10));
					gameOver.show();
					
				}
				updateCurrentInformation();
				updateStatusBar();
				prepareTurns();
				updateBoard();
			} catch (Exception e1) {
				throwException(e1.getMessage());
			}
		});
		
		
		currentControls.getChildren().addAll(attack,move,castAbility,useLeaderAbility, endCurrentTurn);
		currentControls.setAlignment(Pos.CENTER);
		currentControls.setPadding(new Insets(10,10,30,10));
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
			iv.setFitHeight(80);
			iv.setFitWidth(80);
			
			turnOrderStatus.getChildren().add(iv);
			tmp.insert((Champion)q.remove());
		}
		while (!tmp.isEmpty()) {
			q.insert((Champion)tmp.remove());
		}
	}
	
	// Show the Current Champion's Attributes
	public static void updateCurrentInformation() {
		currentInformation.getChildren().clear();
		// Get Current Champion
		Champion champion = (Champion)q.peekMin();
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
		Label championType = new Label("Type: " + type);
		championType.setFont(new Font("Didot.",11));
		Label championName = new Label("Name: " + champion.getName());
		championName.setFont(new Font("Didot.",11));
		Label championMaxHP = new Label("HP: " + champion.getCurrentHP() + "/" + champion.getMaxHP());
		championMaxHP.setFont(new Font("Didot.",11));
		Label championMana = new Label("Mana: " + champion.getMana() + "");
		championMana.setFont(new Font("Didot.",11));
		Label championActions = new Label("Actions Points: " + champion.getCurrentActionPoints() + "/" + champion.getMaxActionPointsPerTurn());
		championActions.setFont(new Font("Didot.",11));
		Label championSpeed = new Label ("Speed: " + champion.getSpeed() + "");
		championSpeed.setFont(new Font("Didot.",11));
		Label championRange = new Label ("Attack Range: " + champion.getAttackRange() + "");
		championRange.setFont(new Font("Didot.",11));
		Label championDamage = new Label ("Attack Damage: " + champion.getAttackDamage() + "");
		championDamage.setFont(new Font("Didot.",11));
		Label championAppliedEffects = new Label ("Applied Effects: " + championEffects);
		championAppliedEffects.setFont(new Font("Didot.",11));
		Label championCondition = new Label ("Condition: " + champion.getCondition());
		championCondition.setFont(new Font("Didot.",11));
		Region region1 = new Region();
		region1.setMinHeight(15);
		// Get Current Champion's Abilities
		Ability a1 = champion.getAbilities().get(0);
		Ability a2 = champion.getAbilities().get(1);
		Ability a3 = champion.getAbilities().get(2);
		// First Ability's Attributes
		Label a1Name = new Label (a1.getName());
		a1Name.setFont(new Font("Didot.",11));
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
		a1Type.setFont(new Font("Didot.",11));
		Label a1Amount = new Label (abilityAmount1);
		a1Amount.setFont(new Font("Didot.",11));
		Label a1Mana = new Label ("Mana Cost: " + a1.getManaCost());
		a1Mana.setFont(new Font("Didot.",11));
		Label a1Cool = new Label ("Cooldown: " + a1.getCurrentCooldown() + "/" + a1.getBaseCooldown());
		a1Cool.setFont(new Font("Didot.",11));
		Label a1Range = new Label ("Range: " + a1.getCastRange());
		a1Range.setFont(new Font("Didot.",11));
		Label a1Area = new Label ("Cast Area: " + a1.getCastArea());
		a1Area.setFont(new Font("Didot.",11));
		Label a1Action = new Label ("Required Action Points: " + a1.getRequiredActionPoints());
		a1Action.setFont(new Font("Didot.",11));
		Region region2 = new Region();
		// Second Ability's Attributes
		region2.setMinHeight(15);
		Label a2Name = new Label (a2.getName());
		a2Name.setFont(new Font("Didot.",11));
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
		a2Type.setFont(new Font("Didot.",11));
		Label a2Amount = new Label (abilityAmount2);
		a2Amount.setFont(new Font("Didot.",11));
		Label a2Mana = new Label ("Mana Cost: " + a2.getManaCost());
		a2Mana.setFont(new Font("Didot.",11));
		Label a2Cool = new Label ("Cooldown: " + a2.getCurrentCooldown() + "/" + a2.getBaseCooldown());
		a2Cool.setFont(new Font("Didot.",11));
		Label a2Range = new Label ("Range: " + a2.getCastRange());
		a2Range.setFont(new Font("Didot.",11));
		Label a2Area = new Label ("Cast Area: " + a2.getCastArea());
		a2Area.setFont(new Font("Didot.",11));
		Label a2Action = new Label ("Required Action Points: " + a2.getRequiredActionPoints());	
		a2Action.setFont(new Font("Didot.",11));
		Region region3 = new Region();
		region3.setMinHeight(15);
		// Third Ability's Attributes
		Label a3Name = new Label (a3.getName());
		a3Name.setFont(new Font("Didot.",11));
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
		a3Type.setFont(new Font("Didot.",11));
		Label a3Amount = new Label (abilityAmount3);
		a3Amount.setFont(new Font("Didot.",11));
		Label a3Mana = new Label ("Mana Cost: " + a3.getManaCost());
		a3Mana.setFont(new Font("Didot.",11));
		Label a3Cool = new Label ("Cooldown: " + a3.getCurrentCooldown() + "/" + a3.getBaseCooldown());
		a3Cool.setFont(new Font("Didot.",11));
		Label a3Range = new Label ("Range: " + a3.getCastRange());
		a3Range.setFont(new Font("Didot.",11));
		Label a3Area = new Label ("Cast Area: " + a3.getCastArea());
		a3Area.setFont(new Font("Didot.",11));
		Label a3Action = new Label ("Required Action Points: " + a3.getRequiredActionPoints());
		a3Action.setFont(new Font("Didot.",11));
		// Configuring Node
		currentInformation.setMaxWidth(250);
		currentInformation.setMinWidth(250);
		currentInformation.setAlignment(Pos.TOP_LEFT);
		currentInformation.setPadding(new Insets(10,10,10,10));
		currentInformation.getChildren().addAll(championType,championName,championMaxHP,championMana,championActions,
				championSpeed, championRange, championDamage, championAppliedEffects, championCondition, 
				region1, a1Name, a1Type, a1Amount, a1Mana, a1Cool, a1Range, a1Area, a1Action, 
				region2, a2Name, a2Type, a2Amount, a2Mana, a2Cool, a2Range, a2Area, a2Action,
				region3, a3Name, a3Type, a3Amount, a3Mana, a3Cool, a3Range, a3Area, a3Action);	
	}
	
	// Show Status of Players' Team and Leader Ability
	public static void updateStatusBar() {
		gameStatus.getChildren().clear();
		Label player1Name = new Label(player1.getName());
		player1Name.setFont(new Font("Didot.",16));
		gameStatus.getChildren().add(player1Name);
		
		for (Champion c : player1.getTeam()) {
			Image image = new Image(aliveMap.get(c));
			if (c.getCondition() == Condition.KNOCKEDOUT)
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
		
		for (Champion c : player2.getTeam()) {
			Image image = new Image(aliveMap.get(c));
			if (c.getCondition() == Condition.KNOCKEDOUT)
				image = new Image(deadMap.get(c));
			ImageView iv = new ImageView(image);
			iv.setFitHeight(80);
			iv.setFitWidth(80);
			gameStatus.getChildren().add(iv);
		}
		
		Label player2Name = new Label(player2.getName());
		player1Name.setFont(new Font("Didot.",16));
		gameStatus.getChildren().add(player2Name);
	}
	
	// Update Board Buttons
	public static void updateBoard() {
		for(int i=0;i<5;i++) {
			for(int j=0;j<5;j++) {
				Button btn = new Button();
				btn.setMinHeight(120);
				btn.setMinWidth(120);
				btn.setMaxHeight(120);
				btn.setMaxWidth(120);
				boardButtons[j][4-i] = btn;
				
				if(board[i][j] instanceof Cover) {
					Image img = new Image("./application/media/cover.jpeg");
					ImageView iv = new ImageView(img);
					iv.setFitHeight(110);
					iv.setFitWidth(110);
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
					iv.setFitHeight(110);
					iv.setFitWidth(110);
					btn.setGraphic(iv);
					if (c == q.peekMin() && player1.getTeam().contains(q.peekMin())) {
						btn.setStyle("-fx-background-color: #010098;");
					}
					else if (c == q.peekMin() && player2.getTeam().contains(q.peekMin())) {
						btn.setStyle("-fx-background-color: #9a0000; ");
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
				boardView.add(btn,j,4-i);
			}
		}
	}
	
	// Create Pop-up With Exception Message
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

	public static void viewLeaderAbility() {
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}