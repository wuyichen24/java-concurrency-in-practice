package net.jcip.examples.ch11;

import java.util.*;

import net.jcip.annotations.*;

/**
 * ServerStatusBeforeSplit
 *
 * @list 11.6
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>The example of a single lock can be split into multiple locks for better scalability.
 */
@ThreadSafe
public class ServerStatusBeforeSplit {
    @GuardedBy("this") public final Set<String> users;               // The two types of information are completely independent: one for users, another one for queries.
    @GuardedBy("this") public final Set<String> queries;

    public ServerStatusBeforeSplit() {
        users = new HashSet<String>();
        queries = new HashSet<String>();
    }

    public synchronized void addUser(String u) {                     
        users.add(u);
    }

    public synchronized void addQuery(String q) {
        queries.add(q);
    }

    public synchronized void removeUser(String u) {
        users.remove(u);
    }

    public synchronized void removeQuery(String q) {
        queries.remove(q);
    }
}
