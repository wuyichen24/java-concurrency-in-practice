package net.jcip.examples.ch11;

import java.util.*;

import net.jcip.annotations.*;

/**
 * ThreeStooges
 * 
 * @list 11.3
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>The example of lock elision: If a local object is never escaped, lock acquisitions can be eliminated for calling its method.
 */
@Immutable
public final class ThreeStooges {
    private final Set<String> stooges = new HashSet<String>();

    public ThreeStooges() {
        stooges.add("Moe");
        stooges.add("Larry");
        stooges.add("Curly");
    }

    public boolean isStooge(String name) {
        return stooges.contains(name);
    }

    public String getStoogeNames() {
        List<String> stooges = new Vector<String>();                 // The only reference to the List is the local variable stooges
        stooges.add("Moe");                                          // So the all four lock acquisitions for add() and toString() can be eliminated.
        stooges.add("Larry");
        stooges.add("Curly");
        return stooges.toString();
    }
}
