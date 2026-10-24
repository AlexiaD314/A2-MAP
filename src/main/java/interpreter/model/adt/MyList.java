package interpreter.model.adt;
import interpreter.exceptions.MyException;
import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements IList<T> {
    private List<T> list;
    public MyList() {
        this.list = new ArrayList<>();
    }
    @Override
    public void add(T v) {
        list.add(v);
    }
    @Override
    public T get(int index) throws MyException{
        if(index < 0 || index >= list.size())
            throw new MyException("Index out of bounds");
        return list.get(index);
    }
    @Override
    public int size() {
        return list.size();
    }
    @Override
    public String toString() {
        return list.toString();
    }
}
