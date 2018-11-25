package danch.math.formula;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Product extends Formula {
	ArrayList<Formula> components;

	public Product(Formula left, Formula right) {
		if (left == null || right == null) {
			throw new IllegalArgumentException("components of a product must not be null");
		}
		components = new ArrayList<>();
		components.add(left);
		components.add(right);
	}
	public Product(Collection<Formula> components) {
		for (Formula comp : components) {
			if (comp == null) {
				throw new IllegalArgumentException("components of a product must not be null");
			}
		}
		this.components = new ArrayList<>(components);
	}
	@Override
	public double evaluate(VariableBinder variableBinder) {
		return components.stream().map(formula -> formula.evaluate(variableBinder)).reduce((left, right) -> left * right).get();
	}

	@Override
	public Formula differentiate(VariableRef withRespectTo) {
		long variantTerms = components.stream().filter(form -> !form.isInvariant(withRespectTo)).count();
		if (variantTerms == 0) {
			return new Literal(0.0);
		}
		if (variantTerms == 1) {
			//the invariant terms are effectively costants here, so they'll be multiplied by the differential of the variant term
			ArrayList<Formula> invariants = components.stream().filter(form -> form.isInvariant(withRespectTo)).collect(Collectors.toCollection(ArrayList::new));
			Optional<Formula> optVariant = components.stream().filter(form -> !form.isInvariant(withRespectTo)).findFirst();
			Formula variantTerm = optVariant.get();
			if (variantTerm instanceof VariableRef) {
				return new Product(invariants);//the diff. of the variable is now '1', so we'er down to du/dx = C
			}
			Formula dudx = variantTerm.differentiate(withRespectTo);
			invariants.add(dudx);
			return new Product(invariants);
		}
		//more than one variant term, we use the product rule
		ArrayList<Formula> productTerms = new ArrayList<>();
		ArrayList<Formula> variants = components.stream().filter(form -> !form.isInvariant(withRespectTo)).collect(Collectors.toCollection(ArrayList::new));
		for (Formula factor : variants) {
			ArrayList<Formula> factorsInTerm = new ArrayList<>();
			Formula dfdx = factor.differentiate(withRespectTo);
			factorsInTerm.add(dfdx);
			for (Formula rawFactor : variants) {
				if (rawFactor != factor) {
					factorsInTerm.add(rawFactor.cloneValue());
				}
			}
			ArrayList<Formula> constants = components.stream().filter(form -> form.isConstant()).collect(Collectors.toCollection(ArrayList::new));
			for (Formula constant : constants) {
				factorsInTerm.add(constant.cloneValue());
			}
			productTerms.add(new Product(factorsInTerm));
		}
		return new Sum(productTerms);
	}

	@Override
	public boolean isInvariant(VariableRef withRespectTo) {
		return components.stream().map(formula -> formula.isInvariant(withRespectTo)).reduce(true, (left, right) -> left && right);
	}

	@Override
	public boolean isConstant() {
		return components.stream().map(formula -> formula.isConstant()).reduce(true, (left, right) -> left && right);
	}

    @Override
    public Collection<Formula> postOrderTraversal() {
        Collection<Formula> children = components.stream().flatMap(f -> f.postOrderTraversal().stream()).
                collect(Collectors.toCollection(ArrayList::new));
        children.add(this);
        return children;
    }

    @Override
	public Formula algebraicMultiply(VariableRef variableRef) {
		ArrayList<Formula> newList = new ArrayList<>();
		newList.add(variableRef);
		newList.addAll(components);
		return new Product(newList);
	}

	@Override
	public void bindVariablesAsConstants(char series, VariableBinder variableBinder) {
		components.forEach(formula -> formula.bindVariablesAsConstants(series, variableBinder));
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		if (components.size()==2 && components.get(0) instanceof Literal && 
				(components.get(1) instanceof VariableRef || components.get(1) instanceof Power)) 
		{
			buffer.append(components.get(0).toString());
			buffer.append(components.get(1).toString());
		} else {
			components.forEach(formula -> {
				if (buffer.length()!=0) {
					buffer.append(" * ");
				}
				if (formula instanceof Sum) {
					buffer.append('(');
				}
				buffer.append(formula.toString());
				if (formula instanceof Sum) {
					buffer.append(')');
				}
			});
		}
		return buffer.toString();
	}

}
