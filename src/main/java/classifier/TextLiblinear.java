package classifier;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.AbstractMap.SimpleEntry;
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
import represent.SparseMatrix;
import represent.SparseVector;

public class TextLiblinear implements Serializable {

    private static final long serialVersionUID = 1833141383231227489L;
    public double bias;
    public SolverType solverType;
    public double c;
    public double eps;
    private Model model;

    public TextLiblinear() {
        this.bias = 1;
        this.solverType = SolverType.L2R_LR;
        this.c = 1;
        this.eps = 0.001;
    }

    public TextLiblinear(double bias, SolverType solverType, double c,
            double eps) {
        this.bias = bias;
        this.solverType = solverType;
        this.c = c;
        this.eps = eps;
    }

    public TextLiblinear fit(SparseVector x, int y) {
        SparseMatrix X = new SparseMatrix(1, x.getDim());
        for (int i = 0; i < x.getDim(); i++)
            X.set(0, i, x.get(i));
        List<Integer> ys = Lists.newArrayList(y);
        return fit(X, ys);
    }

    public TextLiblinear fit(SparseMatrix X, List<Integer> y) {
        if (X.getDimX() != y.size())
            throw new IllegalArgumentException(
                    "X and y's length are different");

        Problem problem = null;
        File tmp = null;
        Writer writer = null;
        try {
            tmp = File.createTempFile(String.valueOf(
                    System.currentTimeMillis()), "");
            writer = Files.asCharSink(tmp, Charsets.UTF_8)
                    .openBufferedStream();

            for (int i = 0; i < X.getDimX(); i++) {
                int yi = y.get(i);
                // liblinear start index from 1
                writer.append(String.valueOf(yi+1));
                writer.append(" ");

                SparseVector sv = X.getx(i);
                for (Entry<Integer, Double> e : sv.entrySet()) {
                    // liblinear start index from 1
                    writer.append(String.valueOf(e.getKey()+1));
                    writer.append(":");
                    writer.append(String.valueOf(e.getValue()));
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
            System.err.println(
                    "Writing tmp file error, please check your permission");
            if (writer != null)
                try {writer.close();} catch (IOException e1) {}
            System.exit(-1);
        }
        Parameter parameter = new Parameter(solverType, c, eps);
        Linear.disableDebugOutput();
        model = Linear.train(problem, parameter);

        return this;
    }

    public List<Integer> predict(SparseMatrix X) {
        List<Integer> ybars = Lists.newArrayList();
        for (int i = 0; i < X.getDimX(); i++) {
            SparseVector x = X.getx(i);
            ybars.add(predict(x));
        }
        return ybars;
    }

    public int predict(SparseVector x) {
        Feature[] instance = new Feature[x.getNonZeroDim()];
        int i = 0;
        for (Entry<Integer, Double> entry : x.entrySet()) {
            // liblinear predict index start from 1
            instance[i++] = new FeatureNode(entry.getKey()+1, entry.getValue());
        }
        // liblinear ybar index start from 1
        return (int) Linear.predict(model, instance) - 1;
    }

    public List<Entry<Integer, Double>> predictProb(SparseMatrix X) {
        List<Entry<Integer, Double>> ybarsWithProb = Lists.newArrayList();
        for (int i = 0; i < X.getDimX(); i++) {
            SparseVector x = X.getx(i);
            ybarsWithProb.add(predictProb(x));
        }
        return ybarsWithProb;
    }

    public Entry<Integer, Double> predictProb(SparseVector x) {
        Feature[] instance = new Feature[x.getNonZeroDim()];
        int i = 0;
        for (Entry<Integer, Double> entry : x.entrySet()) {
            // liblinear predict index start from 1
            instance[i++] = new FeatureNode(entry.getKey()+1, entry.getValue());
        }
        // liblinear ybar index start from 1
        int ybar = (int) Linear.predict(model, instance) - 1;

        double[] probEstimates = new double[model.getNrClass()];
        Linear.predictProbability(model, instance, probEstimates);
        return new SimpleEntry<Integer, Double>(ybar, probEstimates[ybar]);
    }

    public void serialize(String filename)
            throws IOException {
    	Writer w = Files.asCharSink(new File(filename), Charsets.UTF_8).openBufferedStream();
    	w.write(String.valueOf(this.bias)); // line 0
    	w.write("\n====\n"); // line 1
    	if (solverType == SolverType.L2R_LR) w.write("L2R_LR");
    	if (solverType == SolverType.L2R_L2LOSS_SVC_DUAL) w.write("L2R_L2LOSS_SVC_DUAL");
    	if (solverType == SolverType.L2R_L2LOSS_SVC) w.write("L2R_L2LOSS_SVC");
    	if (solverType == SolverType.L2R_L1LOSS_SVC_DUAL) w.write("L2R_L1LOSS_SVC_DUAL");
    	if (solverType == SolverType.MCSVM_CS) w.write("MCSVM_CS");
    	if (solverType == SolverType.L1R_L2LOSS_SVC) w.write("L1R_L2LOSS_SVC");
    	if (solverType == SolverType.L1R_LR) w.write("L1R_LR");
    	if (solverType == SolverType.L2R_LR_DUAL) w.write("L2R_LR_DUAL");
    	if (solverType == SolverType.L2R_L2LOSS_SVR) w.write("L2R_L2LOSS_SVR");
    	if (solverType == SolverType.L2R_L2LOSS_SVR_DUAL) w.write("L2R_L2LOSS_SVR_DUAL");
    	if (solverType == SolverType.L2R_L1LOSS_SVR_DUAL) w.write("L2R_L1LOSS_SVR_DUAL"); // line 2
    	w.write("\n====\n"); // line 3
    	w.write(String.valueOf(this.c)); // line 4
    	w.write("\n====\n"); // line 5
    	w.write(String.valueOf(this.eps)); // line 6
    	if (model != null)
    		model.save(new File(filename+"__.__model.txt"));
    	w.close();
    }

    public static TextLiblinear deserialize(String filename)
            throws IOException, ClassNotFoundException {
    	List<String> lines = Files.readLines(new File(filename), Charsets.UTF_8);
		if (lines.size() != 7 || !lines.get(1).equals("====") 
				|| !lines.get(3).equals("====") 
				|| !lines.get(5).equals("====")) 
			throw new IOException("deserialize file format error");
    	
		TextLiblinear textLiblinear = new TextLiblinear();
		textLiblinear.bias = Double.parseDouble(lines.get(0));
		if (lines.get(2).equals("L2R_LR")) textLiblinear.solverType = SolverType.L2R_LR;
		if (lines.get(2).equals("L2R_L2LOSS_SVC_DUAL")) textLiblinear.solverType = SolverType.L2R_L2LOSS_SVC_DUAL;
		if (lines.get(2).equals("L2R_L2LOSS_SVC")) textLiblinear.solverType = SolverType.L2R_L2LOSS_SVC;
		if (lines.get(2).equals("L2R_L1LOSS_SVC_DUAL")) textLiblinear.solverType = SolverType.L2R_L1LOSS_SVC_DUAL;
		if (lines.get(2).equals("MCSVM_CS")) textLiblinear.solverType = SolverType.MCSVM_CS;
		if (lines.get(2).equals("L1R_L2LOSS_SVC")) textLiblinear.solverType = SolverType.L1R_L2LOSS_SVC;
		if (lines.get(2).equals("L1R_LR")) textLiblinear.solverType = SolverType.L1R_LR;
		if (lines.get(2).equals("L2R_LR_DUAL")) textLiblinear.solverType = SolverType.L2R_LR_DUAL;
		if (lines.get(2).equals("L2R_L2LOSS_SVR")) textLiblinear.solverType = SolverType.L2R_L2LOSS_SVR;
		if (lines.get(2).equals("L2R_L2LOSS_SVR_DUAL")) textLiblinear.solverType = SolverType.L2R_L2LOSS_SVR_DUAL;
		if (lines.get(2).equals("L2R_L1LOSS_SVR_DUAL")) textLiblinear.solverType = SolverType.L2R_L1LOSS_SVR_DUAL;
		textLiblinear.c = Double.parseDouble(lines.get(4));
		textLiblinear.eps = Double.parseDouble(lines.get(6));
		File modelFile = new File(filename + "__.__model.txt");
		if (modelFile.exists())
			textLiblinear.model = Model.load(modelFile);
        return textLiblinear;
    }

}
