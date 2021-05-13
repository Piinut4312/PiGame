package pi_game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;

public class PiGame extends Application{

    public static double SCR_WIDTH = 1024;
    public static double SCR_HEIGHT = 768;

    public static double RADIUS = 200;

    public Canvas canvas;
    public GraphicsContext gc;

    public ShooterSprite shooter;

    public Media SHOOT_SOUND = new Media(new File("src/pi_game/shoot.mp3").toURI().toString());

    public MediaPlayer media_player = new MediaPlayer(SHOOT_SOUND);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){

        Group root = new Group();
        Scene scene = new Scene(root, SCR_WIDTH, SCR_HEIGHT);
        primaryStage.setTitle("Pi: The Circular Shooting Game");
        primaryStage.setScene(scene);

        canvas = new Canvas(SCR_WIDTH, SCR_HEIGHT);
        root.getChildren().add(canvas);

        gc = canvas.getGraphicsContext2D();

        Image drop = new Image("pi_game/drop.png");
        Circle circle = new Circle(SCR_WIDTH/2, SCR_HEIGHT/2, RADIUS);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);
        circle.toBack();
        root.getChildren().add(circle);

        shooter = new ShooterSprite(drop, SCR_WIDTH/2, SCR_HEIGHT/2, 16, 16);

        media_player.setVolume(0.5);
        media_player.setAutoPlay(true);

        final long start = System.nanoTime();
        new AnimationTimer(){

            @Override
            public void handle(long now) {
                double t = (now-start)/1000000000.0;
                scene.setOnMouseMoved(
                        event -> shooter.update(event.getX(), event.getY())
                );
                gc.clearRect(0, 0, SCR_WIDTH, SCR_HEIGHT);
                shooter.render(gc);
            }
        }.start();

        primaryStage.show();
    }

}
