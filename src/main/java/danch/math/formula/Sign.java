package danch.math.formula;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;

public class Sign extends Formula {
    private Formula value;

    public Sign(Formula value) {
        this.value = value;
    }

    @Override
    public double evaluate(VariableBinder variableBinder) {
        double val = value.evaluate(variableBinder);
        if (val < 0.0) {
            return -1;
        } else {
            return 1;
        }
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
        String buff = "Sign(" +
                value.toString() +
                ')';
        return buff;
    }
}
