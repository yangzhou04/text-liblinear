package preprocess;

import java.util.List;

import com.google.common.collect.Lists;

public class Tokenizer {

    public List<String> parse(String input) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public List<List<String>> parse(List<String> inputs) {
        List<List<String>> ret = Lists.newArrayList();
        for (String input : inputs) {
            ret.add(parse(input));
        }
        return ret;
    }

    public String parse(String input, String splitPat) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public List<String> parse(List<String> inputs, String splitPat) {
        List<String> ret = Lists.newArrayList();
        for (String input : inputs) {
            ret.add(parse(input, splitPat));
        }
        return ret;
    }

}
