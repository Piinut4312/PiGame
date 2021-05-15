package pi_game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class PiGame extends Application{

    public static double SCR_WIDTH = 1024;
    public static double SCR_HEIGHT = 768;

    public static double RADIUS = 200;

    public Canvas canvas;
    public GraphicsContext gc;

    public static Image shooter_texture = new Image("pi_game/shooter.png", 32, 32, true, false);
    public static Image bullet_texture = new Image("pi_game/drop.png", 16, 16, true, false);
    public static Image background = new Image("pi_game/background.jpg");

    public static ImageView shooter_image = new ImageView(shooter_texture);
    public static ImageView bullet_image = new ImageView(bullet_texture);

    public ShooterSprite shooter;

    public ArrayList<BulletSprite> bullets = new ArrayList<>();

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
        root.getChildren().add(shooter_image);

        gc = canvas.getGraphicsContext2D();

        Circle circle = new Circle(SCR_WIDTH/2, SCR_HEIGHT/2, RADIUS);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.AZURE);
        circle.setStrokeWidth(2);
        circle.toBack();
        root.getChildren().add(circle);

        Line line1 = new Line(SCR_WIDTH/2-16, SCR_HEIGHT/2, SCR_WIDTH/2+16, SCR_HEIGHT/2);
        Line line2 = new Line(SCR_WIDTH/2, SCR_HEIGHT/2-16, SCR_WIDTH/2, SCR_HEIGHT/2+16);
        line1.setStroke(Color.AZURE);
        line2.setStroke(Color.AZURE);
        line1.setStrokeWidth(2);
        line2.setStrokeWidth(2);
        root.getChildren().addAll(line1, line2);

        scene.setCursor(Cursor.DEFAULT);

        shooter = new ShooterSprite(shooter_image, SCR_WIDTH/2, SCR_HEIGHT/2, 32, 32);

        media_player.setVolume(0.2);
        media_player.setAutoPlay(true);
        media_player.setOnReady(()->media_player.play());

        final long start = System.nanoTime();

        new AnimationTimer(){

            @Override
            public void handle(long now) {
                double t = (now-start)/1000000000.0;
                gc.clearRect(0, 0, SCR_WIDTH, SCR_HEIGHT);
                gc.drawImage(background, 0, 0);
                scene.setOnMouseMoved(
                        event -> shooter.update(event.getX(), event.getY())
                );
                shooter.update(bullets, root);
                System.out.println(bullets.size());
                for(int i = 0; i < bullets.size(); i++){
                    BulletSprite bullet = bullets.get(i);
                    if(bullet.isAlive()){
                        bullet.update();
                    }else{
                        bullets.remove(i);
                    }
                }
                shooter.render();

            }
        }.start();

        primaryStage.show();
    }

}
