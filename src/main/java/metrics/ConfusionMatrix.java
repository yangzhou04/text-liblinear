package metrics;

import java.util.List;

public class ConfusionMatrix {

    public static double precision(List<String> trueY, List<String> predY) {
        // TODO
        return 0d;
    }
    
    public static double recall(List<String> trueY, List<String> predY) {
        // TODO
        return 0d;
    }
    
    public static double fmeasure(List<String> trueY, List<String> predY) {
        double p = precision(trueY, predY);
        double r = recall(trueY, predY);
        return 2*p*r / (p+r);
    }

}
