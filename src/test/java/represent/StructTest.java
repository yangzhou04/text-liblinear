package represent;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class StructTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testNamedStruct() throws Exception {
		Struct<String> s = new Struct<String>();
		s.put("test", "1");
		Assert.assertEquals("1", s.get("test"));
	}
}
