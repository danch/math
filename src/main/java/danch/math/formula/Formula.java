package danch.math.formula;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class Formula implements Cloneable {
	public abstract double evaluate(Function<VariableRef, Double> variableBinder);
	public abstract Formula differentiate(VariableRef withRespectTo);
	public abstract boolean isInvariant(VariableRef withRespectTo);
	public abstract boolean isConstant();
	public abstract Collection<Formula> postOrderTraversal();
	
	public Formula cloneValue() {
		try {
			return (Formula)this.clone();
		} catch (CloneNotSupportedException c) {
			//yes, it is...
		}
		return null;//will never happen, but CloneNotSupported is a checked exception 
	}
	public static double emptyVariableBinder(VariableRef variable) {
		throw new IllegalStateException("emptyVariableBinder called - this should only be used when an expression is known to be constant");
	}
	public abstract Formula algebraicMultiply(VariableRef variableRef);
	public abstract void bindVariablesAsConstants(char series, Function<VariableRef, Double> variableBinder);
	
	public static Formula[] getGradiant(Formula f, List<VariableRef> variables) {
		Formula[] gradiant = new Formula[variables.size()];
		int index = 0;
		for (VariableRef var : variables) {
			gradiant[index] = f.differentiate(var);
			index++;
		}
		return gradiant;
	}
}
