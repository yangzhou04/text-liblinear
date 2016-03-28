package represent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by zhouyang on 16/3/25.
 */
public class SparseMatrixTest {

    private SparseMatrix sm;

    @Before
    public void setUp() throws Exception {
        Matrix mat = new Matrix(new double[][] {
                new double[]{1,0,0,0},
                new double[]{0,1,0,0},
                new double[]{0,0,1,0},
        });
        sm = new SparseMatrix(mat);
    }

    @Test
    public void testGet() throws Exception {
        Assert.assertTrue(Double.compare(1.0, sm.get(0,0))==0);
        Assert.assertTrue(Double.compare(1.0, sm.get(1,1))==0);
        Assert.assertTrue(Double.compare(1.0, sm.get(2,2))==0);
        Assert.assertTrue(Double.compare(0.0, sm.get(0,1))==0);
        Assert.assertTrue(Double.compare(0.0, sm.get(2,3))==0);
    }

    @Test
    public void testGetx() throws Exception {
        SparseVector sv = sm.getx(0);
        Assert.assertTrue(Double.compare(1.0, sv.get(0)) == 0);
        Assert.assertTrue(Double.compare(0.0, sv.get(3)) == 0);
        Assert.assertEquals(4, sv.getDim());
    }

    @Test
    public void testSetx() throws Exception {
        SparseVector sv = new SparseVector(new double[]{0,1,0,1});
        sm.setx(0, sv);
        Assert.assertTrue(Double.compare(1.0, sm.get(0,3))==0);
    }

    @Test
    public void testSet() throws Exception {
        sm.set(1,1,2);
        Assert.assertTrue(Double.compare(2.0, sm.get(1,1))==0);
    }

    @Test
    public void testGetDimX() throws Exception {
        Assert.assertEquals(3, sm.getDimX());
    }

    @Test
    public void testGetDimY() throws Exception {
        Assert.assertEquals(4, sm.getDimY());
    }

    @Test
    public void testFromMatrix() throws Exception {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == j)
                    Assert.assertTrue(Double.compare(1.0, sm.get(i, j))==0);
                else
                    Assert.assertTrue(Double.compare(0.0, sm.get(i, j))==0);
            }
        }
    }

    @Test
    public void testToMatrix() throws Exception {
        Matrix mat = sm.toMatrix();
        Assert.assertEquals(3, mat.getDimX());
        Assert.assertEquals(4, mat.getDimY());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == j)
                    Assert.assertTrue(Double.compare(1.0, mat.get(i, j)) == 0);
                else
                    Assert.assertTrue(Double.compare(0.0, mat.get(i, j)) == 0);
            }
        }
    }

}