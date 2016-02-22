package represent;

public class Matrix {

    private double[][] container;
    
    public Matrix(int dimX, int dimY) {
        container = new double[dimX][];
        for (int i = 0; i < dimX; i++)
            container[i] = new double[dimY];
    }
    
    public double get(int i, int j) {
        return container[i][j];
    }
    
    public void set(int i, int j, double val) {
        container[i][j] = val;
    }
}
