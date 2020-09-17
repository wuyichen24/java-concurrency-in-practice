package net.jcip.examples.ch03;

import java.math.BigInteger;
import javax.servlet.*;

import net.jcip.annotations.*;

/**
 * VolatileCachedFactorizer
 *
 * @list 3.13
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>This combination of an immutable holder object and a volatile reference used to ensure its timely visibility, 
 * allows VolatileCachedFactorizer to be thread-safe even though it does no explicit locking.
 */
@ThreadSafe
public class VolatileCachedFactorizer extends GenericServlet implements Servlet {
    private volatile OneValueCache cache = new OneValueCache(null, null);

    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = cache.getFactors(i);
        if (factors == null) {
            factors = factor(i);
            cache = new OneValueCache(i, factors);    //  When a thread sets the volatile cache field to reference a new OneValueCache, the new cached data becomes immediately visible to other threads (timely visibility).
        }
        encodeIntoResponse(resp, factors);
    }

    void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
    }

    BigInteger extractFromRequest(ServletRequest req) {
        return new BigInteger("7");
    }

    BigInteger[] factor(BigInteger i) {
        // Doesn't really factor
        return new BigInteger[]{i};
    }
}

