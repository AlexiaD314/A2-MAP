package interpreter.model.exp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import interpreter.model.value.IValue;

class RelationalExpTest {

    private static IExp num(int n) {
        return new ValueExp(new IntValue(n));
    }

    @Test
    void testOperators() throws MyException {
        IDictionary<String, IValue> symTable = new MyDictionary<>();

        // <
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.LT, num(3), num(5)).eval(symTable));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.LT, num(5), num(3)).eval(symTable));

        // <=
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.LE, num(5), num(5)).eval(symTable));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.LE, num(6), num(5)).eval(symTable));

        // ==
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.EQ, num(4), num(4)).eval(symTable));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.EQ, num(4), num(5)).eval(symTable));

        // !=
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.NE, num(4), num(5)).eval(symTable));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.NE, num(4), num(4)).eval(symTable));

        // >
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.GT, num(7), num(2)).eval(symTable));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.GT, num(2), num(7)).eval(symTable));

        // >=
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.GE, num(5), num(5)).eval(symTable));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.GE, num(2), num(5)).eval(symTable));
    }

    @Test
    void testNonIntegerOperandThrows() {
        IDictionary<String, IValue> symTable = new MyDictionary<>();

        IExp invalidExp = new RelationalExp(RelOp.LT, new ValueExp(new StringValue("abc")), num(5));
        assertThrows(ExpressionException.class, () -> invalidExp.eval(symTable));

        IExp invalidExp2 = new RelationalExp(RelOp.GT, num(5), new ValueExp(new StringValue("abc")));
        assertThrows(ExpressionException.class, () -> invalidExp2.eval(symTable));
    }

    @Test
    void testToString() {
        IExp exp = new RelationalExp(RelOp.LT, num(2), num(3));
        assertEquals("(2 < 3)", exp.toString());
    }
}