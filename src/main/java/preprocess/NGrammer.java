package preprocess;

import java.util.List;

import com.google.common.collect.Lists;

public class NGrammer extends Tokenizer {

    private int n = 2;

    public int getNgram() {
        return n;
    }

    public void setNgram(int ngram) {
        this.n = ngram;
    }

    public List<List<String>> parse(List<String> srcList, TokenizeType type,
            int n) {
        List<List<String>> ret = Lists.newArrayList();
        for (String src : srcList) {
            ret.add(parse(src, type, n));
        }
        return ret;
    }

    public List<String> parse(String src, TokenizeType type, int n) {
        if (src == null)
            return Lists.newArrayList();

        List<String> ngram = Lists.newArrayList();
        if (type == TokenizeType.NGRAM) {
            List<String> twoGrams = parse(src, n);
            ngram.addAll(twoGrams);
        } else if (type == TokenizeType.NGRAM_ALL) {
            for (int i = 0; i < n; i++) {
                ngram.addAll(parse(src, i + 1));
            }
        } else
            throw new IllegalArgumentException("NGrammer's TokenizeType must"
                    + " be either NGRAM or NGRAM_ALL");
        return ngram;
    }

    public List<String> parse(String src, TokenizeType type) {
        // Using 2-gram by default
        return parse(src, type, 2);
    }

    private List<String> parse(String src, int n) {
        List<String> ngram = Lists.newArrayList();
        if (src == null || src.length() - n + 1 <= 0)
            return ngram;
        for (int i = 0; i < src.length() - n + 1; i++) {
            ngram.add(src.substring(i, i + n));
        }
        return ngram;
    }
}
