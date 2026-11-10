package interpreter.model.adt;
import interpreter.exceptions.MyException;
import java.util.List;

public interface IStack<T> {
    void push(T v);
    T pop() throws MyException;
    boolean isEmpty();
    List<T> toList();
}
