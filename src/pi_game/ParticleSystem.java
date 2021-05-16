package pi_game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pi_game.sprites.ParticleSprite;

import java.util.ArrayList;
import java.util.Random;

public class ParticleSystem {

    private int amount;
    private double x, y;
    private ArrayList<ParticleSprite> particles;
    private Random rng = new Random();
    private boolean finished;

    public ParticleSystem(int amount, double x, double y, double vel, double size, int max_age, ArrayList<Color> colors, ParticleSystemProperties properties){
        this.amount = amount;
        this.x = x;
        this.y = y;
        particles = new ArrayList<>();
        for(int i = 0; i < amount; i++){
            particles.add(new ParticleSprite(x, y, 360*rng.nextDouble(), vel+properties.genVelOffset(), size+properties.genSizeOffset(), max_age+properties.genAgeOffset(), colors.get(rng.nextInt(colors.size()))));
        }
        this.finished = false;
    }

    public void update(GraphicsContext gc){
        if(!finished){
            for(int i = 0; i < amount; i++){
                ParticleSprite particle = particles.get(i);
                particle.move();
                particle.render(gc);
                finished = finished || particle.isAlive();
            }
            finished = !finished;
        }
    }

    public boolean isFinished(){
        return this.finished;
    }

}
