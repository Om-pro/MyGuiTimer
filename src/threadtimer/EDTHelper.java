package threadtimer;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Ompro on 15.04.2019.
 */
public class EDTHelper {
    public static boolean isEventDispatchThread() {
        return SwingUtilities.isEventDispatchThread();
    }

    public static void invokeLater(Runnable runnable, boolean forgoOnTerminate) {
        if(!forgoOnTerminate || isEventDispatchThread()) {
            SwingUtilities.invokeLater(runnable);
        } else {
            Thread currentThread = Thread.currentThread();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if(currentThread.isAlive()) {
                        runnable.run();
                    }
                }
            });
        }
    }

    public static void invokeAndWait(Runnable runnable, boolean forgoOnInterrupt) throws InterruptedException {
        if (isEventDispatchThread()) {
            runnable.run();
        } else {
            AtomicBoolean doIt = new AtomicBoolean(true);
            try {
                if (forgoOnInterrupt) {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            if (doIt.get()) {
                                runnable.run();
                            }
                        }
                    });
                } else {
                    SwingUtilities.invokeAndWait(runnable);
                }
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if(cause instanceof Error)
                    throw (Error)cause;
                else if(cause instanceof RuntimeException)
                    throw (RuntimeException) cause;
                else
                    throw new Error("Impossible situation!");
            } catch(InterruptedException e) {
                doIt.set(false);
                throw e;
            }
        }
    }
}
