package math;

import danch.math.formula.TaggedVariableRef;
import danch.math.formula.Formula;
import danch.math.formula.Power;
import danch.math.formula.Literal;
import danch.math.formula.Sum;
import danch.math.formula.Product;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestTaggedVariableRef {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test_PowerOfInvariant() {
		Formula termOne = new Power(new TaggedVariableRef('x', "tag0"), new Literal(2.0));
		Formula baseTermOne = new Power(new TaggedVariableRef('x', "tag1"), new Literal(2.0));
		Formula baseTermTwo = new Product(new Literal(4.0), new TaggedVariableRef('x', "tag1"));
		Formula base = new Sum(baseTermOne, baseTermTwo);
		Formula termTwo = new Power(base, new Literal(2.0));
		
		Formula wholeFormula = new Sum(termOne, termTwo);
		
		Formula dydx0 = wholeFormula.differentiate(new TaggedVariableRef('x', "tag0"));
		double value = dydx0.evaluate((variableRef) -> 4.0);
		assertEquals(8.0, value, 0.00001);
	}

	@Test
	public void test_EqualsWithNullTag() {
		String[] var1Tags = {"tag0", null};
		TaggedVariableRef var1 = new TaggedVariableRef('x', var1Tags);
		String[] var2Tags = {"tag0", "tag1"};
		TaggedVariableRef var2 = new TaggedVariableRef('x', var2Tags);
		String[] var3Tags = {"tag0", null};
		TaggedVariableRef var3 = new TaggedVariableRef('x', var3Tags);

		assertNotEquals("var1 should not equal var2", var1, var2);
		assertEquals("var2 should equal var3", var1, var3);
	}
}
