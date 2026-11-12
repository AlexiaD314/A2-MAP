package interpreter.model.stmt;

import java.io.BufferedReader;
import java.io.IOException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.type.IntType;
import interpreter.model.type.StringType;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import interpreter.model.value.IValue;

public class ReadFileStmt implements IStmt {
    private final IExp exp;
    private final String varName;

    public ReadFileStmt(IExp exp, String varName) {
        this.exp = exp;
        this.varName = varName;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        if (!symTable.isDefined(varName))
            throw new StatementException("readFile: variable " + varName + " is not defined.");
        if (!symTable.lookup(varName).getType().equals(new IntType()))
            throw new StatementException("readFile: variable " + varName + " is not of type int.");

        IValue val = exp.eval(symTable);
        if (!val.getType().equals(new StringType()))
            throw new StatementException("readFile: expression " + exp + " is not a string.");

        StringValue strVal = (StringValue) val;
        var fileTable = state.getFileTable();
        if (!fileTable.isDefined(strVal))
            throw new StatementException("readFile: file " + strVal + " is not opened in FileTable.");

        BufferedReader br = (BufferedReader) fileTable.lookup(strVal);
        try {
            String line = br.readLine();
            int intVal = (line == null) ? 0 : Integer.parseInt(line.strip());
            symTable.put(varName, new IntValue(intVal));
        } catch (IOException | NumberFormatException e) {
            throw new StatementException("readFile error: " + e.getMessage());
        }
        return state;
    }

    @Override
    public String toString() {
        return "readFile(" + exp + ", " + varName + ")";
    }
}