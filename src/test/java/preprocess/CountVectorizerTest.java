package preprocess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import junit.framework.Assert;

public class CountVectorizerTest {

	private CountVectorizer countVectorizer;
	private List<List<String>> textX = Lists.newArrayList();

	@Before
	public void setUp() throws Exception {
		countVectorizer = new CountVectorizer();
		textX.add(Lists.newArrayList("a", "b", "c", "d", "a", "b", "c"));
	}

	@Test
	public void testEmptyFit() throws Exception {
		// one empty instance
		List<String> instance = Lists.newArrayList();
		List<Entry<Integer, Integer>> res =
		        (List<Entry<Integer, Integer>>) countVectorizer.fit(instance)
		                .transform(instance);
		Assert.assertEquals(0, res.size());

		// empty instances
		List<List<String>> instances = Lists.newArrayList();
		instances.add(new ArrayList<String>());
		instances.add(new ArrayList<String>());
		instances.add(new ArrayList<String>());
		List<List<Entry<Integer, Integer>>> ress =
		        (List<List<Entry<Integer, Integer>>>) countVectorizer
		                .fit(instances).transform(instances);
		Assert.assertEquals(3, ress.size());
		Assert.assertEquals(0, ress.get(0).size());
		Assert.assertEquals(0, ress.get(1).size());
		Assert.assertEquals(0, ress.get(2).size());
	}

	@Test(expected = ClassCastException.class)
	public void testFitWrongCast() throws Exception {
		List<List<String>> instances = Lists.newArrayList();
		instances.add(Lists.newArrayList("a", "b"));
		instances.add(Lists.newArrayList("c", "b"));
		instances.add(Lists.newArrayList("d", "b"));
		// cast wrong, should be List<List<Entry<Integer, Integer>>>

		List<Entry<Integer, Integer>> ress =
		        (List<Entry<Integer, Integer>>) countVectorizer
		                .fit(instances).transform(instances);

		Assert.assertEquals(3, ress.size());
		Assert.assertEquals(false, ress.get(0) instanceof Entry);
		Assert.assertEquals(true, ress.get(0) instanceof List);

		ress.get(1).getKey(); // throw ClassCastException
	}

	@Test
	public void testFit() throws Exception {
		// fit one entry
		List<String> instance =
		        Lists.newArrayList("a", "b", "c", "d", "a", "b", "c");
		countVectorizer.fit(instance);

		List<Entry<Integer, Integer>> result0 =
		        (List<Entry<Integer, Integer>>) countVectorizer
		                .transform(instance);
		Assert.assertEquals(result0.get(0).getKey().intValue(), 1);
		Assert.assertEquals(result0.get(0).getValue().intValue(), 2);

		Assert.assertEquals(result0.get(1).getKey().intValue(), 2);
		Assert.assertEquals(result0.get(1).getValue().intValue(), 2);

		Assert.assertEquals(result0.get(2).getKey().intValue(), 3);
		Assert.assertEquals(result0.get(2).getValue().intValue(), 2);

		Assert.assertEquals(result0.get(3).getKey().intValue(), 4);
		Assert.assertEquals(result0.get(3).getValue().intValue(), 1);

		List<List<String>> textXX = Lists.newArrayList();
		textXX.add(Lists.newArrayList("a", "b", "c"));
		textXX.add(Lists.newArrayList("d", "b", "c"));
		Assert.assertEquals(2, textXX.size());
		countVectorizer.fit(textXX);
		List<List<Entry<Integer, Integer>>> res =
		        (List<List<Entry<Integer, Integer>>>) countVectorizer
		                .transform(textXX);
		Assert.assertEquals(1, res.get(0).get(0).getKey().intValue());
		Assert.assertEquals(1, res.get(0).get(0).getValue().intValue());
		Assert.assertEquals(2, res.get(0).get(1).getKey().intValue());
		Assert.assertEquals(1, res.get(0).get(1).getValue().intValue());
		Assert.assertEquals(3, res.get(0).get(2).getKey().intValue());
		Assert.assertEquals(1, res.get(0).get(2).getValue().intValue());
	}

	@Test
	public void testFitTransform() throws Exception {
		List<List<Entry<Integer, Integer>>> result =
		        (List<List<Entry<Integer, Integer>>>) countVectorizer
		                .fitTransform(textX);
		List<Entry<Integer, Integer>> result0 = result.get(0);
		Assert.assertEquals(result0.get(0).getKey().intValue(), 1);
		Assert.assertEquals(result0.get(0).getValue().intValue(), 2);

		Assert.assertEquals(result0.get(1).getKey().intValue(), 2);
		Assert.assertEquals(result0.get(1).getValue().intValue(), 2);

		Assert.assertEquals(result0.get(2).getKey().intValue(), 3);
		Assert.assertEquals(result0.get(2).getValue().intValue(), 2);

		Assert.assertEquals(result0.get(3).getKey().intValue(), 4);
		Assert.assertEquals(result0.get(3).getValue().intValue(), 1);
	}

	@Test
	public void testSerializeAndDeserialize() throws Exception {
		countVectorizer.fit(textX);
		countVectorizer.serialize("data/count_vec.ser");
		CountVectorizer countVectorizer2 = new CountVectorizer();
		countVectorizer2.deserialize("data/count_vec.ser");

		List<List<Entry<Integer, Integer>>> result =
		        (List<List<Entry<Integer, Integer>>>) countVectorizer2
		                .fitTransform(textX);

		List<Entry<Integer, Integer>> result0 = result.get(0);
		Assert.assertEquals(result0.get(0).getKey().intValue(), 1);
		Assert.assertEquals(result0.get(0).getValue().intValue(), 2);

		Assert.assertEquals(result0.get(1).getKey().intValue(), 2);
		Assert.assertEquals(result0.get(1).getValue().intValue(), 2);

		Assert.assertEquals(result0.get(2).getKey().intValue(), 3);
		Assert.assertEquals(result0.get(2).getValue().intValue(), 2);

		Assert.assertEquals(result0.get(3).getKey().intValue(), 4);
		Assert.assertEquals(result0.get(3).getValue().intValue(), 1);
	}

}
