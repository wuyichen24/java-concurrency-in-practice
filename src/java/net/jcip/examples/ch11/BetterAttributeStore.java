package net.jcip.examples.ch11;

import java.util.*;
import java.util.regex.*;

import net.jcip.annotations.*;

/**
 * BetterAttributeStore
 * 
 * @list 11.5
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Rewrites {@code AttributeStore} to reduce significantly the lock duration.
 */
@ThreadSafe
public class BetterAttributeStore {
    @GuardedBy("this") private final Map<String, String>
            attributes = new HashMap<String, String>();

    public boolean userLocationMatches(String name, String regexp) {
        String key = "users." + name + ".location";
        String location;
        synchronized (this) {                                        // Only synchronize the Map.get() call.
            location = attributes.get(key);
        }
        if (location == null)
            return false;
        else
            return Pattern.matches(regexp, location);
    }
}
