package pi_game;

import com.sun.javafx.geom.Vec2d;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class GameSprite {
    private Image texture;
    private double x, y, width, height;

    public GameSprite(Image texture, double x, double y, double width, double height) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void update(double newX, double newY);

    public void render(GraphicsContext gc){
        gc.drawImage(texture, x-width/2, y-height/2);
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
}
