package interpreter.model.value;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.type.StringType;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.stmt.NopStmt;
import interpreter.model.PrgState;
import interpreter.model.adt.*;

class StringValueTest {

    @Test
    void stringVariableDefaultValue() throws Exception {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new NopStmt());
        new VarDeclStmt("str", new StringType()).execute(state);
        assertEquals(new StringValue(""), state.getSymTable().lookup("str"));
    }

    @Test
    void stringValueEqualsAndHashCode() {
        StringValue s1 = new StringValue("hello");
        StringValue s2 = new StringValue("hello");
        StringValue s3 = new StringValue("world");

        assertEquals(s1, s2);
        assertNotEquals(s1, s3);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void boolAndRefValuesAndTypes() {
        BoolValue b1 = new BoolValue(true);
        BoolValue b2 = new BoolValue(true);
        BoolValue b3 = new BoolValue(false);
        assertEquals(new BoolType(), b1.getType());
        assertEquals(b1, b2);
        assertNotEquals(b1, b3);
        assertEquals(b1.hashCode(), b2.hashCode());
        assertEquals("bool", new BoolType().toString());

        RefType rt = new RefType(new IntType());
        RefValue r1 = new RefValue(1, new IntType());
        RefValue r2 = new RefValue(1, new IntType());
        RefValue r3 = new RefValue(2, new IntType());
        assertEquals(rt, r1.getType());
        assertEquals(r1, r2);
        assertNotEquals(r1, r3);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertEquals("Ref(int)", rt.toString());
        assertEquals(new RefValue(0, new IntType()), rt.defaultValue());
    }
}