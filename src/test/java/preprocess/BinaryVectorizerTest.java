package preprocess;

import com.google.common.collect.Lists;
import junit.framework.Assert;
import org.junit.Test;
import represent.SparseMatrix;
import represent.SparseVector;

import java.io.File;
import java.util.List;

public class BinaryVectorizerTest {

    @Test
    public void testFitAndTransform() throws Exception {
        // test empty
        String input = "";
        String splitPat = " ";
        BinaryVectorizer vec = new BinaryVectorizer(splitPat);
        vec.fit(input);
        SparseVector sv = vec.transform(input);
        Assert.assertEquals(0, sv.getDim());

        sv = vec.fitTransform(input);
        Assert.assertEquals(0, sv.getDim());

        // test non-empty
        input = "a b a c d e";
        sv = vec.fit(input).transform(input);
        Assert.assertEquals(5, sv.getDim());
        Assert.assertEquals(1.0, sv.get(0));
        Assert.assertEquals(1.0, sv.get(1));
        Assert.assertEquals(1.0, sv.get(2));
        Assert.assertEquals(1.0, sv.get(3));
        Assert.assertEquals(1.0, sv.get(4));
    }

    @Test
    public void testFitAndTransformList() throws Exception {
        // test empty
        List<String> inputs = Lists.newArrayList();
        String splitPat = " ";
        BinaryVectorizer vec = new BinaryVectorizer(splitPat);
        SparseMatrix sm = vec.fit(inputs).transform(inputs);
        Assert.assertEquals(0, sm.getDimX());
        Assert.assertEquals(0, sm.getDimY());

        // test non-empty list but empty string
        inputs.add("");
        inputs.add(" ");
        sm = vec.fit(inputs).transform(inputs);
        Assert.assertEquals(2, sm.getDimX());
        Assert.assertEquals(0, sm.getDimY());

        // test non-empty
        inputs.clear();
        inputs.add("a b c d e a");
        inputs.add("a b c d e f ");
        sm = vec.fit(inputs).transform(inputs);
        Assert.assertEquals(2, sm.getDimX());
        Assert.assertEquals(6, sm.getDimY());
        Assert.assertEquals(1.0, sm.get(0, 0));
        Assert.assertEquals(1.0, sm.get(0, 1));
        Assert.assertEquals(1.0, sm.get(0, 2));
        Assert.assertEquals(1.0, sm.get(0, 3));
        Assert.assertEquals(1.0, sm.get(0, 4));
        Assert.assertEquals(0.0, sm.get(0, 5));
        Assert.assertEquals(1.0, sm.get(1, 0));
        Assert.assertEquals(1.0, sm.get(1, 1));
        Assert.assertEquals(1.0, sm.get(1, 2));
        Assert.assertEquals(1.0, sm.get(1, 3));
        Assert.assertEquals(1.0, sm.get(1, 4));
        Assert.assertEquals(1.0, sm.get(1, 5));
    }


    @Test
    public void testSerializeAndDeserialize() throws Exception {
        String serFilename = "data/test_count_vec.ser";
        String splitPat = " ";
        BinaryVectorizer vec = new BinaryVectorizer(splitPat);
        List<String> textX = Lists.newArrayList();
        textX.add("a b c d   a  b c");
        textX.add("e e e");
        vec.fit(textX);
        vec.serialize(serFilename);

        // deserialize
        BinaryVectorizer deserVec = BinaryVectorizer.deserialize(serFilename);
        SparseMatrix result = deserVec.transform(textX);
        Assert.assertEquals(2, result.getDimX());
        Assert.assertEquals(5, result.getDimY());

        Assert.assertEquals(1.0, result.get(0, 0));
        Assert.assertEquals(1.0, result.get(0, 1));
        Assert.assertEquals(1.0, result.get(0, 2));
        Assert.assertEquals(1.0, result.get(0, 3));
        Assert.assertEquals(0.0, result.get(0, 4));

        Assert.assertEquals(0.0, result.get(1, 0));
        Assert.assertEquals(0.0, result.get(1, 1));
        Assert.assertEquals(0.0, result.get(1, 2));
        Assert.assertEquals(0.0, result.get(1, 3));
        Assert.assertEquals(1.0, result.get(1, 4));

        new File(serFilename).delete();
    }

}
