package metrics.crossvalid;

import java.util.List;
import java.util.Random;

import represent.Struct;

public class TrainTestSplit {

    public static Struct<List<String>>
            split(List<String> X, List<String> y, long seed, double percent) {
        if (X.size() != y.size())
            throw new IllegalArgumentException(
                    "X and y's length are different");
        int len = X.size();

        Random random = new Random(seed);
        for (int i = 0; i < len; i++) {
            int randint = random.nextInt(len);
            swap(X, i, randint);
            swap(y, i, randint);
        }

        int splitPos = (int) (len * percent);
        List<String> trainX = X.subList(0, splitPos);
        List<String> testX = X.subList(splitPos, len);
        List<String> trainy = y.subList(0, splitPos);
        List<String> testy = y.subList(splitPos, len);
        @SuppressWarnings("unchecked")
        Struct<List<String>> entry =
                new Struct<List<String>>(trainX, testX, trainy, testy);
        return entry;
    }

    private static void swap(List<String> src, int i, int j) {
        String tmp = src.get(i);
        src.set(i, src.get(j));
        src.set(j, tmp);
    }
}
