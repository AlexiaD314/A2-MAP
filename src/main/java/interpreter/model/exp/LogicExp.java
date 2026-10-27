package interpreter.model.exp;

import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.type.BoolType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public class LogicExp implements IExp {
    private IExp left;
    private IExp right;
    private LogicOp op;

    public LogicExp(LogicOp op, IExp left, IExp right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public IValue eval(IDictionary<String, IValue> symTable) throws MyException {
        IValue v1 = left.eval(symTable);
        if (!v1.getType().equals(new BoolType())) {
            throw new MyException("First operand is not a boolean.");
        }
        IValue v2 = right.eval(symTable);
        if (!v2.getType().equals(new BoolType())) {
            throw new MyException("Second operand is not a boolean.");
        }
        boolean b1 = ((BoolValue) v1).getVal();
        boolean b2 = ((BoolValue) v2).getVal();

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