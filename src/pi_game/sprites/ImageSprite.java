package pi_game.sprites;

import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import pi_game.ResourceLocation;

public class ImageSprite {

    private ResourceLocation resource;
    private Image texture;
    private ImageView image;
    private FadeTransition fadeOut;
    private FadeTransition fadeIn;

    public ImageSprite(ResourceLocation resource){
        this.resource = resource;
        this.texture = new Image(resource.toString());
        this.image = new ImageView(texture);
    }

    public ImageSprite(ResourceLocation resource, int width, int height){
        this.resource = resource;
        this.texture = new Image(resource.toString(), width, height, true, false);
        this.image = new ImageView(texture);
    }

    public ImageView getImageView(){
        return this.image;
    }

    public void setPos(double centerX, double centerY){
        image.setTranslateX(centerX-texture.getWidth()/2);
        image.setTranslateY(centerY-texture.getHeight()/2);
    }

    public FadeTransition getFadeOut(){
        return this.fadeOut;
    }

    public FadeTransition getFadeIn(){
        return this.fadeIn;
    }

    public void initFadeOut(double duration, double start, double end){
        fadeOut = new FadeTransition(Duration.seconds(duration), this.image);
        fadeOut.setFromValue(start);
        fadeOut.setToValue(end);
    }

    public void initFadeIn(double duration, double start, double end){
        fadeIn = new FadeTransition(Duration.seconds(duration), this.image);
        fadeIn.setFromValue(start);
        fadeIn.setToValue(end);
    }

}
