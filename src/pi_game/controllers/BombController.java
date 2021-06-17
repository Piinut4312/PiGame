package pi_game.controllers;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import pi_game.*;
import pi_game.sprites.BulletSprite;
import pi_game.sprites.TargetSprite;

import java.util.ArrayList;
import java.util.Random;

public class BombController {

    private ArrayList<TargetSprite> bombs;
    private final int SPAWN_RATE;
    private int spawn_timer;
    private Random rng;
    private GameController parent;

    private static ResourceLocation explosion_sound_rl = new ResourceLocation("sounds/explosion.mp3");
    private static AudioClip EXPLOSION_SOUND = new AudioClip(explosion_sound_rl.fromFileToString());

    public BombController(GameController parent, int spawn_rate){
        bombs = new ArrayList<>();
        SPAWN_RATE = spawn_rate;
        rng = new Random();
        this.parent = parent;
        EXPLOSION_SOUND.setVolume(0.1);
    }

    public void restart(){
        for(TargetSprite bomb : bombs){
            bomb.kill();
        }
        this.bombs.clear();
        this.spawn_timer = 0;
    }

    public boolean update(Group group, BulletController bulletController, ParticleController particleController){
        spawn_timer++;
        if(spawn_timer >= SPAWN_RATE){
            ImageView bombImage = new ImageView(GameController.bomb_texture);
            group.getChildren().add(bombImage);
            bombs.add(new TargetSprite(bombImage, PiGame.SCR_WIDTH/2, PiGame.SCR_HEIGHT/2, 16, 16, 360*rng.nextFloat(), 3));
            spawn_timer = 0;
        }
        for(int i = 0; i < bombs.size(); i++){
            TargetSprite bomb = bombs.get(i);

            if(bomb.isAlive()){
                if(bomb.update()){
                    return true;
                }
                for (BulletSprite bullet : bulletController.getBulletList()) {
                    if (bomb.intersects(bullet)) {
                        bullet.kill();
                        bomb.kill();
                        EXPLOSION_SOUND.play();
                        particleController.addParticleSystem(new ParticleSystem(40, bomb.getX(), bomb.getY(), 100, 40, 60, ParticleColors.EXPLOSION, new ParticleSystemProperties(40, 20, 12)));
                        return false;
                    }
                }
            }
            else{
                bombs.remove(i);
            }
        }
        return true;
    }

}
