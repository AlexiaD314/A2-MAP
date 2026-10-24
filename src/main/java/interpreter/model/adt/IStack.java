package interpreter.model.adt;
import interpreter.exceptions.MyException;

public interface IStack<T> {
    void push(T v);
    T pop() throws MyException;
    boolean isEmpty();
}
