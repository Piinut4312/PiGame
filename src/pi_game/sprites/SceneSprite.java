package pi_game.sprites;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import pi_game.PiGame;

public class SceneSprite {

    private Canvas canvas;
    private GraphicsContext gc;
    private Group group;
    private Scene scene;

    public SceneSprite(){
        this(PiGame.SCR_WIDTH, PiGame.SCR_HEIGHT);
    }

    public SceneSprite(double width, double height){
        this.canvas = new Canvas(width, height);
        this.gc = canvas.getGraphicsContext2D();
        this.group = new Group();
        this.scene = new Scene(this.group, PiGame.SCR_WIDTH, PiGame.SCR_HEIGHT);
        this.group.getChildren().add(this.canvas);
    }

    public void init(){}

    public void update(){}

    public GraphicsContext getGc() {
        return this.gc;
    }

    public Group getGroup() {
        return this.group;
    }

    public Scene getScene() {
        return this.scene;
    }
}
