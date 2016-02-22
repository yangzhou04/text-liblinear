package preprocess;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class CountVectorizerTest {

    private CountVectorizer countVectorizer;
    
    @Before
    public void setUp() throws Exception {
        countVectorizer = new CountVectorizer();
    }

    @After
    public void tearDown() throws Exception {
    }
    
    @Test
            public void testFitTransform() throws Exception {
                List<List<String>> textX = Lists.newArrayList();
                textX.add(Lists.newArrayList("a","b","c","d","a","b","c"));
                System.out.println(countVectorizer.fitTransform(textX));
            }

}
