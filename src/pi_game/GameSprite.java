package pi_game;

import com.sun.javafx.geom.Vec2d;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;

public abstract class GameSprite {
    private ImageView texture;
    private double x, y, width, height, angle;

    public GameSprite(ImageView texture, double x, double y, double width, double height, double angle) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
    }

    public abstract void update(double newX, double newY);

    public void render(){
        texture.setScaleX(2);
        texture.setScaleY(2);
        texture.setX(x-width/2);
        texture.setY(y-height/2);
        texture.setRotate(angle);
    }

    public Rectangle2D getBoundingBox(){
        return new Rectangle2D(x, y, width, height);
    }

    public boolean intersects(GameSprite sprite){
        return sprite.getBoundingBox().intersects(this.getBoundingBox());
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setPos(Vec2d v){
        this.setX(v.x);
        this.setY(v.y);
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

}
