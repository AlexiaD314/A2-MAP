package interpreter.model.exp;

import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.type.BoolType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public record LogicExp(LogicOp op, IExp left, IExp right) implements IExp {
    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap heap) throws MyException {
        IValue v1 = left.eval(symTable, heap);
        if (!v1.getType().equals(new BoolType())) {
            throw new MyException("First operand is not a boolean.");
        }
        IValue v2 = right.eval(symTable, heap);
        if (!v2.getType().equals(new BoolType())) {
            throw new MyException("Second operand is not a boolean.");
        }
        boolean b1 = ((BoolValue) v1).val();
        boolean b2 = ((BoolValue) v2).val();

        switch (op) {
            case AND: return new BoolValue(b1 && b2);
            case OR: return new BoolValue(b1 || b2);
            default: throw new MyException("Unknown logic operator");
        }
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + op.toString() + " " + right.toString() + ")";
    }
}