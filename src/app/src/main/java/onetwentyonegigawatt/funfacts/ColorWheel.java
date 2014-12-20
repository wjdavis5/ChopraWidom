package onetwentyonegigawatt.funfacts;

import java.util.Random;

/**
 * Created by William.Davis on 12/18/2014.
 */
public class ColorWheel {

    public String[] mColors = {
            "#39add1",
            "#3079ab",
            "#c25975",
            "#e15258",
            "#f9845b",
            "#838cc7",
            "#7d669e",
            "#53bbb4",
            "#51b46d",
            "#e0ab18",
            "#637a91",
            "#f092b0",
            "#b7c0c7"
    };
    private Random random = new Random();
    public String GetColor(){
        return mColors[random.nextInt(mColors.length)];
    }
}
