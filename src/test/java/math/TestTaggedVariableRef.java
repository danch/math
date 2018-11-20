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

}
