package net.jcip.examples.ch02;

import java.math.BigInteger;
import javax.servlet.*;

import net.jcip.annotations.*;

/**
 * StatelessFactorizer
 *
 * @list 2.1
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Stateless: It has no fields and references no fields from other classes.
 */
@ThreadSafe
public class StatelessFactorizer extends GenericServlet implements Servlet {
    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        encodeIntoResponse(resp, factors);
    }

    void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
    }

    BigInteger extractFromRequest(ServletRequest req) {
        return new BigInteger("7");
    }

    BigInteger[] factor(BigInteger i) {
        // Doesn't really factor
        return new BigInteger[] { i };
    }
}
