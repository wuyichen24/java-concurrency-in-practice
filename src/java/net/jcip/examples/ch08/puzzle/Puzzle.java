package net.jcip.examples.ch08.puzzle;

import java.util.*;

/**
 * Puzzle
 * 
 * @list 8.13
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Abstraction for puzzles like the 'sliding blocks puzzle'.
 * 
 * <p>P represents a position and M represents a move.
 */
public interface Puzzle <P, M> {
    P initialPosition();               // Get the initial position.

    boolean isGoal(P position);        // Check a position is a goal position or not.

    Set<M> legalMoves(P position);     // Get a set of all the legal moves based on the current position.

    P move(P position, M move);        // Make a move based on the current position and the move, return a new position.
}
