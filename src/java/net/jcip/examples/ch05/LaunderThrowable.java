package net.jcip.examples.ch05;

/**
 * StaticUtilities
 *
 * @list 5.13
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Utility class to encapsulate some of the messier exception-handling logic.
 */
public class LaunderThrowable {
    /**
     * Transform an unchecked Throwable (inconvenient to deal with) to a RuntimeException
     * 
     * If it is a RuntimeException return it;
     * If the Throwable is an Error, throw it; 
     * Otherwise throw IllegalStateException.
     */
    public static RuntimeException launderThrowable(Throwable t) {
        if (t instanceof RuntimeException)
            return (RuntimeException) t;
        else if (t instanceof Error)
            throw (Error) t;
        else
            throw new IllegalStateException("Not unchecked", t);
    }
}
