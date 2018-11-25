package math;

import static org.junit.Assert.*;

import java.util.ArrayList;

import danch.math.formula.*;
import org.junit.Before;
import org.junit.Test;

public class TestTerm {

	@Before
	public void setUp() throws Exception {
	}

    @Test
	public void testExponentialDifferentiat() {
		VariableRef x0 = new IndexedVariableRef('x', 0);
		Power exp = new Power(x0, new Literal(3.0));
		Formula expPrime = exp.differentiate(x0);
		Formula expDoublePrime = expPrime.differentiate(x0);
		double value = expDoublePrime.evaluate(new SimpleVariableBinder(4.0));
		assertEquals(24.0, value, 0.00001);
		Formula expTripplePrime = expDoublePrime.differentiate(x0);
		value = expTripplePrime.evaluate(new SimpleVariableBinder(4.0));
		assertEquals(6.0, value, 0.00001);
	}

	
	@Test
	public void testPower_chainrule() {
		ArrayList<Formula> children = new ArrayList<>();
		children.add(new Power(new IndexedVariableRef('x', 0), new Literal(2.0)));
		children.add(new Literal(2.0));
		Sum sum = new Sum(children);
		Power power = new Power(sum, new Literal(2));
		Formula diff = power.differentiate(new IndexedVariableRef('x', 0));
		double value = diff.evaluate(new SimpleVariableBinder(4.0));
		assertEquals(288.0, value, 0.000001);
	}
	
	@Test
	public void testPower_chainrule_cubic() {
		ArrayList<Formula> children = new ArrayList<>();
		children.add(new Power(new IndexedVariableRef('x', 0), new Literal(2.0)));
		children.add(new Literal(2.0));
		Sum sum = new Sum(children);
		Power power = new Power(sum, new Literal(3));
		Formula diff = power.differentiate(new IndexedVariableRef('x', 0));
		double value = diff.evaluate(new SimpleVariableBinder(4.0));
		assertEquals(7776.0, value, 0.000001);
	}
	
	@Test
	public void testDivision_simple() {
		Formula numerator = new Sum(new Product(new Literal(4.0), new IndexedVariableRef('x', 0)), new Negate(new Literal(2.0)));
		Formula denominator = new Sum(new Power(new IndexedVariableRef('x', 0), new Literal(2.0)), new Literal(1.0));
		Formula division = new Division(numerator, denominator);
		Formula derivative = division.differentiate(new IndexedVariableRef('x', 0));
	}
}
