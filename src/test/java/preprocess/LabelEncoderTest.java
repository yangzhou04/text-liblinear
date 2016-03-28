package preprocess;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import junit.framework.Assert;

public class LabelEncoderTest {

    
    private List<String> source;
    
    @Before
    public void setUp() throws Exception {
        source = Lists.newArrayList("0", "1", "1", "0", "0");
    }

    @Test
    public void testFitAndTransform() throws Exception {
        LabelEncoder le = new LabelEncoder();
        List<Integer> y = le.fit(source).transform(source);
        Assert.assertTrue(0 == y.get(0));
        Assert.assertTrue(1 == y.get(1));
        Assert.assertTrue(1 == y.get(2));
        Assert.assertTrue(0 == y.get(3));
        Assert.assertTrue(0 == y.get(4));
    }

    @Test
    public void testSerializeAndDeserialize() throws Exception {
    	LabelEncoder encoder = new LabelEncoder();
        encoder.fit(source);
        List<Integer> result = encoder.transform(source);
        String filename = "data/encoder.ser";
        encoder.serialize(filename);
        LabelEncoder encoder2 = new LabelEncoder();
        encoder2.deserialize(filename);
        List<Integer> result2 = encoder2.transform(source);
        
        for (int i = 0; i < result.size(); i++) {
            Assert.assertEquals(result.get(i), result2.get(i));
        }
        new File(filename).delete();
    }

}
