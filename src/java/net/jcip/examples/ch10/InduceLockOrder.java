package net.jcip.examples.ch10;

/**
 * InduceLockOrder
 *
 * @list 10.3
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>The fixed version of {@code DynamicOrderDeadlock} to avoid deadlock by inducing the lock-ordering in the consistent order.
 */
public class InduceLockOrder {
    private static final Object tieLock = new Object();

    public void transferMoney(final Account fromAcct,
                              final Account toAcct,
                              final DollarAmount amount)
            throws InsufficientFundsException {
        class Helper {
            public void transfer() throws InsufficientFundsException {
                if (fromAcct.getBalance().compareTo(amount) < 0)
                    throw new InsufficientFundsException();
                else {
                    fromAcct.debit(amount);
                    toAcct.credit(amount);
                }
            }
        }
        int fromHash = System.identityHashCode(fromAcct);                      // Get the hash code of the from account
        int toHash = System.identityHashCode(toAcct);                          // Get the hash code of the to account

        if (fromHash < toHash) {                                               // Acquire the locks based on the descending order of the hash codes of 2 accounts.
            synchronized (fromAcct) {
                synchronized (toAcct) {
                    new Helper().transfer();
                }
            }
        } else if (fromHash > toHash) {
            synchronized (toAcct) {
                synchronized (fromAcct) {
                    new Helper().transfer();
                }
            }
        } else {                                                               // In the rare case that two objects have the same hash code
            synchronized (tieLock) {                                           // To prevent inconsistent lock ordering in this case, a third “tie breaking” lock is used.
                synchronized (fromAcct) {                                      // Ensure that only one thread at a time performs the risky task of acquiring two locks in an arbitrary order
                    synchronized (toAcct) {
                        new Helper().transfer();
                    }
                }
            }
        }
    }

    interface DollarAmount extends Comparable<DollarAmount> {
    }

    interface Account {
        void debit(DollarAmount d);

        void credit(DollarAmount d);

        DollarAmount getBalance();

        int getAcctNo();
    }

    class InsufficientFundsException extends Exception {
    }
}
