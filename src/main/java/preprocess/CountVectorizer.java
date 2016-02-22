package preprocess;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

public class CountVectorizer {

    private Multiset<String> counter;
    private Map<String, Integer> vocabularyMap;
    private int count;

    public CountVectorizer() {
        this.counter = HashMultiset.create();
        this.vocabularyMap = Maps.newHashMap();
    }

    public void fit(List<List<String>> textX) {
        counter.clear();
        for (List<String> textx : textX) {
            for (String token : textx) {
                counter.add(token);
                if (!vocabularyMap.containsKey(token))
                    vocabularyMap.put(token, ++count);
            }
        }
    }

    public List<List<Entry<Integer, Integer>>>
            transform(List<List<String>> textX) {
        List<List<Entry<Integer, Integer>>> ret = Lists.newArrayList();
        Ordering<Entry<Integer, Integer>> asc = Ordering.natural()
                .onResultOf(new Function<Entry<Integer, Integer>, Integer>() {
                    public Integer apply(Entry<Integer, Integer> arg0) {
                        return arg0.getKey();
                    }
                });

        for (List<String> textx : textX) {
            List<Entry<Integer, Integer>> x = Lists.newArrayList();
            Set<String> textxSet = Sets.newHashSet(textx);
            for (String token : textxSet) {
                if (counter.elementSet().contains(token)) {
                    Entry<Integer, Integer> entry =
                            new AbstractMap.SimpleEntry<Integer, Integer>(
                                    vocabularyMap.get(token),
                                    counter.count(token));
                    x.add(entry);
                }
            }
            Collections.sort(x, asc);
            ret.add(x);
        }

        return ret;
    }

    public List<List<Entry<Integer, Integer>>>
            fitTransform(List<List<String>> textX) {
        counter.clear();
        fit(textX);
        return transform(textX);
    }

    public static void serialize(CountVectorizer vectorizer, String filename)
            throws IOException {
        FileOutputStream fileOut = new FileOutputStream(filename);
        ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
        outStream.writeObject(vectorizer);
        outStream.close();
        fileOut.close();
    }

    public static CountVectorizer deserialize(String filename)
            throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        CountVectorizer vectorizer = (CountVectorizer) in.readObject();
        in.close();
        fileIn.close();
        return vectorizer;
    }

}
