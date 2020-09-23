package net.jcip.examples.ch11;

import java.util.*;

import net.jcip.annotations.*;

/**
 * ServerStatusAfterSplit
 * 
 * @list 11.7
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Splitting a single lock in {@code ServerStatusBeforeSplit} into multiple locks.
 */
@ThreadSafe
public class ServerStatusAfterSplit {
    @GuardedBy("users") public final Set<String> users;
    @GuardedBy("queries") public final Set<String> queries;

    public ServerStatusAfterSplit() {
        users = new HashSet<String>();
        queries = new HashSet<String>();
    }

    public void addUser(String u) {
        synchronized (users) {
            users.add(u);
        }
    }

    public void addQuery(String q) {
        synchronized (queries) {
            queries.add(q);
        }
    }

    public void removeUser(String u) {
        synchronized (users) {
            users.remove(u);
        }
    }

    public void removeQuery(String q) {
        synchronized (users) {
            queries.remove(q);
        }
    }
}
