package main;

public class Main {

//	public static double getAccuracy(List<String> textX, List<String> texty) {
//		Struct<List<String>> struct = TrainTestSplit.split(textX, texty,
//		        System.currentTimeMillis(), 0.8);
//
//		List<String> textTrainX = struct.get(TrainTestSplit.TRAIN_X);
//		List<String> textTestX = struct.get(TrainTestSplit.TEST_X);
//		List<String> textTrainy = struct.get(TrainTestSplit.TRAIN_Y);
//		List<String> textTesty = struct.get(TrainTestSplit.TEST_Y);
//
//		Tokenizer tokenizer = new NGrammer(2);
//		List<List<String>> ngramTrainX =
//		        tokenizer.parse(textTrainX, TokenizeType.NGRAM_ALL);
//		// 增加句子长度
//		for (int j = 0; j < ngramTrainX.size(); j++) {
//			ngramTrainX.get(j)
//			        .add(String.valueOf(textTrainX.get(j).length()));
//		}
//		// 增加空格个数
//		for (int j = 0; j < ngramTrainX.size(); j++) {
//			ngramTrainX.get(j)
//			        .add(String.valueOf(
//			                CharMatcher.is(' ').countIn(textTrainX.get(j))));
//		}
//
//		List<List<String>> ngramTestX =
//		        tokenizer.parse(textTestX, TokenizeType.NGRAM_ALL);
//		for (int j = 0; j < ngramTestX.size(); j++) {
//			ngramTestX.get(j)
//			        .add(String.valueOf(textTestX.get(j).length()));
//		}
//		for (int j = 0; j < ngramTestX.size(); j++) {
//			ngramTestX.get(j)
//			        .add(String.valueOf(
//			                CharMatcher.is(' ').countIn(textTestX.get(j))));
//		}
//
//		CountVectorizer countVectorizer = new CountVectorizer();
//		List<List<Entry<Integer, Integer>>> trainX =
//		        (List<List<Entry<Integer, Integer>>>) countVectorizer
//		                .fitTransform(ngramTrainX);
//		List<List<Entry<Integer, Integer>>> testX =
//		        (List<List<Entry<Integer, Integer>>>) countVectorizer
//		                .fitTransform(ngramTestX);
//
//		LabelEncoder labelEncoder = new LabelEncoder();
//		List<Integer> trainy = labelEncoder.fitTransform(textTrainy);
//		List<Integer> testy = labelEncoder.transform(textTesty);
//
//		TextLiblinear model = new TextLiblinear();
//		model.fit(trainX, trainy);
//
//		List<Integer> testybar = model.predictAll(testX);
//
//		return AccuracyScore.calc(testy, testybar);
//	}

//	public static void main(String[] args) throws IOException {
//		List<String> lines = Files.readLines(
//		        new File("data/wenju_label"), Charsets.UTF_8);
//
//		List<String> textX = Lists.newArrayList();
//		List<String> texty = Lists.newArrayList();
//
//		for (String line : lines) {
//			List<String> parts = Splitter.on("\t").splitToList(line);
//			textX.add(parts.get(0));
//			texty.add(parts.get(1));
//		}
//
//		LabelEncoder le = new LabelEncoder();
//		List<Integer> y = le.fitTransform(texty);
//		le.serialize("data/le.txt");
//		Tokenizer tokenizer = new NGrammer(2);
//		List<List<String>> tokenX =
//		        tokenizer.parse(textX, TokenizeType.NGRAM_ALL);
//		CountVectorizer vec = new CountVectorizer();
//		List<List<Entry<Integer, Integer>>> X =
//		        (List<List<Entry<Integer, Integer>>>) vec.fitTransform(tokenX);
//		vec.serialize("data/vec.txt");
//		TextLiblinear model = new TextLiblinear();
//		model.fit(X, y);
//		model.serialize("data/model.txt");
//
		// double maxAcc = 0,
		// minAcc = 1,
		// avgAcc = 0;
		// int times = 30;
		//
		// for (int i = 0; i < times; i++) {
		// double acc = getAccuracy(textX, texty);
		// if (acc < minAcc)
		// minAcc = acc;
		// if (acc > maxAcc)
		// maxAcc = acc;
		// avgAcc += acc;
		// System.out.print(i+",");
		// if ((i+1) % 20 == 0)
		// System.out.println();
		// }

		// System.out.println();
		// System.out.println(maxAcc);
		// System.out.println(minAcc);
		// System.out.println((double) avgAcc / times);
//	}

}
