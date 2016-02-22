package preprocess;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import classifier.TextLiblinear;

public class LabelEncoder implements Serializable {

    private static final long serialVersionUID = 549108942101297894L;
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

    public static void serialize(LabelEncoder encoder, String filename) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(filename);
        ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
        outStream.writeObject(encoder);
        outStream.close();
        fileOut.close();
    }

    public static LabelEncoder deserialize(String filename)
            throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        LabelEncoder encoder =  (LabelEncoder) in.readObject();
        in.close();
        fileIn.close();
        return encoder;
    }

}
