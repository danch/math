package math;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Optional;

import danch.math.formula.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestTerm {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSimpleTerm() {
		Term simpleTerm=new Term(new Literal(2.0), new IndexedVariableRef('x', 0), new Literal(2.0));
		double value = simpleTerm.evaluate((variableRef)-> 2.0d);
		Assert.assertEquals(8.0, value,0.00001);
	}
	
	@Test
	public void testTermWithAddition() {
		Term simpleTerm=new Term(new Literal(2.0), new IndexedVariableRef('x', 0), new Literal(2.0));
		Addition addition = new Addition(simpleTerm, new Literal(4.0));
		double value = addition.evaluate((variableRef)-> 2.0d);
		Assert.assertEquals(12.0, value,0.00001);
	}
	
	@Test
	public void testTermWithMultiplication() {
		Term simpleTerm=new Term(new Literal(2.0), new IndexedVariableRef('x', 0), new Literal(2.0));
		Power operator = new Power(simpleTerm, new Literal(3.0));
		double value = operator.evaluate((variableRef)-> 2.0d);
		Assert.assertEquals(512.0, value,0.00001);
	}
	
	@Test
	public void testTermWithDivision() {
		Term simpleTerm=new Term(new Literal(2.0), new IndexedVariableRef('x', 0), new Literal(2.0));
		Division operator = new Division(simpleTerm, new Literal(2.0));
		double value = operator.evaluate((variableRef)-> 2.0d);
		Assert.assertEquals(4.0, value,0.00001);
	}
	
	@Test
	public void testTermWithNegate() {
		Term simpleTerm=new Term(new Literal(2.0), new IndexedVariableRef('x', 0), new Literal(2.0));
		Negate operator = new Negate(simpleTerm);
		double value = operator.evaluate((variableRef)-> 2.0d);
		Assert.assertEquals(-8.0, value,0.00001);
	}

	@Test
	public void testQuadratic_base() {
		Addition firstOp = buildBareQuadratic();
		double value = firstOp.evaluate((variableRef)-> 2.0d);
		Assert.assertEquals(10.0, value,0.00001);
	}

	private Addition buildBareQuadratic() {
		Term square = new Term(Optional.empty(), Optional.of(new IndexedVariableRef('x', 0)), Optional.of(new Literal(2.0)));
		Term noExp = new Term(Optional.empty(), Optional.of(new IndexedVariableRef('x', 0)), Optional.empty());
		Formula constant = new Literal(4.0);
		Addition secondOp = new Addition(noExp, constant);
		Addition firstOp = new Addition(square, secondOp);
		return firstOp;
	}

	@Test
	public void testQuadratic_coefficients() {
		Addition firstOp = buildQuadraticWithCoefficients();
		double value = firstOp.evaluate((variableRef)-> 2.0d);
		Assert.assertEquals(20.0, value,0.00001);
	}

	private Addition buildQuadraticWithCoefficients() {
		Term square = new Term(Optional.of(new Literal(3.0)), Optional.of(new IndexedVariableRef('x', 0)), Optional.of(new Literal(2.0)));
		Term noExp = new Term(Optional.of(new Literal(2.0)), Optional.of(new IndexedVariableRef('x', 0)), Optional.empty());
		Formula constant = new Literal(4.0);
		Addition secondOp = new Addition(noExp, constant);
		Addition firstOp = new Addition(square, secondOp);
		return firstOp;
	}
	
	@Test
	public void testToString_withCoefficientQuadratic() {
		String testValue = buildQuadraticWithCoefficients().toString();
		assertEquals("3x0^2 + 2x0 + 4", testValue);
	}
	
	@Test
	public void testDifferentiate_bareExponent() {
		Term term = new Term(new Literal(1.0), new IndexedVariableRef('x', 0), new Literal(2.0));
		Formula dudx = term.differentiate(new IndexedVariableRef('x', 0));
		String duString = dudx.toString();
		assertEquals("2x0", duString);
	}
	
	@Test
	public void testDifferentiate_withBaseQuadratic() {
		Formula result = buildBareQuadratic().differentiate(new IndexedVariableRef('x', 0));
		String resultString = result.toString();
		assertEquals("2x0 + 1", resultString);
	}
	
	@Test
	public void testExponentialDifferentiat() {
		VariableRef x0 = new IndexedVariableRef('x', 0);
		Power exp = new Power(x0, new Literal(3.0));
		Formula expPrime = exp.differentiate(x0);
		Formula expDoublePrime = expPrime.differentiate(x0);
		double value = expDoublePrime.evaluate((variableRef) -> 4.0);
		assertEquals(24.0, value, 0.00001);
		Formula expTripplePrime = expDoublePrime.differentiate(x0);
		value = expTripplePrime.evaluate((variableRef) -> 4.0);
		assertEquals(6.0, value, 0.00001);
	}
	
	@Test
	public void testSum_evaluate() {
		ArrayList<Formula> children = new ArrayList<>();
		children.add(new Term(new Literal(2.0), new IndexedVariableRef('x', 0), new Literal(2.0)));
		children.add(new Term(new Literal(4.0), new IndexedVariableRef('x', 0), new Literal(1.0)));
		children.add(new Literal(4.0));
		Sum sum = new Sum(children);
		double value = sum.evaluate((variableRef) -> 4.0);
		assertEquals(52.0, value, 0.00001);
	}
	
	@Test
	public void testSum_differentiate() {
		ArrayList<Formula> children = new ArrayList<>();
		children.add(new Term(new Literal(2.0), new IndexedVariableRef('x', 0), new Literal(2.0)));
		children.add(new Term(new Literal(4.0), new IndexedVariableRef('x', 0), new Literal(1.0)));
		children.add(new Literal(4.0));
		Sum sum = new Sum(children);
		Formula derivative = sum.differentiate(new IndexedVariableRef('x', 0));
		assertEquals("4x0 + 4", derivative.toString());
	}
	
	@Test
	public void testPower_chainrule() {
		ArrayList<Formula> children = new ArrayList<>();
		children.add(new Power(new IndexedVariableRef('x', 0), new Literal(2.0)));
		children.add(new Literal(2.0));
		Sum sum = new Sum(children);
		Power power = new Power(sum, new Literal(2));
		Formula diff = power.differentiate(new IndexedVariableRef('x', 0));
		double value = diff.evaluate((variableRef) -> 4.0);
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
		double value = diff.evaluate((variableRef) -> 4.0);
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
