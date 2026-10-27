package interpreter.model.adt;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import interpreter.exceptions.MyException;

class MyDictionaryTest {

    @Test
    void testPutAndGet() throws MyException {
        IDictionary<String, Integer> dict = new MyDictionary<>();
        dict.update("a", 10);
        dict.update("b", 20);

        assertEquals(10, dict.lookup("a"));
        assertEquals(20, dict.lookup("b"));
        assertTrue(dict.isDefined("a"));
        assertFalse(dict.isDefined("c"));
    }

    @Test
    void testUpdate() throws MyException {
        IDictionary<String, Integer> dict = new MyDictionary<>();
        dict.update("x", 5);
        dict.update("x", 15);

        assertEquals(15, dict.lookup("x"));
    }
}