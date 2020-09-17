package net.jcip.examples.ch03;

/**
 * SafeListener
 * 
 * @list 3.8
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Using a factory method and private constructor to prevent the 
 * this reference from escaping during construction.
 */
public class SafeListener {
    private final EventListener listener;

    private SafeListener() {                                         // Private constructor
        listener = new EventListener() {
            public void onEvent(Event e) {
                doSomething(e);
            }
        };
    }

    public static SafeListener newInstance(EventSource source) {     // Factory method
        SafeListener safe = new SafeListener();
        source.registerListener(safe.listener);
        return safe;
    }

    void doSomething(Event e) {
    }


    interface EventSource {
        void registerListener(EventListener e);
    }

    interface EventListener {
        void onEvent(Event e);
    }

    interface Event {
    }
}

