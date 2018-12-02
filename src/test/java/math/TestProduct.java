package math;

import static org.junit.Assert.*;

import java.util.ArrayList;

import danch.math.formula.*;
import org.junit.Before;
import org.junit.Test;

public class TestProduct {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testProduct_ProductOfFunction() {
		Formula firstTerm = new Literal(4.0);
		Formula secondTerm = new Sum(new Power(new IndexedVariableRef('x', 0), new Literal(2.0)), new IndexedVariableRef('x', 1));
		Product product = new Product(firstTerm, secondTerm);
		Formula dydx0 = product.differentiate(new IndexedVariableRef('x', 0));
		double value = dydx0.evaluate(new SimpleVariableBinder(2.0));
		assertEquals(16, value, 0.00001);
	}
	
	@Test
	public void testProduct_DivisionOfProduct() {
		Formula firstTerm = new Literal(4.0);
		Formula secondTerm = new Sum(new Power(new IndexedVariableRef('x', 0), new Literal(2.0)), new IndexedVariableRef('x', 1));
		Product product = new Product(firstTerm, secondTerm);
		
		Formula denominator = new Sum(new IndexedVariableRef('x', 0), new IndexedVariableRef('x', 1));
		Division div = new Division(product, denominator);
		
		Formula dydx0 = div.differentiate(new IndexedVariableRef('x', 0));
		double value = dydx0.evaluate(new SimpleVariableBinder(2.0));
		assertEquals(2.5, value, 0.00001);
	}

	@Test
	public void testProduct_TwoFactors() {
		Formula factorOne = new Sum(new IndexedVariableRef('x', 0), new Literal(2.0));
		Formula factorTwo = new Sum(new Power(new IndexedVariableRef('x', 0), new Literal(2)), new Literal(4));
		Formula product = new Product(factorOne, factorTwo);
		Formula dudx0 = product.differentiate(new IndexedVariableRef('x', 0));
		double value = dudx0.evaluate(new SimpleVariableBinder(2.0));
		assertEquals(24.0, value, 0.00001);
	}

	@Test
	public void testProduct_TwoFactorsAndConstant() {
		ArrayList<Formula> components = new ArrayList<>();
		components.add(new Literal(4));
		components.add(new Sum(new IndexedVariableRef('x', 0), new Literal(2.0)));
		components.add(new Sum(new Power(new IndexedVariableRef('x', 0), new Literal(2)), new Literal(4)));
		Formula product = new Product(components);
		Formula dudx0 = product.differentiate(new IndexedVariableRef('x', 0));
		double value = dudx0.evaluate(new SimpleVariableBinder(2.0));
		assertEquals(96.0, value, 0.00001);
	}

	@Test
	public void testProduct_ThreeFactors() {
		ArrayList<Formula> components = new ArrayList<>();
		components.add(new Sum(new IndexedVariableRef('x', 0), new Literal(2.0)));
		components.add(new Sum(new Power(new IndexedVariableRef('x', 0), new Literal(2)), new Literal(4)));
		components.add(new Sum(new Power(new IndexedVariableRef('x', 0), new Literal(2)), new IndexedVariableRef('y', 0)));
		Formula product = new Product(components);
		Formula dudx0 = product.differentiate(new IndexedVariableRef('x', 0));
		double value = dudx0.evaluate(new SimpleVariableBinder(2.0));
		assertEquals(272.0, value, 0.00001);
	}
}
