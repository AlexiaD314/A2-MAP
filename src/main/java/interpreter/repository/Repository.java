package interpreter.repository;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private List<PrgState> prgStates;
    private String logFilePath;
    public Repository(String logFilePath) {
        this.prgStates = new ArrayList<>();
        this.logFilePath = logFilePath;
    }
    @Override
    public void addPrg(PrgState prg) {
        prgStates.add(prg);
    }
    @Override
    public PrgState getCurrentPrg() {
        if (prgStates.isEmpty()) {
            return null;
        }
        return prgStates.get(0);
    }
    @Override
    public void logPrgStateExec() throws MyException {
        try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {
            PrgState prg = getCurrentPrg();
            if (prg != null) {
                logFile.println("ExeStack:");
                logFile.println(prg.getExeStack().toString());
                logFile.println("SymTable:");
                logFile.println(prg.getSymTable().toString());
                logFile.println("Out:");
                logFile.println(prg.getOut().toString());
                logFile.println("--------------------------------");
            }
        } catch (IOException e) {
            throw new MyException("Error writing to log file: " + e.getMessage());
        }
    }
    @Override
    public List<PrgState> getPrgList() {
        return prgStates;
    }
    @Override
    public void setPrgList(List<PrgState> prgList) {
        this.prgStates = prgList;
    }
}
