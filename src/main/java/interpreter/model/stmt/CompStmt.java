package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.IStack;

public record CompStmt(IStmt first, IStmt snd) implements IStmt{

    @Override
    public PrgState execute(PrgState prgState) throws MyException{
        IStack<IStmt> stk = prgState.getExeStack();
        stk.push(snd);
        stk.push(first);
        return prgState;
    }
    @Override
    public String toString() {
        return "(" + first.toString() + ";" + snd.toString() + ")";
    }
}