package net.jcip.examples.ch03;

/**
 * UnsafeStates
 *
 * @list 3.6
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>A public method returns a private field, which makes a private field public 
 * (Escaped: Publish an object which should not have been public).
 */
class UnsafeStates {
    private String[] states = new String[]{
        "AK", "AL" /*...*/
    };

    public String[] getStates() {
        return states;               // the states array has escaped its intended scope.
    }
}
