package net.jcip.examples.ch05;

import java.util.*;

/**
 * SafeVectorHelpers
 * 
 * @list 5.2
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Compound actions on Vector using client-side locking, this is the fixed version for {@code UnsafeVectorHelpers}
 */
public class SafeVectorHelpers {
    public static Object getLast(Vector list) {
        synchronized (list) {                                        // Make sure only one thread can run this operation for the list at the same time.
            int lastIndex = list.size() - 1;
            return list.get(lastIndex);
        }
    }

    public static void deleteLast(Vector list) {
        synchronized (list) {                                        // Make sure only one thread can run this operation for the list at the same time.
            int lastIndex = list.size() - 1;
            list.remove(lastIndex);
        }
    }
}
