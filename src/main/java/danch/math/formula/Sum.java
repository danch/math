package danch.math.formula;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Sum extends Formula {
	private ArrayList<Formula> summands = new ArrayList<Formula>();

	public Sum() {
	}
	
	public Sum(Formula first, Formula second) {
		summands.add(first);
		summands.add(second);
	}
	
	public Sum(Collection<? extends Formula> summands) {
		this.summands = new ArrayList<>(summands);
	}
	
	public Sum(Formula[] terms) {
		for (Formula term : terms) {
			summands.add(term);
		}
	}
	@Override
	public double evaluate(Function<VariableRef, Double> variableBinder) {
		return summands.stream().map(formula -> formula.evaluate(variableBinder)).reduce((left, right) -> left + right).get();
	}

	@Override
	public Formula differentiate(VariableRef withRespectTo) {
		ArrayList<Formula> newList = summands.stream().map(formula -> {
				if (formula instanceof VariableRef) {
					if (formula.equals(withRespectTo))
						return new Literal(1.0);
					else
						return new Literal(0);
				} else {
					return formula.differentiate(withRespectTo);
				}
			}).filter(formula -> {
					return !(formula == null || (formula.isConstant() && formula.evaluate(Formula::emptyVariableBinder) == 0.0));
				}).
				collect(Collectors.toCollection(ArrayList::new));
		if (newList.isEmpty()) {
			return null;
		}
		if (newList.size()>1) {
			return new Sum(newList);
		} else {
			return newList.get(0);
		}
	}

	@Override
	public boolean isInvariant(VariableRef withRespectTo) {
		return summands.stream().map(formula -> {
			return formula.isInvariant(withRespectTo);
		}).reduce(true, (left, right) -> left && right);
	}

	@Override
	public boolean isConstant() {
		return summands.stream().map(formula -> formula.isConstant()).reduce(true, (left, right) -> left && right);
	}

	@Override
	public Formula algebraicMultiply(VariableRef variableRef) {
		List<Formula> multiplied = summands.stream().map(formula->formula.algebraicMultiply(variableRef)).
				collect(Collectors.toList());
		return new Sum(multiplied);
	}

	@Override
	public void bindVariablesAsConstants(char series, Function<VariableRef, Double> variableBinder) {
		summands.forEach(formula -> formula.bindVariablesAsConstants(series, variableBinder));
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		summands.forEach(formula -> {
			if (buffer.length()!=0) {
				buffer.append(" + ");
			}
			buffer.append(formula.toString());
		});
		return buffer.toString();
	}
}
