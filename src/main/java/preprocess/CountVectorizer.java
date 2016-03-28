package preprocess;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.io.Files;
import represent.SparseMatrix;
import represent.SparseVector;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CountVectorizer implements Serializable {

	private static final long serialVersionUID = 2332382468482992263L;
	private Map<String, Integer> vocabularyMap = Maps.newHashMap();
	private int count;
    private String splitPat;
    private Splitter splitter;

    public CountVectorizer(String splitPat) {
        this.splitPat = splitPat;
        splitter = Splitter.onPattern(splitPat).omitEmptyStrings();
    }

	public CountVectorizer fit(String input) {
        count = 0;
        vocabularyMap.clear();

        for (String token : splitter.split(input)) {
            if (!vocabularyMap.containsKey(token))
                vocabularyMap.put(token, count++);
        }
		return this;
	}

	public CountVectorizer fit(List<String> inputs) {
		count = 0;
		vocabularyMap.clear();
		
		for (String input : inputs) {
			for (String token : splitter.split(input)) {
				if (!vocabularyMap.containsKey(token))
					vocabularyMap.put(token, count++);
			}
		}
		
		return this;
	}

//	public <E> CountVectorizer fit(List<E> input)
//	        throws IllegalArgumentException {
//		count = 0;
//		vocabularyMap.clear();
//		if (input.size() == 0)
//			return this;
//
//		if (input.get(0) instanceof String) { // tokens list
//			try {
//				@SuppressWarnings("unchecked")
//				List<String> textx = (List<String>) input;
//				for (String token : textx) {
//					if (!vocabularyMap.containsKey(token))
//						vocabularyMap.put(token, ++count);
//				}
//			} catch (IllegalArgumentException e) {
//				throw new IllegalArgumentException("Wrong argument type: must "
//				        + "be List<String> or List<List<String>>");
//			}
//		} else if (input.get(0) instanceof List) { // list of tokens list
//			try {
//				@SuppressWarnings("unchecked")
//				List<List<String>> textX = (List<List<String>>) input;
//				for (List<String> textx : textX) {
//					for (String token : textx) {
//						if (!vocabularyMap.containsKey(token))
//							vocabularyMap.put(token, ++count);
//					}
//				}
//			} catch (Exception e) {
//				throw new IllegalArgumentException("Wrong argument type: must "
//				        + "be List<String> or List<List<String>>");
//			}
//		} else {
//			throw new IllegalArgumentException("Wrong argument type: must "
//			        + "be List<String> or List<List<String>>");
//		}
//
//		return this;
//	}

	public SparseVector transform(String input) {
		SparseVector sv = new SparseVector(vocabularyMap.size());
		Multiset<String> counter = HashMultiset.create();
		for (String token : splitter.split(input)) {
			counter.add(token);
		}
		
		for (String token : counter.elementSet()) {
			sv.set(vocabularyMap.get(token), counter.count(token));
		}
		return sv;
	}

	public SparseMatrix transform(List<String> inputs) {
		SparseMatrix sm = new SparseMatrix(inputs.size(), vocabularyMap.size());
		for (int i = 0; i < inputs.size(); i++) {
			String input = inputs.get(i);
			Multiset<String> counter = HashMultiset.create();
			for (String token : splitter.split(input)) {
				counter.add(token);
			}
			
			for (String token : counter.elementSet()) {
				sm.set(i, vocabularyMap.get(token), counter.count(token));
			}
		}
		return sm;
	}
	
//	public <E> List<?> transform(List<E> input) {
//		if (input.size() == 0)
//			return Lists.newArrayList();
//
//		Ordering<Entry<Integer, Integer>> asc = Ordering.natural()
//		        .onResultOf(new Function<Entry<Integer, Integer>, Integer>() {
//			        public Integer apply(Entry<Integer, Integer> arg0) {
//				        return arg0.getKey();
//			        }
//		        });
//
//		if (input.get(0) instanceof String) { // token list
//			try {
//				List<Entry<Integer, Integer>> transed = Lists.newArrayList();
//				Multiset<String> counter = HashMultiset.create();
//				@SuppressWarnings("unchecked")
//				List<String> textx = (List<String>) input;
//				for (String token : textx) {
//					counter.add(token);
//				}
//				for (String token : counter.elementSet()) {
//					if (vocabularyMap.containsKey(token)) {
//						Entry<Integer, Integer> entry =
//						        new AbstractMap.SimpleEntry<Integer, Integer>(
//						                vocabularyMap.get(token),
//						                counter.count(token));
//						transed.add(entry);
//					}
//				}
//				Collections.sort(transed, asc);
//				return transed;
//			} catch (Exception e) {
//				throw new IllegalArgumentException("Wrong argument type: must "
//				        + "be List<String> or List<List<String>>");
//			}
//		} else if (input.get(0) instanceof List) { // list of token list
//			List<List<Entry<Integer, Integer>>> transed = Lists.newArrayList();
//			@SuppressWarnings("unchecked")
//			List<List<String>> textX = (List<List<String>>) input;
//			for (List<String> textx : textX) {
//				Multiset<String> counter = HashMultiset.create();
//				List<Entry<Integer, Integer>> xi = Lists.newArrayList();
//				for (String token : textx) {
//					counter.add(token);
//				}
//				for (String token : counter.elementSet()) {
//					if (vocabularyMap.containsKey(token)) {
//						Entry<Integer, Integer> entry =
//						        new AbstractMap.SimpleEntry<Integer, Integer>(
//						                vocabularyMap.get(token),
//						                counter.count(token));
//						xi.add(entry);
//					}
//				}
//				Collections.sort(xi, asc);
//				transed.add(xi);
//			}
//			return transed;
//		} else {
//			throw new IllegalArgumentException("Wrong argument type: must "
//			        + "be List<String> or List<List<String>>");
//		}
//	}

	public SparseMatrix fitTransform(List<String> inputs) {
        return fit(inputs).transform(inputs);
	}

    public SparseVector fitTransform(String input) {
        return fit(input).transform(input);
    }

//	public <E> List<?> fitTransform(List<E> textX) {
//		count = 0;
//		vocabularyMap.clear();
//		fit(textX);
//		return transform(textX);
//	}

	public CountVectorizer serialize(String filename) throws IOException {
		Writer w = Files.asCharSink(new File(filename), Charsets.UTF_8)
		        .openBufferedStream();
		w.append(String.valueOf(this.count)); // line 0
		w.append("\n====\n"); // line 1
		w.append(Joiner.on(",").withKeyValueSeparator(":")
				.join(vocabularyMap)); // line 2
        w.append("\n====\n"); // line 3
        w.append(splitPat); // line 4
		w.close();
		return this;
	}

	public static CountVectorizer deserialize(String filename) throws IOException {
		List<String> lines =
		        Files.readLines(new File(filename), Charsets.UTF_8);
        if (lines.size() != 5 || !lines.get(1).equals("====")
                || !lines.get(3).equals("===="))
            throw new IOException("deserialize file format error");

        // any non-empty string as temporal split pattern
        CountVectorizer deserVec = new CountVectorizer(" ");

        deserVec.splitPat = lines.get(4);
        deserVec.splitter = Splitter.onPattern(deserVec.splitPat).omitEmptyStrings();
        deserVec.count = Integer.parseInt(lines.get(0));
		for (Entry<String, String> e : Splitter.on(",")
		        .withKeyValueSeparator(":").split(lines.get(2)).entrySet())
            deserVec.vocabularyMap.put(e.getKey(), Integer.parseInt(e.getValue()));
		return deserVec;
	}

}
