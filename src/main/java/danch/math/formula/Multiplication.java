package danch.math.formula;

import java.util.function.BiFunction;

@Deprecated
public class Multiplication extends Formula {
	Formula left;
	Formula right;
	
	public Multiplication(Formula left, Formula right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public double evaluate(BiFunction<Character, Integer, Double> variableBinder) {
		return left.evaluate(variableBinder) * right.evaluate(variableBinder);
	}
	
	@Override
	public String toString() {
		return "("+left.toString()+") * ("+right.toString()+")";
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
	public Formula differentiate(VariableRef withRespectTo) {
		if (left.isInvariant(withRespectTo) && right.isInvariant(withRespectTo)) {
			//both are invariant == constant in the given independent variable, so our du/dx is 0
			return new Literal(0.0);
		} else
		if (left.isInvariant(withRespectTo)) {
			//left side is invariant, right isn't
			if (right instanceof VariableRef) {
				return left;//special case of product rule for x^1
			}
			Formula newRight = right.differentiate(withRespectTo);
			return new Multiplication(left, newRight);
		} else
		if (right.isInvariant(withRespectTo)) {
			//right side is invariant, left isn't
			if (left instanceof VariableRef) {
				return right;//special case of product rule for x^1
			}
			Formula newLeft = left.differentiate(withRespectTo);
			return new Multiplication(right, newLeft);
		}
		//right and left both vary with variable in consideration, TODO
		throw new IllegalStateException();
	}

	@Override
	public Formula algebraicMultiply(VariableRef variableRef) {
		return new Multiplication(variableRef, this);
	}

	@Override
	public void bindVariablesAsConstants(char series, BiFunction<Character, Integer, Double> variableBinder) {
		left.bindVariablesAsConstants(series, variableBinder);
		right.bindVariablesAsConstants(series, variableBinder);
	}
}
