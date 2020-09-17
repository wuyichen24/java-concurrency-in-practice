package net.jcip.examples.ch03;

/**
 * StuffIntoPublic
 * 
 * @list 3.14
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>The example of not enough to publish an object safely.
 */
public class StuffIntoPublic {
    public Holder holder;            // Simply storing a reference to an object into a public field, it can not publish the object safely.
                                     // The Holder could appear to another thread to be in an inconsistent state.
    public void initialize() {
        holder = new Holder(42);
    }
}
