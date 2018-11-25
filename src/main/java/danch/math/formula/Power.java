package danch.math.formula;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;


public class Power extends Formula {
	Formula base;
	Formula exponent;
	
	public Power(Formula base, Formula exponent) {
		this.base = base;
		this.exponent = exponent;
	}
	
	@Override
	public String toString() {
		String baseString = base.toString();
		String expString;
		if (exponent.isConstant() && exponent.evaluate(Formula.emptyVariableBinder)==1.0) {
			expString =  "";
		} else {
			expString = "^"+exponent.toString();
		}
		return "("+baseString + ")" + expString;
	}
	@Override
	public double evaluate(VariableBinder variableBinder) {
		return Math.pow(base.evaluate(variableBinder), exponent.evaluate(variableBinder));
	}

	@Override
	public Formula differentiate(VariableRef withRespectTo) {
		if (base.isInvariant(withRespectTo) && exponent.isInvariant(withRespectTo)) {
			//if base and exponent are both invariant with respect to the free variable, the derivitive is 0
			return new Literal(0.0);
		} else
		if (!base.isInvariant(withRespectTo)) {
			//It's a nice normal power term
			Formula newCoefficient = exponent.cloneValue();
			Formula newExponent = new Sum(exponent, new Literal(-1));
			if (newExponent.isConstant()) {
				newExponent = new Literal(newExponent.evaluate(Formula.emptyVariableBinder));
			}
			if (newCoefficient.isConstant() && newCoefficient.evaluate(Formula.emptyVariableBinder)==1.0) {
				if (newExponent.isConstant() && newExponent.evaluate(Formula.emptyVariableBinder)==0.0) {
					return new Literal(1.0);
				} else {
					return new Power(base, newExponent);
				}
			} else {
				if (!(base instanceof VariableRef)) {
					//base is a function of the variable in consideration, so we need to use Chain rule
					Formula baseDerivative = base.differentiate(withRespectTo);
					ArrayList<Formula> product = new ArrayList<>();
					product.add(newCoefficient);
					if (newExponent.isConstant() && newExponent.evaluate(Formula.emptyVariableBinder)==1.0) {
						product.add(base);
					} else {
						product.add(new Power(base, newExponent));
					}
					if (baseDerivative != null) {
						product.add(baseDerivative);
					}
					return new Product(product);
				} else {
					if (newExponent.isConstant() && newExponent.evaluate(Formula.emptyVariableBinder)==0.0) {
						return newCoefficient;
					} else {
						if (newExponent.isConstant() && newExponent.evaluate(Formula.emptyVariableBinder)==1.0) {
							return new Product(newCoefficient, base);
						} else {
							return new Product(newCoefficient, new Power(base, newExponent));
						}
					}
				}
			}
		} else {
			throw new IllegalStateException("Unsupported attempt to differentiate a Power with invariant base and variant power");
		}
	}

    @Override
    public Collection<Formula> postOrderTraversal() {
        List<Formula> collection = new ArrayList<>();
        collection.addAll(base.postOrderTraversal());
        collection.addAll(exponent.postOrderTraversal());
        collection.add(this);
        return collection;
    }

	@Override
	public boolean isInvariant(VariableRef withRespectTo) {
		return base.isInvariant(withRespectTo) && exponent.isInvariant(withRespectTo);
	}

	@Override
	public boolean isConstant() {
		return base.isConstant() && exponent.isConstant();
	}

	@Override
	public Formula algebraicMultiply(VariableRef variableRef) {
		return new Product(variableRef, this);
	}

	@Override
	public void bindVariablesAsConstants(char series, VariableBinder variableBinder) {
		base.bindVariablesAsConstants(series, variableBinder);
		exponent.bindVariablesAsConstants(series, variableBinder);
	}

}
