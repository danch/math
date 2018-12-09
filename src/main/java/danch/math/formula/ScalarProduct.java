package danch.math.formula;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ScalarProduct extends Formula {
    public static final char DOT_PRODUCT_SYMBOL = '\u2219';

    private VariableRef scalar;
    private VariableRef rightRange;

    private Optional<Double> boundScalarValue = Optional.empty();
    private Optional<double[]> boundRightValue = Optional.empty();

    public ScalarProduct(VariableRef scalar, VariableRef rightRange) {
        if (scalar instanceof TaggedVariableRef && hasNull(((TaggedVariableRef)scalar).getTags())) {
            throw new IllegalArgumentException("Variable passed as scalar is a vector reference");
        }
        this.scalar = scalar;
        this.rightRange = rightRange;
    }

    private boolean hasNull(String[] tags) {
        for (String tag : tags) {
            if (tag == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double evaluate(VariableBinder variableBinder) {
        double scalarValue = boundScalarValue.orElse(variableBinder.getValue(scalar));
        double[] rightVector = boundRightValue.orElse(variableBinder.getVectorValue(rightRange));

        double accumulator = 0.0;
        for (int i=0;i<rightVector.length;i++) {
            accumulator += scalarValue * rightVector[i];
        }

        return accumulator;
    }

    @Override
    public Formula differentiate(VariableRef withRespectTo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isInvariant(VariableRef withRespectTo) {
        return scalar.isInvariant(withRespectTo) && rightRange.isInvariant(withRespectTo);
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public Collection<Formula> postOrderTraversal() {
        List<Formula> collection = new ArrayList<>();
        collection.addAll(scalar.postOrderTraversal());
        collection.addAll(rightRange.postOrderTraversal());
        collection.add(this);
        return collection;
    }

    @Override
    public Formula algebraicMultiply(VariableRef variableRef) {
        return new Product(variableRef, this);
    }

    @Override
    public void bindVariablesAsConstants(char series, VariableBinder variableBinder) {
        if (scalar.getSeriesSymbol()==series) {
            boundScalarValue = Optional.of(variableBinder.getValue(scalar));
        }
        if (rightRange.getSeriesSymbol()==series) {
            boundRightValue = Optional.of(variableBinder.getVectorValue(rightRange));
        }
    }
    @Override
    public String toString() {
        return "("+scalar.toString()+DOT_PRODUCT_SYMBOL+rightRange.toString()+")";
    }
}
