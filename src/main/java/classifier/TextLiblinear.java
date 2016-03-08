package classifier;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
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

public class TextLiblinear implements Serializable {

    private static final long serialVersionUID = 1833141383231227489L;
    public double bias;
    public SolverType solverType;
    public double c;
    public double eps;
    public Model model;

    public TextLiblinear() {
        this.bias = 0;
        this.solverType = SolverType.L2R_L2LOSS_SVC;
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

    public <E> TextLiblinear fit(List<E> inputX, List<Integer> y) {
    	if (inputX.size() != y.size())
            throw new IllegalArgumentException(
                    "X and y's length are different");
    	
    	if (inputX.size() == 0)
    		throw new IllegalArgumentException(
                    "Empty training list");
    	
    	List<List<Entry<Integer, Integer>>> X = Lists.newArrayList();
    	
    	if (inputX.get(0) instanceof Entry) { // only one instance
    		try {
    			@SuppressWarnings("unchecked")
				List<Entry<Integer, Integer>> x = (List<Entry<Integer, Integer>>)inputX;
    			X.add(x);
			} catch (Exception e) {
				throw new IllegalArgumentException("X's type must be "
	    				+ "List<List<Entry<Integer, Integer>>> "
	    				+ "or List<Entry<Integer, Integer>>");
			}
    	} else if (inputX.get(0) instanceof List){ // instances
    		try {
    			@SuppressWarnings("unchecked")
				List<List<Entry<Integer, Integer>>> inputX2 = (List<List<Entry<Integer, Integer>>>) inputX;
				X.addAll(inputX2);
			} catch (Exception e) {
				throw new IllegalArgumentException("X's type must be "
	    				+ "List<List<Entry<Integer, Integer>>> "
	    				+ "or List<Entry<Integer, Integer>>");
			}
    	} else {
    		throw new IllegalArgumentException("X's type must be "
    				+ "List<List<Entry<Integer, Integer>>> "
    				+ "or List<Entry<Integer, Integer>>");
    	}
    	
    	fitHelper(X, y);
    	
    	return this;
    }
    
    private TextLiblinear fitHelper(List<List<Entry<Integer, Integer>>> X,
            List<Integer> y) {
        Problem problem = null;
        File tmp = null;
        try {
            tmp = File
                    .createTempFile(String.valueOf(System.currentTimeMillis()),
                            "");
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
            System.err.println(
                    "Writing tmp file error, please check your permission");
            System.exit(-1);
        }

        Parameter parameter = new Parameter(solverType, c, eps);
        Linear.disableDebugOutput();
        model = Linear.train(problem, parameter);
        return this;
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
    
    public List<Entry<Integer, Double>> predictProbAll(List<List<Entry<Integer, Integer>>> X) {
    	List<Entry<Integer, Double>> ret = Lists.newArrayList();
        for (List<Entry<Integer, Integer>> textx : X) {
            ret.add(predictProbOne(textx));
        }
        return ret;
    }
    
    public Entry<Integer, Double> predictProbOne(List<Entry<Integer, Integer>> x) {
    	Feature[] instance = new Feature[x.size()];
        int i = 0;
        for (Entry<Integer, Integer> entry : x) {
            instance[i++] = new FeatureNode(entry.getKey(), entry.getValue());
        }
        int ybar = (int) Linear.predict(model, instance);
        
        double[] probEstimates = new double[model.getNrClass()];
        Linear.predictProbability(model, instance, probEstimates);
        // liblinear predict index start from 1
        return new SimpleEntry<Integer, Double>(ybar, probEstimates[ybar-1]);
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

    public TextLiblinear deserialize(String filename)
            throws IOException, ClassNotFoundException {
    	List<String> lines = Files.readLines(new File(filename), Charsets.UTF_8);
		if (lines.size() != 7 || !lines.get(1).equals("====") 
				|| !lines.get(3).equals("====") 
				|| !lines.get(5).equals("====")) 
			throw new IOException("deserialize file format error");
    	
		this.bias = Double.parseDouble(lines.get(0));
		if (lines.get(2).equals("L2R_LR")) this.solverType = SolverType.L2R_LR;
		if (lines.get(2).equals("L2R_L2LOSS_SVC_DUAL")) this.solverType = SolverType.L2R_L2LOSS_SVC_DUAL;
		if (lines.get(2).equals("L2R_L2LOSS_SVC")) this.solverType = SolverType.L2R_L2LOSS_SVC;
		if (lines.get(2).equals("L2R_L1LOSS_SVC_DUAL")) this.solverType = SolverType.L2R_L1LOSS_SVC_DUAL;
		if (lines.get(2).equals("MCSVM_CS")) this.solverType = SolverType.MCSVM_CS;
		if (lines.get(2).equals("L1R_L2LOSS_SVC")) this.solverType = SolverType.L1R_L2LOSS_SVC;
		if (lines.get(2).equals("L1R_LR")) this.solverType = SolverType.L1R_LR;
		if (lines.get(2).equals("L2R_LR_DUAL")) this.solverType = SolverType.L2R_LR_DUAL;
		if (lines.get(2).equals("L2R_L2LOSS_SVR")) this.solverType = SolverType.L2R_L2LOSS_SVR;
		if (lines.get(2).equals("L2R_L2LOSS_SVR_DUAL")) this.solverType = SolverType.L2R_L2LOSS_SVR_DUAL;
		if (lines.get(2).equals("L2R_L1LOSS_SVR_DUAL")) this.solverType = SolverType.L2R_L1LOSS_SVR_DUAL;
		this.c = Double.parseDouble(lines.get(4));
		this.eps = Double.parseDouble(lines.get(6));
		File modelFile = new File(filename + "__.__model.txt");
		if (modelFile.exists())
			model = Model.load(modelFile);
        return this;
    }

}
