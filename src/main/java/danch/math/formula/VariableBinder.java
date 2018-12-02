package danch.math.formula;

public interface VariableBinder {
    double getValue(VariableRef forVar);
    double[] getVectorValue(VariableRef forVar);
}
