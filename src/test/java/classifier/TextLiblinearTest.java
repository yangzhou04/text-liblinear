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
import preprocess.CountVectorizer;
import preprocess.LabelEncoder;
import preprocess.NGrammer;
import preprocess.TokenizeType;
import preprocess.Tokenizer;

public class TextLiblinearTest {
    
	private TextLiblinear model;
	
	@Before
    public void setUp() throws Exception {
		model = new TextLiblinear(1.123, SolverType.L1R_LR, 1.1223, 0.00121);
    }

    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void testTrain() throws Exception {
    	List<String> lines = Lists.newArrayList(); // empty train file

        List<String> textX = Lists.newArrayList();
        List<String> texty = Lists.newArrayList();

        for (String line : lines) {
            List<String> parts = Splitter.on("\t").splitToList(line);
            textX.add(parts.get(0));
            texty.add(parts.get(1));
        }

        LabelEncoder le = new LabelEncoder();
        List<Integer> y = le.fitTransform(texty);
        le.serialize("data/empty_le.txt");
        Tokenizer tokenizer = new NGrammer();
        List<List<String>> tokenX = tokenizer.parse(textX, TokenizeType.NGRAM_ALL);
        CountVectorizer vec = new CountVectorizer();
        List<List<Entry<Integer, Integer>>> X = (List<List<Entry<Integer, Integer>>>) vec.fitTransform(tokenX);
        vec.serialize("data/empty_vec.txt");
        TextLiblinear model = new TextLiblinear();
        model.fit(X, y);
        model.serialize("data/empty_model.txt");
    }
	
    @Test
    public void testPredict() throws Exception {
    	
    }
	
    @Test
    public void testSerializeAndDeserialize() throws Exception {
        model.serialize("data/model.ser");
        TextLiblinear model2 = new TextLiblinear();
        model2.deserialize("data/model.ser");
        Assert.assertEquals(model2.bias, model.bias);
        Assert.assertEquals(model2.c, model.c);
        Assert.assertEquals(model2.eps, model.eps);
        Assert.assertEquals(model2.solverType, model.solverType);
    }
    
}
