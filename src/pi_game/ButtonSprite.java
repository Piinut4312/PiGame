package pi_game;

import javafx.scene.image.Image;

public class ButtonSprite{

    ImageSprite image;
    Image normalTexture, hoverTexture;

    public ButtonSprite(ResourceLocation rl1, ResourceLocation rl2, int width, int height) {
        this.image = new ImageSprite(rl1, width, height);
        this.normalTexture = new Image(rl1.toString(), width, height, true, false);
        this.hoverTexture = new Image(rl2.toString(), width, height, true, false);
        this.image.getImageView().setOnMouseEntered(event -> this.image.getImageView().setImage(hoverTexture));
        this.image.getImageView().setOnMouseExited(event -> this.image.getImageView().setImage(normalTexture));
    }

    public void clicked(Runnable runnable){
        this.image.getImageView().setOnMouseClicked(event -> runnable.run());
    }

    public void setPos(double centerX, double centerY){
        image.setPos(centerX, centerY);
    }
}
