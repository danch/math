package danch.math.formula;

import java.util.Optional;
import java.util.function.BiFunction;

public class VariableRef extends Formula {
	char seriesSymbol;
	int seriesIndex;
	Optional<Double> boundValue = Optional.empty();
	
	public VariableRef(char symbol, int index) {
		this.seriesSymbol = symbol;
		this.seriesIndex = index;
	}
	
	public VariableRef(char symbol) {
		this.seriesSymbol = symbol;
		this.seriesIndex = 0;
	}

	public char getSeriesSymbol() {
		return seriesSymbol;
	}

	public void setSeriesSymbol(char seriesSymbol) {
		this.seriesSymbol = seriesSymbol;
	}

	public int getSeriesIndex() {
		return seriesIndex;
	}

	public void setSeriesIndex(int seriesIndex) {
		this.seriesIndex = seriesIndex;
	}

	@Override
	public double evaluate(BiFunction<Character, Integer, Double> variableBinder) {
		Optional<Double> mapResult = boundValue.map(val -> val);
		return mapResult.orElseGet(() -> variableBinder.apply(seriesSymbol, seriesIndex));
	}
	
	@Override
	public String toString() {
		return boundValue.map(val->Literal.format.format(val)).orElse(""+seriesSymbol+seriesIndex);
	}
	
	@Override
	public boolean equals(Object other) {
		//note: this is an OO equals, not a mathematic equals operator
		if (!(other instanceof VariableRef)) {
			return false;
		}
		VariableRef rhs = (VariableRef)other;
		return seriesSymbol == rhs.seriesSymbol && seriesIndex == rhs.seriesIndex;
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
	public Formula algebraicMultiply(VariableRef variableRef) {
		return new Product(variableRef, this);
	}

	@Override
	public void bindVariablesAsConstants(char series, BiFunction<Character, Integer, Double> variableBinder) {
		if (series == this.seriesSymbol) {
			double value = variableBinder.apply(seriesSymbol, seriesIndex);
			boundValue=Optional.of(value);
		}
	}
}
