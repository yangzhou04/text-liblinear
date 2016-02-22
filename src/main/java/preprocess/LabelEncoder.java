package preprocess;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class LabelEncoder {

    private Map<String, Integer> labelMap;
    private Map<Integer, String> reverseLabelMap;
    private int                  count;

    public LabelEncoder() {
        labelMap = Maps.newHashMap();
        reverseLabelMap = Maps.newHashMap();
    }

    public void fit(List<String> textY) {
        labelMap.clear();
        reverseLabelMap.clear();
        for (String texty : textY) {
            if (!labelMap.containsKey(texty)) {
                labelMap.put(texty, ++count);
                reverseLabelMap.put(count, texty);
            }
        }
    }

    public List<Integer> transform(List<String> textY) {
        List<Integer> ret = Lists.newArrayList();
        for (String texty : textY) {
            ret.add(labelMap.get(texty));
        }
        return ret;
    }

    public List<Integer> fitTransform(List<String> textY) {
        labelMap.clear();
        reverseLabelMap.clear();
        fit(textY);
        return transform(textY);
    }

    public String getLabel(int i) {
        return reverseLabelMap.get(i);
    }

}
