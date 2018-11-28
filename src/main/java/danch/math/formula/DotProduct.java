package danch.math.formula;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class DotProduct extends Formula {
    public static final char DOT_PRODUCT_SYMBOL = '\u2219';

    private VariableRef leftRange;
    private VariableRef rightRange;

    private Optional<double[]> boundLeftValue = Optional.empty();
    private Optional<double[]> boundRightValue = Optional.empty();

    public DotProduct(VariableRef leftRange, VariableRef rightRange) {
        this.leftRange = leftRange;
        this.rightRange = rightRange;
    }
    @Override
    public double evaluate(VariableBinder variableBinder) {
        double[] leftVector = boundLeftValue.orElse(variableBinder.getVectorValue(leftRange));
        double[] rightVector = boundRightValue.orElse(variableBinder.getVectorValue(rightRange));

        if (leftVector.length != rightVector.length) {
            throw new IllegalArgumentException("Vectors represented by "+leftVector+" and "+rightVector+
                    " are of different dimensions");
        }

        double accumulator = 0.0;
        for (int i=0;i<leftVector.length;i++) {
            accumulator += leftVector[i] * rightVector[i];
        }

        return accumulator;
    }

    @Override
    public Formula differentiate(VariableRef withRespectTo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isInvariant(VariableRef withRespectTo) {
        return leftRange.isInvariant(withRespectTo) && rightRange.isInvariant(withRespectTo);
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public Collection<Formula> postOrderTraversal() {
        List<Formula> collection = new ArrayList<>();
        collection.addAll(leftRange.postOrderTraversal());
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
        if (leftRange.getSeriesSymbol()==series) {
            boundLeftValue = Optional.of(variableBinder.getVectorValue(leftRange));
        }
        if (rightRange.getSeriesSymbol()==series) {
            boundRightValue = Optional.of(variableBinder.getVectorValue(rightRange));
        }
    }
    @Override
    public String toString() {
        return "("+leftRange.toString()+DOT_PRODUCT_SYMBOL+rightRange.toString()+")";
    }
}
