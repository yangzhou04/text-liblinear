package represent;

public class Matrix {

	private Vector[] container;
	private int dimX, dimY;

	public Matrix(int dimX, int dimY) {
		this.dimX = dimX;
		this.dimY = dimY;
		container = new Vector[dimX];
		for (int i = 0; i < dimX; i++)
			container[i] = new Vector(dimY);
	}

	public Matrix(double[][] values) {
		this(values.length, values[0].length);
		for (int i = 0; i < dimX; i++) {
			for (int j = 0; j < dimY; j++) {
				set(i, j, values[i][j]);
			}
		}
	}
	
	public Matrix(Double[][] values) {
		this(values.length, values[0].length);
		for (int i = 0; i < dimX; i++) {
			for (int j = 0; j < dimY; j++) {
				set(i, j, values[i][j]);
			}
		}
	}
	
	public Matrix(Vector[] values) {
		this.dimX = values.length;
		this.dimY = values[0].getDim();
		container = new Vector[dimX];
		for (int i = 0; i < dimX; i++) {
			if (values[i].getDim() != this.dimY)
				throw new IllegalArgumentException("Dim of Vector[] are different");
			container[i] = values[i];
		}
	}
	
	public int getDimX() {
		return dimX;
	}

	public int getDimY() {
		return dimY;
	}

	public Matrix(SparseMatrix sparseMatrix) {
		fromSparseMatrix(sparseMatrix);
	}

	public double get(int i, int j) {
		return container[i].get(j);
	}

	public void set(int i, int j, double val) {
		container[i].set(j, val);
	}

	public SparseMatrix toSparseMatrix() {
		SparseMatrix sparseMatrix = new SparseMatrix(dimX, dimY);
		for (int i = 0; i < dimX; i++) {
			for (int j = 0; j < dimY; j++) {
				sparseMatrix.set(i, j, this.get(i, j));
			}
		}
		return sparseMatrix;
	}

	public Matrix fromSparseMatrix(SparseMatrix sparseMatrix) {
		dimX = sparseMatrix.getDimX();
		dimY = sparseMatrix.getDimY();
		container = new Vector[dimX];
		for (int i = 0; i < dimX; i++)
			container[i] = new Vector(dimY);
		for (int i = 0; i < dimX; i++) {
			for (int j = 0; j < dimY; j++)
				set(i, j, sparseMatrix.get(i, j));
		}
		return this;
	}
}
