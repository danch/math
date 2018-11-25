package danch.math.formula;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class SumRange extends Formula {
    VariableRef rangeVar;
    Optional<Double> boundValue = Optional.empty();

    public SumRange(VariableRef rangeVar) {
        this.rangeVar = rangeVar;
    }
    @Override
    public double evaluate(VariableBinder variableBinder) {
        return boundValue.orElseGet(() -> {
            double[] values = variableBinder.getVectorValue(rangeVar);
            double output = 0.0;
            for (double v : values) {
                output += v;
            }
            return output;
        });
    }

    @Override
    public Formula differentiate(VariableRef withRespectTo) {
        throw new NotImplementedException();
    }

    @Override
    public boolean isInvariant(VariableRef withRespectTo) {
        return false;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public Collection<Formula> postOrderTraversal() {
        List<Formula> collection = new ArrayList<>();
        collection.add(rangeVar);
        collection.add(this);
        return collection;
    }

    @Override
    public Formula algebraicMultiply(VariableRef variableRef) {
        return new Product(variableRef, this);
    }

    @Override
    public void bindVariablesAsConstants(char series, VariableBinder variableBinder) {
        if (rangeVar.getSeriesSymbol() == series) {
            double value = evaluate(variableBinder);
            this.boundValue = Optional.of(value);
        }
    }
}
