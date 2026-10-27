package interpreter.model.stmt;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;

public class NopStmt implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        return null;
    }
    @Override
    public String toString() {
        return "nop";
    }
}