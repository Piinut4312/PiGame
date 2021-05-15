package pi_game;

import com.sun.javafx.geom.Vec2d;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;

import java.io.File;
import java.util.ArrayList;

public class ShooterSprite extends GameSprite{

    private int shoot_timer;

    private static AudioClip SHOOT_SOUND = new AudioClip(new File("src/pi_game/shoot.mp3").toURI().toString());

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
        if(shoot_timer > 20){
            ImageView bulletImage = new ImageView(PiGame.bullet_texture);
            group.getChildren().add(bulletImage);
            bulletController.getBulletList().add(new BulletSprite(bulletImage, this.getX(), this.getY(), 16, 16, this.getAngle(), 10));
            SHOOT_SOUND.play();
            this.shoot_timer = 0;
        }
        this.render();
    }

    public Vec2d solveNewPos(double mouseX, double mouseY, double offset){
        double dx = mouseX-PiGame.SCR_WIDTH/2;
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
