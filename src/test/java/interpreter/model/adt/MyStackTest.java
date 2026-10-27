package interpreter.model.adt;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import interpreter.exceptions.MyException;

class MyStackTest {

    @Test
    void testPushAndPop() throws MyException {
        IStack<Integer> stack = new MyStack<>();
        stack.push(10);
        stack.push(20);

        assertEquals(20, stack.pop());
        assertEquals(10, stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    void testPopEmptyStackThrows() {
        IStack<Integer> stack = new MyStack<>();
        assertThrows(MyException.class, stack::pop);
    }
}