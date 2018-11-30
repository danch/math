package danch.math.formula;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;

public class Max extends Formula {
    private Formula left;
    private Formula right;

    public Max(Formula left, Formula right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public double evaluate(VariableBinder variableBinder) {
        return Math.max(left.evaluate(variableBinder), right.evaluate(variableBinder));
    }

    @Override
    public Formula differentiate(VariableRef withRespectTo) {
        throw new NotImplementedException();
    }

    @Override
    public boolean isInvariant(VariableRef withRespectTo) {
        return left.isInvariant(withRespectTo) && right.isInvariant(withRespectTo);
    }

    @Override
    public boolean isConstant() {
        return left.isConstant() && right.isConstant();
    }

    @Override
    public Collection<Formula> postOrderTraversal() {
        ArrayList<Formula> list = new ArrayList<>();
        list.addAll(left.postOrderTraversal());
        list.addAll(right.postOrderTraversal());
        list.add(this);
        return list;
    }

    @Override
    public Formula algebraicMultiply(VariableRef variableRef) {
        return new Product(variableRef, this);
    }

    @Override
    public void bindVariablesAsConstants(char series, VariableBinder variableBinder) {
        left.bindVariablesAsConstants(series, variableBinder);
        right.bindVariablesAsConstants(series, variableBinder);
    }

    @Override
    public String toString() {
        String buff = "Max(" +
                left.toString() +
                ',' +
                right.toString() +
                ')';
        return buff;
    }
}
