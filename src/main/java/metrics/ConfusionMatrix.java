package metrics;

import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

public class ConfusionMatrix {

    private List<Integer> trueY;
    private List<Integer> predY;
    private int[][] mat;
    private int dim;

    public ConfusionMatrix(List<Integer> trueY, List<Integer> predY) {
        this.trueY = trueY;
        this.predY = predY;
        computeMatrix();
    }

    private void computeMatrix() {
        Set<Integer> elementTrueY = Sets.newHashSet(trueY);
        Set<Integer> elementPredY = Sets.newHashSet(predY);
        if (!elementPredY.equals(elementTrueY)) {
            throw new IllegalArgumentException("True label and predict" +
                    "label's type are different");
        }
        if (trueY.size() == 0)
            throw new IllegalArgumentException("empty input");

        int dim = elementPredY.size();
        int[][] mat = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            mat[trueY.get(i)][predY.get(i)]++;
        }
        this.mat =  mat;
        this.dim = mat.length;
    }

    public int[][] getMatrix() {
        return mat;
    }

    private int sumRow(int i) {
        int sum = 0;
        for (int j = 0; j < dim; j++) {
            sum += mat[i][j];
        }
        return sum;
    }

    private int sumCol(int j) {
        int sum = 0;
        for (int i = 0; i < dim; i++) {
            sum += mat[i][j];
        }
        return sum;
    }

    public double precisionMicro() {
        int numerator=0, denominator=0;
        for (int i = 0; i < dim; i++) {
            int tp = mat[i][i];
            int fp = sumCol(i) - mat[i][i];
            numerator += tp;
            denominator += (tp+fp);
        }
        return numerator/(double)denominator;
    }

    public double precisionMacro() {
        double precisionSum = 0;
        for (int i = 0; i < dim; i++) {
            int tp = mat[i][i];
            int fp = sumCol(i) - mat[i][i];
            precisionSum += (double)tp / (tp+fp);
        }
        return precisionSum / dim;
    }
    
    public double recallMicro() {
        int numerator=0, denominator=0;
        for (int i = 0; i < dim; i++) {
            int tp = mat[i][i];
            int fn = sumRow(i) - mat[i][i];
            numerator += tp;
            denominator += (tp+fn);
        }
        return numerator/(double)denominator;
    }

    public double recallMacro() {
        double precisionSum = 0;
        for (int i = 0; i < dim; i++) {
            int tp = mat[i][i];
            int fn = sumRow(i) - mat[i][i];
            precisionSum += (double)tp / (tp+fn);
        }
        return precisionSum / dim;
    }
    
    public double f1Micro() {
        double p = precisionMicro();
        double r = recallMicro();
        if (p == 0 && r == 0)
            return 0;
        else
            return 2*p*r / (p+r);
    }

    public double f1Macro() {
        double p = precisionMacro();
        double r = recallMacro();
        if (p == 0 && r == 0)
            return 0;
        else
            return 2*p*r / (p+r);
    }

    public double accuracy() {
        if (trueY.size() != predY.size())
            throw new IllegalArgumentException("trueY.size() != predY.size()");
        if (trueY.size() == 0)
            throw new IllegalArgumentException("empty input");

        int right = 0;
        int total = 0;
        for (int i = 0; i < trueY.size(); i++) {
            if (trueY.get(i).compareTo(predY.get(i)) == 0)
                right++;
            total++;
        }
        return (double)right / total;
    }

}
