package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

public record NewStmt(String varName, IExp exp) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        IDictionary<String, IValue> symTable = state.getSymTable();
        IHeap heap = state.getHeap();

        if (!symTable.isDefined(varName)) {
            throw new StatementException("new: variable " + varName + " is not declared.");
        }
        IValue varVal = symTable.lookup(varName);
        if (!(varVal instanceof RefValue refVal)) {
            throw new StatementException("new: variable " + varName + " is not of RefType.");
        }

        IValue evalVal = exp.eval(symTable, heap);
        if (!evalVal.getType().equals(refVal.locationType())) {
            throw new StatementException("new: expression type " + evalVal.getType()
                    + " does not match location type " + refVal.locationType() + ".");
        }

        int newAddr = heap.allocate(evalVal);
        symTable.update(varName, new RefValue(newAddr, refVal.locationType()));
        return state;
    }

    @Override
    public String toString() {
        return "new(" + varName + ", " + exp + ")";
    }
}