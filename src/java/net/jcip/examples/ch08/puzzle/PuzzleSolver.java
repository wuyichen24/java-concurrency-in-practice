package net.jcip.examples.ch08.puzzle;

import java.util.concurrent.atomic.*;

/**
 * PuzzleSolver
 * 
 * @list 8.18
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Puzzle solver that can recognize when no solution exists.
 */
public class PuzzleSolver <P,M> extends ConcurrentPuzzleSolver<P, M> {
    PuzzleSolver(Puzzle<P, M> puzzle) {
        super(puzzle);
    }

    private final AtomicInteger taskCount = new AtomicInteger(0);              // Keep a count of active solver tasks and set the solution to null when the count drops to zero

    protected Runnable newTask(P p, M m, Node<P, M> n) {
        return new CountingSolverTask(p, m, n);
    }

    class CountingSolverTask extends SolverTask {
        CountingSolverTask(P pos, M move, Node<P, M> prev) {
            super(pos, move, prev);
            taskCount.incrementAndGet();
        }

        public void run() {
            try {
                super.run();
            } finally {
                if (taskCount.decrementAndGet() == 0)                          // Set the solution to null when the count drops to zero (it means there is no solution).
                    solution.setValue(null);
            }
        }
    }
}
