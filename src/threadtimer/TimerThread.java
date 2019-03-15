package threadtimer;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Ompro on 15.04.2019.
 */

public class TimerThread extends Thread {

    private TimerData timerData;
    private AtomicBoolean paused = new AtomicBoolean(false);

    {
        setDaemon(true);
    }

    public TimerThread(TimerData timerData) {
        this.timerData = timerData;
    }

    @Override
    public void run() {
        //super.run();

        long timeCorrection = -System.currentTimeMillis();

        boolean paused = false;

        while (!interrupted()) {

            if (this.paused.get()) {
                if (!paused) {
                    timeCorrection += System.currentTimeMillis();
                    paused = true;
                }
            } else {
                if (paused) {
                    timeCorrection -= System.currentTimeMillis();
                    paused = false;
                }
                long time = System.currentTimeMillis();//timerData.getTime();
                time += timeCorrection;
                timerData.setTime(time);
            }

            try {
                sleep(10);
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }

    public boolean isPaused() {
        return paused.get();
    }

    public void setPaused(boolean paused) {
        this.paused.set(paused);
    }
}
