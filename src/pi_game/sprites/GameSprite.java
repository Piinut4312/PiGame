package pi_game.sprites;

import com.sun.javafx.geom.Vec2d;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;

public abstract class GameSprite {
    private ImageView texture;
    private double x, y, width, height, angle;
    private boolean isDead;
    private double scale;

    public GameSprite(ImageView texture, double x, double y, double width, double height, double angle) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
        this.scale = 2;
        this.isDead = false;
    }

    public void render(){
        texture.setScaleX(scale);
        texture.setScaleY(scale);
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

    public void move(Vec2d v){
        this.setX(this.x + v.x);
        this.setY(this.y + v.y);
    }

    public double getDistanceTo(Vec2d v){
        return v.distance(new Vec2d(this.x, this.y));
    }

    public boolean isAlive(){
        return !this.isDead;
    }

    public void kill(){
        this.isDead = true;
        texture.setVisible(false);
    }

    public double getAngle() {
        return angle;
    }

    public double getRadian(){
        return Math.toRadians(angle);
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

}
