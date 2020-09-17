package net.jcip.examples.ch02;

import net.jcip.annotations.*;

/**
 * LazyInitRace
 *
 * @list 2.3
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Race condition in lazy initialization: If 2 threads execute getInstance() 
 * at same time, thread A may see the instance is not initialized so it tries 
 * to initialize the instance. After thread A sees the instance is null but 
 * before it initializes the instance, thread B may see the same thing and 
 * initialize the instance before thread A initializes it.
 */

@NotThreadSafe
public class LazyInitRace {
    private ExpensiveObject instance = null;

    public ExpensiveObject getInstance() {
        if (instance == null)
            instance = new ExpensiveObject();
        return instance;
    }
}

class ExpensiveObject { }

