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
    private final int spawn_rate;
    private int cur_spawn_rate;
    private int spawn_timer;
    private boolean isBombTriggered;
    private Random rng;
    private GameController parent;
    private int level = 0;
    private static final int max_level = 10;
    private static int[] threshold = new int[]{20, 40, 60, 80, 100, 140, 180, 240, 320, 480};

    private static ResourceLocation explosion_sound_rl = new ResourceLocation("sounds/explosion.mp3");
    private static AudioClip EXPLOSION_SOUND = new AudioClip(explosion_sound_rl.fromFileToString());

    public TargetController(GameController parent, int spawn_rate){
        targets = new ArrayList<>();
        this.spawn_rate = spawn_rate;
        this.cur_spawn_rate = spawn_rate;
        rng = new Random();
        this.parent = parent;
        EXPLOSION_SOUND.setVolume(0.1);
        this.isBombTriggered = false;
    }

    public void restart(){
        for(TargetSprite target : targets){
            target.kill();
        }
        this.targets.clear();
        this.cur_spawn_rate = spawn_rate;
        this.spawn_timer = 0;
    }

    public boolean update(Group group, BulletController bulletController, ParticleController particleController){
        spawn_timer++;
        if(rng.nextFloat() < 0.15){
            spawn_timer++;
        }
        if(spawn_timer >= cur_spawn_rate){
            ImageView targetImage = new ImageView(GameController.target_texture);
            group.getChildren().add(targetImage);
            targets.add(new TargetSprite(targetImage, PiGame.SCR_WIDTH/2, PiGame.SCR_HEIGHT/2, 16, 16, 360*rng.nextFloat(), 1.6));
            spawn_timer = 0;
        }
        if(level < max_level && parent.getScore() >= threshold[level]){
            level++;
            cur_spawn_rate -= 3;
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
                        particleController.addParticleSystem(new ParticleSystem(20, target.getX(), target.getY(), 15, 14, 24, ParticleColors.EXPLOSION, new ParticleSystemProperties(5, 4, 10)));
                        break;
                    }
                }
                if(this.getIsBombTriggered()) {
                    target.kill();
                    particleController.addParticleSystem(new ParticleSystem(20, target.getX(), target.getY(), 15, 14, 24, ParticleColors.EXPLOSION, new ParticleSystemProperties(5, 4, 10)));
                    parent.gainScore();
                }
            }else{
                targets.remove(i);
            }
        }
        this.setIsBombTriggered(false);
        return false;
    }

    public void setIsBombTriggered(boolean b) {
        isBombTriggered = b;
    }

    public boolean getIsBombTriggered() {
        return isBombTriggered;
    }

}
