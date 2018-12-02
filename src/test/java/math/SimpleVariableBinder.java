package math;

import danch.math.formula.VariableBinder;
import danch.math.formula.VariableRef;

class SimpleVariableBinder implements VariableBinder {
    double value;

    public SimpleVariableBinder(double value) {
        this.value = value;
    }

    @Override
    public double getValue(VariableRef forVar) {
    return value;
}

    @Override
    public double[] getVectorValue(VariableRef forVar) {
return new double[]{value};
}
}
