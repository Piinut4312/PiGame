package pi_game;

import com.sun.javafx.geom.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShooterSprite extends GameSprite{

    private int shoot_timer;
    private boolean isDead;

    public ShooterSprite(ImageView texture, double x, double y, double width, double height) {
        super(texture, x, y, width, height, 0);
        this.shoot_timer = 10;
        this.isDead = false;
    }

    public void update(double mouseX, double mouseY){
        if(!this.isDead){
            this.setPos(solveNewPos(mouseX, mouseY, 32));
            this.setFacing();
        }
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
