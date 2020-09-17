package net.jcip.examples.ch05;

import java.util.*;

/**
 * UnsafeVectorHelpers
 *
 * @list 5.1
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of the synchronized vector still needs synchronization for compound actions.
 */
public class UnsafeVectorHelpers {
    public static Object getLast(Vector list) {            // This operation could throws ArrayIndexOutOfBoundsException,
        int lastIndex = list.size() - 1;                   // if 2 different threads call those operations for the same vector at the same time.
        return list.get(lastIndex);
    }

    public static void deleteLast(Vector list) {
        int lastIndex = list.size() - 1;
        list.remove(lastIndex);
    }
}
