package interpreter.model.value;
import interpreter.model.type.BoolType;
import interpreter.model.type.IType;

public class BoolValue implements IValue {
    private final boolean val;
    public BoolValue(boolean val) {
        this.val = val;
    }
    public boolean getVal() {
        return val;
    }
    @Override
    public IType getType() {
        return new BoolType();
    }
    @Override
    public String toString() {
        return Boolean.toString(val);
    }
    @Override
    public boolean equals(Object another) {
        if (another instanceof BoolValue) {
            return this.val == ((BoolValue) another).getVal();
        }
        return false;
    }
}