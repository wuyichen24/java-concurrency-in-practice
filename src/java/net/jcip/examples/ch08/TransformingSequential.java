package net.jcip.examples.ch08;

import java.util.*;
import java.util.concurrent.*;

/**
 * TransformingSequential
 * 
 * @list 8.10
 * @list 8.11
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of transforming sequential loop into parallel loop.
 */
public abstract class TransformingSequential {
    void processSequentially(List<Element> elements) {                         // List 8.10 Run the iterations of a loop sequentially.
        for (Element e : elements)
            process(e);
    }

    void processInParallel(Executor exec, List<Element> elements) {            // List 8.10 Run the iterations of a loop in parallel.
        for (final Element e : elements)
            exec.execute(new Runnable() {
                public void run() {
                    process(e);
                }
            });
    }

    public abstract void process(Element e);

    public <T> void sequentialRecursive(List<Node<T>> nodes,
                                        Collection<T> results) {               // List 8.11 A depth-first traversal of a tree, performing a calculation on each node and placing the result in a collection.
        for (Node<T> n : nodes) {
            results.add(n.compute());
            sequentialRecursive(n.getChildren(), results);
        }
    }

    public <T> void parallelRecursive(final Executor exec,
                                      List<Node<T>> nodes,
                                      final Collection<T> results) {           // List 8.11 A depth-first traversal of a tree, submitting a task to compute the node result.
        for (final Node<T> n : nodes) {
            exec.execute(new Runnable() {
                public void run() {
                    results.add(n.compute());
                }
            });
            parallelRecursive(exec, n.getChildren(), results);
        }
    }

    public <T> Collection<T> getParallelResults(List<Node<T>> nodes)           // List 8.12 Wait for results to be calculated in parallel.
            throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        Queue<T> resultQueue = new ConcurrentLinkedQueue<T>();
        parallelRecursive(exec, nodes, resultQueue);
        exec.shutdown();
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        return resultQueue;
    }

    interface Element {
    }

    interface Node <T> {
        T compute();

        List<Node<T>> getChildren();
    }
}

