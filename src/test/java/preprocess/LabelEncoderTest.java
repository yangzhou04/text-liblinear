package preprocess;

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

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSerializeAndDeserialize() throws Exception {
    	LabelEncoder encoder = new LabelEncoder();
        encoder.fit(source);
        List<Integer> result = encoder.transform(source);
        encoder.serialize("data/encoder.ser");
        LabelEncoder encoder2 = new LabelEncoder();
        encoder2.deserialize("data/encoder.ser");
        List<Integer> result2 = encoder2.transform(source);
        
        for (int i = 0; i < result.size(); i++) {
            Assert.assertEquals(result.get(i), result2.get(i));
        }
    }
}
