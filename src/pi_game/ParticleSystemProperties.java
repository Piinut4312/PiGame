package pi_game;

public class ParticleSystemProperties {

    private double vel_offset;
    private double size_offset;
    private int age_offset;

    public ParticleSystemProperties(double vel_offset, double size_offset, int age_offset){
        this.vel_offset = vel_offset;
        this.size_offset = size_offset;
        this.age_offset = age_offset;
    }

    public double genVelOffset(){
        return MathHelper.genPosNegRandomDouble(vel_offset);
    }

    public double genSizeOffset(){
        return MathHelper.genPosNegRandomDouble(size_offset);
    }

    public int genAgeOffset(){
        return MathHelper.genPosNegRandomInt(age_offset);
    }
}
