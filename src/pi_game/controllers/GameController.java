package pi_game.controllers;

import javafx.animation.AnimationTimer;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import pi_game.*;
import pi_game.sprites.ButtonSprite;
import pi_game.sprites.ImageSprite;
import pi_game.sprites.SceneSprite;
import pi_game.sprites.ShooterSprite;

public class GameController {

    private TargetController targetController;
    private BulletController bulletController;
    private BombController bombController;
    private ShooterSprite shooter;
    private ParticleController particleController;
    private int score;
    private GameState state;

    public static ResourceLocation background_rl = new ResourceLocation("textures/background.jpg");
    public static ResourceLocation shooter_rl = new ResourceLocation("textures/shooter.png");
    public static ResourceLocation bullet_rl = new ResourceLocation("textures/bullet.png");
    public static ResourceLocation target_rl = new ResourceLocation("textures/target.png");
    public static ResourceLocation bomb_rl = new ResourceLocation("textures/bomb.png");
    public static ResourceLocation title_rl = new ResourceLocation("textures/title.png");
    public static ResourceLocation click_info_rl = new ResourceLocation("textures/clickInfo.png");
    public static ResourceLocation restart_button_rl = new ResourceLocation("textures/restart.png");
    public static ResourceLocation restart_hovering_rl = new ResourceLocation("textures/restartHover.png");
    public static ResourceLocation menu_button_rl = new ResourceLocation("textures/menu.png");
    public static ResourceLocation menu_hover_rl = new ResourceLocation("textures/menuHover.png");
    public static ResourceLocation play_button_rl = new ResourceLocation("textures/play.png");
    public static ResourceLocation play_hover_rl = new ResourceLocation("textures/playHover.png");
    public static ResourceLocation setting_button_rl = new ResourceLocation("textures/setting.png");
    public static ResourceLocation setting_hover_rl = new ResourceLocation("textures/settingHover.png");

    public static ResourceLocation press_sound_rl = new ResourceLocation("sounds/button_press.mp3");
    public static ResourceLocation gameplaybgm_rl = new ResourceLocation("sounds/gameplayBGM.mp3");

    public ImageSprite title = new ImageSprite(title_rl, 2*66, 2*92);
    public ImageSprite clickInfo = new ImageSprite(click_info_rl, 713, 41);
    public ImageSprite start_background = new ImageSprite(background_rl, 1200, 800);
    public ImageSprite gameplay_background = new ImageSprite(background_rl, 1200, 800);
    public ImageSprite end_background = new ImageSprite(background_rl, 1200, 800);
    public ImageSprite pause_background = new ImageSprite(background_rl, 1200, 800);

    public ButtonSprite end_restart = new ButtonSprite(restart_button_rl, restart_hovering_rl, 100, 100);
    public ButtonSprite end_menu = new ButtonSprite(menu_button_rl, menu_hover_rl, 100, 100);
    public ButtonSprite pause_restart = new ButtonSprite(restart_button_rl, restart_hovering_rl, 100, 100);
    public ButtonSprite pause_menu = new ButtonSprite(menu_button_rl, menu_hover_rl, 100, 100);
    public ButtonSprite pause_play = new ButtonSprite(play_button_rl, play_hover_rl, 100, 100);
    public ButtonSprite start_setting = new ButtonSprite(setting_button_rl, setting_hover_rl, 100, 100);

    public static Image shooter_texture = new Image(shooter_rl.toString(), 32, 32, true, false);
    public static Image bullet_texture = new Image(bullet_rl.toString(), 16, 16, true, false);
    public static Image target_texture = new Image(target_rl.toString(), 16, 16, true, false);
    public static Image bomb_texture = new Image(bomb_rl.toString(), 14, 14, true, false);


    public static ImageView shooter_image = new ImageView(shooter_texture);

    private SceneSprite startSceneSprite = new SceneSprite(1, 1){

        private void handleClicking(MouseEvent event){
            particleController.addParticleSystem(new ParticleSystem(8, event.getX(), event.getY(), 12, 12, 12, ParticleColors.CLICK, new ParticleSystemProperties(3, 3, 3)));
            gameplay_background.getFadeIn().play();
            PRESS_SOUND.play();
            title.getFadeOut().play();
            clickInfo.getFadeOut().play();
        }

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

            start_background.getImageView().toBack();

            start_setting.setPos(800, 100);
            this.getGroup().getChildren().add(start_setting.getImageView());

            title.getImageView().setOnMouseClicked(this::handleClicking);

            clickInfo.getImageView().setOnMouseClicked(this::handleClicking);

           start_background.getImageView().setOnMouseClicked(this::handleClicking);

            title.getFadeIn().play();
            clickInfo.getFadeIn().play();
        }

