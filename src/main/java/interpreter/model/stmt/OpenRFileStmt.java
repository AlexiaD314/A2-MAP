package interpreter.model.stmt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.type.StringType;
import interpreter.model.value.StringValue;
import interpreter.model.value.IValue;

public class OpenRFileStmt implements IStmt {
    private final IExp exp;

    public OpenRFileStmt(IExp exp) {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue val = exp.eval(state.getSymTable());
        if (val.getType().equals(new StringType())) {
            StringValue strVal = (StringValue) val;
            var fileTable = state.getFileTable();

            if (!fileTable.isDefined(strVal)) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(strVal.val()));
                    fileTable.put(strVal, br);
                } catch (IOException e) {
                    throw new StatementException("Error opening file: " + e.getMessage());
                }
            } else {
                throw new StatementException("File is already opened.");
            }
        } else {
            throw new StatementException("Expression evaluated to non-string type.");
        }
        return null;
    }

    @Override
    public String toString() {
        return "openRFile(" + exp + ")";
    }
}