package pi_game;

import javafx.animation.AnimationTimer;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

public class GameController {

    private TargetController targetController;
    private BulletController bulletController;
    private ShooterSprite shooter;
    private int score;
    private GameState state;

    public static Canvas gameplayCanvas;
    public static GraphicsContext gameplayGC;

    private Group gameplayGroup;

    private Scene gameplayScene;

    public static final double CENTER_X = PiGame.SCR_WIDTH/2;
    public static final double CENTER_Y = PiGame.SCR_HEIGHT/2;

    public GameController(int spawn_rate) {
        targetController = new TargetController(this, spawn_rate);
        bulletController = new BulletController();
        shooter = new ShooterSprite(PiGame.shooter_image, CENTER_X, CENTER_Y, 32, 32);
        score = 0;
        state = GameState.GAMEPLAY;
        gameplayGroup = new Group();
        gameplayScene = new Scene(gameplayGroup, PiGame.SCR_WIDTH, PiGame.SCR_HEIGHT);

    }

    public Scene getScene(GameState gameState){
        if(gameState == GameState.GAMEPLAY){
            return this.gameplayScene;
        }else{
            return this.gameplayScene;
            //nothing here... for now
        }
    }

    private void initGameplayScene(){
        gameplayCanvas = new Canvas(PiGame.SCR_WIDTH, PiGame.SCR_HEIGHT);
        gameplayGC = gameplayCanvas.getGraphicsContext2D();

        gameplayGroup.getChildren().addAll(gameplayCanvas, PiGame.shooter_image);

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

    public void gainScore(){
        score++;
    }

    private void renderScoreText(int x, int y, int fontSize, Paint textColor){
        gameplayGC.setFill(textColor);
        gameplayGC.setFont(new Font("Times New Roman", fontSize));
        gameplayGC.fillText("Score: "+score, x, y);
    }

    public void update(){

        if(state == GameState.GAMEPLAY){
            initGameplayScene();

            //final long start = System.nanoTime();

            new AnimationTimer(){

                @Override
                public void handle(long now) {
                    //double t = (now-start)/1000000000.0;
                    gameplayGC.drawImage(PiGame.background, 0, 0);
                    renderScoreText(800, 100, 32, Color.WHITE);
                    gameplayScene.setOnMouseMoved(
                            event -> shooter.update(event.getX(), event.getY())
                    );
                    shooter.update(bulletController, gameplayGroup);
                    bulletController.update();
                    targetController.update(gameplayGroup, bulletController);
                }
            }.start();
        }

    }
}
