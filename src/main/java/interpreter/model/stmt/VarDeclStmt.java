package interpreter.model.stmt;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.type.IType;
import interpreter.model.value.IValue;
import interpreter.model.adt.IDictionary;

public class VarDeclStmt implements IStmt{
    private String name;
    private IType type;
    public VarDeclStmt(String name, IType type) {
        this.name = name;
        this.type = type;
    }
    @Override
    public PrgState execute(PrgState state) throws MyException {
        IDictionary<String, IValue> symTable = state.getSymTable();
        if(symTable.isDefined(name)){
            throw new MyException("Variable " + name + " already exists.");
        }
        symTable.update(name, type.defaultValue());
        return null;
    }
    @Override
    public String toString(){
        return type.toString() + " " + name;
    }
}
