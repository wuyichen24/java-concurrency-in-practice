package net.jcip.examples.ch08.puzzle;

import java.util.*;

/**
 * SequentialPuzzleSolver
 * 
 * @list 8.15
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>A sequential solver for the puzzle framework that performs a depth-first search of the puzzle space. It terminates when it finds a solution
 */
public class SequentialPuzzleSolver <P, M> {
    private final Puzzle<P, M> puzzle;
    private final Set<P> seen = new HashSet<P>();

    public SequentialPuzzleSolver(Puzzle<P, M> puzzle) {
        this.puzzle = puzzle;
    }

    public List<M> solve() {                                         // Base function
        P pos = puzzle.initialPosition(); 
        return search(new Node<P, M>(pos, null, null));
    }

    private List<M> search(Node<P, M> node) {                        // Recursive function
        if (!seen.contains(node.pos)) {
            seen.add(node.pos);
            if (puzzle.isGoal(node.pos))                             // If the current position is the goal, return the solution as a series of moves.
                return node.asMoveList();
            for (M move : puzzle.legalMoves(node.pos)) {             // Get a set of all the legal moves based on current position.
                P pos = puzzle.move(node.pos, move);
                Node<P, M> child = new Node<P, M>(pos, move, node);
                List<M> result = search(child);                      // For each legal move, try recursively (depth-first).
                if (result != null)
                    return result;
            }
        }
        return null;
    }
}
