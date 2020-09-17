package net.jcip.examples.ch03;

import java.util.*;

import net.jcip.annotations.*;

/**
 * ThreeStooges
 *
 * @list 3.11
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of immutable objects can still use mutable objects internally to manage their state.
 */
@Immutable
 public final class ThreeStooges {
    private final Set<String> stooges = new HashSet<String>(); // stooges field is final.

    public ThreeStooges() {                                    // Proper construction.
        stooges.add("Moe");            
        stooges.add("Larry");
        stooges.add("Curly");
    }

    public boolean isStooge(String name) {
        return stooges.contains(name);
    }

    public String getStoogeNames() {
        List<String> stooges = new Vector<String>();
        stooges.add("Moe");
        stooges.add("Larry");
        stooges.add("Curly");
        return stooges.toString();
    }
}
