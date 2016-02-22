package metrics;

import java.util.List;

public class AccuracyScore {

    public static double calc(List<Integer> trueY, List<Integer> predY) {
        if (trueY.size() != predY.size())
            throw new IllegalArgumentException("trueY.size() != predY.size()");
        
        int right = 0;
        int total = 0;
        for (int i = 0; i < trueY.size(); i++) {
            if (trueY.get(i).compareTo(predY.get(i)) == 0)
                right++;
            total++;
        }
        return (double)right / total;
    }

}
