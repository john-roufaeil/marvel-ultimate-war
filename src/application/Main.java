package application;
	
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import engine.Game;
import engine.Player;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import model.world.Champion;
import javafx.scene.Group;
import javafx.scene.Parent;
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
import javafx.scene.layout.StackPane;
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
	static boolean full = false;
	static int idx1 = 0;
	static int idx2 = 0;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Marvel - Ultimate War");
		primaryStage.setFullScreen(true);
//		primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		Image icon = new Image("icon.png");
		primaryStage.getIcons().add(icon);
		
		scene1(primaryStage);
		
		primaryStage.setScene(homepage);
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
		TextField name1TextField = new TextField();
		HBox firstPlayer = new HBox();
		firstPlayer.setAlignment(Pos.CENTER);
		firstPlayer.getChildren().addAll(name1Label, name1TextField);
		TextField name2TextField = new TextField();
		Label name2Label = new Label("Second Player Name: ");
		HBox secondPlayer = new HBox();
		secondPlayer.setAlignment(Pos.CENTER);
		secondPlayer.getChildren().addAll(name2Label, name2TextField);
		
		Button startBtn = new Button("Begin Game!");
		startBtn.setOnAction(e -> {
//			primaryStage.setScene(begin);
			player1 = new Player(name1TextField.getText());
			player2 = new Player(name2TextField.getText());
			try {
				game = new Game(player1, player2);
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			scene2(primaryStage);
		});
		HBox btn = new HBox();
		btn.setAlignment(Pos.CENTER);
		btn.getChildren().add(startBtn);
		root1.getChildren().addAll(firstPlayer, secondPlayer, btn);
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
		ArrayList<Button> buttons = new ArrayList<>();
		ArrayList<ImageView> images = new ArrayList<>();
		int a = 0; int b = 0;
		
		ArrayList<Champion> champions = Game.getAvailableChampions();
		map = new HashMap<Champion,Boolean>();
		for(Champion c : champions) {
			map.put(c, false);
		}
		
		for (int i = 1; i <= 15; i++) {
//			ArrayList<Champion> champions = Game.getAvailableChampions();
			Champion champion = champions.get(i-1);
			Image ch = new Image("./application/media/" + i + ".jpeg");
			ImageView iv = new ImageView(ch);
			iv.setFitHeight(50);
			iv.setFitWidth(50);
			images.add(iv);
			Button btn = new Button();
			btn.setPrefSize(80, 80);
		    btn.setGraphic(iv);
		    btn.setOnAction((e) -> {
		    	show(champion, root2, chosenChampions,ch,btn);
		    });
		    
		    
		    
		    champsgrid.add(btn, a, b);
		    a++;
		    if (a == 5) {
		    	a = 0;
		    	b++;
		    }
		    buttons.add(btn);
		}
		
		
		
	
//		while ((player1.getTeam().size() == 3 && player2.getTeam().size() == 3)) {
//			System.out.println("DONE");
//			return;
//		}
		
		
	}
	
	public static void show(Champion champion, BorderPane root2, HBox chosenChampions,Image ch,Button btn) {
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
		Label championA1 = new Label (champion.getAbilities().get(0).getName());
		Label championA2 = new Label (champion.getAbilities().get(1).getName());
		Label championA3 = new Label (champion.getAbilities().get(2).getName());
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
				full = true;
				details.getChildren().clear();
				Label chooseLeaderLabel1 = new Label("Choose a leader for the first team");
				details.getChildren().add(chooseLeaderLabel1);
				
				for (int i = 1; i <= 3; i++) {
					idx1 = i-1;
					Button button = new Button();
					button.setOnAction(event -> {
						chooseLeader(player1, player1.getTeam().get(idx1), details);
					});
					button.setPrefSize(50, 50);
					ImageView img = (ImageView)(chosenChampions.getChildren().get(i));
				    button.setGraphic(img);
					details.getChildren().add(button);
				}
				
				Label chooseLeaderLabel2 = new Label("Choose a leader for the second team");
				details.getChildren().add(chooseLeaderLabel2); 
			
				
				for (int i = 5; i <= 7; i++) {
					idx2 = i-5;
					Button button = new Button();
					button.setOnAction(event -> {
						chooseLeader(player2,player2.getTeam().get(idx2), details);
					});
					button.setPrefSize(50, 50);
					ImageView img = (ImageView)(chosenChampions.getChildren().get(i));
				    button.setGraphic(img);
					details.getChildren().add(button);
				}
			    chosenChampions.getChildren().clear();

			}
		});
		
		if (!full)
				details.getChildren().addAll(championType, championName, championMaxHP, championMana, championActions,
				championSpeed, championRange, championDamage,championA1,championA2,championA3, choose);
		
		
		
		root2.setCenter(details);
	}
	
	public static void chooseLeader(Player player, Champion c, VBox details) {
		
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
			details.getChildren().add(play);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}