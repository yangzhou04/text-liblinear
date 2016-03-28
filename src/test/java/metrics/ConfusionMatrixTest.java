package metrics;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by zhouyang on 16/3/25.
 */
public class ConfusionMatrixTest {

    @Test
    public void testConfusionMatrix() throws Exception {
        List<Integer> trueY = Lists.newArrayList(1,0,1,0);
        List<Integer> predY = Lists.newArrayList(1,1,1,0);
        ConfusionMatrix mat = new ConfusionMatrix(trueY, predY);
        int[][] cm = mat.getMatrix();
        Assert.assertEquals(2, cm[0][0]);
        Assert.assertEquals(0, cm[0][1]);
        Assert.assertEquals(1, cm[1][0]);
        Assert.assertEquals(1, cm[1][1]);
    }


}