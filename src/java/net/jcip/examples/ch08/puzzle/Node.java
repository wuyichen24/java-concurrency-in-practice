package net.jcip.examples.ch08.puzzle;

import java.util.*;

import net.jcip.annotations.*;

/**
 * Node
 * 
 * @list 8.14
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Represents a position that has been reached through some series of moves, holding a reference to the move that created the position and the previous Node.
 */
@Immutable
public class Node <P, M> {
    final P pos;
    final M move;
    final Node<P, M> prev;

    public Node(P pos, M move, Node<P, M> prev) {
        this.pos = pos;
        this.move = move;
        this.prev = prev;
    }

    List<M> asMoveList() {                                           // Get a list of moves to the current position.
        List<M> solution = new LinkedList<M>();
        for (Node<P, M> n = this; n.move != null; n = n.prev)
            solution.add(0, n.move);
        return solution;
    }
}
