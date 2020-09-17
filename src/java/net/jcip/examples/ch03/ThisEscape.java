package net.jcip.examples.ch03;

/**
 * ThisEscape
 *
 * @list 3.7
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>The example of the “this” reference escapes during construction before it is fully constructed.
 */
public class ThisEscape {
    public ThisEscape(EventSource source) {
        source.registerListener(new EventListener() {  // When ThisEscape publishes the EventListener, it implicitly publishes the enclosing ThisEscape instance as well.
            public void onEvent(Event e) {
                doSomething(e);
            }
        });
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

