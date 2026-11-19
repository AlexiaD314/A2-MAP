package interpreter.model.adt;

import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;
import java.util.*;
import java.util.stream.Collectors;

public final class GarbageCollector {
    private GarbageCollector() {}

    public static Map<Integer, IValue> unsafeGarbageCollector(Collection<Integer> symTableAddresses,
                                                              Map<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> symTableAddresses.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Set<Integer> reachableAddresses(Collection<IValue> symTableValues,
                                                  Map<Integer, IValue> heap) {
        Set<Integer> reachable = new HashSet<>();
        Deque<Integer> worklist = new ArrayDeque<>();

        for (IValue val : symTableValues) {
            if (val instanceof RefValue ref && ref.address() != 0) {
                if (reachable.add(ref.address())) {
                    worklist.add(ref.address());
                }
            }
        }

        while (!worklist.isEmpty()) {
            int addr = worklist.pop();
            IValue heapVal = heap.get(addr);
            if (heapVal instanceof RefValue ref && ref.address() != 0) {
                if (reachable.add(ref.address())) {
                    worklist.add(ref.address());
                }
            }
        }

        return reachable;
    }

    public static Map<Integer, IValue> safeGarbageCollector(Collection<IValue> symTableValues,
                                                            Map<Integer, IValue> heap) {
        Set<Integer> reachable = reachableAddresses(symTableValues, heap);
        return heap.entrySet().stream()
                .filter(e -> reachable.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}