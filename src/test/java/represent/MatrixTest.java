package represent;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class MatrixTest {

	private Matrix mat;
	@Before
	public void setUp() throws Exception {
		mat = new Matrix(10, 9);
		mat.set(0, 0, 0);
		mat.set(1, 1, 0.1);
		mat.set(1, 3, 0.3);
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testSet() throws Exception {
		mat.set(0, 0, 0.1);
		Assert.assertEquals(0.1, mat.get(0, 0));
		mat.set(0, 10, 0.1);
	}
	
	@Test
	public void testToSparseMatrix() throws Exception {
		SparseMatrix sm = mat.toSparseMatrix();
		Assert.assertEquals(10, sm.getDimX());
		Assert.assertEquals(9, sm.getDimY());
		
		Assert.assertEquals(0.0, sm.get(0, 0));
		Assert.assertEquals(0.1, sm.get(1, 1));
		Assert.assertEquals(0.3, sm.get(1, 3));
	}
	
	@Test
	public void testFromSparseMatrix() throws Exception {
		SparseMatrix sm = new SparseMatrix(10, 9);
		sm.set(0, 0, 1.0);
		sm.set(1, 1, 1.1);
		sm.set(1, 2, 1.2);
		sm.set(9, 8, 1.98);
		Matrix m = new Matrix(sm);
		Assert.assertEquals(10, m.getDimX());
		Assert.assertEquals(9, m.getDimY());
		Assert.assertEquals(1.0, m.get(0, 0));
		Assert.assertEquals(1.1, m.get(1, 1));
		Assert.assertEquals(1.2, m.get(1, 2));
		Assert.assertEquals(1.98, m.get(9, 8));
	}
	
	@Test
	public void testConstruct() throws Exception {
		
	}
}
