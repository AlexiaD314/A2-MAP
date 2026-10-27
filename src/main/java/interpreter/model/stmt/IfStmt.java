package interpreter.model.stmt;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.exp.IExp;
import interpreter.model.type.BoolType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public class IfStmt implements IStmt {
    private IExp cond;
    private IStmt thenS;
    private IStmt elseS;
    public IfStmt(IExp cond, IStmt thenS, IStmt elseS) {
        this.cond = cond;
        this.thenS = thenS;
        this.elseS = elseS;
    }
    @Override
    public PrgState execute(PrgState state) throws MyException {
        IDictionary<String, IValue> symTable = state.getSymTable();
        IValue val = cond.eval(symTable);
        if (!val.getType().equals(new BoolType())) {
            throw new MyException("Conditional expression is not a boolean.");
        }
        BoolValue boolVal = (BoolValue) val;
        if (boolVal.getVal()) {
            state.getExeStack().push(thenS);
        } else {
            state.getExeStack().push(elseS);
        }
        return null;
    }
    @Override
    public String toString() {
        return "(if " + cond.toString() + " then " + thenS.toString() + " else " + elseS.toString() + ")";
    }
}