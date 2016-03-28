package metrics.crossvalid;

import java.util.AbstractMap;
import java.util.List;
import java.util.Random;

import represent.Struct;

public class TrainTestSplit {

	public static final String TRAIN_X = "TRAIN_X";
	public static final String TRAIN_Y = "TRAIN_Y";
	public static final String TEST_X = "TEST_X";
	public static final String TEST_Y = "TEST_Y";

	public static Struct<List<String>>
	        split(List<String> X, List<String> y, double percent) {
		return split(X, y, System.currentTimeMillis(), percent);
	}

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
		        new Struct<List<String>>(
		                new AbstractMap.SimpleEntry<String, List<String>>(
		                        TRAIN_X, trainX),
		                new AbstractMap.SimpleEntry<String, List<String>>(
		                        TEST_X, testX),
		                new AbstractMap.SimpleEntry<String, List<String>>(
		                        TRAIN_Y, trainy),
		                new AbstractMap.SimpleEntry<String, List<String>>(
		                        TEST_Y, testy));
		return entry;
	}

	private static void swap(List<String> src, int i, int j) {
		String tmp = src.get(i);
		src.set(i, src.get(j));
		src.set(j, tmp);
	}
}
