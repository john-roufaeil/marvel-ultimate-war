package application;
	
import java.io.FileInputStream;
import java.io.InputStream;

import engine.Game;
import engine.Player;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
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
		
		// Homepage Scene_________________________________________________________________________________________________________
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
			primaryStage.setScene(begin);
			player1 = new Player(name1TextField.getText());
			player2 = new Player(name2TextField.getText());
			game = new Game(player1, player2);
		});
		HBox btn = new HBox();
		btn.setAlignment(Pos.CENTER);
		btn.getChildren().add(startBtn);
		root1.getChildren().addAll(firstPlayer, secondPlayer, btn);
		
		
		// Begin Scene____________________________________________________________________________________________________
		BorderPane root2 = new BorderPane();
		begin = new Scene(root2);
		primaryStage.setFullScreen(true);
		
		HBox status = new HBox();
		status.setAlignment(Pos.CENTER);
		status.setSpacing(10);
		status.setPadding(new Insets(10, 10, 10, 10));
		Label l2_1 = new Label();
//		System.out.println(player1.getName());
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
		Label l2_2 = new Label();
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
		status.getChildren().addAll(l2_1, chosen1_1v, chosen1_2v, chosen1_3v, l2_2, chosen2_1v, chosen2_2v, chosen2_3v);
		root2.setTop(status);
		
		GridPane champs = new GridPane();
		champs.setPadding(new Insets(10, 10, 10, 10));
		champs.setStyle("-fx-background-color: #222;");
		champs.setAlignment(Pos.CENTER);
		root2.setBottom(champs);
		
		Image ch1 = new Image("images.jpeg");
	    ImageView CA = new ImageView(ch1);
	    CA.setFitHeight(50);
	    CA.setFitWidth(50);
	    Button btn1 = new Button();
		btn1.setPrefSize(50, 50);
	    btn1.setGraphic(CA);
	    champs.add(btn1, 0, 0);
	    
	    
	    Button btn2 = new Button();
		btn2.setPrefSize(50, 50);
	    btn2.setGraphic(CA);
	    champs.add(btn2, 1, 0);
	    
	    Button btn3 = new Button();
		btn3.setPrefSize(50, 50);
	    btn3.setGraphic(CA);
	    champs.add(btn3, 2, 0);
	    
	    Button btn4 = new Button();
		btn4.setPrefSize(50, 50);
	    btn4.setGraphic(CA);
	    champs.add(btn4, 3, 0);
	    
	    Button btn5 = new Button();
		btn5.setPrefSize(50, 50);
	    btn5.setGraphic(CA);
	    champs.add(btn5, 4, 0);
	    
	    Button btn6 = new Button();
		btn6.setPrefSize(50, 50);
	    btn6.setGraphic(CA);
	    champs.add(btn6, 0, 1);
	    
	    Button btn7 = new Button();
		btn7.setPrefSize(50, 50);
	    btn7.setGraphic(CA);
	    champs.add(btn7, 1, 1);

	    Button btn8 = new Button();
		btn8.setPrefSize(50, 50);
	    btn8.setGraphic(CA);
	    champs.add(btn8, 2, 1);
	    
	    Button btn9 = new Button();
		btn9.setPrefSize(50, 50);
	    btn9.setGraphic(CA);
	    champs.add(btn9, 3, 1);
	    
	    Button btn10 = new Button();
		btn10.setPrefSize(50, 50);
	    btn10.setGraphic(CA);
	    champs.add(btn10, 4, 1);
	    
	    Button btn11 = new Button();
		btn11.setPrefSize(50, 50);
	    btn11.setGraphic(CA);
	    champs.add(btn11, 0, 2);
	    
	    Button btn12 = new Button();
		btn12.setPrefSize(50, 50);
	    btn12.setGraphic(CA);
	    champs.add(btn12, 1, 2);
	    
	    Button btn13 = new Button();
		btn13.setPrefSize(50, 50);
	    btn13.setGraphic(CA);
	    champs.add(btn13, 2, 2);
	    
	    Button btn14 = new Button();
		btn14.setPrefSize(50, 50);
	    btn14.setGraphic(CA);
	    champs.add(btn14, 3, 2);
	    
	    Button btn15 = new Button();
		btn15.setPrefSize(50, 50);
	    btn15.setGraphic(CA);
	    champs.add(btn15, 4, 2);
	    

		
		// GameView Scene_____________________________________________________________________________________________________
		
		primaryStage.setScene(homepage);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}