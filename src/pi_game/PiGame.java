package pi_game;

import javafx.application.Application;
import javafx.stage.Stage;
import pi_game.controllers.GameController;

public class PiGame extends Application{

    public static double SCR_WIDTH = 1024;
    public static double SCR_HEIGHT = 768;

    public static double RADIUS = 210;

    GameController gameController = new GameController(40);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){

        primaryStage.setTitle("Pi: The Circular Shooting Game");

        gameController.runGame(primaryStage);

    }

}
