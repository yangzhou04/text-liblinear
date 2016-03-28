package preprocess;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class NGrammer extends Tokenizer {

    // using 2-gram by default
    private int n = 2;
    private NGrammerType type;

    public NGrammer(int n, NGrammerType type) {
    	this.n = n;
        this.type = type;
    }
    
    public int getNgram() {
        return n;
    }

    @Override
    public String parse(String src, String splitPat) {
        if (type == NGrammerType.NON_ACCUMULATE) {
            return parse(src, n, splitPat);
        } else if (type == NGrammerType.ACCUMULATE) {
            List<String> tmp = Lists.newArrayList();
            for (int i = 0; i < n; i++) {
                tmp.add(parse(src, i+1, splitPat));
            }
            return Joiner.on(splitPat).join(tmp);
        } else
            throw new IllegalArgumentException("NGrammer's type must"
                    + " be either NON_ACCUMULATE or ACCUMULATE");
    }

    @Override
    public List<String> parse(String src) {
        if (src == null)
            return Lists.newArrayList();

        List<String> ngram = Lists.newArrayList();
        if (type == NGrammerType.NON_ACCUMULATE) {
            List<String> twoGrams = parse(src, n);
            ngram.addAll(twoGrams);
        } else if (type == NGrammerType.ACCUMULATE) {
            for (int i = 0; i < n; i++) {
                ngram.addAll(parse(src, i + 1));
            }
        } else
            throw new IllegalArgumentException("NGrammer's type must"
                    + " be either NON_ACCUMULATE or ACCUMULATE");
        return ngram;
    }

    public static List<String> parse(String src, int n) {
        List<String> ngram = Lists.newArrayList();
        if (src == null || src.length() - n + 1 <= 0)
            return ngram;
        for (int i = 0; i < src.length() - n + 1; i++) {
            ngram.add(src.substring(i, i + n));
        }
        return ngram;
    }

    public static String parse(String src, int n, String splitPat) {
        if (src == null || src.length() - n + 1 <= 0)
            return "";
        List<String> ngramList = parse(src, n);
        return Joiner.on(splitPat).join(ngramList);
    }
}
