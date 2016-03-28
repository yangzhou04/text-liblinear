/**
 * 
 */
package preprocess;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zhouyang
 *
 */
public class NGrammerTest {

    @Test
    public void testParse() throws Exception {
        NGrammer ng = new NGrammer(2, NGrammerType.ACCUMULATE);
        String s = "1234";
        Assert.assertEquals("1 2 3 4 12 23 34", ng.parse(s, " "));
        Assert.assertArrayEquals(new String[]{
                "1", "2", "3", "4", "12", "23", "34"}, ng.parse(s).toArray());

        ng = new NGrammer(2, NGrammerType.NON_ACCUMULATE);
        Assert.assertEquals("12 23 34", ng.parse(s, " "));
        Assert.assertArrayEquals(new String[]{
                 "12", "23", "34"}, ng.parse(s).toArray());
    }
}
