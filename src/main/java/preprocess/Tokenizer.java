package preprocess;

import java.util.List;

import com.google.common.collect.Lists;

public class Tokenizer {

    public List<String> parse(String src, TokenizeType type) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public List<List<String>> parse(List<String> srcList, TokenizeType type) {
        List<List<String>> ret = Lists.newArrayList();
        for (String src : srcList) {
            ret.add(parse(src, type));
        }
        return ret;
    }

}
