package represent;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Map.Entry;

public class SparseMatrix {

	private Map<Integer, SparseVector> container = Maps.newHashMap();
    private int dimX, dimY;

    public SparseMatrix(int dimX, int dimY) {
    	this.dimX = dimX;
    	this.dimY = dimY;
    }
    
    public SparseMatrix(Matrix matrix) {
    	fromMatrix(matrix);
    }
    
    public double get(int i, int j) {
    	if (i >= dimX || j >= dimY) {
        	throw new IndexOutOfBoundsException("at position: x=" + i + ",y=" + j);
        }
        if (container.containsKey(i))
			return container.get(i).get(j);
		else
        	return 0;
    }

	public SparseVector getx(int i) {
		if (i >= dimX) {
			throw new IndexOutOfBoundsException("at position: x=" + i);
		}
		if (container.containsKey(i))
			return container.get(i);
		else
			return new SparseVector(dimY);
	}

	public SparseMatrix setx(int i, SparseVector val) {
		if (val.getDim() != dimY)
			throw new IllegalArgumentException("Dimension error" + val.getDim());
		container.put(i, val);
		return this;
	}

    public void set(int i, int j, double val) {
    	if (i >= dimX || j >= dimY) {
        	throw new IndexOutOfBoundsException("at position: x=" + i + ",y=" + j);
        }
    	if (Double.compare(val, 0) != 0) {
    		if (container.containsKey(i)) {
				container.get(i).set(j, val);
			} else {
				SparseVector sv = new SparseVector(dimY);
				sv.set(j, val);
				container.put(i, sv);
			}
    	}
    }
    
    public int getDimX() {
    	return dimX;
    }
    
    public int getDimY() {
    	return dimY;
    }
    
    public SparseMatrix fromMatrix(Matrix matrix) {
    	dimX = matrix.getDimX();
    	dimY = matrix.getDimY();
    	for (int i = 0; i < dimX; i++) {
    		for (int j = 0; j < dimY; j++) {
    			this.set(i, j, matrix.get(i, j));
    		}
    	}
    	
    	return this;
    }
    
    public Matrix toMatrix() {
    	Matrix matrix = new Matrix(dimX, dimY);
    	for (int i = 0; i < dimX; i++) {
    		for (int j = 0; j < dimY; j++) {
    			matrix.set(i, j, this.get(i, j));
    		}
    	}
    	return matrix;
    }

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Dimx="+dimX+","+"DimY="+dimY+"\n");
		for (int i = 0; i < dimX; i++) {
			SparseVector sv = container.get(i);
			for (Entry<Integer, Double> e : sv.entrySet()) {
				buffer.append(e.getKey() + ":" +e.getValue()+",");
			}
			buffer.append("\n");
		}
		return buffer.substring(0, buffer.length());
	}
}

