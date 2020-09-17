package net.jcip.examples.ch04;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.concurrent.*;

/**
 * VisualComponent
 * 
 * @list 4.9
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Delegating thread safety to multiple underlying state variables
 */
public class VisualComponent {
	// There is no relationship between the set of mouse listeners and key listeners; the two are independent, 
	// and therefore VisualComponent can delegate its thread safety obligations to two underlying thread-safe lists.
    private final List<KeyListener> keyListeners = new CopyOnWriteArrayList<KeyListener>();
    private final List<MouseListener> mouseListeners = new CopyOnWriteArrayList<MouseListener>();

    public void addKeyListener(KeyListener listener) {
        keyListeners.add(listener);
    }

    public void addMouseListener(MouseListener listener) {
        mouseListeners.add(listener);
    }

    public void removeKeyListener(KeyListener listener) {
        keyListeners.remove(listener);
    }

    public void removeMouseListener(MouseListener listener) {
        mouseListeners.remove(listener);
    }
}
