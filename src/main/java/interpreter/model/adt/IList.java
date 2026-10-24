package interpreter.model.adt;
import interpreter.exceptions.MyException;

public interface IList<T> {
    void add(T v);
    T get(int index) throws MyException;
    int size();
}
