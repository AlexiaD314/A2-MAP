package interpreter.model.value;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import interpreter.model.type.StringType;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.stmt.NopStmt;
import interpreter.model.PrgState;
import interpreter.model.adt.*;

class StringValueTest {

    @Test
    void stringVariableDefaultValue() throws Exception {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new NopStmt());
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
}