package represent;

import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Maps;

public class SparseVector{

    private Map<Integer, Double> container;
    private int dim;
    
    public SparseVector(int dim) {
        this.dim = dim;
        container = Maps.newTreeMap();
    }
    
    public SparseVector(Vector vector) {
    	fromVector(vector);
    }
    
    public void set(int i, double val) {
        if (i >= dim) {
        	throw new IndexOutOfBoundsException("at position: " + i);
        }
        if (Double.compare(val, 0) != 0)
        	container.put(i, val);
    }
    
    public double get(int i) {
    	if (container.containsKey(i))
    		return container.get(i);
    	else
    		return 0;
    }
    
    public Vector toVector() {
    	Vector vector = new Vector(dim);
    	for (Iterator<Integer> iter = container.keySet().iterator();
    			iter.hasNext(); ) {
    		int index = iter.next();
    		vector.set(index, container.get(index));
    	}
    	return vector;
    }
    
    public int getDim() {
    	return dim;
    }
    
    public SparseVector fromVector(Vector vector) {
    	container.clear();
    	dim = vector.getDim();
    	for (int i = 0; i < dim; i++) {
    		if (Double.compare(vector.get(i), 0) != 0) {
    			container.put(i, vector.get(i));
    		}
    	}
    	return this;
    }

}
