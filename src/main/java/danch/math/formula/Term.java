package danch.math.formula;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Term extends Formula {
	Optional<Formula> coefficient;
	Optional<VariableRef> variable;
	Optional<Formula> exponent;
	
	public Term(Optional<Formula> coefficient, Optional<VariableRef> variable, Optional<Formula> exponent) {
		this.coefficient = coefficient;
		this.variable = variable;
		this.exponent = exponent;
	}
	public Term(Formula coefficient, VariableRef variable, Formula exponent) {
		this.coefficient = Optional.of(coefficient);
		this.variable = Optional.of(variable);
		this.exponent = Optional.of(exponent);
	}

	@Override
	public double evaluate(Function<VariableRef, Double> variableBinder) {
		double expExpansion = Math.pow(variable.map(var -> var.evaluate(variableBinder)).orElse(1.0d), 
				exponent.map(exp->exp.evaluate(variableBinder)).orElse(1.0d));
		return expExpansion * coefficient.map(co -> co.evaluate(variableBinder)).orElse(1.0).doubleValue();
	}
	
	@Override
	public String toString() {
		String coString = coefficient.map(co->co.toString()).orElse("");
		String varString = variable.map(var -> var.toString()).orElse("");
		String expString = exponent.map(exp-> {
			if (exp.isConstant() && exp.evaluate(Formula::emptyVariableBinder)==1.0) {
				return "";
			} else {
				return "^"+exp.toString();
			}
		}).orElse("");
		return coString + varString + expString;
	}
	@Override
	public Formula differentiate(VariableRef withRespectTo) {
		if (variable.map(var->var.equals(withRespectTo)).orElse(false)) {
			Formula newCoefficient = new Product(coefficient.map(co->co).orElse(new Literal(1.0)),
					exponent.map(exp->exp).orElse(new Literal(1.0)));
			if (newCoefficient.isConstant()) {
				newCoefficient = new Literal(newCoefficient.evaluate(Formula::emptyVariableBinder));
			}
			Formula newExponent = new Addition(exponent.map(exp->exp).orElse(new Literal(1.0d)), new Literal(-1));
			if (newExponent.isConstant()) {
				double newExpVal = newExponent.evaluate(Formula::emptyVariableBinder);
				if (newExpVal==0.0) {
					newExponent = null;
				} else {
					newExponent = new Literal(newExpVal);
				}
			}
			if (newExponent==null) {
				return newCoefficient;
			}
			return new Term(Optional.of(newCoefficient), variable, Optional.of(newExponent));
		} else {
			return null;
		}
	}
	@Override
	public boolean isInvariant(VariableRef withRespectTo) {
		return coefficient.map(co->co.isInvariant(withRespectTo)).orElse(true) &&
				variable.map(var->isInvariant(withRespectTo)).orElse(true) &&
				exponent.map(exp->exp.isInvariant(withRespectTo)).orElse(true);
	}
	@Override
	public boolean isConstant() {
		return !variable.isPresent();
	}
	@Override
	public Formula algebraicMultiply(VariableRef variableRef) {
		Formula newCoefficient = coefficient.map(var -> var.algebraicMultiply(variableRef)).orElse(variableRef);
		return new Term(Optional.of(newCoefficient), variable, exponent);
	}
	@Override
	public void bindVariablesAsConstants(char series, Function<VariableRef, Double> variableBinder) {
		coefficient.ifPresent(co->co.bindVariablesAsConstants(series, variableBinder));
		variable.ifPresent(var->var.bindVariablesAsConstants(series, variableBinder));
		exponent.ifPresent(exp->exp.bindVariablesAsConstants(series, variableBinder));
	}
}
