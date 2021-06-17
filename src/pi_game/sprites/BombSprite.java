package pi_game.sprites;

import com.sun.javafx.geom.Vec2d;
import javafx.scene.image.ImageView;
import pi_game.PiGame;

public class BombSprite extends GameSprite{

    private double vel;

    public BombSprite(ImageView texture, double x, double y, double width, double height, double angle, double vel) {
        super(texture, x, y, width, height, angle);
        this.vel = vel;
        this.setScale(4);
    }

    public boolean update(){
        this.move(new Vec2d(-vel*(Math.cos(Math.PI/2+this.getRadian())), -vel*(Math.sin(Math.PI/2+this.getRadian()))));
        if(this.getDistanceTo(new Vec2d(PiGame.SCR_WIDTH/2, PiGame.SCR_HEIGHT/2)) > PiGame.RADIUS){
            this.kill();
            return true;
        }else{
            this.render();
            return false;
        }
    }

}
