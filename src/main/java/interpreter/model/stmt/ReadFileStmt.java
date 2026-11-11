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
        if (symTable.isDefined(varName)) {
            if (symTable.lookup(varName).getType().equals(new IntType())) {
                IValue val = exp.eval(symTable);
                if (val.getType().equals(new StringType())) {
                    StringValue strVal = (StringValue) val;
                    var fileTable = state.getFileTable();

                    if (fileTable.isDefined(strVal)) {
                        BufferedReader br = fileTable.lookup(strVal);
                        try {
                            String line = br.readLine();
                            int intVal = (line == null) ? 0 : Integer.parseInt(line);
                            symTable.put(varName, new IntValue(intVal));
                        } catch (IOException | NumberFormatException e) {
                            throw new StatementException("Error reading from file: " + e.getMessage());
                        }
                    } else {
                        throw new StatementException("File not opened.");
                    }
                } else {
                    throw new StatementException("File name expression not a string.");
                }
            } else {
                throw new StatementException("Variable " + varName + " not of int type.");
            }
        } else {
            throw new StatementException("Variable " + varName + " not defined in symTable.");
        }
        return null;
    }

    @Override
    public String toString() {
        return "readFile(" + exp + ", " + varName + ")";
    }
}