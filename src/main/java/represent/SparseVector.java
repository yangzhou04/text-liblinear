package represent;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SparseVector {

	private Map<Integer, Double> container = Maps.newTreeMap();
	private int dim;

	public SparseVector(int dim) {
		this.dim = dim;
	}

	public SparseVector(int dim, Map<Integer, Double> valuePairs) {
		this(dim);
		for (Entry<Integer, Double> pair : valuePairs.entrySet()) {
			int index = pair.getKey();
			if (index >= dim)
				throw new IllegalArgumentException(
				        "value index exceed the dim: " + index);
			container.put(index, pair.getValue());
		}
	}

	public SparseVector(double[] values) {
		this(values.length);
		for (int i = 0; i < values.length; i++) {
			if (Double.compare(values[i], 0) != 0)
				container.put(i, values[i]);
		}
	}

	public SparseVector(Double[] values) {
		this(values.length);
		for (int i = 0; i < values.length; i++) {
			if (Double.compare(values[i], 0) != 0)
				container.put(i, values[i]);
		}
	}

	public SparseVector(Vector vector) {
		fromVector(vector);
	}

	public void set(int i, double val) {
		if (i >= dim) { throw new IndexOutOfBoundsException(
		        "at position: " + i); }
		if (Double.compare(val, 0) != 0)
			container.put(i, val);
	}

	public double get(int i) {
		if (i >= dim) { throw new IndexOutOfBoundsException(
		        "at position: " + i); }
		if (container.containsKey(i))
			return container.get(i);
		else
			return 0;
	}

	public int getNonZeroDim() {
		return container.size();
	}

	public Set<Integer> keySet() {
		return container.keySet();
	}

	public Set<Entry<Integer, Double>> entrySet() {
		return container.entrySet();
	}

	public Vector toVector() {
		Vector vector = new Vector(dim);
		for (Entry<Integer, Double> entry : container.entrySet()) {
			vector.set(entry.getKey(), entry.getValue());
		}
		return vector;
	}

	public int getDim() {
		return dim;
	}

	public SparseVector fromVector(Vector vector) {
		container = Maps.newHashMap();
		dim = vector.getDim();
		for (int i = 0; i < dim; i++) {
			if (Double.compare(vector.get(i), 0) != 0) {
				container.put(i, vector.get(i));
			}
		}
		return this;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("Dim="+dim+"\n");
		for (Entry<Integer, Double> e : entrySet()) {
			buffer.append(e.getKey() + ":" + e.getValue() + ",");
		}
		return buffer.substring(0, buffer.length()-1);
	}
}
