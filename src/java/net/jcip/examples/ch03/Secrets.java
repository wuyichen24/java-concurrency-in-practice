package net.jcip.examples.ch03;

import java.util.*;

/**
 * Secrets
 *
 * @list 3.5
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>The example of publishing an object as a public static field.
 */
class Secrets {
    public static Set<Secret> knownSecrets;

    public void initialize() {
        knownSecrets = new HashSet<Secret>();
    }
}


class Secret {
}
