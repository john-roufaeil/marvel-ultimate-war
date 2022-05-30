package application;
	
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Marvel - Ultimate War");
		primaryStage.setFullScreen(true);
		primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
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
		HBox chosen = new HBox();
		chosen.setAlignment(Pos.CENTER);
		chosen.setSpacing(10);
		chosen.setPadding(new Insets(15, 15, 15, 15));
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
		chosen.getChildren().addAll(l2_1, chosen1_1v, chosen1_2v, chosen1_3v, region, chosen2_1v, chosen2_2v, chosen2_3v, l2_2);
		root2.setTop(chosen);

	    
		
	    // Champions Buttons
		GridPane champsgrid = new GridPane();
		champsgrid.setPadding(new Insets(10, 10, 10, 10));
		champsgrid.setStyle("-fx-background-color: #222;");
		champsgrid.setAlignment(Pos.CENTER);
		root2.setBottom(champsgrid);
		ArrayList<Button> buttons = new ArrayList<>();
		ArrayList<ImageView> images = new ArrayList<>();
		int a = 0; int b = 0;
		for (int i = 1; i <= 15; i++) {
			ArrayList<Champion> champions = Game.getAvailableChampions();
			String name = champions.get(i-1).getName();
			Image ch = new Image("./application/media/12.jpeg");
			ImageView iv = new ImageView(ch);
			iv.setFitHeight(50);
			iv.setFitWidth(50);
			images.add(iv);
			Button btn = new Button();
			btn.setPrefSize(50, 50);
		    btn.setGraphic(iv);
		    champsgrid.add(btn, a, b);
		    a++;
		    if (a == 5) {
		    	a = 0;
		    	b++;
		    }
		    buttons.add(btn);
		}
		
		
		
		// Show Attributes
		VBox details = new VBox();
		details.setPadding(new Insets(10, 10, 10, 10));
		details.setAlignment(Pos.CENTER);
		Label clickMsg = new Label("Click on a champion to show details.");
		details.getChildren().add(clickMsg);
		root2.setCenter(details);
	}
	
	public static void show(String name) {
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}