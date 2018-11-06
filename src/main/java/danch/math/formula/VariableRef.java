package danch.math.formula;

import java.util.Optional;
import java.util.function.BiFunction;

public class VariableRef extends Formula {
	private char seriesSymbol;
	private int[] indices;
	private Optional<Double> boundValue = Optional.empty();
	
	public VariableRef(char symbol, int index) {
		this.seriesSymbol = symbol;
		this.indices = new int[] { index };
	}
	
	public VariableRef(char symbol) {
		this.seriesSymbol = symbol;
		this.indices = new int[] {0};
	}

	public char getSeriesSymbol() {
		return seriesSymbol;
	}

	public void setSeriesSymbol(char seriesSymbol) {
		this.seriesSymbol = seriesSymbol;
	}

	public int getSeriesIndex() {
		return indices[0];
	}

	public void setSeriesIndex(int seriesIndex) {
		this.indices[0] = seriesIndex;
	}

	@Override
	public double evaluate(BiFunction<Character, int[], Double> variableBinder) {
		return boundValue.orElseGet(() -> variableBinder.apply(seriesSymbol, indices));
	}

	private String seriesToString() {
	    StringBuffer buffer = new StringBuffer();
	    for (int i: indices) {
	        if (buffer.length()>0)
	            buffer.append(' ');
	        buffer.append(i);
        }
        return buffer.toString();
    }
	@Override
	public String toString() {
		return boundValue.map(val->Literal.format.format(val)).orElse(""+seriesSymbol+seriesToString());
	}
	
	@Override
	public boolean equals(Object other) {
		//note: this is an OO equals, not a mathematic equals operator
		if (!(other instanceof VariableRef)) {
			return false;
		}

		VariableRef rhs = (VariableRef)other;

		if (indices.length != rhs.indices.length)
			return false;

		if (seriesSymbol != rhs.seriesSymbol)
		    return false;

		for (int i=0;i<indices.length;i++) {
		    if (indices[i]!=rhs.indices[i]) {
		        return false;
            }
        }
        return true;
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
	public void bindVariablesAsConstants(char series, BiFunction<Character, int[], Double> variableBinder) {
		if (series == this.seriesSymbol) {
			double value = variableBinder.apply(seriesSymbol, indices);
			boundValue=Optional.of(value);
		}
	}
}
