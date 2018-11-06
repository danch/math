package danch.math.formula;

import java.util.function.BiFunction;

public class Addition extends Formula {
	Formula left;
	Formula right;
	
	public Addition(Formula left, Formula right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public double evaluate(BiFunction<Character, int[], Double> variableBinder) {
		return left.evaluate(variableBinder) + right.evaluate(variableBinder);
	}
	
	@Override
	public String toString() {
		return left.toString()+" + "+right.toString();
	}

	@Override
	public Formula differentiate(VariableRef withRespectTo) {
		Formula newLeft = left.differentiate(withRespectTo);
		Formula newRight = right.differentiate(withRespectTo);
		if (newLeft==null && newRight==null) {
			return null;
		}
		if (newLeft==null || (newLeft.isConstant() && newLeft.evaluate(Formula::emptyVariableBinder)==0.0)) {
			return newRight;
		}
		if (newRight==null || (newRight.isConstant() && newRight.evaluate(Formula::emptyVariableBinder)==0.0)) {
			return newLeft;
		}
		return new Addition(newLeft, newRight);
	}

	@Override
	public boolean isInvariant(VariableRef withRespectTo) {
		return left.isInvariant(withRespectTo) && right.isInvariant(withRespectTo);
	}

	@Override
	public boolean isConstant() {
		return left.isConstant() && right.isConstant();
	}

	@Override
	public Formula algebraicMultiply(VariableRef variableRef) {
		Formula newLeft = left.algebraicMultiply(variableRef);
		Formula newRight = right.algebraicMultiply(variableRef);
		return new Addition(newLeft, newRight);
	}

	@Override
	public void bindVariablesAsConstants(char series, BiFunction<Character, int[], Double> variableBinder) {
		left.bindVariablesAsConstants(series, variableBinder);
		right.bindVariablesAsConstants(series, variableBinder);
	}

}
