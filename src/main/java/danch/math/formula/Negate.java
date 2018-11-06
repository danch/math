package danch.math.formula;

import java.util.function.BiFunction;

public class Negate extends Formula {
	Formula operand;
	
	public Negate(Formula operand) {
		this.operand = operand;
	}

	@Override
	public double evaluate(BiFunction<Character, int[], Double> variableBinder) {
		return -operand.evaluate(variableBinder);
	}

	
	@Override
	public String toString() {
		return "-("+operand.toString()+")";
	}

	@Override
	public Formula differentiate(VariableRef withRespectTo) {
		return new Negate(operand.differentiate(withRespectTo));
	}

	@Override
	public boolean isInvariant(VariableRef withRespectTo) {
		return operand.isInvariant(withRespectTo);
	}

	@Override
	public boolean isConstant() {
		return operand.isConstant();
	}

	@Override
	public Formula algebraicMultiply(VariableRef variableRef) {
		return new Negate(operand.algebraicMultiply(variableRef));
	}

	@Override
	public void bindVariablesAsConstants(char series, BiFunction<Character, int[], Double> variableBinder) {
		operand.bindVariablesAsConstants(series, variableBinder);
	}
}
