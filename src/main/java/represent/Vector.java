package represent;

public class Vector {

	private double[] container;
	private int dim;

	public Vector(int dim) {
        container = new double[dim];
        this.dim = dim;
    }
	
	public Vector(double[] values) {
		this.dim = values.length;
		container = new double[this.dim];
		System.arraycopy(values, 0, container, 0, this.dim);
	}
	
	public Vector(Double[] values) {
		this.dim = values.length;
		container = new double[this.dim];
		for (int i = 0; i < this.dim; i++)
			container[i] = values[i];
	}
	
	public Vector(SparseVector sparseVector) {
		fromSparseVector(sparseVector);
	}

	public void set(int i, double val) {
		container[i] = val;
	}

	public double get(int i) {
		return container[i];
	}
	
	public int getDim() {
		return dim;
	}

	public Vector fromSparseVector(SparseVector sparseVector) {
		dim = sparseVector.getDim();
		container = new double[dim];
		for (int i = 0; i < dim; i++)
			container[i] = sparseVector.get(i);
		return this;
	}
	
	public SparseVector toSparseVector() {
		SparseVector sparseVector = new SparseVector(dim);
		for (int i = 0; i < dim; i++)
			if (Double.compare(container[i], 0) != 0)
				sparseVector.set(i, container[i]);
		return sparseVector;
	}
}
