package classifier;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;
import de.bwaldvogel.liblinear.SolverType;

public class TextLiblinear {
    private double     bias;
    private SolverType solverType;
    private double     c;
    private double     eps;
    private Model      model;

    public TextLiblinear() {
        this.bias = 0;
        this.solverType = SolverType.L2R_LR;
        this.c = 1;
        this.eps = 0.01;
    }

    public TextLiblinear(double bias, SolverType solverType, double c,
            double eps) {
        this.bias = bias;
        this.solverType = solverType;
        this.c = c;
        this.eps = eps;
    }

    public void fit(List<List<Entry<Integer, Integer>>> X,
            List<Integer> y) {
        if (X.size() != y.size())
            throw new IllegalArgumentException(
                    "X and y's length are different");

        

        Problem problem = null;
        File tmp = null;
        try {
            tmp = File
                    .createTempFile(String.valueOf(System.currentTimeMillis()), "");
            Writer writer =
                    Files.asCharSink(tmp, Charsets.UTF_8).openBufferedStream();
            for (int i = 0; i < X.size(); i++) {
                int yi = y.get(i);
                writer.append(String.valueOf(yi));
                writer.append(" ");
                for (Entry<Integer, Integer> x : X.get(i)) {
                    int xjKey = x.getKey();
                    int xjVal = x.getValue();
                    writer.append(String.valueOf(xjKey));
                    writer.append(":");
                    writer.append(String.valueOf(xjVal));
                    writer.append(" ");
                }
                writer.append("\n");
            }
            writer.close();
            problem = Problem.readFromFile(tmp, bias);
            tmp.deleteOnExit();
        } catch (InvalidInputDataException e) {
            System.err.println("System internal error: this should not happen");
            if (tmp != null)
                tmp.deleteOnExit();
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("Writing tmp file error, please check your permission");
            System.exit(-1);
        }

        Parameter parameter = new Parameter(solverType, c, eps);
        Linear.disableDebugOutput();
        model = Linear.train(problem, parameter);
    }

    public void dump(String path) throws IOException {
        File dst = new File(path);
        dst.getParentFile().mkdirs();
        model.save(dst);
    }

    public void load(String path) throws IOException {
        model = Model.load(new File(path));
    }

    public List<Integer> predictAll(List<List<Entry<Integer, Integer>>> textX) {
        List<Integer> ret = Lists.newArrayList();
        for (List<Entry<Integer, Integer>> textx : textX) {
            ret.add(predictOne(textx));
        }
        return ret;
    }

    public int predictOne(List<Entry<Integer, Integer>> x) {
        Feature[] instance = new Feature[x.size()];
        int i = 0;
        for (Entry<Integer, Integer> entry : x) {
            instance[i++] = new FeatureNode(entry.getKey(), entry.getValue());
        }
        return (int) Linear.predict(model, instance);
    }

}
