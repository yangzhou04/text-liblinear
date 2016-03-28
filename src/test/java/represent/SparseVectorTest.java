package represent;

import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

import junit.framework.Assert;

public class SparseVectorTest {

	private SparseVector sv;
	
	@Before
	public void setUp() throws Exception {
		sv = new SparseVector(10);
		sv.set(0, 1.0);
		sv.set(1, 2);
		sv.set(8, 0.4);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSparseVectorFromValuePairsDimExceed() throws Exception {
		int dim = 10;
		Map<Integer, Double> valuePairs = Maps.newHashMap();
		valuePairs.put(10, 0.5);
		
		@SuppressWarnings("unused")
		SparseVector sv = new SparseVector(dim, valuePairs);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testSet() throws Exception {
		sv.set(0, 0.4);
		for (int i = 0; i < 10; i++) {
			if (i == 0) {
				Assert.assertNotSame(1.0, sv.get(i));
				Assert.assertEquals(0.4, sv.get(0));
			} else if (i == 1) {
				Assert.assertEquals(2.0, sv.get(i));
			} else if (i == 8) {
				Assert.assertEquals(0.4, sv.get(i));
			} else {
				Assert.assertEquals(0.0, sv.get(i));
			}
		}
		sv.set(10, 1.0);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGet() throws Exception {
		for (int i = 0; i < 10; i++) {
			if (i == 0) {
				Assert.assertEquals(1.0, sv.get(i));
			} else if (i == 1) {
				Assert.assertEquals(2.0, sv.get(i));
			} else if (i == 8) {
				Assert.assertEquals(0.4, sv.get(i));
			} else {
				Assert.assertEquals(0.0, sv.get(i));
			}
		}
		sv.get(10);
	}
	
	@Test
	public void testFromVector() throws Exception {
		Vector vector = new Vector(10);
		vector.set(0, 0);
		vector.set(1, 0.1);
		vector.set(6, 0.6);
		SparseVector sv = new SparseVector(vector);
		Assert.assertEquals(10, sv.getDim());
		for (int i = 0; i < sv.getDim(); i++) {
			if (i == 0) {
				Assert.assertEquals(0.0, sv.get(i));
			} else if (i == 1) {
				Assert.assertEquals(0.1, sv.get(i));
			} else if (i == 6) {
				Assert.assertEquals(0.6, sv.get(i));
			} else {
				Assert.assertEquals(0.0, sv.get(i));
			}
		}
	}
	
	@Test
	public void testToVector() throws Exception {
		Vector vector = sv.toVector();
		for (int i = 0; i < vector.getDim(); i++) {
			if (i == 0) {
				Assert.assertEquals(1.0, vector.get(i));
			} else if (i == 1) {
				Assert.assertEquals(2.0, vector.get(i));
			} else if (i == 8) {
				Assert.assertEquals(0.4, vector.get(i));
			} else {
				Assert.assertEquals(0.0, vector.get(i));
			}
		}
	}
}
