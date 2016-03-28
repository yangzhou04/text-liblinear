package represent;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class VectorTest {

	private Vector vec;

	@Before
	public void setUp() throws Exception {
		vec = new Vector(10);
		vec.set(0, 0);
		vec.set(1, 0.1);
		vec.set(2, 0.2);
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testSet() throws Exception {
		vec.set(0, 0.6);
		Assert.assertEquals(0.6, vec.get(0));
		vec.set(10, 1);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGet() throws Exception {
		vec.set(0, 0.6);
		Assert.assertEquals(0.6, vec.get(0));
		vec.get(10);
	}
	
	@Test
	public void testConstruct() throws Exception {
		Vector vec1 = new Vector(new Double[] {1.0, 1.1, 1.3});
		Assert.assertEquals(3, vec1.getDim());
		Assert.assertEquals(1.0, vec1.get(0));
		Assert.assertEquals(1.1, vec1.get(1));
		Assert.assertEquals(1.3, vec1.get(2));
		
		Vector vec2 = new Vector(new double[] {1.0, 1.1, 1.3});
		Assert.assertEquals(3, vec2.getDim());
		Assert.assertEquals(1.0, vec2.get(0));
		Assert.assertEquals(1.1, vec2.get(1));
		Assert.assertEquals(1.3, vec2.get(2));
		
		Vector vec3 = new Vector(3);
		Assert.assertEquals(3, vec3.getDim());
		Assert.assertEquals(0.0, vec3.get(0));
		Assert.assertEquals(0.0, vec3.get(1));
		Assert.assertEquals(0.0, vec3.get(2));
	}
	
	@Test
	public void testToSparseVector() throws Exception {
		SparseVector sv = vec.toSparseVector();
		Assert.assertEquals(10, sv.getDim());
		for (int i = 3; i < 10; i++)
			Assert.assertEquals(0.0, sv.get(i));
		Assert.assertEquals(0.0, sv.get(0));
		Assert.assertEquals(0.1, sv.get(1));
		Assert.assertEquals(0.2, sv.get(2));
	}
	
	@Test
	public void testFromSparseVector() throws Exception {
		SparseVector sv = new SparseVector(10);
		sv.set(5, 0.5);
		
		Vector vector = new Vector(sv);
		Assert.assertEquals(10, vec.getDim());
		Assert.assertEquals(0.5, vector.get(5));
		for (int i = 0; i < 10; i++) {
			if (i != 5) {
				Assert.assertEquals(0.0, vector.get(i));
			}
		}
	}
	
}
