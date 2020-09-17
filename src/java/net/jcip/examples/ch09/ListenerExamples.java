package net.jcip.examples.ch09;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;

/**
 * ListenerExamples
 *
 * @list 9.4
 * @list 9.5
 * @list 9.6
 * @list 9.8
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of GUI action listener.
 */
public class ListenerExamples {
    private static ExecutorService exec = Executors.newCachedThreadPool();

    private final JButton colorButton = new JButton("Change color");
    private final Random random = new Random();

    private void backgroundRandom() {
        colorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                colorButton.setBackground(new Color(random.nextInt()));
            }
        });
    }


    private final JButton computeButton = new JButton("Big computation");

    private void longRunningTask() {                                           // List 9.4 Start a long-running task without feedback.
        computeButton.addActionListener(new ActionListener() {                 // Gets the long-running task out of the event thread in a “fire and forget” manner
            public void actionPerformed(ActionEvent e) {
                exec.execute(new Runnable() {
                    public void run() {
                        /* Do big computation */
                    }
                });
            }
        });
    }


    private final JButton button = new JButton("Do");
    private final JLabel label = new JLabel("idle");

    private void longRunningTaskWithFeedback() {                               // List 9.5 Start a long-running with feedback
        button.addActionListener(new ActionListener() {                        // On completion the task must submit another task to run in the event thread to update the user interface.
            public void actionPerformed(ActionEvent e) {
                button.setEnabled(false);                                      // Before executing the task, disable the button and set the label as busy.
                label.setText("busy");
                exec.execute(new Runnable() {
                    public void run() {
                        try {
                            /* Do big computation */
                        } finally {
                            GuiExecutor.instance().execute(new Runnable() {    // When the task is completed, queue another task to re-enable the button and set the label as idle.
                                public void run() {
                                    button.setEnabled(true);
                                    label.setText("idle");
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private final JButton startButton = new JButton("Start");
    private final JButton cancelButton = new JButton("Cancel");
    private Future<?> runningTask = null; // thread-confined

    private void taskWithCancellation() {                                      // List 9.6 Cancel a running task.
        startButton.addActionListener(new ActionListener() {                   // A task that polls the thread's interrupted status and returns early on interruption.
            public void actionPerformed(ActionEvent e) {
                if (runningTask != null) {
                    runningTask = exec.submit(new Runnable() {
                        public void run() {
                            while (moreWork()) {
                                if (Thread.currentThread().isInterrupted()) {
                                    cleanUpPartialWork();
                                    break;
                                }
                                doSomeWork();
                            }
                        }

                        private boolean moreWork() {
                            return false;
                        }

                        private void cleanUpPartialWork() {
                        }

                        private void doSomeWork() {
                        }

                    });
                }
                ;
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (runningTask != null)
                    runningTask.cancel(true);
            }
        });
    }

    private void runInBackground(final Runnable task) {                        // List 9.8 Run a task by using {@code BackgroundTask}.
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                class CancelListener implements ActionListener {
                    BackgroundTask<?> task;
                    public void actionPerformed(ActionEvent event) {
                        if (task != null)
                            task.cancel(true);
                    }
                }
                final CancelListener listener = new CancelListener();
                listener.task = new BackgroundTask<Void>() {
                    public Void compute() {
                        while (moreWork() && !isCancelled())
                            doSomeWork();
                        return null;
                    }

                    private boolean moreWork() {
                        return false;
                    }

                    private void doSomeWork() {
                    }

                    public void onCompletion(boolean cancelled, String s, Throwable exception) {
                        cancelButton.removeActionListener(listener);
                        label.setText("done");
                    }
                };
                cancelButton.addActionListener(listener);
                exec.execute(task);
            }
        });
    }
}
