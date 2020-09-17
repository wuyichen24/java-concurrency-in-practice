package net.jcip.examples.ch10;

import java.util.concurrent.atomic.*;

import net.jcip.examples.ch10.DynamicOrderDeadlock.DollarAmount;

/**
 * DynamicOrderDeadlock
 * 
 * @list 10.2
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of dynamic lock-ordering deadlock: The lock-ordering depends on on external inputs.
 */
public class DynamicOrderDeadlock {
    public static void transferMoney(Account fromAccount, Account toAccount, DollarAmount amount)            // The lock order depends on the order of arguments passed to this function.
            throws InsufficientFundsException {                                                              // Like:     A: transferMoney(myAccount, yourAccount, 10);
        synchronized (fromAccount) {                                                                         //           B: transferMoney(yourAccount, myAccount, 20);
            synchronized (toAccount) {
                if (fromAccount.getBalance().compareTo(amount) < 0)
                    throw new InsufficientFundsException();
                else {
                    fromAccount.debit(amount);
                    toAccount.credit(amount);
                }
            }
        }
    }

    static class DollarAmount implements Comparable<DollarAmount> {
        public DollarAmount(int amount) {
        }

        public DollarAmount add(DollarAmount d) {
            return null;
        }

        public DollarAmount subtract(DollarAmount d) {
            return null;
        }

        public int compareTo(DollarAmount dollarAmount) {
            return 0;
        }
    }

    static class Account {
        private DollarAmount balance = new DollarAmount(0);
        private final int acctNo;
        private static final AtomicInteger sequence = new AtomicInteger();

        public Account() {
            acctNo = sequence.incrementAndGet();
        }

        void debit(DollarAmount d) {
            balance = balance.subtract(d);
        }

        void credit(DollarAmount d) {
            balance = balance.add(d);
        }

        DollarAmount getBalance() {
            return balance;
        }

        int getAcctNo() {
            return acctNo;
        }
    }

    static class InsufficientFundsException extends Exception {
    }
}
