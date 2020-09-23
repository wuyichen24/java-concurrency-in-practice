package net.jcip.examples.ch11;

import java.util.*;
import java.util.regex.*;

import net.jcip.annotations.*;

/**
 * AttributeStore
 * 
 * @list 11.4
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>The example of holding a lock longer than necessary.
 */
@ThreadSafe
public class AttributeStore {
    @GuardedBy("this") private final Map<String, String>
            attributes = new HashMap<String, String>();

    public synchronized boolean userLocationMatches(String name, String regexp) {        // The entire userLocationMatches method is synchronized.
        String key = "users." + name + ".location";
        String location = attributes.get(key);                                           // But the only portion of the code that actually needs the lock is the call to Map.get().
        if (location == null)
            return false;
        else
            return Pattern.matches(regexp, location);
    }
}
