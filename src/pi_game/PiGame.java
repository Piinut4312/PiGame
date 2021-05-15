package pi_game;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PiGame extends Application{

    public static double SCR_WIDTH = 1024;
    public static double SCR_HEIGHT = 768;

    public static double RADIUS = 200;

    public static Image shooter_texture = new Image("pi_game/resources/shooter.png", 32, 32, true, false);
    public static Image bullet_texture = new Image("pi_game/resources/drop.png", 16, 16, true, false);
    public static Image target_texture = new Image("pi_game/resources/target.png", 16, 16, true, false);
    public static Image background = new Image("pi_game/resources/background.jpg");

    public static ImageView shooter_image = new ImageView(shooter_texture);

    GameController gameController = new GameController(40);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){

        primaryStage.setTitle("Pi: The Circular Shooting Game");
        primaryStage.setScene(gameController.getScene());

        gameController.runGame();

        primaryStage.show();
    }

}
