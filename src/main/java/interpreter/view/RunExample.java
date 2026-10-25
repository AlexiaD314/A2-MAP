package interpreter.view;
import interpreter.controller.Controller;
import interpreter.exceptions.MyException;

public class RunExample extends Command {
    private Controller ctr;
    public RunExample(String key, String description, Controller ctr) {
        super(key, description);
        this.ctr = ctr;
    }
    @Override
    public void execute() {
        try{
            ctr.allStep();
            System.out.println("Program executed successfully! Check log file.");
        } catch (MyException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