        @Override
        public void update() {
            this.getGc().clearRect(0, 0, PiGame.SCR_WIDTH, PiGame.SCR_HEIGHT);
        }
    };

    public int getScore() {
        return score;
    }

    private SceneSprite gameplaySceneSprite = new SceneSprite(){
        @Override
        public void init() {
            gameplay_background.setPos(CENTER_X, CENTER_Y);
            gameplay_background.initFadeOut(0.3, 1.0, 0.4);
            gameplay_background.initFadeIn(0.3, 0.4, 1.0);
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

            this.getScene().setOnMouseClicked(
                    event -> {
                        state = GameState.PAUSE;
                        player.pause();
                    }
            );

            this.getScene().setCursor(Cursor.DEFAULT);
        }

        @Override
        public void update() {
            player.play();
            this.getGc().clearRect(0, 0, PiGame.SCR_WIDTH, PiGame.SCR_HEIGHT);
            renderText(this.getGc(),800, 100, 32, "Times New Roman", Color.WHITE, TextAlignment.LEFT, "Score: "+score);
            shooter.update(bulletController, this.getGroup());
            bulletController.update();
            if(!bombController.update(this.getGroup(), bulletController, particleController)) {
                targetController.setIsBombTriggered(true);
            }
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

            end_restart.setPos(CENTER_X, CENTER_Y+100);
            end_restart.clicked(()->{
                gameplay_background.getFadeIn().play();
                PRESS_SOUND.play();
                restart();
                state = GameState.GAMEPLAY;
            });

            end_menu.setPos(CENTER_X, CENTER_Y+250);
            end_menu.clicked(()->{
                PRESS_SOUND.play();
                restart();
                title.getFadeIn().play();
                clickInfo.getFadeIn().play();
                state = GameState.START;
            });

            this.getScene().setOnMouseClicked(event -> particleController.addParticleSystem(new ParticleSystem(8, event.getX(), event.getY(), 12, 12, 12, ParticleColors.CLICK, new ParticleSystemProperties(3, 3, 3))));
            this.getGroup().getChildren().addAll(end_restart.getImageView(), end_menu.getImageView());

            end_background.getImageView().toBack();
        }

        @Override
        public void update() {
            player.stop();
            this.getGc().clearRect(0, 0, PiGame.SCR_WIDTH, PiGame.SCR_HEIGHT);
            renderText(this.getGc(), CENTER_X, CENTER_Y-100, 56, "Times New Roman", Color.WHITE, TextAlignment.CENTER, "Final Score: "+score);
        }
    };

    private SceneSprite pauseSceneSprite = new SceneSprite(){

        private int frameWidth = 400;
        private int frameHeight = 600;
        private int arcRadius = 40;

        @Override
        public void init() {
            pause_background.setPos(CENTER_X, CENTER_Y);
            this.getGroup().getChildren().add(pause_background.getImageView());

            Rectangle frame = new Rectangle();
            frame.setX(CENTER_X-frameWidth/2);
            frame.setY(CENTER_Y-frameHeight/2);
            frame.setWidth(frameWidth);
            frame.setHeight(frameHeight);
            frame.setArcWidth(arcRadius);
            frame.setArcHeight(arcRadius);
            frame.setFill(Color.TRANSPARENT);
            frame.setStroke(Color.WHITE);
            frame.setStrokeWidth(4);

            pause_restart.setPos(CENTER_X, CENTER_Y+75);
            pause_menu.setPos(CENTER_X, CENTER_Y+200);
            pause_play.setPos(CENTER_X, CENTER_Y-50);

            pause_menu.clicked(()->{
                PRESS_SOUND.play();
                restart();
                title.getFadeIn().play();
                clickInfo.getFadeIn().play();
                state = GameState.START;
            });

            pause_restart.clicked(()->{
                PRESS_SOUND.play();
                gameplay_background.getFadeIn().play();
                player.stop();
                restart();
                state = GameState.GAMEPLAY;
            });

            pause_play.clicked(()->{
                PRESS_SOUND.play();
                gameplay_background.getFadeIn().play();
                state = GameState.GAMEPLAY;
            });

            this.getScene().setOnMouseClicked(event -> particleController.addParticleSystem(new ParticleSystem(8, event.getX(), event.getY(), 12, 12, 12, ParticleColors.CLICK, new ParticleSystemProperties(3, 3, 3))));
            this.getGroup().getChildren().addAll(frame, pause_restart.getImageView(), pause_menu.getImageView(), pause_play.getImageView());

            pause_background.getImageView().toBack();
        }

        @Override
        public void update() {
            this.getGc().clearRect(0, 0, PiGame.SCR_WIDTH, PiGame.SCR_HEIGHT);
            renderText(this.getGc(), CENTER_X, CENTER_Y-175, 48, "Times New Roman", Color.WHITE, TextAlignment.CENTER, "Paused");
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
        bombController = new BombController(this, 12*spawn_rate);
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
        bombController.restart();
        score = 0;
        player.stop();
    }

    public Scene getScene(){
        switch(state){
            case START:
                return startSceneSprite.getScene();
            case GAMEPLAY:
                return gameplaySceneSprite.getScene();
            case END:
                return endSceneSprite.getScene();
            case PAUSE:
                return pauseSceneSprite.getScene();
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
            case PAUSE:
                return pauseSceneSprite.getGc();
            default:
                return null;
        }
    }

    public void gainScore(){
        score++;
    }

    private void renderText(GraphicsContext gc, double x, double y, int fontSize, String font, Paint textColor, TextAlignment alignment, String text){
        gc.setFill(textColor);
        gc.setGlobalAlpha(1.0);
        gc.setFont(new Font(font, fontSize));
        gc.setTextAlign(alignment);
        gc.fillText(text, x, y);
    }

    public void runGame(Stage stage){

        startSceneSprite.init();
        gameplaySceneSprite.init();
        endSceneSprite.init();
        pauseSceneSprite.init();

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
                    case PAUSE:
                        pauseSceneSprite.update();
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