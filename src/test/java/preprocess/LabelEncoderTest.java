package preprocess;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import junit.framework.Assert;

public class LabelEncoderTest {

    private LabelEncoder encoder;
    private List<String> source;
    
    @Before
    public void setUp() throws Exception {
        encoder = new LabelEncoder();
        source = Lists.newArrayList("0", "1", "1", "0", "0");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSerializeAndDeserialize() throws Exception {
        encoder.fit(source);
        List<Integer> test1 = encoder.transform(source);
        LabelEncoder.serialize(encoder, "encoder.ser");
        LabelEncoder encoder2 = LabelEncoder.deserialize("encoder.ser");
        List<Integer> test2 = encoder2.transform(source);
        
        for (int i = 0; i < test1.size(); i++) {
            Assert.assertEquals(test1.get(i), test2.get(i));
        }
    }
}
