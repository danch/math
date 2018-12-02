package math;

import static org.junit.Assert.*;
import danch.math.formula.SumRange;
import danch.math.formula.VariableRef;
import org.junit.Test;

public class TestSumRange {
    @Test
    public void test_Evaluate() {
        SumRange sut = new SumRange(new VariableRef('a'));
        double sum = sut.evaluate(new ConstantVectorVariableBinder(new double[]{5.0, 10.0, 15.0}));
        assertEquals(30.0, sum, 0.0001);
    }
}
