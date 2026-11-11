package interpreter.model.stmt;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.FileWriter;
import java.io.PrintWriter;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import interpreter.model.value.ValueExp;

class FileStmtTest {

    @Test
    void readGivesTheNextNumberThenZeroAtTheEnd() throws Exception {
        try (PrintWriter writer = new PrintWriter(new FileWriter("test.in"))) {
            writer.println("7");
        }
        ValueExp fileExp = new ValueExp(new StringValue("test.in"));
        PrgState state = PrgState.of(new NopStmt());
        new VarDeclStmt("x", new IntType()).execute(state);

        new OpenRFileStmt(fileExp).execute(state);
        new ReadFileStmt(fileExp, "x").execute(state);
        assertEquals(new IntValue(7), state.getSymTable().lookup("x"));

        new ReadFileStmt(fileExp, "x").execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));

        new CloseRFileStmt(fileExp).execute(state);
    }

    @Test
    void openingTheSameFileTwiceThrows() throws Exception {
        try (PrintWriter writer = new PrintWriter(new FileWriter("test.in"))) {
            writer.println("7");
        }
        ValueExp fileExp = new ValueExp(new StringValue("test.in"));
        PrgState state = PrgState.of(new NopStmt());
        new VarDeclStmt("x", new IntType()).execute(state);

        new OpenRFileStmt(fileExp).execute(state);
        assertThrows(StatementException.class, () -> new OpenRFileStmt(fileExp).execute(state));

        new CloseRFileStmt(fileExp).execute(state);
    }

    @Test
    void closingUnopenedFileThrows() {
        ValueExp fileExp = new ValueExp(new StringValue("test.in"));
        assertThrows(StatementException.class, () -> new CloseRFileStmt(fileExp).execute(state));
    }
}