package danch.math.formula;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;

public class SquareRoot extends Formula {
    private Formula value;

    public SquareRoot(Formula value) {
        this.value = value;
    }

    @Override
    public double evaluate(VariableBinder variableBinder) {
        double val = value.evaluate(variableBinder);
        if (val<0) {
            throw new IllegalArgumentException("Value < 0 (child formula: "+value.toString()+")");
        }
        return Math.sqrt(val);
    }

    @Override
    public Formula differentiate(VariableRef withRespectTo) {
        throw new NotImplementedException();
    }

    @Override
    public boolean isInvariant(VariableRef withRespectTo) {
        return value.isInvariant(withRespectTo);
    }

    @Override
    public boolean isConstant() {
        return value.isConstant();
    }

    @Override
    public Collection<Formula> postOrderTraversal() {
        ArrayList<Formula> list = new ArrayList<>();
        list.addAll(value.postOrderTraversal());
        list.add(this);
        return list;
    }

    @Override
    public Formula algebraicMultiply(VariableRef variableRef) {
        return new Product(variableRef, this);
    }

    @Override
    public void bindVariablesAsConstants(char series, VariableBinder variableBinder) {
        value.bindVariablesAsConstants(series, variableBinder);
    }

    @Override
    public String toString() {
        String buff = "Sqrt(" +
                value.toString() +
                ')';
        return buff;
    }
}
