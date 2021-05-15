package pi_game;

import javafx.scene.Group;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Random;

public class TargetController {

    private ArrayList<TargetSprite> targets;
    private final int SPAWN_RATE;
    private int spawn_timer;
    private Random rng;
    private GameController parent;

    public TargetController(GameController parent, int spawn_rate){
        targets = new ArrayList<>();
        SPAWN_RATE = spawn_rate;
        rng = new Random();
        this.parent = parent;
    }

    public void update(Group group, BulletController bulletController){
        spawn_timer++;
        if(spawn_timer >= SPAWN_RATE){
            ImageView targetImage = new ImageView(PiGame.target_texture);
            group.getChildren().add(targetImage);
            targets.add(new TargetSprite(targetImage, PiGame.SCR_WIDTH/2, PiGame.SCR_HEIGHT/2, 16, 16, 360*rng.nextFloat(), 2));
            spawn_timer = 0;
        }
        for(int i = 0; i < targets.size(); i++){
            TargetSprite target = targets.get(i);
            if(target.isAlive()){
                target.update();
                for (BulletSprite bullet : bulletController.getBulletList()) {
                    if (target.intersects(bullet)) {
                        bullet.kill();
                        target.kill();
                        parent.gainScore();
                        break;
                    }
                }
            }else{
                targets.remove(i);
            }
        }
    }

}
