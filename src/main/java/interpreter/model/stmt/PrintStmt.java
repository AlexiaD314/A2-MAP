package interpreter.model.stmt;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;

public class PrintStmt implements IStmt {
    private IExp exp;
    public PrintStmt(IExp exp) {
        this.exp = exp;
    }
    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue val = exp.eval(state.getSymTable());
        state.getOut().add(val);
        return null;
    }
    @Override
    public String toString() {
        return "print(" + exp.toString() + ")";
    }
}
