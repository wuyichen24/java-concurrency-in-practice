package net.jcip.examples.ch04;

import java.util.*;

import net.jcip.annotations.*;

/**
 * BetterVector
 * 
 * @list 4.13
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of extending an thread-safe class: Add a put-if-absent method into Vector
 */
@ThreadSafe
public class BetterVector <E> extends Vector<E> {
    // When extending a serializable class, you should redefine serialVersionUID
    static final long serialVersionUID = -3963416950630760754L;

    public synchronized boolean putIfAbsent(E x) {
        boolean absent = !contains(x);
        if (absent)
            add(x);
        return absent;
    }
}
