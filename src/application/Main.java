package application;
	
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import engine.Game;
import engine.Player;
import engine.PriorityQueue;
import exceptions.ChampionDisarmedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Popup;
import javafx.stage.Stage;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.effects.Effect;
import model.world.Champion;
import model.world.Condition;
import model.world.Cover;
import model.world.Direction;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;

public class Main extends Application {
	static Game game;
	static Player player1, player2;
	static Scene homepage, begin, gameview;
	static Champion leader1;
	static Champion leader2;
	static HashMap<Champion,Boolean> map;
	static ArrayList<Button> chooseLeaderButtons;
	static ArrayList<Button> championsButtons;
	static boolean full = false;
	static int idx1 = 0;
	static int idx2 = 0;
	static GridPane boardView;
	static Object[][] board ;
	static ArrayList<Button> actions;
//	static Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Marvel - Ultimate War");
		primaryStage.setFullScreen(true);
		primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		Image icon = new Image("icon.png");
		primaryStage.getIcons().add(icon);
		scene1(primaryStage);
		primaryStage.show();
	}
	
	public static void scene1(Stage primaryStage) {
		VBox root1 = new VBox();
		root1.setAlignment(Pos.CENTER);
		root1.setPadding(new Insets(10, 10, 10, 10));
		root1.setSpacing(10);
		homepage = new Scene(root1,400,400);
//		Image background = new Image("download.jpeg");
//		ImagePattern b = new ImagePattern(background);
//		homepage.setFill(b);
		
		Label name1Label = new Label("First Player Name: ");
		name1Label.setFont(new Font("Didot.",14));
		TextField name1TextField = new TextField();
		HBox firstPlayer = new HBox();
		firstPlayer.setAlignment(Pos.CENTER);
		firstPlayer.getChildren().addAll(name1Label, name1TextField);
		TextField name2TextField = new TextField();
		Label name2Label = new Label("Second Player Name: ");
		name2Label.setFont(new Font("Didot.",14));
		HBox secondPlayer = new HBox();
		secondPlayer.setAlignment(Pos.CENTER);
		secondPlayer.getChildren().addAll(name2Label, name2TextField);
		
		Button startBtn = new Button("Begin Game!");
		startBtn.setOnAction(e -> {
			player1 = new Player(name1TextField.getText());
			player2 = new Player(name2TextField.getText());
			try {
				game = new Game(player1, player2);
			} catch (IOException e1) {}
			scene2(primaryStage);
		});
		HBox btn = new HBox();
		btn.setAlignment(Pos.CENTER);
		btn.getChildren().add(startBtn);
		root1.getChildren().addAll(firstPlayer, secondPlayer, btn);
		primaryStage.setScene(homepage);

	}
	
	public static void scene2(Stage primaryStage){
		// Scene organisation
		BorderPane root2 = new BorderPane();
		begin = new Scene(root2);
		primaryStage.setScene(begin);
		primaryStage.setFullScreen(true);

		// Chosen Bar
		HBox chosenChampions = new HBox();
		chosenChampions.setAlignment(Pos.CENTER);
		chosenChampions.setSpacing(10);
		chosenChampions.setPadding(new Insets(15, 15, 15, 15));
		Label l2_1 = new Label(player1.getName());
		Image chosen1_1 = new Image("icon.png");
		ImageView chosen1_1v = new ImageView(chosen1_1);
		chosen1_1v.setFitWidth(50);
		chosen1_1v.setFitHeight(50);
		Image chosen1_2 = new Image("icon.png");
		ImageView chosen1_2v = new ImageView(chosen1_2);
		chosen1_2v.setFitWidth(50);
		chosen1_2v.setFitHeight(50);
		Image chosen1_3 = new Image("icon.png");
		ImageView chosen1_3v = new ImageView(chosen1_3);
		chosen1_3v.setFitWidth(50);
		chosen1_3v.setFitHeight(50);
		Region region = new Region();
		region.setMinWidth(100);
		Label l2_2 = new Label(player2.getName());
		Image chosen2_1 = new Image("icon.png");
		ImageView chosen2_1v = new ImageView(chosen2_1);
		chosen2_1v.setFitWidth(50);
		chosen2_1v.setFitHeight(50);
		Image chosen2_2 = new Image("icon.png");
		ImageView chosen2_2v = new ImageView(chosen2_2);
		chosen2_2v.setFitWidth(50);
		chosen2_2v.setFitHeight(50);
		Image chosen2_3 = new Image("icon.png");
		ImageView chosen2_3v = new ImageView(chosen2_3);
		chosen2_3v.setFitWidth(50);
		chosen2_3v.setFitHeight(50);
		chosenChampions.getChildren().addAll(l2_1, chosen1_1v, chosen1_2v, chosen1_3v, region, chosen2_1v, chosen2_2v, chosen2_3v, l2_2);
		root2.setTop(chosenChampions);
		
		// Show Details
		VBox msg = new VBox();
		msg.setPadding(new Insets(10, 10, 10, 10));
		msg.setAlignment(Pos.CENTER);
		Label clickMsg = new Label("Click on a champion to show details.");
		msg.getChildren().add(clickMsg);
		root2.setCenter(msg);
		
		
	    // Champions Buttons
		GridPane champsgrid = new GridPane();
		champsgrid.setPadding(new Insets(10, 10, 10, 10));
		champsgrid.setStyle("-fx-background-color: #222;");
		champsgrid.setAlignment(Pos.CENTER);
		root2.setBottom(champsgrid);
		championsButtons = new ArrayList<>();
		ArrayList<ImageView> images = new ArrayList<>();
		int a = 0; int b = 0;
		ArrayList<Champion> champions = Game.getAvailableChampions();
		map = new HashMap<Champion,Boolean>();
		for(Champion c : champions) {
			map.put(c, false);
		}
		for (int i = 1; i <= 15; i++) {
			Champion champion = champions.get(i-1);
			Image ch = new Image("./application/media/" + i + ".jpeg");
			ImageView iv = new ImageView(ch);
			iv.setFitHeight(80);
			iv.setFitWidth(80);
			images.add(iv);
			Button btn = new Button();
			btn.setPrefSize(70, 70);
		    btn.setGraphic(iv);
		    btn.setOnAction((e) -> {
		    	show(champion, root2, chosenChampions,ch, btn, primaryStage);
		    });
		    champsgrid.add(btn, a, b);
		    a++;
		    if (a == 5) {
		    	a = 0;
		    	b++;
		    }
		    championsButtons.add(btn);
		}
	}
	
	public static void show(Champion champion, BorderPane root2, HBox chosenChampions,Image ch,Button btn, Stage primaryStage) {
		VBox details = new VBox();
    	details.setPadding(new Insets(10, 10, 10, 10));
    	details.setAlignment(Pos.CENTER);
    	String type = "";
    	if (champion.getClass().toString().equals("class model.world.AntiHero"))
    		type = "AntiHero";
    	else if (champion.getClass().toString().equals("class model.world.Hero"))
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
		Button choose = new Button("Confirm Champion");
		
		boolean chosen = map.get(champion);
		if(chosen) choose.setDisable(true);
		
		choose.setOnAction(e -> {
			if(player1.getTeam().size()<3) {
				player1.getTeam().add(champion);
				if(player1.getTeam().size()==1) {
					ImageView img = (ImageView)(chosenChampions.getChildren().get(1));
					img.setImage(ch);
				}
				
				else if(player1.getTeam().size()==2) {
					ImageView img = (ImageView)(chosenChampions.getChildren().get(2));
					img.setImage(ch);
				}
				
				else if(player1.getTeam().size()==3) {
					ImageView img = (ImageView)(chosenChampions.getChildren().get(3));
					img.setImage(ch);					
				}
			}
			
			else {
				player2.getTeam().add(champion);
				if(player2.getTeam().size()==1) {
					ImageView img = (ImageView)(chosenChampions.getChildren().get(5));
					img.setImage(ch);
				}
				
				else if(player2.getTeam().size()==2) {
					ImageView img = (ImageView)(chosenChampions.getChildren().get(6));
					img.setImage(ch);
				}
				
				else if(player2.getTeam().size()==3) {
					ImageView img = (ImageView)(chosenChampions.getChildren().get(7));
					img.setImage(ch);					
				}				
			}
			choose.setDisable(true);
			
			for(Map.Entry<Champion,Boolean> m : map.entrySet()) {
				if(m.getKey() == champion) {
					map.put(m.getKey(),true);
				}
			}

			if (player1.getTeam().size() + player2.getTeam().size() == 6) {
				// Disable all champions Buttons
				for(Button b : championsButtons) {
					b.setDisable(true);
				}
					
				full = true;
				details.getChildren().clear();
				Label chooseLeaderLabel1 = new Label("Choose a leader for the first team");
				chooseLeaderLabel1.setFont(new Font("Didot.",14));
				details.getChildren().add(chooseLeaderLabel1);
				
				for (int i = 1; i <= 3; i++) {
					idx1 = i-1;
					Button button = new Button();
					button.setOnAction(event -> {
						chooseLeader(player1, player1.getTeam().get(idx1), details, primaryStage);
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
					idx2 = i-5;
					Button button = new Button();
					button.setOnAction(event -> {
						chooseLeader(player2,player2.getTeam().get(idx2), details, primaryStage);
					});
					button.setPrefSize(50, 50);
					ImageView img = (ImageView)(chosenChampions.getChildren().get(i));
				    button.setGraphic(img);
				    img.setFitHeight(60);
				    img.setFitWidth(60);
					details.getChildren().add(button);
					details.setSpacing(10);
				}
			    chosenChampions.getChildren().clear();

			}
		});
		
		if (!full)
				details.getChildren().addAll(championType, championName, championMaxHP, championMana, championActions,
				championSpeed, championRange, championDamage,championAbilities, choose);
		
		
		
		root2.setCenter(details);
	}
	
	public static void chooseLeader(Player player, Champion c, VBox details, Stage primaryStage) {
		
		player.setLeader(c);
		if (player == player1) {
			details.getChildren().get(1).setDisable(true);
			details.getChildren().get(2).setDisable(true);
			details.getChildren().get(3).setDisable(true);
		}

		else if (player == player2) {
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
	
	public static void scene3(Stage primaryStage) throws IOException {
		// Scene organisation                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
		BorderPane root3 = new BorderPane();
		gameview = new Scene(root3);
		primaryStage.setScene(gameview);
		primaryStage.setFullScreen(true);
		
		HBox gameStatus = new HBox();
		VBox turnOrderStatus = new VBox();
		HBox currentControls = new HBox();
		boardView = new GridPane();
		VBox currentInformation = new VBox(2);
		
		root3.setTop(gameStatus);
		root3.setRight(turnOrderStatus);
		root3.setBottom(currentControls);
		root3.setLeft(currentInformation);
		root3.setCenter(boardView);
		
		// Game Status Bar
		Label player1Name = new Label(player1.getName());
		player1Name.setFont(new Font("Didot.",16));
		gameStatus.getChildren().add(player1Name);
//		ArrayList<ImageView> player1Images = new ArrayList<>();
		
		//MAP EACH CHAMPION WITH HIS/HER IMAGE
		HashMap<Champion,String> map = new HashMap<Champion,String>();
		
		for (Champion c : player1.getTeam()) {
			for (int i = 0; i < 15; i++) {
				if (c == Game.getAvailableChampions().get(i)) {
					int a = i+1;
					Image image = new Image("./application/media/" + a + ".jpeg");
					ImageView iv = new ImageView(image);
					iv.setFitHeight(80);
					iv.setFitWidth(80);
					gameStatus.getChildren().add(iv);
//					player1Images.add(iv);
					
					map.put(Game.getAvailableChampions().get(i),"./application/media/" + a + ".jpeg");
					
					break;
				}
			}
		}
		Image usedLeaderAbility = new Image("./application/media/pow.jpeg");
		ImageView firstLeaderAbility = new ImageView(usedLeaderAbility);
		Region r = new Region();
		r.setMinWidth(100);
		ImageView secondLeaderAbility = new ImageView(usedLeaderAbility);
		firstLeaderAbility.setFitHeight(80);
		firstLeaderAbility.setFitWidth(80);
		secondLeaderAbility.setFitHeight(80);
		secondLeaderAbility.setFitWidth(80);
		gameStatus.getChildren().addAll(firstLeaderAbility, r, secondLeaderAbility);
//		ArrayList<ImageView> player2Images = new ArrayList<>();
		
		
		for (Champion c : player2.getTeam()) {
			for (int i = 0; i < 15; i++) {
				if (c == Game.getAvailableChampions().get(i)) {
					int a = i+1;
					Image image = new Image("./application/media/" + a + ".jpeg");
					ImageView iv = new ImageView(image);
					iv.setFitHeight(80);
					iv.setFitWidth(80);
					gameStatus.getChildren().add(iv);
//					player2Images.add(iv);
					map.put(Game.getAvailableChampions().get(i),"./application/media/" + a + ".jpeg");
					
					break;
				}
			}
		}
		
		
		Label player2Name = new Label(player2.getName());
		player1Name.setFont(new Font("Didot.",16));
		gameStatus.getChildren().add(player2Name);
		gameStatus.setPadding(new Insets(10,10,10,10));
		gameStatus.setSpacing(10);
		gameStatus.setAlignment(Pos.CENTER);
		
		
		// Turn Order Status Bar
		Label turnLabel = new Label("Next in Turn: ");
		turnOrderStatus.getChildren().add(turnLabel);
		turnOrderStatus.setAlignment(Pos.TOP_RIGHT);
		turnOrderStatus.setMaxWidth(250);
		turnOrderStatus.setMinWidth(250);
		turnOrderStatus.setPadding(new Insets(15,15,15,15));
		
		game = new Game(player1,player2);
		PriorityQueue q = game.getTurnOrder();
		PriorityQueue tmp = new PriorityQueue(q.size());
		
		prepareTurns(map,turnOrderStatus,q,tmp);
		q = tmp;
		PriorityQueue turnOrder = game.getTurnOrder();
		turnOrder = q;
			
		
		// Current Information
			updateCurrentInformation(currentInformation, q);
			game = new Game(player1,player2);
			
			// Board View
			Button[][] boardButtons = new Button[5][5];
			prepareBoard(map,boardButtons, q);
		
			boardView.setAlignment(Pos.CENTER);
			
			
			actions = new ArrayList<Button>();
			Button attack = new Button("Attack");
			attack.setMinHeight(30);
			attack.setMinWidth(30);
			actions.add(attack);
			PriorityQueue Q1 = q;
			attack.setOnAction(e->{
				Button up = new Button("Attack Up");
				
				PriorityQueue Q = Q1;
				up.setOnAction(ee ->{
					boolean f = true;
					
					try {
						game.attack(Direction.UP);
						currentInformation.getChildren().clear();
						updateCurrentInformation(currentInformation, Q);
					} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException e1) {
						f = false;
						throwException(e1.getMessage());
					}
					
					
					if(f) {
						prepareChampions(map,boardButtons, Q);
					}
					
					
				});
				
				Button down = new Button("Attack Down");
				down.setOnAction(ee ->{
					boolean f = true;
					
					try {
						game.attack(Direction.DOWN);
						currentInformation.getChildren().clear();
						updateCurrentInformation(currentInformation, Q);
					} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException e1) {
						f = false;
						throwException(e1.getMessage());

					}
					
					
					if(f) {
						prepareChampions(map,boardButtons, Q);
					}
									
				});
				
				Button left = new Button("Attack Left");
				left.setOnAction(ee ->{
					boolean f = true;
					
					try {
						game.attack(Direction.LEFT);
						currentInformation.getChildren().clear();
						updateCurrentInformation(currentInformation, Q);
					} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException e1) {
						f = false;
						throwException(e1.getMessage());

					}
					
					
					if(f) {
						prepareChampions(map,boardButtons, Q);
					}
					
					
				});
				Button right = new Button("Attack Right");
				right.setOnAction(ee ->{
					boolean f = true;
					
					try {
						game.attack(Direction.RIGHT);
						currentInformation.getChildren().clear();
						updateCurrentInformation(currentInformation, Q);
					} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException e1) {
						f = false;
						throwException(e1.getMessage());
					}
					
					
					if(f) {
						prepareChampions(map,boardButtons, Q);
					}
					
				});
//				System.out.println(currentControls.getChildren().size());
				while(currentControls.getChildren().size()>4) {
					currentControls.getChildren().remove(currentControls.getChildren().size()-1);
				}
				
				Region region = new Region();
				region.setMinWidth(10);
				
				currentControls.getChildren().addAll(region,up,down,left,right);
			});
			
			
			
			
			Button move = new Button("Move");
			move.setMinHeight(30);
			move.setMinWidth(30);
			actions.add(move);
			
			PriorityQueue Q = q;
			move.setOnAction(e->{
				
				Button up = new Button("UP");
				
				up.setOnAction(ee ->{
					boolean f = true;
					try {
						game.move(Direction.UP);
						currentInformation.getChildren().clear();
						updateCurrentInformation(currentInformation, Q);
					} catch (NotEnoughResourcesException | UnallowedMovementException e1) {
						f = false;
						throwException(e1.getMessage());
					}
					
					
					if(f) {
						prepareChampions(map,boardButtons, Q);
					}
					
					
				});
				Button down = new Button("DOWN");
				down.setOnAction(ee ->{
					boolean f = true;
					try {
						game.move(Direction.DOWN);
						currentInformation.getChildren().clear();
						updateCurrentInformation(currentInformation, Q);
					} catch (NotEnoughResourcesException | UnallowedMovementException e1) {
						f = false;
						throwException(e1.getMessage());
					}
					
					if(f) {
						prepareChampions(map,boardButtons, Q);
					}
									
				});
				
				Button left = new Button("LEFT");
				left.setOnAction(ee ->{
					boolean f = true;
					try {
						game.move(Direction.LEFT);
						currentInformation.getChildren().clear();
						updateCurrentInformation(currentInformation, Q);
					} catch (NotEnoughResourcesException | UnallowedMovementException e1) {
						f = false;
						throwException(e1.getMessage());
					}
					
					if(f) {
						prepareChampions(map,boardButtons, Q);
					}
					
					
					
				});
				Button right = new Button("RIGHT");
				right.setOnAction(ee ->{
					boolean f = true;
					try {
						game.move(Direction.RIGHT);
						currentInformation.getChildren().clear();
						updateCurrentInformation(currentInformation, Q);
					} catch (NotEnoughResourcesException | UnallowedMovementException e1) {
						f = false;
						throwException(e1.getMessage());
					}
					
					if(f) {
						prepareChampions(map,boardButtons, Q);
					}
					
				});
				
//				System.out.println(currentControls.getChildren().size());
				while(currentControls.getChildren().size()>4) {
					currentControls.getChildren().remove(currentControls.getChildren().size()-1);
				}
				
				Region region = new Region();
				region.setMinWidth(10);
				
				currentControls.getChildren().addAll(region,up,down,left,right);
			});
			
			
			Button castAbility = new Button("Cast Ability");
			castAbility.setMinHeight(30);
			castAbility.setMinWidth(30);
			actions.add(castAbility);
			
			
			
			
			Button useLeaderAbility = new Button("Use Leader Ability");
			useLeaderAbility.setMinHeight(30);
			useLeaderAbility.setMinWidth(30);
			actions.add(useLeaderAbility);
			
			
			
			currentControls.getChildren().addAll(attack,move,castAbility,useLeaderAbility);
			currentControls.setAlignment(Pos.CENTER);
			currentControls.setPadding(new Insets(10,10,30,10));
	}
	
	public static void updateCurrentInformation(VBox currentInformation, PriorityQueue q) {
		Champion champion = (Champion)q.peekMin();
		String type="";
		if (champion.getClass().toString().equals("class model.world.AntiHero"))
    		type = "AntiHero";
    	else if (champion.getClass().toString().equals("class model.world.Hero"))
    		type = "Hero";
    	else
    		type = "Villain";
		
		String championEffects = "";
		for (Effect e : champion.getAppliedEffects()) {
			championEffects += e.getName() + "(" + e.getDuration() + ")" + ", ";
		}
		if (championEffects.length() >= 2)
			championEffects = championEffects.substring(0,championEffects.length()-2) + ".";
		Label championType = new Label("Type: " + type);
		championType.setFont(new Font("Didot.",12));
		Label championName = new Label("Name: " + champion.getName());
		championName.setFont(new Font("Didot.",12));
		Label championMaxHP = new Label("HP: " + champion.getCurrentHP() + "/" + champion.getMaxHP());
		championMaxHP.setFont(new Font("Didot.",12));
		Label championMana = new Label("Mana: " + champion.getMana() + "");
		championMana.setFont(new Font("Didot.",12));
		Label championActions = new Label("Actions Points: " + champion.getCurrentActionPoints() + "/" + champion.getMaxActionPointsPerTurn());
		championActions.setFont(new Font("Didot.",12));
		Label championSpeed = new Label ("Speed: " + champion.getSpeed() + "");
		championSpeed.setFont(new Font("Didot.",12));
		Label championRange = new Label ("Attack Range: " + champion.getAttackRange() + "");
		championRange.setFont(new Font("Didot.",12));
		Label championDamage = new Label ("Attack Damage: " + champion.getAttackDamage() + "");
		championDamage.setFont(new Font("Didot.",12));
		Label championAppliedEffects = new Label ("Applied Effects: " + championEffects);
		championAppliedEffects.setFont(new Font("Didot.",12));
		Label championCondition = new Label ("Condition: " + champion.getCondition());
		championCondition.setFont(new Font("Didot.",12));
		Region region1 = new Region();
		region1.setMinHeight(30);

		Ability a1 = champion.getAbilities().get(0);
		Ability a2 = champion.getAbilities().get(1);
		Ability a3 = champion.getAbilities().get(2);
		
		Label a1Name = new Label (a1.getName());
		a1Name.setFont(new Font("Didot.",12));
		Label a1Mana = new Label ("Mana Cost: " + a1.getManaCost());
		a1Mana.setFont(new Font("Didot.",12));
		Label a1Cool = new Label ("Cooldown: " + a1.getCurrentCooldown() + "/" + a1.getBaseCooldown());
		a1Cool.setFont(new Font("Didot.",12));
		Label a1Range = new Label ("Range: " + a1.getCastRange());
		a1Range.setFont(new Font("Didot.",12));
		Label a1Area = new Label ("Cast Area: " + a1.getCastArea());
		a1Area.setFont(new Font("Didot.",12));
		Label a1Action = new Label ("Required Action Points: " + a1.getRequiredActionPoints());
		a1Action.setFont(new Font("Didot.",12));
		Region region2 = new Region();
		region2.setMinHeight(30);
		Label a2Name = new Label (a2.getName());
		a2Name.setFont(new Font("Didot.",12));
		Label a2Mana = new Label ("Mana Cost: " + a2.getManaCost());
		a2Mana.setFont(new Font("Didot.",12));
		Label a2Cool = new Label ("Cooldown: " + a2.getCurrentCooldown() + "/" + a2.getBaseCooldown());
		a2Cool.setFont(new Font("Didot.",12));
		Label a2Range = new Label ("Range: " + a2.getCastRange());
		a2Range.setFont(new Font("Didot.",12));
		Label a2Area = new Label ("Cast Area: " + a2.getCastArea());
		a2Area.setFont(new Font("Didot.",12));
		Label a2Action = new Label ("Required Action Points: " + a2.getRequiredActionPoints());	
		a2Action.setFont(new Font("Didot.",12));
		Region region3 = new Region();
		region3.setMinHeight(30);
		Label a3Name = new Label (a3.getName());
		a3Name.setFont(new Font("Didot.",12));
		Label a3Mana = new Label ("Mana Cost: " + a3.getManaCost());
		a3Mana.setFont(new Font("Didot.",12));
		Label a3Cool = new Label ("Cooldown: " + a3.getCurrentCooldown() + "/" + a3.getBaseCooldown());
		a3Cool.setFont(new Font("Didot.",12));
		Label a3Range = new Label ("Range: " + a3.getCastRange());
		a3Range.setFont(new Font("Didot.",12));
		Label a3Area = new Label ("Cast Area: " + a3.getCastArea());
		a3Area.setFont(new Font("Didot.",12));
		Label a3Action = new Label ("Required Action Points: " + a3.getRequiredActionPoints());
		a3Action.setFont(new Font("Didot.",12));
		
		currentInformation.setMaxWidth(250);
		currentInformation.setMinWidth(250);
		currentInformation.setAlignment(Pos.TOP_LEFT);
		currentInformation.setPadding(new Insets(10,10,10,10));
		currentInformation.getChildren().addAll(championType,championName,championMaxHP,championMana,championActions,
				championSpeed, championRange, championDamage, championAppliedEffects, championCondition, region1, a1Name, a1Mana, a1Cool, a1Range, a1Area, a1Action, 
				region2, a2Name, a2Mana, a2Cool, a2Range, a2Area, a2Action,region3, a3Name, a3Mana, a3Cool, a3Range, a3Area, a3Action);		
		
	}
	
	public static void prepareBoard(HashMap<Champion,String> map,Button[][] boardButtons, PriorityQueue q) {
		
		board = game.getBoard();
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
						Stage cuurentHealth = new Stage();
						cuurentHealth.setTitle("Cover");
						VBox window = new VBox(10);
						window.setAlignment(Pos.CENTER);
						Scene scene = new Scene(window);
						Button OK = new Button("OK");
						OK.setOnAction( ee -> cuurentHealth.close());
						cuurentHealth.setScene(scene);
						cuurentHealth.setMinWidth(400);
						cuurentHealth.setMinHeight(200);
						Text msgText =new Text("Cover's health: " + ((Cover)(board[a][b])).getCurrentHP());
						window.getChildren().addAll(msgText, OK);
						window.setPadding(new Insets(10,10,10,10));
						cuurentHealth.show();
					});
				}
				
				else if(board[i][j] instanceof Champion){
					Image img = new Image(map.get((Champion)board[i][j]));
					ImageView iv = new ImageView(img);
					iv.setFitHeight(110);
					iv.setFitWidth(110);
					btn.setGraphic(iv);
					if (((Champion)board[i][j]) == q.peekMin() && player1.getTeam().contains(q.peekMin())) {
						btn.setStyle("-fx-background-color: #010098;");
					}
					else if (((Champion)board[i][j]) == q.peekMin() && player2.getTeam().contains(q.peekMin())) {
						btn.setStyle("-fx-background-color: #9a0000; ");
					}
					int a = i;
					int b = j;
					btn.setOnAction(e -> {
						Stage cuurentHealth = new Stage();
						cuurentHealth.setTitle(((Champion)board[a][b]).getName());
						VBox window = new VBox(10);
						window.setAlignment(Pos.CENTER);
						Scene scene = new Scene(window);
						Button OK = new Button("OK");
						OK.setOnAction( ee -> cuurentHealth.close());
						cuurentHealth.setScene(scene);
						cuurentHealth.setMinWidth(400);
						cuurentHealth.setMinHeight(200);
						Text teamText;
						if (player1.getTeam().contains((Champion)(board[a][b])))
							teamText = new Text("Belonging to first team");
						else 
							teamText = new Text("Belonging to second team");
						Text healthText =new Text("Champion's health: " + ((Champion)(board[a][b])).getCurrentHP()
								+ "/" + ((Champion)(board[a][b])).getMaxHP());
						Text conditionText =new Text("Champion's condition: " + ((Champion)(board[a][b])).getCondition());
						String effects = "";
						for (Effect effect : ((Champion)board[a][b]).getAppliedEffects()) {
							effects += effect.getName() + ", ";
						}
						if (effects.length() >= 2)
							effects = effects.substring(0, effects.length()-2);
						Text effectsText =new Text("Effects on Champion: " + effects);
						window.getChildren().addAll(teamText, healthText, conditionText, effectsText, OK);
						window.setPadding(new Insets(10,10,10,10));
						cuurentHealth.show();
					});
				}
				boardView.add(btn,j,4-i);
			}
		}	
	}
	
	public static void prepareChampions(HashMap<Champion,String> map,Button[][] boardButtons, PriorityQueue q) {
		board = game.getBoard();
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
						Stage cuurentHealth = new Stage();
						cuurentHealth.setTitle("Cover");
						VBox window = new VBox(10);
						window.setAlignment(Pos.CENTER);
						Scene scene = new Scene(window);
						Button OK = new Button("OK");
						OK.setOnAction( ee -> cuurentHealth.close());
						cuurentHealth.setScene(scene);
						cuurentHealth.setMinWidth(400);
						cuurentHealth.setMinHeight(200);
						Text msgText =new Text("Cover's health: " + ((Cover)(board[a][b])).getCurrentHP());
						window.getChildren().addAll(msgText, OK);
						window.setPadding(new Insets(10,10,10,10));
						cuurentHealth.show();
					});
				}
				
				else if(board[i][j] instanceof Champion){
					Image img = new Image(map.get((Champion)board[i][j]));
					ImageView iv = new ImageView(img);
					iv.setFitHeight(110);
					iv.setFitWidth(110);
					btn.setGraphic(iv);
					if (((Champion)board[i][j]) == q.peekMin() && player1.getTeam().contains(q.peekMin())) {
						btn.setStyle("-fx-background-color: #010098;");
					}
					else if (((Champion)board[i][j]) == q.peekMin() && player2.getTeam().contains(q.peekMin())) {
						btn.setStyle("-fx-background-color: #9a0000; ");
					}
					int a = i;
					int b = j;
					btn.setOnAction(e -> {
						Stage cuurentHealth = new Stage();
						cuurentHealth.setTitle(((Champion)board[a][b]).getName());
						VBox window = new VBox(10);
						window.setAlignment(Pos.CENTER);
						Scene scene = new Scene(window);
						Button OK = new Button("OK");
						OK.setOnAction( ee -> cuurentHealth.close());
						cuurentHealth.setScene(scene);
						cuurentHealth.setMinWidth(400);
						cuurentHealth.setMinHeight(200);
						Text teamText;
						if (player1.getTeam().contains((Champion)(board[a][b])))
							teamText = new Text("Belonging to first team");
						else 
							teamText = new Text("Belonging to second team");
						Text healthText =new Text("Champion's health: " + ((Champion)(board[a][b])).getCurrentHP()
								+ "/" + ((Champion)(board[a][b])).getMaxHP());
						Text conditionText =new Text("Champion's condition: " + ((Champion)(board[a][b])).getCondition());
						String effects = "";
						for (Effect effect : ((Champion)board[a][b]).getAppliedEffects()) {
							effects += effect.getName() + ", ";
						}
						if (effects.length() >= 2)
							effects = effects.substring(0, effects.length()-2);
						Text effectsText =new Text("Effects on Champion: " + effects);
						window.getChildren().addAll(teamText, healthText, conditionText, effectsText, OK);
						window.setPadding(new Insets(10,10,10,10));
						cuurentHealth.show();
					});
				}
					
				boardView.add(btn,j,4-i);
				
			}
		}
	}
	
	public static void prepareTurns(HashMap<Champion,String> map,VBox turnOrderStatus, PriorityQueue q, PriorityQueue tmp) {
//		System.out.println(q.peekMin());
		while(!q.isEmpty()){
			Image img = new Image(map.get((Champion)q.peekMin()));
			ImageView iv = new ImageView(img);
			iv.setFitHeight(80);
			iv.setFitWidth(80);
			
			turnOrderStatus.getChildren().add(iv);
			turnOrderStatus.setSpacing(15);
			tmp.insert((Champion)q.remove());
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
	
	public static void main(String[] args) {
		launch(args);
	}
	
}