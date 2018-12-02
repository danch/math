package danch.math.formula;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Negate extends Formula {
	Formula operand;
	
	public Negate(Formula operand) {
		this.operand = operand;
	}

	@Override
	public double evaluate(VariableBinder variableBinder) {
		return -operand.evaluate(variableBinder);
	}


    @Override
    public Collection<Formula> postOrderTraversal() {
        List<Formula> collection = new ArrayList<Formula>();
        collection.addAll(operand.postOrderTraversal());
        collection.add(this);
        return collection;
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
	public void bindVariablesAsConstants(char series, VariableBinder variableBinder) {
		operand.bindVariablesAsConstants(series, variableBinder);
	}
}
