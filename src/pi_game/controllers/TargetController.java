package pi_game.controllers;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import pi_game.*;
import pi_game.sprites.BulletSprite;
import pi_game.sprites.TargetSprite;

import java.util.ArrayList;
import java.util.Random;

public class TargetController {

    private ArrayList<TargetSprite> targets;
    private final int SPAWN_RATE;
    private int spawn_timer;
    private Random rng;
    private GameController parent;

    private static ResourceLocation explosion_sound_rl = new ResourceLocation("sounds/explosion.mp3");
    private static AudioClip EXPLOSION_SOUND = new AudioClip(explosion_sound_rl.fromFileToString());

    public TargetController(GameController parent, int spawn_rate){
        targets = new ArrayList<>();
        SPAWN_RATE = spawn_rate;
        rng = new Random();
        this.parent = parent;
        EXPLOSION_SOUND.setVolume(0.1);
    }

    public void restart(){
        for(TargetSprite target : targets){
            target.kill();
        }
        this.targets.clear();
        this.spawn_timer = 0;
    }

    public boolean update(Group group, BulletController bulletController, ParticleController particleController){
        spawn_timer++;
        if(spawn_timer >= SPAWN_RATE){
            ImageView targetImage = new ImageView(GameController.target_texture);
            group.getChildren().add(targetImage);
            targets.add(new TargetSprite(targetImage, PiGame.SCR_WIDTH/2, PiGame.SCR_HEIGHT/2, 16, 16, 360*rng.nextFloat(), 2));
            spawn_timer = 0;
        }
        for(int i = 0; i < targets.size(); i++){
            TargetSprite target = targets.get(i);
            if(target.isAlive()){
                if(target.update()){
                    return true;
                }
                for (BulletSprite bullet : bulletController.getBulletList()) {
                    if (target.intersects(bullet)) {
                        bullet.kill();
                        target.kill();
                        EXPLOSION_SOUND.play();
                        parent.gainScore();
                        particleController.addParticleSystem(new ParticleSystem(16, target.getX(), target.getY(), 15, 12, 20, ParticleColors.EXPLOSION, new ParticleSystemProperties(5, 4, 10)));
                        break;
                    }
                }
            }else{
                targets.remove(i);
            }
        }
        return false;
    }

}
