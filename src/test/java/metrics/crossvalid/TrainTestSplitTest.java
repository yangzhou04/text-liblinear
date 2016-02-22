package metrics.crossvalid;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import represent.Struct;

public class TrainTestSplitTest {

    private List<String> X;
    private List<String> y;
    
    @Before
    public void setUp() throws Exception {
        X = Lists.newArrayList("a", "b", "c", "d", "e", "f", "g");
        y = Lists.newArrayList("1", "2", "1", "2", "1", "2", "1");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSplit() throws Exception {
        Struct<List<String>> result = TrainTestSplit.split(X, y, 0, 0.8);
        for (int i = 0; i < 4; i++)
            System.out.println(result.get(i));
    }
    
    @Test
    public void testSwap() throws Exception {
//        splitter.swap(X, 0, 1);
//        Assert.assertEquals(X.get(0), "b");
//        Assert.assertEquals(X.get(1), "a");
    }
}
