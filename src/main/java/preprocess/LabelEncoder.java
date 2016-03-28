package preprocess;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

public class LabelEncoder implements Serializable {

    private static final long serialVersionUID = 549108942101297894L;
    private Map<String, Integer> labelMap;
    private Map<Integer, String> reverseLabelMap;
    private int                  count;

    public LabelEncoder() {
        labelMap = Maps.newHashMap();
        reverseLabelMap = Maps.newHashMap();
    }

    public LabelEncoder fit(List<String> textY) {
        count = 0;
        labelMap.clear();
        reverseLabelMap.clear();
        for (String texty : textY) {
            if (!labelMap.containsKey(texty)) {
                labelMap.put(texty, count++);
                reverseLabelMap.put(count, texty);
            }
        }
        return this;
    }

    public List<Integer> transform(List<String> textY) {
        List<Integer> ret = Lists.newArrayList();
        for (String texty : textY) {
            ret.add(labelMap.get(texty));
        }
        return ret;
    }

    public List<Integer> fitTransform(List<String> textY) {
        return fit(textY).transform(textY);
    }

    public String getLabel(int i) {
        return reverseLabelMap.get(i);
    }

    public void serialize(String filename) throws IOException {
    	Writer w = Files.asCharSink(new File(filename), Charsets.UTF_8).openBufferedStream();
    	w.write(String.valueOf(this.count)); // line 0
    	w.write("\n====\n"); // line 1
    	w.write(Joiner.on(",").withKeyValueSeparator(":").join(labelMap)); // line 2
    	w.write("\n====\n"); // line 3
    	w.write(Joiner.on(",").withKeyValueSeparator(":").join(reverseLabelMap)); // line 4
    	w.close();
    }

    public LabelEncoder deserialize(String filename)
            throws IOException, ClassNotFoundException {
    	List<String> lines = Files.readLines(new File(filename), Charsets.UTF_8);
    	if (lines.size() != 5 || !lines.get(1).equals("====") 
				|| !lines.get(3).equals("====")) 
			throw new IOException("deserialize file format error");
    	
    	this.count = Integer.parseInt(lines.get(0));
		for (Entry<String, String> e : Splitter.on(",").withKeyValueSeparator(":").split(lines.get(2)).entrySet())
			this.labelMap.put(e.getKey(), Integer.parseInt(e.getValue()));
		
		for (Entry<String, String> e : Splitter.on(",").withKeyValueSeparator(":").split(lines.get(4)).entrySet())
			this.reverseLabelMap.put(Integer.parseInt(e.getKey()), e.getValue());
		
    	return this;
    }

}
