package pi_game;

import javafx.animation.AnimationTimer;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.File;

public class GameController {

    private TargetController targetController;
    private BulletController bulletController;
    private ShooterSprite shooter;
    private int score;
    private GameState state;

    public static Canvas gameplayCanvas;
    public static Canvas endCanvas;
    public static GraphicsContext gameplayGC;
    public static GraphicsContext endGC;

    public static Image background = new Image("pi_game/resources/background.jpg");
    public static Image shooter_texture = new Image("pi_game/resources/shooter.png", 32, 32, true, false);
    public static Image bullet_texture = new Image("pi_game/resources/drop.png", 16, 16, true, false);
    public static Image target_texture = new Image("pi_game/resources/target.png", 16, 16, true, false);

    public static ImageView shooter_image = new ImageView(shooter_texture);

    private Group gameplayGroup;
    private Group endGroup;
    private Group startGroup;
    private Group pauseGroup;

    private Scene gameplayScene;
    private Scene endScene;
    private Scene startScene;
    private Scene pauseScene;

    private static final Media GAMEPLAY_BGM = new Media(new File("src/pi_game/gameplayBGM.mp3").toURI().toString());
    private MediaPlayer player = new MediaPlayer(GAMEPLAY_BGM);

    public static final double CENTER_X = PiGame.SCR_WIDTH/2;
    public static final double CENTER_Y = PiGame.SCR_HEIGHT/2;

    public GameController(int spawn_rate) {
        targetController = new TargetController(this, spawn_rate);
        bulletController = new BulletController();
        shooter = new ShooterSprite(shooter_image, CENTER_X, CENTER_Y, 32, 32);
        score = 0;
        state = GameState.GAMEPLAY;
        gameplayGroup = new Group();
        endGroup = new Group();
        gameplayScene = new Scene(gameplayGroup, PiGame.SCR_WIDTH, PiGame.SCR_HEIGHT);
        endScene = new Scene(endGroup, PiGame.SCR_WIDTH, PiGame.SCR_HEIGHT);
        player.setVolume(0.3);
    }

    public Scene getScene(){
        switch(state){
            case GAMEPLAY:
                return this.gameplayScene;
            case END:
                return this.endScene;
            default:
                return this.gameplayScene;
        }
    }

    private void initGameplayScene(){
        gameplayCanvas = new Canvas(PiGame.SCR_WIDTH, PiGame.SCR_HEIGHT);
        gameplayGC = gameplayCanvas.getGraphicsContext2D();
        gameplayGroup.getChildren().addAll(gameplayCanvas, shooter_image);

        Circle circle = new Circle(CENTER_X, CENTER_Y, PiGame.RADIUS);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.AZURE);
        circle.setStrokeWidth(2);
        circle.toBack();
        gameplayGroup.getChildren().add(circle);

        Line line1 = new Line(CENTER_X-16, CENTER_Y, CENTER_X+16, CENTER_Y);
        Line line2 = new Line(CENTER_X, CENTER_Y-16, CENTER_X, CENTER_Y+16);
        line1.setStroke(Color.AZURE);
        line2.setStroke(Color.AZURE);
        line1.setStrokeWidth(2);
        line2.setStrokeWidth(2);
        gameplayGroup.getChildren().addAll(line1, line2);

        gameplayScene.setCursor(Cursor.DEFAULT);
    }

    private void initEndScene(){
        endCanvas = new Canvas(PiGame.SCR_WIDTH, PiGame.SCR_HEIGHT);
        endGC = endCanvas.getGraphicsContext2D();
        endGroup.getChildren().add(endCanvas);
    }

    public void gainScore(){
        score++;
    }

    private void renderText(GraphicsContext gc, double x, double y, int fontSize, Paint textColor, TextAlignment alignment, String text){
        gc.setFill(textColor);
        gc.setFont(new Font("Times New Roman", fontSize));
        gc.setTextAlign(alignment);
        gc.fillText(text, x, y);
    }

    public void runGame(Stage stage){

        initGameplayScene();
        initEndScene();

        new AnimationTimer(){

            @Override
            public void handle(long now) {

                stage.setScene(getScene());

                switch(state){
                    case GAMEPLAY:
                        player.play();
                        gameplayGC.drawImage(background, 0, 0);
                        renderText(gameplayGC,800, 100, 32, Color.WHITE, TextAlignment.LEFT, "Score: "+score);
                        gameplayScene.setOnMouseMoved(
                                event -> shooter.update(event.getX(), event.getY())
                        );
                        shooter.update(bulletController, gameplayGroup);
                        bulletController.update();
                        if(targetController.update(gameplayGroup, bulletController)){
                            state = GameState.END;
                        }
                        break;
                    case END:
                        endGC.drawImage(background, 0, 0);
                        player.stop();
                        renderText(endGC, CENTER_X, CENTER_Y-100, 56, Color.WHITE, TextAlignment.CENTER, "Final Score: "+score);
                        break;
                    default:
                        System.out.println("Who am I? Why am I here?");
                }

            }
        }.start();

        stage.show();

    }
}
