package pi_game.controllers;

import javafx.scene.canvas.GraphicsContext;
import pi_game.ParticleSystem;

import java.util.ArrayList;
import java.util.Iterator;

public class ParticleController {

    private ArrayList<ParticleSystem> particleSystems = new ArrayList<>();

    public void update(GraphicsContext gc){
        Iterator<ParticleSystem> iter = particleSystems.iterator();
        while(iter.hasNext()){
            ParticleSystem system = iter.next();
            system.update(gc);
            if(system.isFinished()){
                iter.remove();
            }

        }
    }

    public ArrayList<ParticleSystem> getParticleSystems() {
        return particleSystems;
    }

    public void addParticleSystem(ParticleSystem system){
        this.particleSystems.add(system);
    }

}
