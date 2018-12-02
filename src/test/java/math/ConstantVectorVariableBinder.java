package math;

import danch.math.formula.VariableBinder;
import danch.math.formula.VariableRef;

class ConstantVectorVariableBinder implements VariableBinder {
    private double[] vector;

    public ConstantVectorVariableBinder(double[] value) {
        this.vector = value;
    }

    @Override
    public double getValue(VariableRef forVar) {
        return 0;
    }

    @Override
    public double[] getVectorValue(VariableRef forVar) {
        return vector;
    }
}
