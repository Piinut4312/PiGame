package pi_game;

import javafx.animation.AnimationTimer;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class GameController {

    private TargetController targetController;
    private BulletController bulletController;
    private ShooterSprite shooter;
    private ParticleController particleController;
    private int score;
    private GameState state;

    public static ResourceLocation background_rl = new ResourceLocation("textures/background.jpg");
    public static ResourceLocation shooter_rl = new ResourceLocation("textures/shooter.png");
    public static ResourceLocation bullet_rl = new ResourceLocation("textures/drop.png");
    public static ResourceLocation target_rl = new ResourceLocation("textures/target.png");
    public static ResourceLocation title_rl = new ResourceLocation("textures/title.png");
    public static ResourceLocation click_info_rl = new ResourceLocation("textures/clickInfo.png");
    public static ResourceLocation restart_button_rl = new ResourceLocation("textures/restart.png");
    public static ResourceLocation restart_hovering_rl = new ResourceLocation("textures/restartHover.png");
    public static ResourceLocation menu_button_rl = new ResourceLocation("textures/menu.png");
    public static ResourceLocation menu_hover_rl = new ResourceLocation("textures/menuHover.png");

    public static ResourceLocation press_sound_rl = new ResourceLocation("sounds/button_press.mp3");
    public static ResourceLocation gameplaybgm_rl = new ResourceLocation("sounds/gameplayBGM.mp3");

    public ImageSprite title = new ImageSprite(title_rl, 2*66, 2*92);
    public ImageSprite clickInfo = new ImageSprite(click_info_rl, 713, 41);
    public ImageSprite start_background = new ImageSprite(background_rl, 1200, 800);
    public ImageSprite gameplay_background = new ImageSprite(background_rl, 1200, 800);
    public ImageSprite end_background = new ImageSprite(background_rl, 1200, 800);

    public ButtonSprite restart = new ButtonSprite(restart_button_rl, restart_hovering_rl, 100, 100);
    public ButtonSprite menu = new ButtonSprite(menu_button_rl, menu_hover_rl, 100, 100);

    public static Image shooter_texture = new Image(shooter_rl.toString(), 32, 32, true, false);
    public static Image bullet_texture = new Image(bullet_rl.toString(), 16, 16, true, false);
    public static Image target_texture = new Image(target_rl.toString(), 16, 16, true, false);

    public static ImageView shooter_image = new ImageView(shooter_texture);

    private SceneSprite startSceneSprite = new SceneSprite(){
        @Override
        public void init() {

            title.initFadeOut(1, 1.0, 0.0);
            clickInfo.initFadeOut(1, 1.0, 0.0);
            clickInfo.getFadeOut().setOnFinished(event -> state = GameState.GAMEPLAY);

            title.initFadeIn(1, 0.0, 1.0);
            clickInfo.initFadeIn(1, 0.0, 1.0);

            start_background.setPos(CENTER_X, CENTER_Y);
            title.setPos(CENTER_X, CENTER_Y-100);
            clickInfo.setPos(CENTER_X, CENTER_Y+100);
            this.getGroup().getChildren().addAll(start_background.getImageView(), title.getImageView(), clickInfo.getImageView());

            title.getFadeIn().play();
            clickInfo.getFadeIn().play();
        }

        @Override
        public void update() {
            this.getScene().setOnMouseClicked(event -> {
                gameplay_background.getFadeIn().play();
                PRESS_SOUND.play();
                title.getFadeOut().play();
                clickInfo.getFadeOut().play();
            });
        }
    };

    private SceneSprite gameplaySceneSprite = new SceneSprite(){
        @Override
        public void init() {
            gameplay_background.setPos(CENTER_X, CENTER_Y);
            gameplay_background.initFadeOut(0.3, 1.0, 0.0);
            gameplay_background.initFadeIn(0.3, 0.0, 1.0);
            gameplay_background.getFadeOut().setOnFinished(event -> state = GameState.END);

            this.getGroup().getChildren().addAll(gameplay_background.getImageView(), shooter_image);

            Circle circle = new Circle(CENTER_X, CENTER_Y, PiGame.RADIUS);
            circle.setFill(Color.TRANSPARENT);
            circle.setStroke(Color.AZURE);
            circle.setStrokeWidth(2);
            circle.toBack();
            this.getGroup().getChildren().add(circle);

            Circle smallCircle = new Circle(CENTER_X, CENTER_Y, 10);
            smallCircle.setFill(Color.AZURE);
            smallCircle.setStroke(Color.AZURE);
            smallCircle.setStrokeWidth(2);
            smallCircle.toBack();
            this.getGroup().getChildren().add(smallCircle);

            this.getGc().getCanvas().toFront();

            this.getScene().setOnMouseMoved(
                    event -> shooter.update(event.getX(), event.getY())
            );

            this.getScene().setCursor(Cursor.DEFAULT);
        }

        @Override
        public void update() {
            player.play();
            this.getGc().clearRect(0, 0, PiGame.SCR_WIDTH, PiGame.SCR_HEIGHT);
            renderText(this.getGc(),800, 100, 32, Color.WHITE, TextAlignment.LEFT, "Score: "+score);
            shooter.update(bulletController, this.getGroup());
            bulletController.update();
            if(targetController.update(this.getGroup(), bulletController, particleController)){
                gameplay_background.getFadeOut().play();
            }
        }
    };

    private SceneSprite endSceneSprite = new SceneSprite(){
        @Override
        public void init() {

            end_background.setPos(CENTER_X, CENTER_Y);

            this.getGroup().getChildren().addAll(end_background.getImageView());

            restart.setPos(CENTER_X, CENTER_Y+100);
            restart.clicked(()->{
                gameplay_background.getFadeIn().play();
                PRESS_SOUND.play();
                restart();
                state = GameState.GAMEPLAY;
            });

            menu.setPos(CENTER_X, CENTER_Y+250);
            menu.clicked(()->{
                PRESS_SOUND.play();
                restart();
                title.getFadeIn().play();
                clickInfo.getFadeIn().play();
                state = GameState.START;
            });

            this.getScene().setOnMouseClicked(event -> particleController.addParticleSystem(new ParticleSystem(8, event.getX(), event.getY(), 12, 12, 12, ParticleColors.CLICK, new ParticleSystemProperties(3, 3, 3))));
            this.getGroup().getChildren().addAll(restart.image.getImageView(), menu.image.getImageView());

            end_background.getImageView().toBack();
        }

        @Override
        public void update() {
            player.stop();
            this.getGc().clearRect(0, 0, PiGame.SCR_WIDTH, PiGame.SCR_HEIGHT);
            renderText(this.getGc(), CENTER_X, CENTER_Y-100, 56, Color.WHITE, TextAlignment.CENTER, "Final Score: "+score);
        }
    };

    private static AudioClip PRESS_SOUND = new AudioClip(press_sound_rl.fromFileToString());

    private static final Media GAMEPLAY_BGM = new Media(gameplaybgm_rl.fromFileToString());
    private MediaPlayer player = new MediaPlayer(GAMEPLAY_BGM);

    public static final double CENTER_X = PiGame.SCR_WIDTH/2;
    public static final double CENTER_Y = PiGame.SCR_HEIGHT/2;

    public GameController(int spawn_rate) {
        targetController = new TargetController(this, spawn_rate);
        bulletController = new BulletController();
        shooter = new ShooterSprite(shooter_image, CENTER_X, CENTER_Y, 32, 32);
        particleController = new ParticleController();
        score = 0;
        state = GameState.START;
        player.setVolume(0.3);
        PRESS_SOUND.setVolume(0.3);
    }

    public void restart(){
        targetController.restart();
        bulletController.restart();
        score = 0;
    }

    public Scene getScene(){
        switch(state){
            case START:
                return startSceneSprite.getScene();
            case GAMEPLAY:
                return gameplaySceneSprite.getScene();
            case END:
                return endSceneSprite.getScene();
            default:
                return null;
        }
    }

    public GraphicsContext getGC(){
        switch (state){
            case START:
                return startSceneSprite.getGc();
            case GAMEPLAY:
                return gameplaySceneSprite.getGc();
            case END:
                return endSceneSprite.getGc();
            default:
                return null;
        }
    }

    public void gainScore(){
        score++;
    }

    private void renderText(GraphicsContext gc, double x, double y, int fontSize, Paint textColor, TextAlignment alignment, String text){
        gc.setFill(textColor);
        gc.setGlobalAlpha(1.0);
        gc.setFont(new Font("Times New Roman", fontSize));
        gc.setTextAlign(alignment);
        gc.fillText(text, x, y);
    }

    public void runGame(Stage stage){

        startSceneSprite.init();
        gameplaySceneSprite.init();
        endSceneSprite.init();

        new AnimationTimer(){

            @Override
            public void handle(long now) {

                stage.setScene(getScene());

                switch(state){
                    case START:
                        startSceneSprite.update();
                        break;
                    case GAMEPLAY:
                        gameplaySceneSprite.update();
                        break;
                    case END:
                        endSceneSprite.update();
                        break;
                    default:
                        System.out.println("Who am I? Why am I here?");
                }
                particleController.update(getGC());

            }
        }.start();

        stage.show();
    }
}
