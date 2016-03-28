package classifier;

import java.io.File;
import java.util.List;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

import de.bwaldvogel.liblinear.SolverType;
import junit.framework.Assert;
import preprocess.*;
import represent.SparseMatrix;

public class TextLiblinearTest {

    private SparseMatrix X;
    private List<Integer> y;

    @Before
    public void setUp() throws Exception {
        List<String> lines = Files.readLines(new File("data/wenju_label"), Charsets.UTF_8);

        List<String> textX = Lists.newArrayList();
        List<String> texty = Lists.newArrayList();
        for (String line : lines) {
            List<String> parts = Splitter.on("\t").splitToList(line);
            if (parts.size() != 2)
                continue;
            textX.add(parts.get(0));
            texty.add(parts.get(1));
            Assert.assertTrue(parts.get(1).equals("是")||parts.get(1).equals("否"));
        }

        NGrammer ngrammer = new NGrammer(2, NGrammerType.ACCUMULATE);
        String splitPat = " ";
        List<String> tokenizedX = ngrammer.parse(textX, splitPat);
        CountVectorizer vec = new CountVectorizer(splitPat);
        X = vec.fit(tokenizedX).transform(tokenizedX);
        LabelEncoder le = new LabelEncoder();
        y = le.fit(texty).transform(texty);
    }

    @Test
    public void testTrainAndPredict() throws Exception {
        // only lr support prob predict
        TextLiblinear model = new TextLiblinear(0, SolverType.L2R_LR, 1, 0.001);
        List<Integer> ybar = model.fit(X, y).predict(X);
        Assert.assertEquals(ybar.size(), y.size());
        for (int i = 0; i < ybar.size(); i++) {
            Integer ybari = ybar.get(i);
            Assert.assertTrue(ybari==0 || ybari==1);
        }
        List<Entry<Integer, Double>> ybarProb = model.fit(X, y).predictProb(X);
        Assert.assertEquals(ybarProb.size(), y.size());
        for (int i = 0; i < ybarProb.size(); i++) {
            Entry<Integer, Double> entry = ybarProb.get(i);
            Assert.assertTrue(entry.getKey()==0 || entry.getKey()==1);
            Assert.assertTrue(entry.getValue()>=0 || entry.getValue()<=1);
        }
    }
	
    @Test
    public void testSerializeAndDeserialize() throws Exception {
        TextLiblinear model = new TextLiblinear(0, SolverType.L2R_LR, 1, 0.001);
        List<Entry<Integer, Double>> result = model.fit(X, y).predictProb(X);
        String serPath = "data/model.ser";
        model.serialize(serPath);
        TextLiblinear model2 = new TextLiblinear();
        model2.deserialize(serPath);
        new File(serPath).delete();
        Assert.assertEquals(model2.bias, model.bias);
        Assert.assertEquals(model2.c, model.c);
        Assert.assertEquals(model2.eps, model.eps);
        Assert.assertEquals(model2.solverType, model.solverType);
        List<Entry<Integer, Double>> result2 = model2.fit(X, y).predictProb(X);

        Assert.assertEquals(result.size(), result2.size());
        for (int i = 0; i < result2.size(); i++) {
            Entry<Integer, Double> e2 = result2.get(i);
            Entry<Integer, Double> e = result.get(i);
            Assert.assertEquals(e, e2);
        }
    }
}
