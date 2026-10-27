package interpreter.model.adt;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import interpreter.exceptions.MyException;

class MyDictionaryTest {

    @Test
    void testPutAndGet() throws MyException {
        IDictionary<String, Integer> dict = new MyDictionary<>();
        dict.put("a", 10);
        dict.put("b", 20);

        assertEquals(10, dict.lookup("a"));
        assertEquals(20, dict.lookup("b"));
        assertTrue(dict.isDefined("a"));
        assertFalse(dict.isDefined("c"));
    }

    @Test
    void testUpdate() throws MyException {
        IDictionary<String, Integer> dict = new MyDictionary<>();
        dict.put("x", 5);
        dict.put("x", 15);

        assertEquals(15, dict.lookup("x"));
    }
}