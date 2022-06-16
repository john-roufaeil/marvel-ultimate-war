/*
 * TODO
 * 
 *  
 *  on leader ability use, all board glows (red for villain, blue for hero, green for antihero)
 *  
/ * 
 *  update buttons keyboard move
 * -------------------------------------------------------------------
 *  update README
 *  
 *  make video
 *  
 *  write LinkedIn post
 * 
 *  be creative with pop-ups and make sure they pop in the center of screen
 *  
 *  make sure there are no warnings or errors in the whole project
 *  
 * -----------------------------------------
 *  add select and deselect champions and leaders
 *  separate each view in a separate class
 *  surround not functioning well in crowd control abilitiies
 */

package application;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import engine.Game;
import engine.Player;
import engine.PriorityQueue;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import javafx.animation.PauseTransition;
import javafx.fxml.Initializable;
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
import model.world.Cover;
import model.world.Damageable;
import model.world.Direction;
import model.world.Hero;
import model.world.Villain;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class View extends Application implements Initializable {
	static Game game;
	static Scene scene;
	static BorderPane root3;
	static HashMap<Champion, Boolean> chosenMap;
	static HashMap<Champion, String> aliveMap;
	static HashMap<Champion, String> deadMap;
	static HashMap<Champion, String> soundMap;
	static PriorityQueue q;
	static Stage primaryStage;
	static boolean twoPlayerMode;
	static MediaPlayer songPlayer;
	static MediaPlayer soundPlayer;
	
	@Override
	public void start(Stage primaryStage1) throws Exception {
		View.primaryStage = primaryStage1;
		primaryStage.setTitle("Marvel - Ultimate War");
		primaryStage.setFullScreen(true);
		primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		Image icon = new Image("./application/media/gameIcon.jpeg");
		primaryStage.getIcons().add(icon);
		
		File mediaFile = new File("./src/application/media/intro.mp4");
	    Media media = new Media(mediaFile.toURI().toString());
		MediaPlayer introMediaPlayer = new MediaPlayer(media);
		MediaView introMediaView = new MediaView(introMediaPlayer);
		introMediaPlayer.setAutoPlay(true);
		introMediaView.setMediaPlayer(introMediaPlayer);
		introMediaView.fitHeightProperty().bind(primaryStage.heightProperty());
		introMediaView.fitWidthProperty().bind(primaryStage.widthProperty());
		introMediaView.setVisible(true);
		BorderPane root0 = new BorderPane();
		StackPane stack = new StackPane();
		stack.getChildren().add(introMediaView);
		stack.getChildren().add(root0);
		Label skipLabel = new Label("Click anywhere to skip.");
		skipLabel.setPadding(new Insets(30,30,30,30));
		skipLabel.setTextFill(Color.color(1, 1, 1));
		skipLabel.setFont(new Font("Didot.", 25));
		HBox skipBox = new HBox();
		skipBox.setAlignment(Pos.BOTTOM_CENTER);
		skipBox.getChildren().add(skipLabel);
		root0.setBottom(skipBox);
		
		stack.setOnMouseClicked(e -> {
			introMediaPlayer.stop();
			checkPlayingMode();
		});
		
		introMediaPlayer.setOnEndOfMedia( () -> { checkPlayingMode(); });
		Scene scene = new Scene(stack);
		View.scene = scene;
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	public static void checkPlayingMode() {
		Media song = new Media(new File("./src/application/media/song.wav").toURI().toString());
		songPlayer = new MediaPlayer(song);
		songPlayer.setOnEndOfMedia(new Runnable() {
		      public void run() {
		        songPlayer.seek(Duration.ZERO);
		      }
		});
		songPlayer.setAutoPlay(true);
		songPlayer.setVolume(0.75);
		songPlayer.play();
		
		// Scene Organisation
		BorderPane root1 = new BorderPane();
		Image background = new Image("application/media/backgrounds/back1.jpeg");
		ImageView backgroundIV = new ImageView(background);
		backgroundIV.fitHeightProperty().bind(primaryStage.heightProperty());
		backgroundIV.fitWidthProperty().bind(primaryStage.widthProperty());
		root1.getChildren().add(backgroundIV);
//		Scene homePage = new Scene(root1, 400, 400);

		// Choose Player Mode
		HBox chooseMode = new HBox(50);
		HBox welcomeBox = new HBox(10);
		Label welcome = new Label("WELCOME TO WAR!");
		welcome.setFont(new Font("Didot.", 70));
		welcomeBox.setAlignment(Pos.CENTER);
		welcomeBox.getChildren().add(welcome);
		chooseMode.setPadding(new Insets(10, 10, 70, 10));
		chooseMode.setAlignment(Pos.CENTER);

		Button onePlayer = new Button("1 Player\nUnder Development");
		onePlayer.setDisable(true);
		onePlayer.setTextAlignment(TextAlignment.CENTER);
		onePlayer.setMinHeight(90);
		onePlayer.setMinWidth(230);
		onePlayer.setOnAction(e -> {
			twoPlayerMode = false;
			enterNames(root1);
		});

		Button twoPlayers = new Button("2 Players");
		twoPlayers.setMinHeight(90);
		twoPlayers.setMinWidth(230);
		twoPlayers.setOnAction(e -> {
			twoPlayerMode = true;
			enterNames(root1);
		});

		chooseMode.getChildren().addAll(onePlayer, twoPlayers);
		root1.setBottom(chooseMode);
		root1.setTop(welcomeBox);
		scene.setRoot(root1);
//		primaryStage.setScene(homePage);
		primaryStage.setFullScreen(true);
	}

	public static void enterNames(BorderPane root1) {
		VBox enterNamesVBox = new VBox(15);
		enterNamesVBox.setPadding(new Insets(10, 10, 70, 10));
		root1.setBottom(enterNamesVBox);
		Label enterFirstPlayerNameLabel = new Label();
		if (twoPlayerMode) {
			enterFirstPlayerNameLabel.setText("First Player Name: ");
		} else {
			enterFirstPlayerNameLabel.setText("Enter Your Name: ");
		}
		enterFirstPlayerNameLabel.setTextFill(Color.color(1, 1, 1));
		enterFirstPlayerNameLabel.setFont(new Font("Didot.", 16));
		TextField name1TextField = new TextField();
		HBox firstPlayerHBox = new HBox();
		firstPlayerHBox.setAlignment(Pos.CENTER);
		firstPlayerHBox.getChildren().addAll(enterFirstPlayerNameLabel, name1TextField);

		// Second Player Enter Name
		Label enterSecondPlayerNameLabel = new Label("Second Player Name: ");
		enterSecondPlayerNameLabel.setTextFill(Color.color(1, 1, 1));
		enterSecondPlayerNameLabel.setFont(new Font("Didot.", 16));
		TextField name2TextField = new TextField();
		HBox secondPlayerHBox = new HBox();
		secondPlayerHBox.setAlignment(Pos.CENTER);
		secondPlayerHBox.getChildren().addAll(enterSecondPlayerNameLabel, name2TextField);

		// Begin Game Button
		Button startBtn = new Button("Begin Game!");
		startBtn.setOnAction(e -> {
			Player player1 = new Player(name1TextField.getText());
			Player player2 = new Player("Computer");
			if (twoPlayerMode)
				player2 = new Player(name2TextField.getText());
			try {
				View.game = new Game(player1, player2);
				q = View.game.getTurnOrder();
			} catch (IOException e1) {
				throwException(e1.getMessage());
				;
			}
			scene2();
		});
		// Configuring Nodes
		HBox buttonHBox = new HBox();
		buttonHBox.setAlignment(Pos.CENTER);
		buttonHBox.getChildren().add(startBtn);
		if (twoPlayerMode)
			enterNamesVBox.getChildren().addAll(firstPlayerHBox, secondPlayerHBox, buttonHBox);
		else
			enterNamesVBox.getChildren().addAll(firstPlayerHBox, buttonHBox);

		root1.setBottom(enterNamesVBox);
//		primaryStage.setScene(homepage);
		primaryStage.setFullScreen(true);
	}

	// Choose Champions
	public static void scene2() {
		Image background = new Image("application/media/backgrounds/avengers-background.jpg");
		ImageView backgroundIV = new ImageView(background);
		backgroundIV.fitHeightProperty().bind(primaryStage.heightProperty());
		backgroundIV.fitWidthProperty().bind(primaryStage.widthProperty());

		// Map Champions with their Images
		soundMap = new HashMap<Champion, String>();
		aliveMap = new HashMap<Champion, String>();
		deadMap = new HashMap<Champion, String>();
		for (int i = 1; i <= 24; i++) {
			soundMap.put(Game.getAvailableChampions().get(i - 1), "./src/application/media/champions/" + i + ".wav");
			aliveMap.put(Game.getAvailableChampions().get(i - 1), "./application/media/champions/" + i + ".jpeg");
			deadMap.put(Game.getAvailableChampions().get(i - 1), "./application/media/champions/" + i + "d.jpeg");
		}

		// Scene Organisation
		BorderPane root2 = new BorderPane();
		root2.getChildren().add(backgroundIV);
		scene.setRoot(root2);
//		Scene begin = new Scene(root2);
//		primaryStage.setScene(begin);
		primaryStage.setFullScreen(true);
		HBox chosenChampions = new HBox();
		VBox detailsVBox = new VBox();
		GridPane champsgrid = new GridPane();

		root2.setTop(chosenChampions);
		root2.setCenter(detailsVBox);
		root2.setBottom(champsgrid);

		// Chosen Champions Bar
		// First Player Label and Selected Champions ImageViews
		Label player1LabelScene2 = new Label(View.game.getFirstPlayer().getName());
		player1LabelScene2.setTextFill(Color.color(1, 1, 1));
		player1LabelScene2.setFont(new Font("Didot.", 15));
		Image notYetSelected = new Image("application/media/gameIcon.jpeg");
		ImageView chosen1_1 = new ImageView(notYetSelected);
		chosen1_1.setFitWidth(70);
		chosen1_1.setFitHeight(70);
		ImageView chosen1_2 = new ImageView(notYetSelected);
		chosen1_2.setFitWidth(70);
		chosen1_2.setFitHeight(70);
		ImageView chosen1_3 = new ImageView(notYetSelected);
		chosen1_3.setFitWidth(70);
		chosen1_3.setFitHeight(70);
		Region region = new Region();
		region.setMinWidth(100);
		// Second Player Label and Selected Champions ImageViews
		ImageView chosen2_1 = new ImageView(notYetSelected);
		chosen2_1.setFitWidth(70);
		chosen2_1.setFitHeight(70);
		ImageView chosen2_2 = new ImageView(notYetSelected);
		chosen2_2.setFitWidth(70);
		chosen2_2.setFitHeight(70);
		ImageView chosen2_3 = new ImageView(notYetSelected);
		chosen2_3.setFitWidth(70);
		chosen2_3.setFitHeight(70);
		Label player2LabelScene2 = new Label(View.game.getSecondPlayer().getName());
		player2LabelScene2.setTextFill(Color.color(1, 1, 1));
		player2LabelScene2.setFont(new Font("Didot.", 15));
		// Configuring Nodes
		chosenChampions.getChildren().addAll(player1LabelScene2, chosen1_1, chosen1_2, chosen1_3, region, chosen2_1,
				chosen2_2, chosen2_3, player2LabelScene2);
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
//		champsgrid.setBackground(new Background(new BackgroundFill(Color.AQUA, CornerRadii.EMPTY, Insets.EMPTY)));
		chosenMap = new HashMap<Champion, Boolean>();
		int a = 0;
		int b = 0;
		champsgrid.setHgap(5);
		champsgrid.setVgap(5);
		for (Champion c : Game.getAvailableChampions()) {
			chosenMap.put(c, false);
			Image ch = new Image(aliveMap.get(c));
			ImageView iv = new ImageView(ch);
			Button btn = new Button();
			iv.setFitHeight(85);
			iv.setFitWidth(85);
			btn.setMinHeight(85);
			btn.setMaxHeight(85);
			btn.setMinWidth(85);
			btn.setMaxWidth(85);
			btn.setGraphic(iv);

//			BackgroundImage bImage = new BackgroundImage(ch, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(btn.getWidth(), btn.getHeight(), true, true, true, false));
//			Background backGround = new Background(bImage);
//			btn.setBackground(backGround);
			btn.setOnAction((e) -> {
				boolean picked = false;
				show(c, root2, chosenChampions, ch, btn);
				if (picked)
					btn.setDisable(true);
			});

			champsgrid.add(btn, a, b);
			a++;
			if (a == 12) {
				a = 0;
				b++;
			}

		}

		// Configuring Nodes
		champsgrid.setPadding(new Insets(10, 10, 10, 10));
//		champsgrid.setStyle("-fx-background-color: #222;");
		champsgrid.setAlignment(Pos.CENTER);
	}

	// Show Pressed Champion's Details
	public static void show(Champion champion, BorderPane root2, HBox chosenChampions, Image ch, Button btn) {
		// Organisation
		StackPane stack = new StackPane();
		VBox details = new VBox(15);	
		// Configuring Nodes
		details.setPadding(new Insets(10, 10, 10, 10));
		details.setAlignment(Pos.CENTER);
		root2.setCenter(stack);

		// Information Labels about Pressed Champion
		String type = "";
		if (champion instanceof AntiHero)
			type = "AntiHero";
		else if (champion instanceof Hero)
			type = "Hero";
		else
			type = "Villain";
//    	Label championType = new Label("Type: " + type);
//    	championType.setFont(new Font("Impact",18));
		Label championNameAndType = new Label(champion.getName().toUpperCase() + " (" + type + ")");
		Label championMaxHP = new Label("Maximum HP: " + champion.getMaxHP() + "");
		Label championMana = new Label("Mana: " + champion.getMana() + "");
		Label championActions = new Label(
				"Maximum Actions Points per Turn: " + champion.getMaxActionPointsPerTurn() + "");
		Label championSpeed = new Label("Speed: " + champion.getSpeed() + "");
		Label championRange = new Label("Attack Range: " + champion.getAttackRange() + "");
		Label championDamage = new Label("Attack Damage: " + champion.getAttackDamage() + "");
		Label championAbilities = new Label("Abilities: " + champion.getAbilities().get(0).getName() + ", "
				+ champion.getAbilities().get(1).getName() + ", " + champion.getAbilities().get(2).getName() + ".");
		// Choose Button
		Button choose = new Button("Pick");
		boolean chosen = chosenMap.get(champion);
		if (chosen)
			choose.setDisable(true);
		choose.setOnAction(e -> {
			// Playing the champion's sound
//			songPlayer.pause();
			Media sound = new Media(new File(soundMap.get(champion)).toURI().toString());
			soundPlayer = new MediaPlayer(sound);
			soundPlayer.setVolume(0.5);
			soundPlayer.play();
//			soundPlayer.setOnEndOfMedia(() -> songPlayer.play());
			
			
			// Putting the Chosen Champion's Image in Status Bar
			if (View.game.getFirstPlayer().getTeam().size() < 3) {
				View.game.getFirstPlayer().getTeam().add(champion);
				if (View.game.getFirstPlayer().getTeam().size() == 1) {
					ImageView img = (ImageView) (chosenChampions.getChildren().get(1));
					img.setImage(ch);
				}

				else if (View.game.getFirstPlayer().getTeam().size() == 2) {
					ImageView img = (ImageView) (chosenChampions.getChildren().get(2));
					img.setImage(ch);
				}

				else if (View.game.getFirstPlayer().getTeam().size() == 3) {
					ImageView img = (ImageView) (chosenChampions.getChildren().get(3));
					img.setImage(ch);

					if (!twoPlayerMode) {

						do {
							int i = (int) (Math.random() * Game.getAvailableChampions().size());
							Champion cc = Game.getAvailableChampions().get(i);

							Image cch = new Image(aliveMap.get(cc));
							ImageView iv = new ImageView(cch);
							iv.setFitHeight(80);
							iv.setFitWidth(80);
							if (!View.game.getFirstPlayer().getTeam().contains(cc) && !View.game.getSecondPlayer().getTeam().contains(cc)) {
								View.game.getSecondPlayer().getTeam().add(cc);
								if (View.game.getSecondPlayer().getTeam().size() == 1) {
									ImageView imgg = (ImageView) (chosenChampions.getChildren().get(5));
									imgg.setImage(cch);
								}

								else if (View.game.getSecondPlayer().getTeam().size() == 2) {
									ImageView imgg = (ImageView) (chosenChampions.getChildren().get(6));
									imgg.setImage(cch);
								}

								else if (View.game.getSecondPlayer().getTeam().size() == 3) {
									ImageView imgg = (ImageView) (chosenChampions.getChildren().get(7));
									imgg.setImage(cch);
								}

							}
						} while (View.game.getSecondPlayer().getTeam().size() < 3);

					}
				}
			}

			else {
				if (twoPlayerMode) {
					View.game.getSecondPlayer().getTeam().add(champion);
					if (View.game.getSecondPlayer().getTeam().size() == 1) {
						ImageView img = (ImageView) (chosenChampions.getChildren().get(5));
						img.setImage(ch);
					}

					else if (View.game.getSecondPlayer().getTeam().size() == 2) {
						ImageView img = (ImageView) (chosenChampions.getChildren().get(6));
						img.setImage(ch);
					}

					else if (View.game.getSecondPlayer().getTeam().size() == 3) {
						ImageView img = (ImageView) (chosenChampions.getChildren().get(7));
						img.setImage(ch);
					}
				}
			}

			// Disabling Choose Button and Setting the Chosen Champion to true in chosenMap
			choose.setDisable(true);
			for (Map.Entry<Champion, Boolean> m : chosenMap.entrySet()) {
				if (m.getKey() == champion) {
					chosenMap.put(m.getKey(), true);
				}
			}
			// Disable All Champions Buttons When Teams Full and Ask to Choose Leaders
			if (View.game.getFirstPlayer().getTeam().size() + View.game.getSecondPlayer().getTeam().size() == 6) {
				root2.setBottom(null);

				details.getChildren().clear();

				Label chooseLeaderLabel1 = new Label("Choose a leader for the first team");
				chooseLeaderLabel1.setFont(new Font("Didot.", 14));
				chooseLeaderLabel1.setTextFill(Color.color(1, 1, 1));
				details.getChildren().add(chooseLeaderLabel1);

				for (int i = 1; i <= 3; i++) {
					Button button = new Button();
					int a = i - 1;
					button.setOnAction(event -> {
						chooseLeader(View.game.getFirstPlayer(), View.game.getFirstPlayer().getTeam().get(a), details);
					});
					button.setMaxHeight(80);
					button.setMinHeight(80);
					button.setMaxWidth(80);
					button.setMinWidth(80);
					ImageView img = (ImageView) (chosenChampions.getChildren().get(i));
					button.setGraphic(img);
					img.setFitHeight(80);
					img.setFitWidth(80);
					details.getChildren().add(button);
				}

				Label chooseLeaderLabel2 = new Label("Choose a leader for the second team");
				chooseLeaderLabel2.setFont(new Font("Didot.", 14));
				chooseLeaderLabel2.setTextFill(Color.color(1, 1, 1));
				details.getChildren().add(chooseLeaderLabel2);

				for (int i = 5; i <= 7; i++) {
					Button button = new Button();
					int a = i - 5;
					button.setOnAction(event -> {
						chooseLeader(View.game.getSecondPlayer(), View.game.getSecondPlayer().getTeam().get(a), details);
					});
					button.setMaxHeight(80);
					button.setMinHeight(80);
					button.setMaxWidth(80);
					button.setMinWidth(80);
					ImageView img = (ImageView) (chosenChampions.getChildren().get(i));
					button.setGraphic(img);
					img.setFitHeight(80);
					img.setFitWidth(80);
					details.getChildren().add(button);
				}
				if (!twoPlayerMode) {
					details.getChildren().get(5).setDisable(true);
					details.getChildren().get(6).setDisable(true);
					details.getChildren().get(7).setDisable(true);
				}

				chosenChampions.getChildren().clear();
			}
		});

		if (View.game.getFirstPlayer().getTeam().size() == 3 && !twoPlayerMode)
			choose.fire();
		
		
		details.getChildren().addAll(championNameAndType, championMaxHP, championMana, championActions,
				championSpeed, championRange, championDamage, championAbilities, choose);

		Rectangle shade = new Rectangle();
		shade.setFill(Color.BLACK);
		shade.setOpacity(0.6);
		shade.setArcWidth(30.0); 
	    shade.setArcHeight(30.0);  
		shade.setWidth(800);
		shade.setHeight(350);
		stack.getChildren().addAll(shade, details);
		
		for (Node n : details.getChildren()) {
			if (n instanceof Label) {
				((Labeled) n).setFont(new Font("Impact", 18));
				((Labeled) n).setTextFill(Color.color(1, 1, 1));
			}
		}
	}

	// Set Leader and Disable Choosing Another Leader
	public static void chooseLeader(Player player, Champion c, VBox details) {
		
		if (player == View.game.getFirstPlayer()) {
			View.game.getFirstPlayer().setLeader(c);
			details.getChildren().get(1).setDisable(true);
			details.getChildren().get(2).setDisable(true);
			details.getChildren().get(3).setDisable(true);

			if (!twoPlayerMode) {
				int i = (int) (Math.random() * View.game.getSecondPlayer().getTeam().size());
				View.game.getSecondPlayer().setLeader(View.game.getSecondPlayer().getTeam().get(i));
				details.getChildren().get(5).setDisable(true);
				details.getChildren().get(6).setDisable(true);
				details.getChildren().get(7).setDisable(true);
			}
		}

		else if (player == View.game.getSecondPlayer()) {
			View.game.getSecondPlayer().setLeader(c);
			details.getChildren().get(5).setDisable(true);
			details.getChildren().get(6).setDisable(true);
			details.getChildren().get(7).setDisable(true);
		}

		if (View.game.getFirstPlayer().getLeader() != null && View.game.getSecondPlayer().getLeader() != null) {
			Button play = new Button("Play");
			play.setOnAction(e -> {
				try {
					scene3();
				} catch (IOException e1) {
				}
			});
			details.getChildren().add(play);
		}
	}

	// Open Board Game View
	public static void scene3() throws IOException {
		Image backgroundBoard = new Image("application/media/backgrounds/gameplay-background.png");

//		Image backgroundBoard = new Image("application/media/backgrounds/gameplay-1.jpeg");
		ImageView backgroundBoardIV = new ImageView(backgroundBoard);
		backgroundBoardIV.fitHeightProperty().bind(primaryStage.heightProperty());
		backgroundBoardIV.fitWidthProperty().bind(primaryStage.widthProperty());

		// Assign players' champions team arrays for image views
		View.game.placeChampions();
		View.game.prepareChampionTurns();

		// Scene organisation
		root3 = new BorderPane();
		root3.getChildren().add(backgroundBoardIV);
		scene.setRoot(root3);
//		Scene gameview = new Scene(root3);
		keyMoved();
//		primaryStage.setScene(gameview);
		primaryStage.setFullScreen(true);

		BorderPane gameStatus = new BorderPane();
		HBox statsBar = new HBox(10);
		HBox muteBox = new HBox();
		HBox helpBox = new HBox();
		VBox turnOrderStatus = new VBox(15);
		HBox currentControls = new HBox(30);
		GridPane boardView = new GridPane();
		VBox currentInformation = new VBox(5);
		root3.setTop(gameStatus);
		root3.setRight(turnOrderStatus);
		root3.setBottom(currentControls);
		root3.setLeft(currentInformation);
		root3.setCenter(boardView);
		gameStatus.setCenter(statsBar);
		gameStatus.setLeft(muteBox);
		gameStatus.setRight(helpBox);

		gameStatus.setPadding(new Insets(10, 10, 10, 10));
		statsBar.setAlignment(Pos.CENTER);
		turnOrderStatus.setPadding(new Insets(15, 15, 15, 15));
		turnOrderStatus.setAlignment(Pos.TOP_RIGHT);
		turnOrderStatus.setMaxWidth(400);
		turnOrderStatus.setMinWidth(400);
		currentControls.setAlignment(Pos.CENTER);
		currentControls.setPadding(new Insets(10, 10, 30, 10));
		boardView.setAlignment(Pos.CENTER);
		currentInformation.setMaxWidth(400);
		currentInformation.setMinWidth(400);
		currentInformation.setAlignment(Pos.TOP_LEFT);
		currentInformation.setPadding(new Insets(10, 10, 10, 30));

		muteBox.setPadding(new Insets(10,10,10,10));
		helpBox.setPadding(new Insets(10,10,10,10));
		muteBox.setAlignment(Pos.TOP_LEFT);
		helpBox.setAlignment(Pos.TOP_RIGHT);
		Image mute = new Image("./application/media/buttons/mute.png");
		ImageView muteIV = new ImageView(mute);
		muteIV.setCursor(Cursor.HAND);
		muteIV.setFitHeight(50);
		muteIV.setFitWidth(50);
		muteIV.setOnMouseClicked(e -> {
			if(songPlayer.isMute()) {
				songPlayer.setMute(false);
			}
			else {
				songPlayer.setMute(true);
			}
		});
		muteBox.getChildren().add(muteIV);
		Image help = new Image("./application/media/buttons/help.png");
		ImageView helpIV = new ImageView(help);
		helpIV.setCursor(Cursor.HAND);
		helpIV.setFitHeight(50);
		helpIV.setFitWidth(50);
		helpIV.setOnMouseClicked(e -> showHelp());
		helpBox.getChildren().add(helpIV);
		
		showControls();
		updateStatusBar();
		prepareTurns();
		updateCurrentInformation();
		updateBoard();

		if (!twoPlayerMode && View.game.getSecondPlayer().getTeam().contains(View.game.getCurrentChampion())) {
			computerAction();
		}
	}

	// Update the Turn Order Status
	public static void prepareTurns() {
		((Pane) root3.getRight()).getChildren().clear();
		PriorityQueue tmp = new PriorityQueue(q.size());
		Label turnLabel = new Label("Next in Turn: ");
		turnLabel.setTextFill(Color.color(1, 1, 1));
		turnLabel.setFont(new Font("Didot.", 15));
		((Pane) root3.getRight()).getChildren().add(turnLabel);
		while (!q.isEmpty()) {
			Image img = new Image(aliveMap.get((Champion) q.peekMin()));
			ImageView iv = new ImageView(img);
			iv.setFitHeight(60);
			iv.setFitWidth(60);

			((Pane) root3.getRight()).getChildren().add(iv);
			tmp.insert((Champion) q.remove());
		}
		while (!tmp.isEmpty()) {
			q.insert((Champion) tmp.remove());
		}
	}

	// Update Current Champion's Information
	public static void updateCurrentInformation() {
		((Pane) root3.getLeft()).getChildren().clear();
		// Get Current Champion
		Champion champion = View.game.getCurrentChampion();
		
		
		
		// Get Attributes of Current Champion
		String type = "";
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
			championEffects = championEffects.substring(0, championEffects.length() - 2) + ".";
		Label championName = new Label(champion.getName().toUpperCase());
		championName.setStyle("-fx-font-weight: bold");
		championName.setTextAlignment(TextAlignment.CENTER);
		championName.setFont(new Font("Didot.", 20));
		championName.setTextFill(Color.color(1, 1, 1));
		Label championType = new Label("Type: " + type);
		championType.setFont(new Font("Didot.", 15));
		Pane championHP = progressBarWithText("HP", champion.getCurrentHP(), champion.getMaxHP(), "#E71D36");
		Pane championActionPoints = progressBarWithText("Actions Left", champion.getCurrentActionPoints(), 
				champion.getMaxActionPointsPerTurn(), "#FF9F1C");
		
		Pane championMana = progressBarWithText("Mana", champion.getMana(), champion.getMaxMana(), "#2EC4B6");
//		Label championMaxHP = new Label("HP: " + champion.getCurrentHP() + "/" + champion.getMaxHP());
//		championMaxHP.setFont(new Font("Didot.", 15));
//		Label championMana = new Label("Mana: " + champion.getMana() + "");
//		championMana.setFont(new Font("Didot.", 15));
//		Label championActions = new Label(
//				"Actions Points: " + champion.getCurrentActionPoints() + "/" + champion.getMaxActionPointsPerTurn());
//		championActions.setFont(new Font("Didot.", 15));
		Label championSpeed = new Label("Speed: " + champion.getSpeed() + "");
		championSpeed.setFont(new Font("Didot.", 15));
		Label championRange = new Label("Attack Range: " + champion.getAttackRange() + "");
		championRange.setFont(new Font("Didot.", 15));
		Label championDamage = new Label("Attack Damage: " + champion.getAttackDamage() + "");
		championDamage.setFont(new Font("Didot.", 15));
		Label championAppliedEffects = new Label("Applied Effects: " + championEffects);
		championAppliedEffects.setFont(new Font("Didot.", 15));
		Label championCondition = new Label("Condition: " + champion.getCondition());
		championCondition.setFont(new Font("Didot.", 15));
		Region region1 = new Region();
		region1.setMinHeight(15);
		// Get Current Champion's Abilities
		Ability a1 = champion.getAbilities().get(0);
		Ability a2 = champion.getAbilities().get(1);
		Ability a3 = champion.getAbilities().get(2);

		VBox temp = new VBox(5);
		if (twoPlayerMode || !twoPlayerMode && View.game.getFirstPlayer().getTeam().contains(View.game.getCurrentChampion())) {
			Button a1Button = (Button) ((Pane) root3.getBottom()).getChildren().get(0);
			Button a2Button = (Button) ((Pane) root3.getBottom()).getChildren().get(1);
			Button a3Button = (Button) ((Pane) root3.getBottom()).getChildren().get(2);
			Button leaderButton = (Button) ((Pane) root3.getBottom()).getChildren().get(4);

			// First Ability's Attributes
			a1Button.setOnMouseEntered(e -> {
				Label a1Name = new Label("First Ability: " + a1.getName());
				a1Name.setFont(new Font("Didot.", 15));
				String abilityType1 = "";
				String abilityAmount1 = "";
				if (a1 instanceof DamagingAbility) {
					abilityType1 = "Damaging Ability";
					abilityAmount1 = "Damaging amount: " + ((DamagingAbility) a1).getDamageAmount();
				}
				if (a1 instanceof HealingAbility) {
					abilityType1 = "Healing Ability";
					abilityAmount1 = "Healing amount: " + ((HealingAbility) a1).getHealAmount();
				} else if (a1 instanceof CrowdControlAbility) {
					abilityType1 = "Crowd Control Ability";
					abilityAmount1 = "Casted effect: " + ((CrowdControlAbility) a1).getEffect().getName() + "("
							+ ((CrowdControlAbility) a1).getEffect().getDuration() + " turns)";
				}
				Label a1Type = new Label("Type: " + abilityType1);
				a1Type.setFont(new Font("Didot.", 15));
				Label a1Amount = new Label(abilityAmount1);
				a1Amount.setFont(new Font("Didot.", 15));
				Label a1Mana = new Label("Mana Cost: " + a1.getManaCost());
				a1Mana.setFont(new Font("Didot.", 15));
				Label a1Cool = new Label("Cooldown: " + a1.getCurrentCooldown() + "/" + a1.getBaseCooldown());
				a1Cool.setFont(new Font("Didot.", 15));
				Label a1Range = new Label("Range: " + a1.getCastRange());
				a1Range.setFont(new Font("Didot.", 15));
				Label a1Area = new Label("Cast Area: " + a1.getCastArea());
				a1Area.setFont(new Font("Didot.", 15));
				Label a1Action = new Label("Required Action Points: " + a1.getRequiredActionPoints());
				a1Action.setFont(new Font("Didot.", 15));
				temp.getChildren().addAll(a1Name, a1Type, a1Amount, a1Mana, a1Cool, a1Range, a1Area, a1Action);
				for (Node n : temp.getChildren()) {
					if (n instanceof Label)
						((Label) n).setTextFill(Color.color(1, 1, 1));
				}
			});

			// Second Ability's Attributes
			a2Button.setOnMouseEntered(e -> {
				Label a2Name = new Label("Second Ability: " + a2.getName());
				a2Name.setFont(new Font("Didot.", 15));
				String abilityType2 = "";
				String abilityAmount2 = "";
				if (a2 instanceof DamagingAbility) {
					abilityType2 = "Damaging Ability";
					abilityAmount2 = "Damaging amount: " + ((DamagingAbility) a2).getDamageAmount();
				}
				if (a2 instanceof HealingAbility) {
					abilityType2 = "Healing Ability";
					abilityAmount2 = "Healing amount: " + ((HealingAbility) a2).getHealAmount();
				} else if (a2 instanceof CrowdControlAbility) {
					abilityType2 = "Crowd Control Ability";
					abilityAmount2 = "Casted effect: " + ((CrowdControlAbility) a2).getEffect().getName() + "("
							+ ((CrowdControlAbility) a2).getEffect().getDuration() + " turns)";
				}
				Label a2Type = new Label("Type: " + abilityType2);
				a2Type.setFont(new Font("Didot.", 15));
				Label a2Amount = new Label(abilityAmount2);
				a2Amount.setFont(new Font("Didot.", 15));
				Label a2Mana = new Label("Mana Cost: " + a2.getManaCost());
				a2Mana.setFont(new Font("Didot.", 15));
				Label a2Cool = new Label("Cooldown: " + a2.getCurrentCooldown() + "/" + a2.getBaseCooldown());
				a2Cool.setFont(new Font("Didot.", 15));
				Label a2Range = new Label("Range: " + a2.getCastRange());
				a2Range.setFont(new Font("Didot.", 15));
				Label a2Area = new Label("Cast Area: " + a2.getCastArea());
				a2Area.setFont(new Font("Didot.", 15));
				Label a2Action = new Label("Required Action Points: " + a2.getRequiredActionPoints());
				a2Action.setFont(new Font("Didot.", 15));
				temp.getChildren().addAll(a2Name, a2Type, a2Amount, a2Mana, a2Cool, a2Range, a2Area, a2Action);
				for (Node n : temp.getChildren()) {
					if (n instanceof Label)
						((Label) n).setTextFill(Color.color(1, 1, 1));
				}
			});
						
			// Third Ability's Attributes
			a3Button.setOnMouseEntered(e -> {
				Label a3Name = new Label("Third Ability: " + a3.getName());
				a3Name.setFont(new Font("Didot.", 15));
				String abilityType3 = "";
				String abilityAmount3 = "";
				if (a3 instanceof DamagingAbility) {
					abilityType3 = "Damaging Ability";
					abilityAmount3 = "Damaging amount: " + ((DamagingAbility) a3).getDamageAmount();
				}
				if (a3 instanceof HealingAbility) {
					abilityType3 = "Healing Ability";
					abilityAmount3 = "Healing amount: " + ((HealingAbility) a3).getHealAmount();
				} else if (a3 instanceof CrowdControlAbility) {
					abilityType3 = "Crowd Control Ability";
					abilityAmount3 = "Casted effect: " + ((CrowdControlAbility) a3).getEffect().getName() + "("
							+ ((CrowdControlAbility) a3).getEffect().getDuration() + " turns)";
				}
				Label a3Type = new Label("Type: " + abilityType3);
				a3Type.setFont(new Font("Didot.", 15));
				Label a3Amount = new Label(abilityAmount3);
				a3Amount.setFont(new Font("Didot.", 15));
				Label a3Mana = new Label("Mana Cost: " + a3.getManaCost());
				a3Mana.setFont(new Font("Didot.", 15));
				Label a3Cool = new Label("Cooldown: " + a3.getCurrentCooldown() + "/" + a3.getBaseCooldown());
				a3Cool.setFont(new Font("Didot.", 15));
				Label a3Range = new Label("Range: " + a3.getCastRange());
				a3Range.setFont(new Font("Didot.", 15));
				Label a3Area = new Label("Cast Area: " + a3.getCastArea());
				a3Area.setFont(new Font("Didot.", 15));
				Label a3Action = new Label("Required Action Points: " + a3.getRequiredActionPoints());
				a3Action.setFont(new Font("Didot.", 15));
				temp.getChildren().addAll(a3Name, a3Type, a3Amount, a3Mana, a3Cool, a3Range, a3Area, a3Action);
				for (Node n : temp.getChildren()) {
					if (n instanceof Label)
						((Label) n).setTextFill(Color.color(1, 1, 1));
				}
			});

			// Leader Ability's Attributes
			leaderButton.setOnMouseEntered(e -> {
				Champion current = View.game.getCurrentChampion();
				Label leaderAttributes = new Label();
				if (current != View.game.getFirstPlayer().getLeader() && 
						current != View.game.getSecondPlayer().getLeader())
					leaderAttributes.setText("Current champion is not a leader, \n"
							+ "cannot use leader ability!");
				else if (current == View.game.getFirstPlayer().getLeader() && View.game.isFirstLeaderAbilityUsed()
						|| current == View.game.getSecondPlayer().getLeader() && View.game.isSecondLeaderAbilityUsed())
					leaderAttributes.setText("The leader already used their leader ability, \n"
							+ "cannot use it more than once!");
				
				else if (current instanceof Hero) 
					leaderAttributes.setText("Removes all negative effects\n from their team and adds"
							+ "\nan Embrace effect to them which \nlasts for 2 rounds.");
				
				else if (current instanceof AntiHero) 
					leaderAttributes.setText("Stuns all non-leader champions for 2 rounds.");
				
				else if (current instanceof Villain) 
					leaderAttributes.setText("Instantly kills all enemy champions with \nhealth points less "
							+ "than 30% \nof their maximum health points.");
				
				leaderAttributes.setTextFill(Color.color(1, 1, 1));
				leaderAttributes.setFont(new Font("Didot.", 15));
				temp.getChildren().add(leaderAttributes);
				
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
			
			leaderButton.setOnMouseExited(e -> {
				temp.getChildren().clear();
			});
			
			boolean punch = false;
			for (Effect e : View.game.getCurrentChampion().getAppliedEffects()) {
				if (e instanceof Disarm) {
					punch = true;
					break;
				}
			} 
			if (punch && View.game.getCurrentChampion().getAbilities().size() > 3) {
				Button a4Button = (Button) ((Pane) root3.getBottom()).getChildren().get(5);
				Ability a4 = View.game.getCurrentChampion().getAbilities().get(3);
				a4Button.setOnMouseEntered(e -> {
					Label a4Name = new Label("Fourth Ability: " + a4.getName());
					a4Name.setFont(new Font("Didot.", 15));
					String abilityType4 = "";
					String abilityAmount4 = "";
					if (a4 instanceof DamagingAbility) {
						abilityType4 = "Damaging Ability";
						abilityAmount4 = "Damaging amount: " + ((DamagingAbility) a4).getDamageAmount();
					}
					if (a4 instanceof HealingAbility) {
						abilityType4 = "Healing Ability";
						abilityAmount4 = "Healing amount: " + ((HealingAbility) a4).getHealAmount();
					} else if (a4 instanceof CrowdControlAbility) {
						abilityType4 = "Crowd Control Ability";
						abilityAmount4 = "Casted effect: " + ((CrowdControlAbility) a4).getEffect().getName() + "("
								+ ((CrowdControlAbility) a4).getEffect().getDuration() + " turns)";
					}
					Label a4Type = new Label("Type: " + abilityType4);
					a4Type.setFont(new Font("Didot.", 15));
					Label a4Amount = new Label(abilityAmount4);
					a4Amount.setFont(new Font("Didot.", 15));
					Label a4Mana = new Label("Mana Cost: " + a4.getManaCost());
					a4Mana.setFont(new Font("Didot.", 15));
					Label a4Cool = new Label("Cooldown: " + a4.getCurrentCooldown() + "/" + a4.getBaseCooldown());
					a4Cool.setFont(new Font("Didot.", 15));
					Label a4Range = new Label("Range: " + a4.getCastRange());
					a4Range.setFont(new Font("Didot.", 15));
					Label a4Area = new Label("Cast Area: " + a4.getCastArea());
					a4Area.setFont(new Font("Didot.", 15));
					Label a4Action = new Label("Required Action Points: " + a4.getRequiredActionPoints());
					a4Action.setFont(new Font("Didot.", 15));
					temp.getChildren().addAll(a4Name, a4Type, a4Amount, a4Mana, a4Cool, a4Range, a4Area, a4Action);
					for (Node n : temp.getChildren()) {
						if (n instanceof Label)
							((Label) n).setTextFill(Color.color(1, 1, 1));
					}
				});

				a4Button.setOnMouseExited(e -> {
					temp.getChildren().clear();
				});
			}
		}

		// Configuring Nodec
		((Pane) root3.getLeft()).getChildren().addAll(championName, championHP, championActionPoints, championMana,
				 championType, championSpeed, championRange, championDamage, championAppliedEffects,
				championCondition, region1, temp);
		for (Node n : ((Pane) root3.getLeft()).getChildren()) {
			if (n instanceof Label)
				((Label) n).setTextFill(Color.color(1, 1, 1));
		}

	}

	// Update the Status of Players' Champions and Leader Ability
	public static void updateStatusBar() {
		BorderPane gameStatus = ((BorderPane) root3.getTop());
		HBox statsBar = (HBox) gameStatus.getCenter();
		statsBar.getChildren().clear();
		Label player1Name = new Label(View.game.getFirstPlayer().getName());
		player1Name.setFont(new Font("Didot.", 16));
		statsBar.getChildren().add(player1Name);

		for (Champion c : View.game.getFirstPlayer().getTeam()) {
			Image image = new Image(aliveMap.get(c));
			if (c.getCurrentHP() == 0)
				image = new Image(deadMap.get(c));
			ImageView iv = new ImageView(image);
			iv.setFitHeight(80);
			iv.setFitWidth(80);
			statsBar.getChildren().add(iv);
		}

		Image LeaderAbilityNotUsed = new Image("./application/media/pow.jpeg");
		Image LeaderAbilityUsed = new Image("./application/media/powd.jpeg");
		ImageView firstLeaderAbility = new ImageView();
		ImageView secondLeaderAbility = new ImageView();
		if (!View.game.isFirstLeaderAbilityUsed())
			firstLeaderAbility = new ImageView(LeaderAbilityNotUsed);
		if (View.game.isFirstLeaderAbilityUsed())
			firstLeaderAbility = new ImageView(LeaderAbilityUsed);
		if (!View.game.isSecondLeaderAbilityUsed())
			secondLeaderAbility = new ImageView(LeaderAbilityNotUsed);
		if (View.game.isSecondLeaderAbilityUsed())
			secondLeaderAbility = new ImageView(LeaderAbilityUsed);

		Region r = new Region();
		r.setMinWidth(100);
		firstLeaderAbility.setFitHeight(80);
		firstLeaderAbility.setFitWidth(80);
		secondLeaderAbility.setFitHeight(80);
		secondLeaderAbility.setFitWidth(80);
		statsBar.getChildren().addAll(firstLeaderAbility, r, secondLeaderAbility);

		for (Champion c : View.game.getSecondPlayer().getTeam()) {
			Image image = new Image(aliveMap.get(c));
			if (c.getCurrentHP() == 0)
				image = new Image(deadMap.get(c));
			ImageView iv = new ImageView(image);
			iv.setFitHeight(80);
			iv.setFitWidth(80);
			statsBar.getChildren().add(iv);
		}
		Label player2Name = new Label(View.game.getSecondPlayer().getName());
		player2Name.setFont(new Font("Didot.", 16));
		player1Name.setTextFill(Color.color(1, 1, 1));
		player2Name.setTextFill(Color.color(1, 1, 1));
		statsBar.getChildren().add(player2Name);
	}

	public static void updateBoard() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				boolean isCurrent1 = false;
				boolean isCurrent2 = false;
				Button btn = new Button();
				btn.setMinHeight(100);
				btn.setMinWidth(100);
				btn.setMaxHeight(100);
				btn.setMaxWidth(100);
				
				if (View.game.getBoard()[i][j] instanceof Cover) {
					Cover cover = (Cover)(View.game.getBoard()[i][j]);
					btn.setTooltip(new Tooltip("Cover's health: " + ((Cover)View.game.getBoard()[i][j]).getCurrentHP() 
							+ "\nDistance: " + (int)
							(Math.abs(View.game.getCurrentChampion().getLocation().x - cover.getLocation().getX()) + 
							Math.abs(View.game.getCurrentChampion().getLocation().y - cover.getLocation().getY()))));
					Image img = new Image("./application/media/glass1.jpeg");
					if (cover.getCurrentHP() < 600 && cover.getCurrentHP() >= 300) {
						img = new Image("./application/media/glass2.jpeg");
					}
					else if (cover.getCurrentHP() < 300 && cover.getCurrentHP() >= 0) {
						img = new Image("./application/media/glass3.jpeg");
					}
					ImageView iv = new ImageView(img);
					iv.setFitHeight(90);
					iv.setFitWidth(90);
					btn.setGraphic(iv);
				}

				else if (View.game.getBoard()[i][j] instanceof Champion) {
					Champion c = (Champion) View.game.getBoard()[i][j];
					btn.setTooltip(new Tooltip("HP: " + c.getCurrentHP() + "/" + c.getMaxHP() + "\nDistance: " + (int)
							(Math.abs(View.game.getCurrentChampion().getLocation().x - c.getLocation().getX()) + 
							Math.abs(View.game.getCurrentChampion().getLocation().y - c.getLocation().getY()))));	
					Image img = new Image(aliveMap.get(c));
					ImageView iv = new ImageView(img);
					iv.setFitHeight(90);
					iv.setFitWidth(90);
					btn.setGraphic(iv);
					Champion current = View.game.getCurrentChampion();
					if (c == current && View.game.getFirstPlayer().getTeam().contains(current)) {
//						btn.setStyle("-fx-background-color: #010098;");
						isCurrent1 = true;
					} else if (c == current && View.game.getSecondPlayer().getTeam().contains(current)) {
//						btn.setStyle("-fx-background-color: #9a0000; ");
						isCurrent2 = true;
					}
					btn.setOnAction(e -> {
						putGlowAnimation(btn ,Color.BLUE);
						putFadeAnimation(btn);
						Stage currentHealth = new Stage();
						String type = "";
						if (c instanceof Hero)
							type = "Hero";
						else if (c instanceof AntiHero)
							type = "AntiHero";
						else
							type = "Villain";
						currentHealth.setTitle(c.getName() + " (" + type + ")");
						VBox window = new VBox(10);
						window.setAlignment(Pos.CENTER);
						Scene scene = new Scene(window);
						Button OK = new Button("OK");
						OK.setOnAction(ee -> currentHealth.close());
						currentHealth.setScene(scene);
						currentHealth.setMinWidth(400);
						currentHealth.setMinHeight(200);
						Text teamText;
						if (View.game.getFirstPlayer().getTeam().contains(c))
							teamText = new Text("Belonging to first team");
						else
							teamText = new Text("Belonging to second team");
						Text healthText = new Text("Champion's health: " + c.getCurrentHP() + "/" + c.getMaxHP());
						Text conditionText = new Text("Champion's condition: " + c.getCondition());
						String effects = "";
						for (Effect effect : c.getAppliedEffects()) {
							effects += effect.getName() + "(" + effect.getDuration() + "), ";
						}
						if (effects.length() >= 2)
							effects = effects.substring(0, effects.length() - 2);
						Text effectsText = new Text("Effects on Champion: " + effects);
						Text otherText = new Text("Mana: " + c.getMana() + ", " + "Speed: " + c.getSpeed() + ", \n"
								+ "Max Actions per Turn: " + c.getMaxActionPointsPerTurn() + ", \n" + "Attack Range: "
								+ c.getAttackRange() + ", " + "Attack Damage: " + c.getAttackDamage() + ".");
						otherText.setTextAlignment(TextAlignment.CENTER);
						Text leaderText = new Text("Champion is NOT a leader.");
						if (View.game.getFirstPlayer().getLeader() == c || View.game.getSecondPlayer().getLeader() == c)
							leaderText = new Text("Champion is a leader");
						window.getChildren().addAll(teamText, healthText, conditionText, effectsText, otherText,
								leaderText, OK);
						window.setPadding(new Insets(10, 10, 10, 10));
						currentHealth.show();
					});
				}

				if (!isCurrent1 && !isCurrent2)
					btn.setStyle("-fx-border-color: #ddd ; -fx-background-color: #09072d");
				else if (isCurrent1) {
					btn.setStyle("-fx-border-color: #ddd ; -fx-background-color: #010098");
				} else if (isCurrent2) {
					btn.setStyle("-fx-border-color: #ddd ; -fx-background-color: #9a0000");
				}
				((GridPane) root3.getCenter()).add(btn, j, 4 - i);
			}
		}
	}
	
	public static void updateBoard(Damageable attackTarget) {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				StackPane sp = new StackPane();
				boolean isCurrent1 = false;
				boolean isCurrent2 = false;
				Button btn = new Button();
				sp.getChildren().add(btn);
				btn.setMinHeight(100);
				btn.setMinWidth(100);
				btn.setMaxHeight(100);
				btn.setMaxWidth(100);
				
				if (View.game.getBoard()[i][j] instanceof Cover) {
					Cover cover = (Cover)(View.game.getBoard()[i][j]);
					btn.setTooltip(new Tooltip("Cover's health: " + ((Cover)View.game.getBoard()[i][j]).getCurrentHP() 
							+ "\nDistance: " + (int)
							(Math.abs(View.game.getCurrentChampion().getLocation().x - cover.getLocation().getX()) + 
							Math.abs(View.game.getCurrentChampion().getLocation().y - cover.getLocation().getY()))));
					Image img = new Image("./application/media/glass1.jpeg");
					if (cover.getCurrentHP() < 600 && cover.getCurrentHP() >= 300) {
						img = new Image("./application/media/glass2.jpeg");
					}
					else if (cover.getCurrentHP() < 300 && cover.getCurrentHP() >= 0) {
						img = new Image("./application/media/glass3.jpeg");
					}
					ImageView iv = new ImageView(img);
					iv.setFitHeight(90);
					iv.setFitWidth(90);
					btn.setGraphic(iv);
					ImageView ivp = null;
					if (View.game.getBoard()[i][j] == attackTarget) {
						Image pow = new Image("/application/media/animation/pow.jpeg");
						ivp = new ImageView(pow);
						ivp.setFitHeight(100);
						ivp.setFitWidth(100);
						sp.getChildren().add(ivp);
						PauseTransition pause = new PauseTransition(Duration.seconds(2));
						pause.play();
						ImageView ivp2 = ivp;
						pause.setOnFinished(e -> sp.getChildren().remove(ivp2));
					}
				}

				else if (View.game.getBoard()[i][j] instanceof Champion) {
					Champion c = (Champion) View.game.getBoard()[i][j];
					btn.setTooltip(new Tooltip("HP: " + c.getCurrentHP() + "/" + c.getMaxHP() + "\nDistance: " + (int)
							(Math.abs(View.game.getCurrentChampion().getLocation().x - c.getLocation().getX()) + 
							Math.abs(View.game.getCurrentChampion().getLocation().y - c.getLocation().getY()))));					Image img = new Image(aliveMap.get(c));
					ImageView iv = new ImageView(img);
					iv.setFitHeight(90);
					iv.setFitWidth(90);
					btn.setGraphic(iv);
					Champion current = View.game.getCurrentChampion();
					if (c == current && View.game.getFirstPlayer().getTeam().contains(current)) {
//						btn.setStyle("-fx-background-color: #010098;");
						isCurrent1 = true;
					} else if (c == current && View.game.getSecondPlayer().getTeam().contains(current)) {
//						btn.setStyle("-fx-background-color: #9a0000; ");
						isCurrent2 = true;
					}
					
					ImageView ivp = null;
					if (c == attackTarget) {
						Image pow = new Image("/application/media/animation/pow.jpeg");
						ivp = new ImageView(pow);
						ivp.setFitHeight(100);
						ivp.setFitWidth(100);
						sp.getChildren().add(ivp);
						PauseTransition pause = new PauseTransition(Duration.seconds(2));
						pause.play();
						ImageView ivp2 = ivp;
						pause.setOnFinished(e -> sp.getChildren().remove(ivp2));
					}
					
					btn.setOnAction(e -> {
						Stage currentHealth = new Stage();
						String type = "";
						if (c instanceof Hero)
							type = "Hero";
						else if (c instanceof AntiHero)
							type = "AntiHero";
						else
							type = "Villain";
						currentHealth.setTitle(c.getName() + " (" + type + ")");
						VBox window = new VBox(10);
						window.setAlignment(Pos.CENTER);
						Scene scene = new Scene(window);
						Button OK = new Button("OK");
						OK.setOnAction(ee -> currentHealth.close());
						currentHealth.setScene(scene);
						currentHealth.setMinWidth(400);
						currentHealth.setMinHeight(200);
						Text teamText;
						if (View.game.getFirstPlayer().getTeam().contains(c))
							teamText = new Text("Belonging to first team");
						else
							teamText = new Text("Belonging to second team");
						Text healthText = new Text("Champion's health: " + c.getCurrentHP() + "/" + c.getMaxHP());
						Text conditionText = new Text("Champion's condition: " + c.getCondition());
						String effects = "";
						for (Effect effect : c.getAppliedEffects()) {
							effects += effect.getName() + "(" + effect.getDuration() + "), ";
						}
						if (effects.length() >= 2)
							effects = effects.substring(0, effects.length() - 2);
						Text effectsText = new Text("Effects on Champion: " + effects);
						Text otherText = new Text("Mana: " + c.getMana() + ", " + "Speed: " + c.getSpeed() + ", \n"
								+ "Max Actions per Turn: " + c.getMaxActionPointsPerTurn() + ", \n" + "Attack Range: "
								+ c.getAttackRange() + ", " + "Attack Damage: " + c.getAttackDamage() + ".");
						otherText.setTextAlignment(TextAlignment.CENTER);
						Text leaderText = new Text("Champion is NOT a leader.");
						if (View.game.getFirstPlayer().getLeader() == c || View.game.getSecondPlayer().getLeader() == c)
							leaderText = new Text("Champion is a leader");
						window.getChildren().addAll(teamText, healthText, conditionText, effectsText, otherText,
								leaderText, OK);
						window.setPadding(new Insets(10, 10, 10, 10));
						currentHealth.show();
					});
				}

				if (!isCurrent1 && !isCurrent2)
					btn.setStyle("-fx-border-color: #ddd ; -fx-background-color: #09072d");
				else if (isCurrent1) {
					btn.setStyle("-fx-border-color: #ddd ; -fx-background-color: #010098");
				} else if (isCurrent2) {
					btn.setStyle("-fx-border-color: #ddd ; -fx-background-color: #9a0000");
				}
				((GridPane) root3.getCenter()).add(sp, j, 4 - i);
			}
		}
	}
	
	public static void updateBoard(ArrayList<Damageable> targets, Ability ability) {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				StackPane sp = new StackPane();
				boolean isCurrent1 = false;
				boolean isCurrent2 = false;
				Button btn = new Button();
				sp.getChildren().add(btn);
				btn.setMinHeight(100);
				btn.setMinWidth(100);
				btn.setMaxHeight(100);
				btn.setMaxWidth(100);
				
				if (View.game.getBoard()[i][j] instanceof Cover) {
					Cover cover = (Cover)(View.game.getBoard()[i][j]);
					btn.setTooltip(new Tooltip("Cover's health: " + ((Cover)View.game.getBoard()[i][j]).getCurrentHP() 
							+ "\nDistance: " + (int)
							(Math.abs(View.game.getCurrentChampion().getLocation().x - cover.getLocation().getX()) + 
							Math.abs(View.game.getCurrentChampion().getLocation().y - cover.getLocation().getY()))));
					Image img = new Image("./application/media/glass1.jpeg");
					if (cover.getCurrentHP() < 600 && cover.getCurrentHP() >= 300) {
						img = new Image("./application/media/glass2.jpeg");
					}
					else if (cover.getCurrentHP() < 300 && cover.getCurrentHP() >= 0) {
						img = new Image("./application/media/glass3.jpeg");
					}
					ImageView iv = new ImageView(img);
					iv.setFitHeight(90);
					iv.setFitWidth(90);
					btn.setGraphic(iv);
					ImageView ivp = null;
					if (targets.contains(View.game.getBoard()[i][j])) {
						if (ability instanceof HealingAbility) {
							Image plus = new Image("./application/media/animation/plus.jpeg");
							ivp = new ImageView(plus);
							ivp.setFitHeight(100);
							ivp.setFitWidth(100);
							sp.getChildren().add(ivp);
							PauseTransition pause = new PauseTransition(Duration.seconds(2));
							pause.play();
							ImageView ivp2 = ivp;
							pause.setOnFinished(e -> sp.getChildren().remove(ivp2));
						}
						else if (ability instanceof DamagingAbility) {
							Image minus = new Image("./application/media/animation/minus.jpeg");
							ivp = new ImageView(minus);
							ivp.setFitHeight(100);
							ivp.setFitWidth(100);
							sp.getChildren().add(ivp);
							PauseTransition pause = new PauseTransition(Duration.seconds(2));
							pause.play();
							ImageView ivp2 = ivp;
							pause.setOnFinished(e -> sp.getChildren().remove(ivp2));
						}
						
//						ivp.setFitHeight(100);
//						ivp.setFitWidth(100);
//						sp.getChildren().add(ivp);
//						PauseTransition pause = new PauseTransition(Duration.seconds(2));
//						pause.play();
//						ImageView ivp2 = ivp;
//						pause.setOnFinished(e -> sp.getChildren().remove(ivp2));
					}
				}

				else if (View.game.getBoard()[i][j] instanceof Champion) {
					Champion c = (Champion) View.game.getBoard()[i][j];
					btn.setTooltip(new Tooltip("HP: " + c.getCurrentHP() + "/" + c.getMaxHP() + "\nDistance: " + (int)
							(Math.abs(View.game.getCurrentChampion().getLocation().x - c.getLocation().getX()) + 
							Math.abs(View.game.getCurrentChampion().getLocation().y - c.getLocation().getY()))));					Image img = new Image(aliveMap.get(c));
					ImageView iv = new ImageView(img);
					iv.setFitHeight(90);
					iv.setFitWidth(90);
					btn.setGraphic(iv);
					Champion current = View.game.getCurrentChampion();
					if (c == current && View.game.getFirstPlayer().getTeam().contains(current)) {
//						btn.setStyle("-fx-background-color: #010098;");
						isCurrent1 = true;
					} else if (c == current && View.game.getSecondPlayer().getTeam().contains(current)) {
//						btn.setStyle("-fx-background-color: #9a0000; ");
						isCurrent2 = true;
					}
					
					ImageView ivp = null;
					if (targets.contains(View.game.getBoard()[i][j])) {
						if (ability instanceof HealingAbility) {
							Image plus = new Image("./application/media/animation/plus.jpeg");
							ivp = new ImageView(plus);
							ivp.setFitHeight(100);
							ivp.setFitWidth(100);
							sp.getChildren().add(ivp);
							PauseTransition pause = new PauseTransition(Duration.seconds(2));
							pause.play();
							ImageView ivp2 = ivp;
							pause.setOnFinished(e -> sp.getChildren().remove(ivp2));
						}
						if (ability instanceof DamagingAbility) {
							Image minus = new Image("./application/media/animation/minus.jpeg");
							ivp = new ImageView(minus);
							ivp.setFitHeight(100);
							ivp.setFitWidth(100);
							sp.getChildren().add(ivp);
							PauseTransition pause = new PauseTransition(Duration.seconds(2));
							pause.play();
							ImageView ivp2 = ivp;
							pause.setOnFinished(e -> sp.getChildren().remove(ivp2));
						}
						if (ability instanceof CrowdControlAbility) {
							Image abilityImage = new Image("./application/media/animation/minus.jpeg");
							switch(((CrowdControlAbility) ability).getEffect().getName()) {
							case "Disarm": abilityImage = new Image("./application/media/animation/disarm.png"); break;
							case "Dodge": abilityImage = new Image("./application/media/animation/dodge.png"); break;
							case "Embrace": abilityImage = new Image("./application/media/animation/embrace.png"); break;
							case "PowerUp": abilityImage = new Image("./application/media/animation/powerUp.png"); break;
							case "Root": abilityImage = new Image("./application/media/animation/root.png"); break;
							case "Shield": abilityImage = new Image("./application/media/animation/shield.png"); break;
							case "Shock": abilityImage = new Image("./application/media/animation/shock.png"); break;
							case "Silence": abilityImage = new Image("./application/media/animation/silence.png"); break;
							case "SpeedUp": abilityImage = new Image("./application/media/animation/speedUp.png"); break;
							case "Stun": abilityImage = new Image("./application/media/animation/stun.png"); break;
							}
							
							ivp = new ImageView(abilityImage);
							ivp.setFitHeight(100);
							ivp.setFitWidth(100);
							sp.getChildren().add(ivp);
							PauseTransition pause = new PauseTransition(Duration.seconds(2));
							pause.play();
							ImageView ivp2 = ivp;
							pause.setOnFinished(e -> sp.getChildren().remove(ivp2));
						}
//						ivp.setFitHeight(100);
//						ivp.setFitWidth(100);
//						sp.getChildren().add(ivp);
//						PauseTransition pause = new PauseTransition(Duration.seconds(2));
//						pause.play();
//						ImageView ivp2 = ivp;
//						pause.setOnFinished(e -> sp.getChildren().remove(ivp2));
					}
					
					btn.setOnAction(e -> {
						Stage currentHealth = new Stage();
						String type = "";
						if (c instanceof Hero)
							type = "Hero";
						else if (c instanceof AntiHero)
							type = "AntiHero";
						else
							type = "Villain";
						currentHealth.setTitle(c.getName() + " (" + type + ")");
						VBox window = new VBox(10);
						window.setAlignment(Pos.CENTER);
						Scene scene = new Scene(window);
						Button OK = new Button("OK");
						OK.setOnAction(ee -> currentHealth.close());
						currentHealth.setScene(scene);
						currentHealth.setMinWidth(400);
						currentHealth.setMinHeight(200);
						Text teamText;
						if (View.game.getFirstPlayer().getTeam().contains(c))
							teamText = new Text("Belonging to first team");
						else
							teamText = new Text("Belonging to second team");
						Text healthText = new Text("Champion's health: " + c.getCurrentHP() + "/" + c.getMaxHP());
						Text conditionText = new Text("Champion's condition: " + c.getCondition());
						String effects = "";
						for (Effect effect : c.getAppliedEffects()) {
							effects += effect.getName() + "(" + effect.getDuration() + "), ";
						}
						if (effects.length() >= 2)
							effects = effects.substring(0, effects.length() - 2);
						Text effectsText = new Text("Effects on Champion: " + effects);
						Text otherText = new Text("Mana: " + c.getMana() + ", " + "Speed: " + c.getSpeed() + ", \n"
								+ "Max Actions per Turn: " + c.getMaxActionPointsPerTurn() + ", \n" + "Attack Range: "
								+ c.getAttackRange() + ", " + "Attack Damage: " + c.getAttackDamage() + ".");
						otherText.setTextAlignment(TextAlignment.CENTER);
						Text leaderText = new Text("Champion is NOT a leader.");
						if (View.game.getFirstPlayer().getLeader() == c || View.game.getSecondPlayer().getLeader() == c)
							leaderText = new Text("Champion is a leader");
						window.getChildren().addAll(teamText, healthText, conditionText, effectsText, otherText,
								leaderText, OK);
						window.setPadding(new Insets(10, 10, 10, 10));
						currentHealth.show();
					});
				}

				if (!isCurrent1 && !isCurrent2)
					btn.setStyle("-fx-border-color: #ddd ; -fx-background-color: #09072d");
				else if (isCurrent1) {
					btn.setStyle("-fx-border-color: #ddd ; -fx-background-color: #010098");
				} else if (isCurrent2) {
					btn.setStyle("-fx-border-color: #ddd ; -fx-background-color: #9a0000");
				}
				((GridPane) root3.getCenter()).add(sp, j, 4 - i);
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
		OK.setOnAction(e -> exception.close());
		exception.setScene(scene);
		exception.setMinWidth(400);
		exception.setMinHeight(200);
		Text msgText = new Text(msg);
		window.getChildren().addAll(msgText, OK);
		window.setPadding(new Insets(10, 10, 10, 10));
		exception.show();
	}

	// Show controls only if player's turn
	public static void showControls() {
		((Pane) root3.getBottom()).getChildren().clear();
		Champion current = View.game.getCurrentChampion();
		ArrayList<Button> actions = new ArrayList<>();
		
//		Label actionsLeft = new Label("ACTIONS\nLEFT: " + current.getCurrentActionPoints());
//		HBox actionsLeftBox = new HBox();
//		actionsLeftBox.getChildren().add(actionsLeft);
//		actionsLeftBox.setAlignment(Pos.CENTER_LEFT);
//		actionsLeft.setTextAlignment(TextAlignment.LEFT);
//		actionsLeft.setTextFill(Color.color(1, 1, 1));
//		actionsLeft.setFont(new Font("Didot.", 16));
		
		Button btnAbility1 = new Button("First\nAbility");
		if (current.getAbilities().get(0) instanceof HealingAbility) {
			btnAbility1.setStyle("-fx-background-radius: 5em; -fx-background-color: #246603; -fx-text-fill: #fff");
		} else if (current.getAbilities().get(0) instanceof DamagingAbility) {
			btnAbility1.setStyle("-fx-background-radius: 5em; -fx-background-color: #272635; -fx-text-fill: #fff");
		} else {
			btnAbility1.setStyle("-fx-background-radius: 5em; -fx-background-color: #144878; -fx-text-fill: #fff");
		}
		btnAbility1.setTextAlignment(TextAlignment.CENTER);
		btnAbility1.setOnAction(e -> castAbility(0));
		btnAbility1.setMinHeight(70);
		btnAbility1.setMaxHeight(70);
		btnAbility1.setMinWidth(70);
		btnAbility1.setMaxWidth(70);

		Button btnAbility2 = new Button("Second\nAbility");
		if (current.getAbilities().get(1) instanceof HealingAbility) {
			btnAbility2.setStyle("-fx-background-radius: 5em; -fx-background-color: #246603; -fx-text-fill: #fff");
		} else if (current.getAbilities().get(1) instanceof DamagingAbility) {
			btnAbility2.setStyle("-fx-background-radius: 5em; -fx-background-color: #272635; -fx-text-fill: #fff");
		} else {
			btnAbility2.setStyle("-fx-background-radius: 5em; -fx-background-color: #144878; -fx-text-fill: #fff");
		}
		btnAbility2.setTextAlignment(TextAlignment.CENTER);
		btnAbility2.setOnAction(e -> castAbility(1));
		btnAbility2.setMinHeight(70);
		btnAbility2.setMaxHeight(70);
		btnAbility2.setMinWidth(70);
		btnAbility2.setMaxWidth(70);

		Button btnAbility3 = new Button("Third\nAbility");
		if (current.getAbilities().get(2) instanceof HealingAbility) {
			btnAbility3.setStyle("-fx-background-radius: 5em; -fx-background-color: #246603; -fx-text-fill: #fff");
		} else if (current.getAbilities().get(2) instanceof DamagingAbility) {
			btnAbility3.setStyle("-fx-background-radius: 5em; -fx-background-color: #272635; -fx-text-fill: #fff");
		} else {
			btnAbility3.setStyle("-fx-background-radius: 5em; -fx-background-color: #144878; -fx-text-fill: #fff");
		}
		btnAbility3.setTextAlignment(TextAlignment.CENTER);
		btnAbility3.setOnAction(e -> castAbility(2));
		btnAbility3.setMinHeight(70);
		btnAbility3.setMaxHeight(70);
		btnAbility3.setMinWidth(70);
		btnAbility3.setMaxWidth(70);

		
		
		
		
		Button btnEndTurn = new Button("END TURN");
		btnEndTurn.setStyle("-fx-background-radius: 1em; -fx-background-color: #AD343E; -fx-text-fill: #fff");
		btnEndTurn.setOnAction(e -> endTurn());
		btnEndTurn.setMinHeight(70);
		btnEndTurn.setMaxHeight(70);
		btnEndTurn.setMinWidth(170);
		btnEndTurn.setMaxWidth(170);

		Button btnLeaderAbility = new Button("Leader\nAbility");
		btnLeaderAbility.setStyle("-fx-background-radius: 1em;");
		btnLeaderAbility.setOnAction(e -> useLeaderAbility());
		btnLeaderAbility.setMinHeight(70);
		btnLeaderAbility.setMaxHeight(70);
		btnLeaderAbility.setMinWidth(70);

		Button btnPunch = new Button("Punch");
		btnPunch.setStyle("-fx-background-radius: 1em;");
		btnPunch.setOnAction(e -> castAbility(3));
		btnPunch.setMinHeight(70);
		btnPunch.setMaxHeight(70);
		btnPunch.setMinWidth(70);
		btnPunch.setMaxWidth(70);

		BorderPane attackOptions = new BorderPane();
		Region r = new Region();
		r.setMinHeight(30);
		r.setMaxHeight(30);
		r.setMinWidth(30);
		r.setMaxWidth(30);
		attackOptions.setCenter(r);
		
		Image attackUpImage = new Image("./application/media/buttons/attackUP.png");
		ImageView attackUpIV = new ImageView(attackUpImage);
		attackUpIV.setOnMouseClicked(e -> attackUp());
		attackUpIV.setCursor(Cursor.HAND);
		attackUpIV.setFitHeight(30);
		attackUpIV.setFitWidth(30);
		HBox box1 = new HBox();
		box1.getChildren().add(attackUpIV);
		box1.setAlignment(Pos.CENTER);
		attackOptions.setTop(box1);
		
		Image attackDownImage = new Image("./application/media/buttons/attackDOWN.png");
		ImageView attackDownIV = new ImageView(attackDownImage);
		attackDownIV.setOnMouseClicked(e -> attackDown());
		attackDownIV.setCursor(Cursor.HAND);
		attackDownIV.setFitHeight(30);
		attackDownIV.setFitWidth(30);
		HBox box2 = new HBox();
		box2.getChildren().add(attackDownIV);
		box2.setAlignment(Pos.CENTER);
		attackOptions.setBottom(box2);
		
		Image attackRightImage = new Image("./application/media/buttons/attackRIGHT.png");
		ImageView attackRightIV = new ImageView(attackRightImage);
		attackRightIV.setOnMouseClicked(e -> attackRight());
		attackRightIV.setCursor(Cursor.HAND);
		attackRightIV.setFitHeight(30);
		attackRightIV.setFitWidth(30);
		HBox box3 = new HBox();
		box3.getChildren().add(attackRightIV);
		box3.setAlignment(Pos.CENTER);
		attackOptions.setRight(box3);
		
		Image attackLeftImage = new Image("./application/media/buttons/attackLEFT.png");
		ImageView attackLeftIV = new ImageView(attackLeftImage);
		attackLeftIV.setOnMouseClicked(e -> attackLeft());
		attackLeftIV.setCursor(Cursor.HAND);
		attackLeftIV.setFitHeight(30);
		attackLeftIV.setFitWidth(30);
		HBox box4 = new HBox();
		box4.getChildren().add(attackLeftIV);
		box4.setAlignment(Pos.CENTER);
		attackOptions.setLeft(box4);
		
		
//		Button btnAttackUp = new Button();
//		btnAttackUp.setStyle("-fx-background-radius: 5em;");
//		btnAttackUp.setOnAction(e -> attackUp());
//		btnAttackUp.setMinHeight(30);
//		btnAttackUp.setMaxHeight(30);
//		btnAttackUp.setMinWidth(30);
//		btnAttackUp.setMaxWidth(30);
//		HBox box1 = new HBox();
//		box1.getChildren().add(btnAttackUp);
//		box1.setAlignment(Pos.CENTER);
//		attackOptions.setTop(box1);
//
//		Button btnAttackDown = new Button();
//		btnAttackDown.setStyle("-fx-background-radius: 5em;");
//		btnAttackDown.setOnAction(e -> attackDown());
//		btnAttackDown.setMinHeight(30);
//		btnAttackDown.setMaxHeight(30);
//		btnAttackDown.setMinWidth(30);
//		btnAttackDown.setMaxWidth(30);
//		attackOptions.setBottom(btnAttackDown);
//		HBox box2 = new HBox();
//		box2.getChildren().add(btnAttackDown);
//		box2.setAlignment(Pos.CENTER);
//		attackOptions.setBottom(box2);
//		
//		Button btnAttackRight = new Button();
//		btnAttackRight.setStyle("-fx-background-radius: 5em;");
//		btnAttackRight.setOnAction(e -> attackRight());
//		btnAttackRight.setMinHeight(30);
//		btnAttackRight.setMaxHeight(30);
//		btnAttackRight.setMinWidth(30);
//		btnAttackRight.setMaxWidth(30);
//		HBox box3 = new HBox();
//		box3.getChildren().add(btnAttackRight);
//		box3.setAlignment(Pos.CENTER);
//		attackOptions.setRight(box3);
//		
//		Button btnAttackLeft = new Button();
//		btnAttackLeft.setStyle("-fx-background-radius: 5em;");
//		btnAttackLeft.setOnAction(e -> attackLeft());
//		btnAttackLeft.setMinHeight(30);
//		btnAttackLeft.setMaxHeight(30);
//		btnAttackLeft.setMinWidth(30);
//		btnAttackLeft.setMaxWidth(30);
//		HBox box4 = new HBox();
//		box4.getChildren().add(btnAttackLeft);
//		box4.setAlignment(Pos.CENTER);
//		attackOptions.setLeft(box4);
		
		BorderPane moveOptions = new BorderPane();
		Region r1 = new Region();
		r1.setMinHeight(30);
		r1.setMaxHeight(30);
		r1.setMinWidth(30);
		r1.setMaxWidth(30);
		moveOptions.setCenter(r1);


		Image moveUpImage = new Image("./application/media/buttons/UP.png");
		ImageView moveUpIV = new ImageView(moveUpImage);
		moveUpIV.setOnMouseClicked(e -> moveUp());
		moveUpIV.setCursor(Cursor.HAND);
		moveUpIV.setFitHeight(30);
		moveUpIV.setFitWidth(30);
		HBox box5 = new HBox();
		box5.getChildren().add(moveUpIV);
		box5.setAlignment(Pos.CENTER);
		moveOptions.setTop(box5);
		
		Image moveDownImage = new Image("./application/media/buttons/DOWN.png");
		ImageView moveDownIV = new ImageView(moveDownImage);
		moveDownIV.setOnMouseClicked(e -> moveDown());
		moveDownIV.setCursor(Cursor.HAND);
		moveDownIV.setFitHeight(30);
		moveDownIV.setFitWidth(30);
		HBox box6 = new HBox();
		box6.getChildren().add(moveDownIV);
		box6.setAlignment(Pos.CENTER);
		moveOptions.setBottom(box6);
		
		Image moveRightImage = new Image("./application/media/buttons/RIGHT.png");
		ImageView moveRightIV = new ImageView(moveRightImage);
		moveRightIV.setOnMouseClicked(e -> moveRight());
		moveRightIV.setCursor(Cursor.HAND);
		moveRightIV.setFitHeight(30);
		moveRightIV.setFitWidth(30);
		HBox box7 = new HBox();
		box7.getChildren().add(moveRightIV);
		box7.setAlignment(Pos.CENTER);
		moveOptions.setRight(box7);
		
		Image moveLeftImage = new Image("./application/media/buttons/LEFT.png");
		ImageView moveLeftIV = new ImageView(moveLeftImage);
		moveLeftIV.setOnMouseClicked(e -> moveLeft());
		moveLeftIV.setCursor(Cursor.HAND);
		moveLeftIV.setFitHeight(30);
		moveLeftIV.setFitWidth(30);
		HBox box8 = new HBox();
		box8.getChildren().add(moveLeftIV);
		box8.setAlignment(Pos.CENTER);
		moveOptions.setLeft(box8);
		
//		Button btnMoveUp = new Button();
//			btnMoveUp.setStyle("-fx-background-radius: 5em;");
//		ImageView iv1 = new ImageView(new Image("/application/media/moveUp.jpeg"));
//		iv1.setFitHeight(30);
//		iv1.setFitWidth(30);
//			circle.setFill(iv1);
//			iv1.setClip();
//		btnMoveUp.setGraphic(iv1);
//		btnMoveUp.setStyle("-fx-background-radius: 5em;");
//		btnMoveUp.setOnAction(e -> moveUp());
//		btnMoveUp.setMinHeight(30);
//		btnMoveUp.setMaxHeight(30);
//		btnMoveUp.setMinWidth(30);
//		btnMoveUp.setMaxWidth(30);
//		moveOptions.setTop(btnMoveUp);
//		HBox box5 = new HBox();
//		box5.getChildren().add(btnMoveUp);
//		box5.setAlignment(Pos.CENTER);
//		moveOptions.setTop(box5);

//		Button btnMoveDown = new Button();
//		btnMoveDown.setStyle("-fx-background-radius: 5em;");
//		btnMoveDown.setOnAction(e -> moveDown());
//		btnMoveDown.setMinHeight(30);
//		btnMoveDown.setMaxHeight(30);
//		btnMoveDown.setMinWidth(30);
//		btnMoveDown.setMaxWidth(30);
//		HBox box6 = new HBox();
//		box6.getChildren().add(btnMoveDown);
//		box6.setAlignment(Pos.CENTER);
//		moveOptions.setBottom(box6);
//
//		Button btnMoveRight = new Button();
//		btnMoveRight.setStyle("-fx-background-radius: 5em;");
//		btnMoveRight.setOnAction(e -> moveRight());
//		btnMoveRight.setMinHeight(30);
//		btnMoveRight.setMaxHeight(30);
//		btnMoveRight.setMinWidth(30);
//		btnMoveRight.setMaxWidth(30);
//		HBox box7 = new HBox();
//		box7.getChildren().add(btnMoveRight);
//		box7.setAlignment(Pos.CENTER);
//		moveOptions.setRight(box7);
//
//		Button btnMoveLeft = new Button();
//		btnMoveLeft.setStyle("-fx-background-radius: 5em;");
//		btnMoveLeft.setOnAction(e -> moveLeft());
//		btnMoveLeft.setMinHeight(30);
//		btnMoveLeft.setMaxHeight(30);
//		btnMoveLeft.setMinWidth(30);
//		btnMoveLeft.setMaxWidth(30);
//		HBox box8 = new HBox();
//		box8.getChildren().add(btnMoveLeft);
//		box8.setAlignment(Pos.CENTER);
//		moveOptions.setLeft(box8);

		((Pane) root3.getBottom()).getChildren().addAll(btnAbility1, btnAbility2, btnAbility3, btnEndTurn, btnLeaderAbility);
		actions.add(btnAbility1);
		actions.add(btnAbility2);
		actions.add(btnAbility3);
		actions.add(btnEndTurn);
		actions.add(btnLeaderAbility);
		
		boolean punch = false;
		for (Effect e : current.getAppliedEffects()) {
			if (e instanceof Disarm) {
				punch = true;
				break;
			}
		}
		if (punch) {
			((Pane) root3.getBottom()).getChildren().add(btnPunch);
			actions.add(btnPunch);
		} else {
			((Pane) root3.getBottom()).getChildren().add(attackOptions);
//			actions.add(btnAttackUp);
//			actions.add(btnAttackDown);
//			actions.add(btnAttackRight);
//			actions.add(btnAttackLeft);
		}

		((Pane) root3.getBottom()).getChildren().add(moveOptions);
//		actions.add(btnMoveUp);
//		actions.add(btnMoveDown);
//		actions.add(btnMoveRight);
//		actions.add(btnMoveLeft);
		
		if (!(View.game.getFirstPlayer().getLeader() == current || View.game.getSecondPlayer().getLeader() == current)) {
			btnLeaderAbility.setTooltip(new Tooltip("Current champion is not a leader"));
		}
		
		if (!(twoPlayerMode || !twoPlayerMode && View.game.getFirstPlayer().getTeam().contains(current))) {
			for (Button b : actions) {
				b.setDisable(true);
			}
		}
	}

	public static void showHelp() {
		Stage helpStage = new Stage();
		helpStage.setTitle("Game Manual");
		helpStage.setMinWidth(500);
		helpStage.setMinHeight(300);
		BorderPane helpRoot = new BorderPane();
		Scene helpScene = new Scene(helpRoot, 750, 450);
		Image backgroundImage = new Image("./application/media/backgrounds/helpBackground.png");
		ImageView background = new ImageView(backgroundImage);
		background.fitHeightProperty().bind(helpStage.heightProperty());
		background.fitWidthProperty().bind(helpStage.widthProperty());
		helpRoot.getChildren().add(background);
		helpStage.setScene(helpScene);
		
		VBox navigation = new VBox(10);
		VBox content = new VBox(5);
		helpRoot.setLeft(navigation);
		helpRoot.setCenter(content);
		
		Button game = new Button("Game");
		Button champions = new Button("Champions");
		Button covers = new Button("Covers");
		Button abilities = new Button("Abilities");
		Button effects = new Button("Effects");
		Button tactics = new Button("Tactics");
		Button invalid = new Button("Invalid\nActions");
		navigation.getChildren().addAll(game, champions, covers, abilities, effects, tactics, invalid);
		navigation.setPadding(new Insets(10,10,10,10));
		navigation.setAlignment(Pos.CENTER);
		navigation.setMaxWidth(220);
		navigation.setMinWidth(220);
		
		for (Node n : navigation.getChildren()) {
			((Button)n).setStyle("-fx-font: 16px \"Serif\"; -fx-padding: 10; -fx-background-color: #011627;"
					+ "-fx-text-fill: #FDFFFC; -fx-alignment: CENTER; -fx-background-radius: 20;");
			((Button)n).setMinWidth(200);
			((Button)n).setMaxWidth(200);
		}
		
		
		helpStage.show();
	}
	
	public static void manualButton() {
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
			Text info = new Text(
					"There are 3 types of abilities:\n " + "Damaging Ability, Healing Ability, Crowd Control Ability.\n"
							+ "Each Ability requires a ceratin amount of actions and mana to be casted.\n"
							+ "Each Ability has a specific Casting Area.");
			info.setTextAlignment(TextAlignment.CENTER);
			Text DA = new Text(
					"Damaging Abilities deal a certain amount\n" + "of damage to enemies within Casting Area.");
			Text HA = new Text(
					"Healing Abilities add a certain amount\n" + "of health to friends within Casting Area.");
			Text CCA = new Text(
					"Crowd Control Abilities activate a certain\n" + "effect on targets within Casting Area.");
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
				Text disarmText = new Text(
						"Target cannot use normal attacks.\n" + "Gain a SINGLETARGET damaging ability");
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
				Text dodgeText = new Text(
						"Target has a 50% chance of dodging normal attacks.\n" + "Increase speed by 5%");
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
				Text embraceText = new Text("Permanently add 20% from maxHP to currentHP,\n"
						+ "Permanently increase mana by 20%,\n" + "Increase speed and attackDamage by 20%.");
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

			Button root = new Button("Root");
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
				Text shieldText = new Text("Block the next attack or damaging ability cast on target.\n"
						+ "Once an attack or ability is blocked, the effect will be removed.\n"
						+ "Increase speed by 2%.");
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
				Text shockText = new Text(
						"Decrease target speed by 10%\n" + "Decrease the target’s normal attack damage by 10%\n"
								+ "Decrease max action points per turn and current action points by 1.");
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
				Text silenceText = new Text("Target cannot use abilities.\n"
						+ "Increase max action points per turn and current action points by 2");
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
				Text speedupText = new Text("Increase speed by 15%.\n"
						+ "Increase max action points per turn and current action points by 1.");
				speedupText.setTextAlignment(TextAlignment.CENTER);
				Button backToTmp = new Button("Back to Effects List");
				backToTmp.setOnAction(eeee -> {
					manualStage.setTitle("Effects Manual");
					manualStage.setScene(tmpScene);
				});
				effectBox.getChildren().addAll(speedupText, backToTmp);
				manualStage.setScene(effectScene);
			});

			Button stun = new Button("Stun");
			stun.setOnAction(eee -> {
				effectBox.getChildren().clear();
				manualStage.setTitle("Stun Effect");
				Text stunText = new Text(
						"Set target to INACTIVE.\n" + "Target is not allowed to play their turn for the duration.");
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
			tmpBox.getChildren().addAll(selectEffect, disarm, dodge, embrace, powerup, root, shield, shock, silence,
					speedup, stun, back);
			manualStage.setScene(tmpScene);
		});

		Button close = new Button("Close");
		close.setOnAction(ee -> manualStage.close());

		manualStage.setScene(mainScene);
		manualStage.setMinWidth(400);
		manualStage.setMinHeight(200);
		mainBox.getChildren().addAll(abilities, effects, close);
		mainBox.setPadding(new Insets(10, 10, 10, 10));
		tmpBox.setPadding(new Insets(10, 10, 10, 10));
		effectBox.setPadding(new Insets(10, 10, 10, 10));
		manualStage.centerOnScreen();
		manualStage.show();
	}

	public static void moveUp() {
		Champion current = View.game.getCurrentChampion();
		try {
			View.game.move(Direction.UP);
			showControls();
			updateCurrentInformation();
			updateStatusBar();
			updateBoard();
			checkWinner();
		} catch (NotEnoughResourcesException | UnallowedMovementException e1) {
			if (!twoPlayerMode && View.game.getSecondPlayer().getTeam().contains(current)) {
				e1.printStackTrace();
			} else {
				throwException(e1.getMessage());
			}
		}
	}

	public static void moveDown() {
		Champion current = View.game.getCurrentChampion();
		try {
			View.game.move(Direction.DOWN);
			showControls();
			updateCurrentInformation();
			updateStatusBar();
			updateBoard();
			checkWinner();
		} catch (NotEnoughResourcesException | UnallowedMovementException e1) {
			if (!twoPlayerMode && View.game.getSecondPlayer().getTeam().contains(current)) {
				e1.printStackTrace();
			} else {
				throwException(e1.getMessage());
			}
		}
	}

	public static void moveRight() {
		Champion current = View.game.getCurrentChampion();
		try {
			View.game.move(Direction.RIGHT);
			showControls();
			updateCurrentInformation();
			updateStatusBar();
			updateBoard();
			checkWinner();
		} catch (NotEnoughResourcesException | UnallowedMovementException e1) {
			if (!twoPlayerMode && View.game.getSecondPlayer().getTeam().contains(current)) {
				e1.printStackTrace();
			} else {
				throwException(e1.getMessage());
			}
		}
	}

	public static void moveLeft() {
		Champion current = View.game.getCurrentChampion();
		try {
			View.game.move(Direction.LEFT);
			showControls();
			updateCurrentInformation();
			updateStatusBar();
			updateBoard();
			checkWinner();
		} catch (NotEnoughResourcesException | UnallowedMovementException e1) {
			if (!twoPlayerMode && View.game.getSecondPlayer().getTeam().contains(current)) {
				e1.printStackTrace();
			} else {
				throwException(e1.getMessage());
			}
		}
	}

	public static void attackUp() {
		Champion current = View.game.getCurrentChampion();
		try {
			Damageable attackTarget = View.game.attack(Direction.UP);
			showControls();
			updateCurrentInformation();
			updateStatusBar();
			updateBoard(attackTarget);
			checkWinner();
			Media attackMedia = new Media(new File("./src/application/media/animation/attack.wav").toURI().toString());
			MediaPlayer attackMediaPlayer = new MediaPlayer(attackMedia);
			attackMediaPlayer.play();
		} catch (Exception e1) {
			if (!twoPlayerMode && View.game.getSecondPlayer().getTeam().contains(current)) {
				e1.printStackTrace();
			} else {
				throwException(e1.getMessage());
			}
		}
	}

	public static void attackDown() {
		Champion current = View.game.getCurrentChampion();
		try {
			Damageable attackTarget = View.game.attack(Direction.DOWN);
			showControls();
			updateCurrentInformation();
			updateStatusBar();
			updateBoard(attackTarget);
			checkWinner();
			Media attackMedia = new Media(new File("./src/application/media/animation/attack.wav").toURI().toString());
			MediaPlayer attackMediaPlayer = new MediaPlayer(attackMedia);
			attackMediaPlayer.play();
		} catch (Exception e1) {
			if (!twoPlayerMode && View.game.getSecondPlayer().getTeam().contains(current)) {
				e1.printStackTrace();
			} else {
				throwException(e1.getMessage());
			}
		}
	}

	public static void attackRight() {
		Champion current = View.game.getCurrentChampion();
		try {
			Damageable attackTarget = View.game.attack(Direction.RIGHT);
			showControls();
			updateCurrentInformation();
			updateStatusBar();
			updateBoard(attackTarget);
			checkWinner();
			Media attackMedia = new Media(new File("./src/application/media/animation/attack.wav").toURI().toString());
			MediaPlayer attackMediaPlayer = new MediaPlayer(attackMedia);
			attackMediaPlayer.play();
		} catch (Exception e1) {
			if (!twoPlayerMode && View.game.getSecondPlayer().getTeam().contains(current)) {
				e1.printStackTrace();
			} else {
				throwException(e1.getMessage());
			}
		}
	}

	public static void attackLeft() {
		Champion current = View.game.getCurrentChampion();
		try {
			Damageable attackTarget = View.game.attack(Direction.LEFT);
			showControls();
			updateCurrentInformation();
			updateStatusBar();
			updateBoard(attackTarget);
			checkWinner();
			Media attackMedia = new Media(new File("./src/application/media/animation/attack.wav").toURI().toString());
			MediaPlayer attackMediaPlayer = new MediaPlayer(attackMedia);
			attackMediaPlayer.play();
		} catch (Exception e1) {
			if (!twoPlayerMode && View.game.getSecondPlayer().getTeam().contains(current)) {
				e1.printStackTrace();
			} else {
				throwException(e1.getMessage());
			}
		}
	}

	public static void castAbility(int abilityIndex) {
		Champion current = View.game.getCurrentChampion();
		Ability ability = current.getAbilities().get(abilityIndex);
		AreaOfEffect area = ability.getCastArea();
		if (area == AreaOfEffect.SELFTARGET || area == AreaOfEffect.TEAMTARGET || area == AreaOfEffect.SURROUND) {
			try {
				ArrayList<Damageable> targets = View.game.castAbility(ability);
				showControls();
				updateCurrentInformation();
				updateStatusBar();
				updateBoard(targets, ability);
				checkWinner();
				Media castAbilityMedia = new Media(new File("./src/application/media/animation/castAbility.wav").toURI().toString());
				MediaPlayer castAbilityMediaPlayer = new MediaPlayer(castAbilityMedia);
				castAbilityMediaPlayer.play();
			} catch (Exception e1) {
				if (!twoPlayerMode && View.game.getSecondPlayer().getTeam().contains(current)) {
					e1.printStackTrace();
				} else {
					throwException(e1.getMessage());
				}
			}
		}

		else if (area == AreaOfEffect.DIRECTIONAL) {
			Stage chooseDirection = new Stage();
			chooseDirection.setTitle("Choose a Direction to Cast Ability");
			VBox window = new VBox(10);
			window.setAlignment(Pos.CENTER);
			Scene scene = new Scene(window);
			Button up = new Button("UP");
			up.setOnAction(ee -> {
				chooseDirection.close();
				try {
					ArrayList<Damageable> targets = View.game.castAbility(ability, Direction.UP);
					showControls();
					updateCurrentInformation();
					updateStatusBar();
					updateBoard(targets, ability);
					checkWinner();
					Media castAbilityMedia = new Media(new File("./src/application/media/animation/castAbility.wav").toURI().toString());
					MediaPlayer castAbilityMediaPlayer = new MediaPlayer(castAbilityMedia);
					castAbilityMediaPlayer.play();
				} catch (Exception e1) {
					if (!twoPlayerMode && View.game.getSecondPlayer().getTeam().contains(current)) {
						e1.printStackTrace();
					} else {
						throwException(e1.getMessage());
					}
				}
			});
			Button down = new Button("DOWN");
			down.setOnAction(ee -> {
				chooseDirection.close();
				try {
					ArrayList<Damageable> targets = View.game.castAbility(ability, Direction.DOWN);
					showControls();
					updateCurrentInformation();
					updateStatusBar();
					updateBoard(targets, ability);
					checkWinner();
					Media castAbilityMedia = new Media(new File("./src/application/media/animation/castAbility.wav").toURI().toString());
					MediaPlayer castAbilityMediaPlayer = new MediaPlayer(castAbilityMedia);
					castAbilityMediaPlayer.play();
				} catch (Exception e1) {
					if (!twoPlayerMode && View.game.getSecondPlayer().getTeam().contains(current)) {
						e1.printStackTrace();
					} else {
						throwException(e1.getMessage());
					}
				}
			});
			Button right = new Button("RIGHT");
			right.setOnAction(ee -> {
				chooseDirection.close();
				try {
					ArrayList<Damageable> targets = View.game.castAbility(ability, Direction.RIGHT);
					showControls();
					updateCurrentInformation();
					updateStatusBar();
					updateBoard(targets, ability);
					checkWinner();
					Media castAbilityMedia = new Media(new File("./src/application/media/animation/castAbility.wav").toURI().toString());
					MediaPlayer castAbilityMediaPlayer = new MediaPlayer(castAbilityMedia);
					castAbilityMediaPlayer.play();
				} catch (Exception e1) {
					if (!twoPlayerMode && View.game.getSecondPlayer().getTeam().contains(current)) {
						e1.printStackTrace();
					} else {
						throwException(e1.getMessage());
					}
				}
			});
			Button left = new Button("LEFT");
			left.setOnAction(ee -> {
				chooseDirection.close();
				try {
					ArrayList<Damageable> targets = View.game.castAbility(ability, Direction.LEFT);
					showControls();
					updateCurrentInformation();
					updateStatusBar();
					updateBoard(targets, ability);
					checkWinner();
					Media castAbilityMedia = new Media(new File("./src/application/media/animation/castAbility.wav").toURI().toString());
					MediaPlayer castAbilityMediaPlayer = new MediaPlayer(castAbilityMedia);
					castAbilityMediaPlayer.play();
				} catch (Exception e1) {
					if (!twoPlayerMode && View.game.getSecondPlayer().getTeam().contains(current)) {
						e1.printStackTrace();
					} else {
						throwException(e1.getMessage());
					}
				}
			});
			chooseDirection.setScene(scene);
			chooseDirection.setMinWidth(400);
			chooseDirection.setMinHeight(200);
			window.getChildren().addAll(up, down, right, left);
			window.setPadding(new Insets(10, 10, 10, 10));
			chooseDirection.show();
		}

		else if (area == AreaOfEffect.SINGLETARGET) {
			Stage chooseCell = new Stage();
			chooseCell.setTitle("Choose a Cell to Cast Ability On");
			VBox window = new VBox(10);
			window.setAlignment(Pos.CENTER);
			Scene scene = new Scene(window);
			Label chooseCellLabel = new Label("Insert cell co-ordinates to cast ability.");
			TextField xField = new TextField();
			xField.setPromptText("X co-ordinate (1 is top)");
			TextField yField = new TextField();
			yField.setPromptText("Y co-ordinate (1 is left)");
			Button confirm = new Button("Confirm");
			confirm.setOnAction(ee -> {
				chooseCell.close();
				try {
					ArrayList<Damageable> targets = View.game.castAbility(ability,
							Math.abs(5 - Integer.parseInt(xField.getText())),Integer.parseInt(yField.getText()) - 1);
					showControls();
					updateCurrentInformation();
					updateStatusBar();
					updateBoard(targets, ability);
					checkWinner();
					Media castAbilityMedia = new Media(new File("./src/application/media/animation/castAbility.wav").toURI().toString());
					MediaPlayer castAbilityMediaPlayer = new MediaPlayer(castAbilityMedia);
					castAbilityMediaPlayer.play();
				} catch (Exception e1) {
					if (!twoPlayerMode && View.game.getSecondPlayer().getTeam().contains(current)) {
						e1.printStackTrace();
					} else {
						throwException(e1.getMessage());
					}
				}
			});
			chooseCell.setScene(scene);
			chooseCell.setMinWidth(400);
			chooseCell.setMinHeight(200);
			window.getChildren().addAll(chooseCellLabel, xField, yField, confirm);
			window.setPadding(new Insets(10, 10, 10, 10));
			chooseCell.show();
		}
	}

	public static void useLeaderAbility() {
		Champion current = View.game.getCurrentChampion();
		try {
			View.game.useLeaderAbility();
			showControls();
			updateCurrentInformation();
			updateStatusBar();
			updateBoard();
			checkWinner();
			Media leaderMedia = new Media(new File("./src/application/media/animation/leader.wav").toURI().toString());
			MediaPlayer leaderMediaPlayer = new MediaPlayer(leaderMedia);
			leaderMediaPlayer.play();
		} catch (Exception e1) {
			if (!twoPlayerMode && View.game.getSecondPlayer().getTeam().contains(current)) {
				e1.printStackTrace();
			} else {
				throwException(e1.getMessage());
			}
		}
	}

	public static void endTurn() {
		View.game.endTurn();
		showControls();
		updateCurrentInformation();
		updateStatusBar();
		prepareTurns();
		updateBoard();
		checkWinner();
		if ((!twoPlayerMode) && View.game.getSecondPlayer().getTeam().contains(View.game.getCurrentChampion())) {
			computerAction();
		}
	}

	public static void computerAction() {
		PauseTransition pause = new PauseTransition(Duration.seconds(3));
		pause.setOnFinished(event -> {
		});
		pause.play();
	}

	public static void checkWinner() {
		Player winner = View.game.checkGameOver();
		if (winner != null) {
			for (Node n : ((Pane) root3.getBottom()).getChildren()) {
				if (n instanceof Button) {
					((Button) n).setDisable(true);
				}
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
			Text msgText = new Text("Congratulations! " + winner.getName() + " is the WINNER");
			window.getChildren().addAll(msgText, exitGame);
			window.setPadding(new Insets(10, 10, 10, 10));
			gameOver.show();
		}

	}
	
	public static void putFadeAnimation(Button b)
	{
		 FadeTransition trans2 = new FadeTransition();
	        trans2.setDuration(Duration.seconds(1));
			trans2.setFromValue(1.0f);
			trans2.setToValue(0.4f);
			trans2.setAutoReverse(true);
			trans2.setCycleCount(Animation.INDEFINITE);
			trans2.setNode(b);
			trans2.play();;
	}

	public static void putGlowAnimation(Button b , Color c){
		 int depth = 70; //Setting the uniform variable for the glow width and height
	        
	        DropShadow borderGlow= new DropShadow();
	        borderGlow.setOffsetY(0f);
	        borderGlow.setOffsetX(0f);
	        borderGlow.setColor(c); 
	        borderGlow.setWidth(depth);
	        borderGlow.setHeight(depth);
	         
	        b.setEffect(borderGlow); //Apply the borderGlow effect to the JavaFX node
	 }
	 
	public static void putGlowAnimationForMainMenu(Button b , Color c){
		    int depth = 70; //Setting the uniform variable for the glow width and height
	        DropShadow borderGlow= new DropShadow();
	        borderGlow.setOffsetY(0f);
	        borderGlow.setOffsetX(0f);
	        borderGlow.setColor(c); 
	        borderGlow.setWidth(depth);
	        borderGlow.setHeight(depth); 
	        b.setEffect(null);
	        
	        b.setOnMouseEntered( f ->{ 
	        b.setEffect(borderGlow);
		});
		b.setOnMouseExited( h -> {
		 b.setEffect(null);
		});
	 }
	
	public static void keyMoved() {
		root3.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.W) {
				if (twoPlayerMode
						|| (!twoPlayerMode && View.game.getFirstPlayer().getTeam().contains(View.game.getCurrentChampion())))
					moveUp();
			}
			if (e.getCode() == KeyCode.S) {
				if (twoPlayerMode
						|| (!twoPlayerMode && View.game.getFirstPlayer().getTeam().contains(View.game.getCurrentChampion())))
					moveDown();
			}
			if (e.getCode() == KeyCode.D) {
				if (twoPlayerMode
						|| (!twoPlayerMode && View.game.getFirstPlayer().getTeam().contains(View.game.getCurrentChampion())))
					moveRight();
			}
			if (e.getCode() == KeyCode.A) {
				if (twoPlayerMode
						|| (!twoPlayerMode && View.game.getFirstPlayer().getTeam().contains(View.game.getCurrentChampion())))
					moveLeft();
			}
			if (e.getCode() == KeyCode.NUMPAD8) {
				if (twoPlayerMode
						|| (!twoPlayerMode && View.game.getFirstPlayer().getTeam().contains(View.game.getCurrentChampion())))
					attackUp();
			}
			if (e.getCode() == KeyCode.NUMPAD2) {
				if (twoPlayerMode
						|| (!twoPlayerMode && View.game.getFirstPlayer().getTeam().contains(View.game.getCurrentChampion())))
					attackDown();
			}
			if (e.getCode() == KeyCode.NUMPAD6) {
				if (twoPlayerMode
						|| (!twoPlayerMode && View.game.getFirstPlayer().getTeam().contains(View.game.getCurrentChampion())))
					attackRight();
			}
			if (e.getCode() == KeyCode.NUMPAD4) {
				if (twoPlayerMode
						|| (!twoPlayerMode && View.game.getFirstPlayer().getTeam().contains(View.game.getCurrentChampion())))
					attackLeft();
			}
			if (e.getCode() == KeyCode.DIGIT1) {
				if (twoPlayerMode
						|| (!twoPlayerMode && View.game.getFirstPlayer().getTeam().contains(View.game.getCurrentChampion())))
					castAbility(0);
			}
			if (e.getCode() == KeyCode.DIGIT2) {
				if (twoPlayerMode
						|| (!twoPlayerMode && View.game.getFirstPlayer().getTeam().contains(View.game.getCurrentChampion())))
					castAbility(1);
			}
			if (e.getCode() == KeyCode.DIGIT3) {
				if (twoPlayerMode
						|| (!twoPlayerMode && View.game.getFirstPlayer().getTeam().contains(View.game.getCurrentChampion())))
					castAbility(2);
			}
			if (e.getCode() == KeyCode.DIGIT4) {
				if (twoPlayerMode
						|| (!twoPlayerMode && View.game.getFirstPlayer().getTeam().contains(View.game.getCurrentChampion())))
					castAbility(3);
			}
			if (e.getCode() == KeyCode.DIGIT5) {
				if (twoPlayerMode
						|| (!twoPlayerMode && View.game.getFirstPlayer().getTeam().contains(View.game.getCurrentChampion())))
					useLeaderAbility();
			}
			if (e.getCode() == KeyCode.H) {
				if (twoPlayerMode
						|| (!twoPlayerMode && View.game.getFirstPlayer().getTeam().contains(View.game.getCurrentChampion())))
					showHelp();
			}
			if (e.getCode() == KeyCode.E) {
				if (twoPlayerMode
						|| (!twoPlayerMode && View.game.getFirstPlayer().getTeam().contains(View.game.getCurrentChampion())))
					endTurn();

			}
		});
	}

	public static Pane progressBarWithText(String labelText, int current, int max, String color) {
        Label label = new Label(labelText + ": " + current + "/" + max);
        label.setStyle("-fx-text-fill: #011627;");
		label.setFont(new Font("Didot.", 15));
        
        ProgressBar progressBar = new ProgressBar(current * 1.0 / max);
        progressBar.setStyle("-fx-accent: " + color +"; -fx-background-insets: 0; "
        		+ "-fx-min-width: 200px; -fx-min-height: 30px; "
        		+ "-fx-text-box-border: #011627; -fx-control-inner-background: #FDFFFC;"
        		+ "-fx-border-radius: 5;");
        
        Pane stackPane = new StackPane(progressBar, label);
        StackPane.setAlignment(label, Pos.CENTER_LEFT);
        label.setPadding(new Insets(0, 0, 0, 50));
        StackPane.setAlignment(progressBar, Pos.CENTER_LEFT);
        return stackPane;
    }
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

}
