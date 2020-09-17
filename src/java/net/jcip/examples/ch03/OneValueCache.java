package net.jcip.examples.ch03;

import java.math.BigInteger;
import java.util.*;

import net.jcip.annotations.*;

/**
 * OneValueCache
 *
 * @list 3.12
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of immutable objects can sometimes provide a weak form of atomicity.
 */
@Immutable
public class OneValueCache {
    private final BigInteger lastNumber;
    private final BigInteger[] lastFactors;

    public OneValueCache(BigInteger i, BigInteger[] factors) {
        lastNumber = i;
        lastFactors = Arrays.copyOf(factors, factors.length);   // with copyOf, it allocates a new separated memory store for the array. It eliminates the chance of other code holds the references of any element in the array.
    }                                                           // So this object is immutable.

    public BigInteger[] getFactors(BigInteger i) {
        if (lastNumber == null || !lastNumber.equals(i))
            return null;
        else
            return Arrays.copyOf(lastFactors, lastFactors.length);
    }
}
