package math;

import static org.junit.Assert.*;
import danch.math.formula.DotProduct;
import danch.math.formula.VariableRef;
import org.junit.Test;

public class TestDotProduct {

    @Test
    public void test_Evaluate() {
        DotProduct sut = new DotProduct(new VariableRef('a'), new VariableRef('b'));
        double result = sut.evaluate(new ConstantVectorVariableBinder(new double[]{1.0, 2.0, 3.0}));
        assertEquals(14.0, result, 0.00001);
    }
}
