package danch.math.formula;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class VariableRef extends Formula {
	protected char seriesSymbol;
	protected Optional<Double> boundValue = Optional.empty();
	
	public VariableRef(char symbol) {
		this.seriesSymbol = symbol;
	}

	public char getSeriesSymbol() {
		return seriesSymbol;
	}

	public void setSeriesSymbol(char seriesSymbol) {
		this.seriesSymbol = seriesSymbol;
	}

	@Override
	public double evaluate(Function<VariableRef, Double> variableBinder) {
		return boundValue.orElseGet(() -> variableBinder.apply(this));
	}


	@Override
	public Formula differentiate(VariableRef withRespectTo) {
		throw new IllegalStateException("VariableRef cannot be differentiated on its own");
	}

	@Override
	public boolean isInvariant(VariableRef withRespectTo) {
		return !this.equals(withRespectTo) || isConstant();
	}

	@Override
	public boolean isConstant() {
		//if I'm bound, I'm effectively constant
		return boundValue.isPresent();
	}

    @Override
    public Collection<Formula> postOrderTraversal() {
        return Arrays.asList(this);
    }

    @Override
	public Formula algebraicMultiply(VariableRef variableRef) {
		return new Product(variableRef, this);
	}

	@Override
	public void bindVariablesAsConstants(char series, Function<VariableRef, Double> variableBinder) {
		if (series == this.seriesSymbol) {
			double value = variableBinder.apply(this);
			boundValue=Optional.of(value);
		}
	}
}
