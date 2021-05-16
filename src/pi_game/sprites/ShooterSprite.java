package pi_game.sprites;

import com.sun.javafx.geom.Vec2d;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import pi_game.PiGame;
import pi_game.ResourceLocation;
import pi_game.controllers.BulletController;
import pi_game.controllers.GameController;

public class ShooterSprite extends GameSprite{

    private int shoot_timer;

    private static ResourceLocation shoot_sound_rl = new ResourceLocation("sounds/shoot.mp3");
    private static AudioClip SHOOT_SOUND = new AudioClip(shoot_sound_rl.fromFileToString());

    public ShooterSprite(ImageView texture, double x, double y, double width, double height) {
        super(texture, x, y, width, height, 0);
        this.shoot_timer = 0;
        SHOOT_SOUND.setVolume(0.03);
    }

    public void update(double mouseX, double mouseY){
        if(this.isAlive()){
            this.setPos(solveNewPos(mouseX, mouseY, 32));
            this.setFacing();
        }
    }

    public void update(BulletController bulletController, Group group){
        this.shoot_timer++;
        if(shoot_timer >= 21){
            ImageView bulletImage = new ImageView(GameController.bullet_texture);
            group.getChildren().add(bulletImage);
            bulletController.getBulletList().add(new BulletSprite(bulletImage, this.getX(), this.getY(), 16, 16, this.getAngle(), 10));
            SHOOT_SOUND.play();
            this.shoot_timer = 0;
        }
        this.render();
    }

    public Vec2d solveNewPos(double mouseX, double mouseY, double offset){
        double dx = mouseX- PiGame.SCR_WIDTH/2;
        double dy = mouseY-PiGame.SCR_HEIGHT/2;
        double size = Vec2d.distance(PiGame.SCR_WIDTH/2, PiGame.SCR_HEIGHT/2, mouseX, mouseY);
        double ratio = (PiGame.RADIUS +offset)/size;
        return new Vec2d(PiGame.SCR_WIDTH/2+dx*ratio, PiGame.SCR_HEIGHT/2+dy*ratio);
    }

    public void setFacing(){
        double dx = getX()-PiGame.SCR_WIDTH/2;
        double dy = getY()-PiGame.SCR_HEIGHT/2;
        double angle = (180/Math.PI)*Math.atan(dy/dx)-90;
        if(dx < 0) angle = 180+angle;
        setAngle(angle);
    }

}
