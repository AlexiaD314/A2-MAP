package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.IValue;

public record RelationalExp(RelOp operator, IExp e1, IExp e2) implements IExp {
    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap heap) throws MyException {
        if (!(e1.eval(symTable, heap) instanceof IntValue(int n1)))
            throw new ExpressionException("First operand is not an integer.");
        if (!(e2.eval(symTable, heap) instanceof IntValue(int n2)))
            throw new ExpressionException("Second operand is not an integer.");
        return new BoolValue(switch (operator) {
            case LT -> n1 < n2;
            case LE -> n1 <= n2;
            case EQ -> n1 == n2;
            case NE -> n1 != n2;
            case GT -> n1 > n2;
            case GE -> n1 >= n2;
        });
    }

    @Override
    public String toString() {
        String opStr = switch (operator) {
            case LT -> "<";
            case LE -> "<=";
            case EQ -> "==";
            case NE -> "!=";
            case GT -> ">";
            case GE -> ">=";
        };
        return "(" + e1 + " " + opStr + " " + e2 + ")";
    }
}