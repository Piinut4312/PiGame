package pi_game;

import java.io.File;

public class ResourceLocation {
    public String resource;

    public ResourceLocation(String resource){
        this.resource = resource;
    }

    public String toString(){
        return "pi_game/resources/"+resource;
    }

    public File toFile(){
        return new File("src/"+toString());
    }

    public String fromFileToString(){
        return toFile().toURI().toString();
    }

}
