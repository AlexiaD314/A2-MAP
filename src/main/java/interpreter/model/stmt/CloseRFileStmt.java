package interpreter.model.stmt;

import java.io.BufferedReader;
import java.io.IOException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.type.StringType;
import interpreter.model.value.StringValue;
import interpreter.model.value.IValue;

public class CloseRFileStmt implements IStmt {
    private final IExp exp;

    public CloseRFileStmt(IExp exp) {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue val = exp.eval(state.getSymTable());
        if (val.getType().equals(new StringType())) {
            StringValue strVal = (StringValue) val;
            var fileTable = state.getFileTable();

            if (fileTable.isDefined(strVal)) {
                BufferedReader br = fileTable.lookup(strVal);
                try {
                    br.close();
                } catch (IOException e) {
                    throw new StatementException("Error closing file: " + e.getMessage());
                }
                fileTable.remove(strVal);
            } else {
                throw new StatementException("File not opened.");
            }
        } else {
            throw new StatementException("Expression not a string type.");
        }
        return null;
    }

    @Override
    public String toString() {
        return "closeRFile(" + exp + ")";
    }
}