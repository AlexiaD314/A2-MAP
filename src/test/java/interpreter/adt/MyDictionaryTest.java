package interpreter.adt;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

import interpreter.controller.Controller;
import interpreter.exceptions.ADTException;
import interpreter.exceptions.RepositoryException;
import interpreter.model.PrgState;
import interpreter.model.adt.GarbageCollector;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.adt.MyList;
import interpreter.model.type.IntType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import interpreter.view.RunExampleCommand;
import interpreter.view.TextMenu;

class MyDictionaryTest {

    @Test
    void testPutAndGet() throws Exception {
        IDictionary<String, Integer> dict = new MyDictionary<>();
        dict.put("a", 10);
        dict.put("b", 20);

        assertEquals(10, dict.lookup("a"));
        assertEquals(20, dict.lookup("b"));
        assertTrue(dict.isDefined("a"));
        assertFalse(dict.isDefined("c"));
    }

    @Test
    void testUpdate() throws Exception {
        IDictionary<String, Integer> dict = new MyDictionary<>();
        dict.put("x", 5);
        dict.put("x", 15);

        assertEquals(15, dict.lookup("x"));
    }

    @Test
    void testListHeapGarbageCollectorAndMenu(@TempDir Path tempDir) throws Exception {
        MyList<Integer> list = new MyList<>();
        list.add(10);
        list.add(20);
        assertTrue(list.toString().contains("10"));
        assertTrue(list.toString().contains("20"));

        MyHeap heap = new MyHeap();
        int a1 = heap.allocate(new IntValue(100));
        int a2 = heap.allocate(new IntValue(200));
        int a3 = heap.allocate(new IntValue(300));
        heap.update(a2, new RefValue(a3, new IntType()));

        assertTrue(heap.isDefined(a1));
        assertEquals(new IntValue(100), heap.get(a1));
        assertThrows(ADTException.class, () -> heap.get(999));
        assertThrows(ADTException.class, () -> heap.update(999, new IntValue(1)));

        Map<Integer, IValue> unsafe = GarbageCollector.unsafeGarbageCollector(List.of(a1), heap.getContent());
        assertTrue(unsafe.containsKey(a1));
        assertFalse(unsafe.containsKey(a2));

        Set<Integer> reachable = GarbageCollector.reachableAddresses(List.of(new RefValue(a2, new IntType())), heap.getContent());
        assertEquals(Set.of(a2, a3), reachable);

        Map<Integer, IValue> safe = GarbageCollector.safeGarbageCollector(List.of(new RefValue(a2, new IntType())), heap.getContent());
        heap.setContent(safe);
        assertFalse(heap.isDefined(a1));
        assertTrue(heap.isDefined(a2));
        assertTrue(heap.isDefined(a3));
        assertNotNull(heap.toString());

        Repository emptyRepo = new Repository(tempDir.resolve("empty.log").toString());
        assertNull(emptyRepo.getCurrentPrg());

        Path subDir = tempDir.resolve("subdir");
        Files.createDirectories(subDir);
        Repository badRepo = new Repository(subDir.toString());
        assertThrows(RepositoryException.class, badRepo::logPrgStateExec);

        PrgState prg = PrgState.of(Examples.EX1);
        Repository repo = new Repository(prg, tempDir.resolve("menu.log").toString());
        assertEquals(1, repo.getPrgList().size());
        repo.setPrgList(List.of(prg));

        Controller ctrl = new Controller(repo, true);
        ctrl.setDisplayFlag(false);
        ctrl.setDisplayFlag(true);

        TextMenu menu = new TextMenu();
        RunExampleCommand runCmd = new RunExampleCommand("1", "EX1", ctrl);
        assertEquals("1", runCmd.getKey());
        assertEquals("EX1", runCmd.getDescription());
        menu.addCommand(runCmd);
        assertNotNull(menu);
    }
}