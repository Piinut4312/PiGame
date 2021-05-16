package pi_game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class ParticleSprite {

    private double x, y, angle, vel, size;
    private int age;
    private final int MAX_AGE;
    private Paint color;
    private boolean isDead;

    public ParticleSprite(double x, double y, double angle, double vel, double size, int max_age, Paint color){
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.age = 0;
        this.MAX_AGE = max_age;
        this.vel = vel;
        this.size = size;
        this.color = color;
        this.isDead = false;
    }

    public void move(){
        this.x += vel*Math.cos(Math.toRadians(angle));
        this.y += vel*Math.sin(Math.toRadians(angle));
        this.age++;
        if(this.age >= MAX_AGE){
            this.isDead = true;
        }
    }

    public void render(GraphicsContext gc){
        gc.setFill(color);
        gc.setGlobalAlpha(1-(1.0*age)/MAX_AGE);
        gc.fillOval(x, y, size, size);
    }

    public boolean isAlive(){
        return !this.isDead;
    }

}