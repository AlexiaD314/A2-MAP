package interpreter.model.stmt;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ReadHeapExp;
import interpreter.model.exp.RelOp;
import interpreter.model.exp.RelationalExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import interpreter.repository.Repository;

class FileStmtTest {
    @TempDir
    Path dir;

    private PrgState state;
    private ValueExp file;

    @BeforeEach
    void setUp() throws Exception {
        Path f = dir.resolve("numbers.in");
        Files.writeString(f, "7\n");
        file = new ValueExp(new StringValue(f.toString()));
        state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new NopStmt());
        new VarDeclStmt("x", new IntType()).execute(state);
    }

    @Test
    void openingNonExistentFileThrows() {
        ValueExp badFileExp = new ValueExp(new StringValue(dir.resolve("nonexistent.in").toString()));
        assertThrows(MyException.class, () -> new OpenRFileStmt(badFileExp).execute(state));
    }

    @Test
    void closingUnopenedFileThrows() {
        assertThrows(MyException.class, () -> new CloseRFileStmt(file).execute(state));
    }

    @Test
    void readingFromUnopenedFileThrows() {
        assertThrows(MyException.class, () -> new ReadFileStmt(file, "x").execute(state));
    }

    @Test
    void readingUndeclaredVariableThrows() throws MyException {
        new OpenRFileStmt(file).execute(state);
        assertThrows(MyException.class, () -> new ReadFileStmt(file, "y").execute(state));
        new CloseRFileStmt(file).execute(state);
    }

    @Test
    void readGivesTheNextNumberThenZeroAtTheEnd() throws MyException {
        new OpenRFileStmt(file).execute(state);
        new ReadFileStmt(file, "x").execute(state);
        assertEquals(new IntValue(7), state.getSymTable().lookup("x"));

        new ReadFileStmt(file, "x").execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));

        new CloseRFileStmt(file).execute(state);
    }

    @Test
    void openingTheSameFileTwiceThrows() throws MyException {
        new OpenRFileStmt(file).execute(state);
        assertThrows(MyException.class,
                () -> new OpenRFileStmt(file).execute(state));

        new CloseRFileStmt(file).execute(state);
    }

    @Test
    void testIfNopWhileAssignNewAndWriteHeap() throws Exception {
        new NopStmt().execute(state);
        assertEquals("nop", new NopStmt().toString());

        Controller ctrl = new Controller(new Repository(state, dir.resolve("stmt.log").toString()), false);

        IStmt progTrue = new CompStmt(new VarDeclStmt("k", new IntType()),
                new CompStmt(new AssignStmt("k", new ValueExp(new IntValue(1))),
                        new IfStmt(new RelationalExp(RelOp.LT, new VarExp("k"), new ValueExp(new IntValue(3))),
                                new AssignStmt("k", new ValueExp(new IntValue(10))),
                                new AssignStmt("k", new ValueExp(new IntValue(20))))));
        PrgState s1 = PrgState.of(progTrue);
        while (!s1.getExeStack().isEmpty()) {
            ctrl.oneStep(s1);
        }
        assertEquals(new IntValue(10), s1.getSymTable().lookup("k"));

        IStmt progFalse = new CompStmt(new VarDeclStmt("k", new IntType()),
                new CompStmt(new AssignStmt("k", new ValueExp(new IntValue(5))),
                        new IfStmt(new RelationalExp(RelOp.LT, new VarExp("k"), new ValueExp(new IntValue(3))),
                                new AssignStmt("k", new ValueExp(new IntValue(10))),
                                new AssignStmt("k", new ValueExp(new IntValue(20))))));
        PrgState s2 = PrgState.of(progFalse);
        while (!s2.getExeStack().isEmpty()) {
            ctrl.oneStep(s2);
        }
        assertEquals(new IntValue(20), s2.getSymTable().lookup("k"));

        PrgState badIf = PrgState.of(new IfStmt(new ValueExp(new IntValue(1)), new NopStmt(), new NopStmt()));
        assertThrows(MyException.class, () -> ctrl.oneStep(badIf));

        PrgState badWhile = PrgState.of(new WhileStmt(new ValueExp(new IntValue(1)), new NopStmt()));
        assertThrows(MyException.class, () -> ctrl.oneStep(badWhile));

        assertThrows(MyException.class, () -> new AssignStmt("nope", new ValueExp(new IntValue(1))).execute(state));
        assertThrows(MyException.class, () -> new VarDeclStmt("x", new IntType()).execute(state));
        assertThrows(MyException.class, () -> new AssignStmt("x", new ValueExp(new BoolValue(true))).execute(state));

        assertThrows(MyException.class, () -> new NewStmt("nope", new ValueExp(new IntValue(1))).execute(state));
        assertThrows(MyException.class, () -> new NewStmt("x", new ValueExp(new IntValue(1))).execute(state));

        new VarDeclStmt("v", new RefType(new IntType())).execute(state);
        assertThrows(MyException.class, () -> new NewStmt("v", new ValueExp(new BoolValue(true))).execute(state));
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);

        assertThrows(MyException.class, () -> new WriteHeapStmt("nope", new ValueExp(new IntValue(1))).execute(state));
        assertThrows(MyException.class, () -> new WriteHeapStmt("x", new ValueExp(new IntValue(1))).execute(state));
        assertThrows(MyException.class, () -> new WriteHeapStmt("v", new ValueExp(new BoolValue(true))).execute(state));
        new WriteHeapStmt("v", new ValueExp(new IntValue(30))).execute(state);
        assertEquals(new IntValue(30), new ReadHeapExp(new VarExp("v")).eval(state.getSymTable(), state.getHeap()));

        assertEquals(new BoolValue(false), new BoolType().defaultValue());
        assertEquals("bool", new BoolType().toString());
        assertEquals("true", new BoolValue(true).toString());
        assertEquals("Ref(int)", new RefType(new IntType()).toString());
    }
}