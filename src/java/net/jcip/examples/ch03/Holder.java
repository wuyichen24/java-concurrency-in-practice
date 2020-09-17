package net.jcip.examples.ch03;

/**
 * Holder
 * 
 * @list 3.15
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>This class at risk of failure if not properly published.
 */
public class Holder {
    private int n;

    public Holder(int n) {
        this.n = n;
    }

    public void assertSanity() {                                     // A thread other than the publishing thread were to call assertSanity, it could throw AssertionError.
        if (n != n)                                                  // A thread may see a stale value the first time it reads a field and then a more up-to-date value the next time, which is why assertSanity can throw AssertionError
            throw new AssertionError("This statement is false.");
    }
}
