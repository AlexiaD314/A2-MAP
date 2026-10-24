package interpreter.model.stmt;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.type.IType;
import interpreter.model.value.IValue;
import interpreter.model.adt.IDictionary;

public class AssignStmt implements IStmt {
    private String id;
    private IExp exp;
    public AssignStmt(String id, IExp exp) {
        this.id = id;
        this.exp = exp;
    }
    @Override
    public PrgState execute(PrgState state) throws MyException {
        IDictionary<String, IValue> symTable = state.getSymTable();
        if(!symTable.isDefined(id)) {
            throw new MyException("AssignStmt " + id + " was not declared.");
        }
        IValue val = exp.eval(symTable);
        IType typID = symTable.lookup(id).getType();
        if(!val.getType().equals(typID)) {
            throw new MyException("Declared type of variable " + id + " and type of assigned expression do not match.");
        }
        symTable.update(id, val);
        return null;
    }
    @Override
    public String toString() {
        return id + "=" + exp.toString();
    }
}
