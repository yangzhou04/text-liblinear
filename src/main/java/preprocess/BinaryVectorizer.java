package preprocess;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import represent.SparseMatrix;
import represent.SparseVector;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class BinaryVectorizer implements Serializable {

    private static final long serialVersionUID = 2332382468482992263L;
    private Map<String, Integer> vocabularyMap = Maps.newHashMap();;
    private int count;
    private String splitPat;
    private Splitter splitter;

    public BinaryVectorizer(String splitPat) {
        this.splitPat = splitPat;
        splitter = Splitter.onPattern(splitPat).omitEmptyStrings();
    }

    public BinaryVectorizer fit(String input) {
        count = 0;
        vocabularyMap.clear();

        for (String token : splitter.split(input)) {
            if (!vocabularyMap.containsKey(token))
                vocabularyMap.put(token, ++count);
        }
        return this;
    }

    public BinaryVectorizer fit(List<String> inputs) {
        count = 0;
        vocabularyMap.clear();

        for (String input : inputs) {
            for (String token : splitter.split(input)) {
                if (!vocabularyMap.containsKey(token))
                    vocabularyMap.put(token, ++count);
            }
        }

        return this;
    }

    public SparseVector transform(String input) {
        SparseVector sv = new SparseVector(vocabularyMap.size());
        for (String token : splitter.split(input)) {
            // index start from 1 due to liblinear
            sv.set(vocabularyMap.get(token)-1, 1);
        }
        return sv;
    }

    public SparseMatrix transform(List<String> inputs) {
        SparseMatrix sm = new SparseMatrix(inputs.size(), vocabularyMap.size());
        for (int i = 0; i < inputs.size(); i++) {
            String input = inputs.get(i);
            for (String token : splitter.split(input)) {
                // index start from 1 due to liblinear
                sm.set(i, vocabularyMap.get(token)-1, 1);
            }
        }
        return sm;
    }

    public SparseMatrix fitTransform(List<String> inputs) {
        return fit(inputs).transform(inputs);
    }

    public SparseVector fitTransform(String input) {
        return fit(input).transform(input);
    }

    public BinaryVectorizer serialize(String filename) throws IOException {
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

    public static BinaryVectorizer deserialize(String filename) throws IOException {
        List<String> lines =
                Files.readLines(new File(filename), Charsets.UTF_8);
        if (lines.size() != 5 || !lines.get(1).equals("====")
                || !lines.get(3).equals("===="))
            throw new IOException("deserialize file format error");

        // any non-empty string as temporal split pattern
        BinaryVectorizer deserVec = new BinaryVectorizer(" ");
        deserVec.splitPat = lines.get(4);
        deserVec.splitter = Splitter.onPattern(deserVec.splitPat).omitEmptyStrings();
        deserVec.count = Integer.parseInt(lines.get(0));
        for (Map.Entry<String, String> e : Splitter.on(",")
                .withKeyValueSeparator(":").split(lines.get(2)).entrySet())
            deserVec.vocabularyMap.put(e.getKey(), Integer.parseInt(e.getValue()));
        return deserVec;
    }

}
