package represent;

public class Vector {

    private double[] container;
    
    public Vector(int dim) {
        container = new double[dim];
    }
    
    public void set(int i, double val) {
        container[i] = val;
    }
    
    public double get(int i) {
        return container[i];
    }

}
