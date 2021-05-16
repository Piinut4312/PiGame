package pi_game;

import java.util.Random;

public class MathHelper {

    public static double genPosNegRandomDouble(double limit){
        Random random = new Random();
        return limit*(1-2*random.nextDouble());
    }

    public static int genPosNegRandomInt(int limit){
        return (int) Math.round(genPosNegRandomDouble(limit));
    }

}
