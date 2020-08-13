package net.jcip.examples.ch3;

import java.util.*;

/**
 * Secrets
 *
 * Publishing an object
 *
 * @author Brian Goetz and Tim Peierls
 */
class Secrets {
    public static Set<Secret> knownSecrets;

    public void initialize() {
        knownSecrets = new HashSet<Secret>();
    }
}


class Secret {
}
