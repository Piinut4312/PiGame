package pi_game.controllers;

import pi_game.sprites.BulletSprite;

import java.util.ArrayList;

public class BulletController {

    private ArrayList<BulletSprite> bullets;

    public BulletController(){
        this.bullets = new ArrayList<>();
    }

    public ArrayList<BulletSprite> getBulletList(){
        return this.bullets;
    }

    public void restart(){
        for(BulletSprite bullet : bullets){
            bullet.kill();
        }
        this.bullets.clear();
    }

    public void update(){
        for(int i = 0; i < bullets.size(); i++){
            BulletSprite bullet = bullets.get(i);
            if(bullet.isAlive()){
                bullet.update();
            }else{
                bullets.remove(i);
            }
        }
    }
}
