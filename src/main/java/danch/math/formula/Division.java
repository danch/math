package danch.math.formula;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;


public class Division extends Formula {
	Formula numerator;
	Formula denominator;
	
	public Division(Formula left, Formula right) {
		this.numerator = left;
		this.denominator = right;
	}

	@Override
	public double evaluate(Function<VariableRef, Double> variableBinder) {
		return numerator.evaluate(variableBinder) / denominator.evaluate(variableBinder);
	}

    @Override
    public Collection<Formula> postOrderTraversal() {
        Collection<Formula> children = new ArrayList<>();
        children.addAll(numerator.postOrderTraversal());
        children.addAll(denominator.postOrderTraversal());
        children.add(this);
        return children;
    }
	
	@Override
	public String toString() {
		return "("+numerator.toString()+") / ("+denominator.toString()+")";
	}


	@Override
	public boolean isInvariant(VariableRef withRespectTo) {
		return numerator.isInvariant(withRespectTo) && denominator.isInvariant(withRespectTo);
	}

	@Override
	public boolean isConstant() {
		return numerator.isConstant() && denominator.isConstant();
	}

	@Override
	public Formula differentiate(VariableRef withRespectTo) {
		Formula numeratorDeriv = numerator.differentiate(withRespectTo);
		if (numeratorDeriv == null) {
			return null;
		}
		if (denominator.isInvariant(withRespectTo)) {
			//we can re-write this to be (1/den)(num) and derive from there
			Division constantPart = new Division(new Literal(1.0), denominator);
			Product result = new Product(constantPart, numeratorDeriv);
			return result;
		} else {
			Formula denominatorDeriv = denominator.differentiate(withRespectTo);
			Formula numFirstTerm = new Product(numeratorDeriv, denominator);
			Formula numSecondTerm = new Product(denominatorDeriv, numerator);
			Formula newNumerator = new Sum(numFirstTerm, new Negate(numSecondTerm));
			
			Formula newDenominator = new Power(denominator, new Literal(2.0));
			
			Formula result = new Division(newNumerator, newDenominator);
			return result;
		}
	}

	@Override
	public Formula algebraicMultiply(VariableRef variableRef) {
		Formula newNumerator = numerator.algebraicMultiply(variableRef);
		return new Division(newNumerator, denominator);
	}

	@Override
	public void bindVariablesAsConstants(char series, Function<VariableRef, Double> variableBinder) {
		numerator.bindVariablesAsConstants(series, variableBinder);
		denominator.bindVariablesAsConstants(series, variableBinder);
	}
}
