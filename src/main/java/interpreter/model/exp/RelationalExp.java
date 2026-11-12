package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.IValue;

public class RelationalExp implements IExp {
    private final RelOp operator;
    private final IExp e1;
    private final IExp e2;

    public RelationalExp(RelOp operator, IExp e1, IExp e2) {
        this.operator = operator;
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public IValue eval(IDictionary<String, IValue> symTable) throws MyException {
        IValue val1 = e1.eval(symTable);
        if (val1.getType().equals(new IntType())) {
            IValue val2 = e2.eval(symTable);
            if (val2.getType().equals(new IntType())) {
                int n1 = ((IntValue) val1).val();
                int n2 = ((IntValue) val2).val();

                switch (operator) {
                    case LT: return new BoolValue(n1 < n2);
                    case LE: return new BoolValue(n1 <= n2);
                    case EQ: return new BoolValue(n1 == n2);
                    case NE: return new BoolValue(n1 != n2);
                    case GT: return new BoolValue(n1 > n2);
                    case GE: return new BoolValue(n1 >= n2);
                    default: throw new ExpressionException("Unknown relational operator.");
                }
            } else {
                throw new ExpressionException("Second operand is not an integer.");
            }
        } else {
            throw new ExpressionException("First operand is not an integer.");
        }
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
        return "(" + e1.toString() + " " + opStr + " " + e2.toString() + ")";
    }
}