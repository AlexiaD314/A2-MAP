package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.IStack;
import interpreter.model.stmt.IStmt;
import interpreter.repository.IRepository;

public class Controller {
    private IRepository repo;
    private boolean displayFlag;

    public Controller(IRepository repo, boolean displayFlag) {
        this.repo = repo;
        this.displayFlag = displayFlag;
    }

    public void setDisplayFlag(boolean value) {
        this.displayFlag = value;
    }

    public PrgState oneStep(PrgState state) throws MyException {
        IStack<IStmt> stk = state.getExeStack();
        if (stk.isEmpty()) {
            throw new MyException("Execution stack is empty!");
        }
        IStmt crtStmt = stk.pop();
        return crtStmt.execute(state);
    }

    public void allStep() throws MyException {
        PrgState prg = repo.getCurrentPrg();

        repo.logPrgStateExec();
        if (displayFlag) {
            display(prg);
        }

        while (!prg.getExeStack().isEmpty()) {
            oneStep(prg);

            repo.logPrgStateExec();
            if (displayFlag) {
                display(prg);
            }
        }
    }

    private void display(PrgState state) {
        System.out.println(state.toString());
    }
}