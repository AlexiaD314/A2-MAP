package interpreter.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import interpreter.view.Examples;

class ControllerTest {

    private PrgState run(IStmt program, String logFileName) throws MyException {
        PrgState state = PrgState.of(program);
        var repo = new Repository(state, logFileName);
        new Controller(repo, false).allStep();
        return state;
    }

    @Test
    void fileExampleReadsBothNumbersAndClosesTheFile() throws Exception {
        try (PrintWriter writer = new PrintWriter(new FileWriter("test_ctrl.in"))) {
            writer.println("15");
            writer.println("50");
        }

        PrgState s = run(Examples.fileExample("test_ctrl.in"), "log_test.txt");
        assertEquals(new IntValue(15), s.getOut().get(0));
        assertEquals(new IntValue(50), s.getOut().get(1));
        assertTrue(s.getFileTable().toMap().isEmpty());
    }

    @Test
    void logFileContainsTheFourSectionsInOrder() throws Exception {
        run(Examples.EX1, "log_ex1.txt");
        String log = Files.readString(Path.of("log_ex1.txt"));
        int a = log.indexOf("ExeStack:"), b = log.indexOf("SymTable:"),
                c = log.indexOf("Out:"), d = log.indexOf("FileTable:");
        assertTrue(0 <= a && a < b && b < c && c < d);
    }
}