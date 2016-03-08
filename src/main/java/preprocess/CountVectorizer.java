package preprocess;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.io.Files;

public class CountVectorizer implements Serializable {

	private static final long serialVersionUID = 2332382468482992263L;
    private Map<String, Integer> vocabularyMap;
    private int count;

    public CountVectorizer() {
        this.vocabularyMap = Maps.newHashMap();
    }

    public <E> CountVectorizer fit(List<E> input) throws IllegalArgumentException{
        count = 0;
        vocabularyMap.clear();
        if (input.size() == 0)
			return this;

        if (input.get(0) instanceof String) { // tokens list
        	try {
        		@SuppressWarnings("unchecked")
				List<String> textx = (List<String>) input;
        		for (String token : textx) {
	                if (!vocabularyMap.containsKey(token))
	                    vocabularyMap.put(token, ++count);
				}
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Wrong argument type: must "
	        			+ "be List<String> or List<List<String>>");
			}
        } else if (input.get(0) instanceof List) { // list of tokens list 
        	try {
        		@SuppressWarnings("unchecked")
				List<List<String>> textX = (List<List<String>>) input;
        		for (List<String> textx : textX) {
        			for (String token : textx) {
		                if (!vocabularyMap.containsKey(token))
		                    vocabularyMap.put(token, ++count);
					}
        		}
			} catch (Exception e) {
				throw new IllegalArgumentException("Wrong argument type: must "
	        			+ "be List<String> or List<List<String>>");
			}
        } else {
        	throw new IllegalArgumentException("Wrong argument type: must "
        			+ "be List<String> or List<List<String>>");
        }
        
        return this;
    }

    public <E> List<?> transform(List<E> input) {
    	if (input.size() == 0)
			return Lists.newArrayList();
    	
    	Ordering<Entry<Integer, Integer>> asc = Ordering.natural()
                .onResultOf(new Function<Entry<Integer, Integer>, Integer>() {
                    public Integer apply(Entry<Integer, Integer> arg0) {
                        return arg0.getKey();
                    }
                });
    	
    	if (input.get(0) instanceof String) { // token list
    		try {
    			List<Entry<Integer, Integer>> transed = Lists.newArrayList();
    			Multiset<String> counter = HashMultiset.create();
    			@SuppressWarnings("unchecked")
				List<String> textx = (List<String>) input;
    			for (String token : textx) {
    				counter.add(token);
    			}
    			for (String token : counter.elementSet()) {
    				Entry<Integer, Integer> entry =
                            new AbstractMap.SimpleEntry<Integer, Integer>(
                                    vocabularyMap.get(token),
                                    counter.count(token));
    				transed.add(entry);
    			}
    			Collections.sort(transed, asc);
    			return transed;
			} catch (Exception e) {
				throw new IllegalArgumentException("Wrong argument type: must "
	        			+ "be List<String> or List<List<String>>");
			}
    	} else if (input.get(0) instanceof List) { // list of token list
    		List<List<Entry<Integer, Integer>>> transed = Lists.newArrayList();
			@SuppressWarnings("unchecked")
			List<List<String>> textX = (List<List<String>>) input;
    		for (List<String> textx : textX) {
    			Multiset<String> counter = HashMultiset.create();
    			List<Entry<Integer, Integer>> xi= Lists.newArrayList();
    			for (String token : textx) {
    				counter.add(token);
    			}
    			for (String token : counter.elementSet()) {
    				Entry<Integer, Integer> entry =
                            new AbstractMap.SimpleEntry<Integer, Integer>(
                                    vocabularyMap.get(token),
                                    counter.count(token));
    				xi.add(entry);
    			}
    			Collections.sort(xi, asc);
    			transed.add(xi);
    		}
    		return transed;
    	} else {
    		throw new IllegalArgumentException("Wrong argument type: must "
        			+ "be List<String> or List<List<String>>");
    	}
    }

    public <E> List<?> fitTransform(List<E> textX) {
    	count = 0;
        vocabularyMap.clear();
        fit(textX);
        return transform(textX);
    }

    public CountVectorizer serialize(String filename) throws IOException {
		Writer w = Files.asCharSink(new File(filename), Charsets.UTF_8).openBufferedStream();
		w.write(String.valueOf(this.count)); // line 0
		w.write("\n====\n"); // line 1
		w.write(Joiner.on(",").withKeyValueSeparator(":").join(vocabularyMap)); // line 2
		w.close();
		return this;
	}
	
	public CountVectorizer deserialize(String filename) throws IOException {
		List<String> lines = Files.readLines(new File(filename), Charsets.UTF_8);
		if (lines.size() != 3 || !lines.get(1).equals("====")) 
			throw new IOException("deserialize file format error");
		
		this.count = Integer.parseInt(lines.get(0));
		for (Entry<String, String> e : Splitter.on(",").withKeyValueSeparator(":").split(lines.get(2)).entrySet())
			this.vocabularyMap.put(e.getKey(), Integer.parseInt(e.getValue()));
		return this;
	}

}
