package net.jcip.examples.ch08.puzzle;

import java.util.*;
import java.util.concurrent.*;

/**
 * ConcurrentPuzzleSolver
 * 
 * @list 8.16
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>A concurrent solver for the puzzle framework by:
 *    - Evaluating the set of possible next positions
 *    - Pruning positions already searched
 *    - Evaluating whether success has yet been achieved (by this task or by some other task)
 *    - Submitting unsearched positions to an Executor
 *    
 * <p>ConcurrentPuzzleSolver uses the internal work queue of the thread pool instead of the call stack to hold the state of the search.
 * 
 * <p>Drawback: This does not deal well with the case where there is no solution.
 */
public class ConcurrentPuzzleSolver <P, M> {
    private final Puzzle<P, M> puzzle;
    private final ExecutorService exec;
    private final ConcurrentMap<P, Boolean> seen;                                        // Record the positions have been visited to avoid infinite loops
    protected final ValueLatch<Node<P, M>> solution = new ValueLatch<Node<P, M>>();      // Record the solution

    public ConcurrentPuzzleSolver(Puzzle<P, M> puzzle) {
        this.puzzle = puzzle;
        this.exec = initThreadPool();
        this.seen = new ConcurrentHashMap<P, Boolean>();
        if (exec instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor tpe = (ThreadPoolExecutor) exec;
            tpe.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        }
    }

    private ExecutorService initThreadPool() {
        return Executors.newCachedThreadPool();
    }

    public List<M> solve() throws InterruptedException {                                 // Base function
        try {
            P p = puzzle.initialPosition();
            exec.execute(newTask(p, null, null));
            Node<P, M> solnPuzzleNode = solution.getValue();
            return (solnPuzzleNode == null) ? null : solnPuzzleNode.asMoveList();
        } finally {
            exec.shutdown();
        }
    }

    protected Runnable newTask(P p, M m, Node<P, M> n) {
        return new SolverTask(p, m, n);
    }

    protected class SolverTask extends Node<P, M> implements Runnable {                  // Recursive task
        SolverTask(P pos, M move, Node<P, M> prev) {
            super(pos, move, prev);
        }

        public void run() {
            if (solution.isSet() || seen.putIfAbsent(pos, true) != null)                 // If already have solved or seen this position, do nothing.
                return; 
            if (puzzle.isGoal(pos))                                                      // If the current position is the goal, return the solution as a series of moves.
                solution.setValue(this);
            else
                for (M m : puzzle.legalMoves(pos))                                       // Get a set of all the legal moves based on current position.
                    exec.execute(newTask(puzzle.move(pos, m), m, this));                 // For each legal move, submit a task to calculate recursively (breadth-first).
        }
    }
}
