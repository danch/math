package danch.math.formula;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Literal extends Formula {
	double value;
	
	public Literal(double val) {
		this.value = val;
	}
	@Override
	public double evaluate(Function<VariableRef, Double> variableBinder) {
		return value;
	}

	static DecimalFormat format=new DecimalFormat("####0.###");
	@Override
	public String toString() {
		return format.format(value);
	}
	@Override
	public Formula differentiate(VariableRef withRespectTo) {
		return new Literal(0.0);
	}
	
	@Override
	public boolean equals(Object other) {
		//note: this is an OO equals, not a mathematic equals operator
		if (!(other instanceof Literal)) {
			return false;
		}
		Literal rhs = (Literal)other;
		return value == rhs.value;
	}
	@Override
	public boolean isInvariant(VariableRef withRespectTo) {
		return true;
	}
	@Override
	public boolean isConstant() {
		return true;
	}
	@Override
	public Formula algebraicMultiply(VariableRef variableRef) {
		return new Product(this, variableRef);
	}
	@Override
	public void bindVariablesAsConstants(char series, Function<VariableRef, Double> variableBinder) {
		//No-op for a constant
	}

    @Override
    public Collection<Formula> postOrderTraversal() {
        return Arrays.asList(this);
    }
}
