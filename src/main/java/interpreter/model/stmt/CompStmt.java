package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.IStack;

public class CompStmt implements IStmt{
    private IStmt first;
    private IStmt snd;
    public CompStmt(IStmt first, IStmt snd) {
        this.first = first;
        this.snd = snd;
    }
    @Override
    public PrgState execute(PrgState prgState) throws MyException{
        IStack<IStmt> stk = prgState.getExeStack();
        stk.push(snd);
        stk.push(first);
        return null;
    }
    @Override
    public String toString() {
        return "(" + first.toString() + ";" + snd.toString() + ")";
    }
}