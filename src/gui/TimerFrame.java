package gui;

import threadtimer.EDTHelper;
import threadtimer.TimerData;
import threadtimer.TimerThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ompro on 15.04.2019.
 */
public class TimerFrame extends JFrame  {
    private JPanel rootPanel;
    private JLabel timerText;
    private JButton startPauseButton;
    private JButton resetButton;

    private TimerThread timerThread;

    {
        setContentPane(rootPanel);
        Dimension size = new Dimension(400, 300);
        setSize(new Dimension(size));
        setMinimumSize(new Dimension(size));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setStoppedState();

        startPauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(timerThread == null) {
                    timerThread = new TimerThread(new TimerInvoker());
                    timerThread.start();
                    setActiveState();
                } else if(timerThread.isPaused()){
                    timerThread.setPaused(false);
                    setActiveState();
                } else {
                    timerThread.setPaused(true);
                    setPausedState();
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(timerThread != null) {
                    timerThread.interrupt();
                    try {
                        timerThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    timerThread = null;
                }
                setStoppedState();
            }
        });
    }

    private void setStoppedState() {
        startPauseButton.setText("Start");
        //resetButton.setEnabled(false);
        setTime(0);
    }

    private void setPausedState() {
        startPauseButton.setText("Continue");
        resetButton.setEnabled(true);
    }

    private void setActiveState() {
        startPauseButton.setText("Pause");
        //resetButton.setEnabled(false);
    }

    private class TimerInvoker implements TimerData {

        @Override
        public void setTime(long time) {
            EDTHelper.invokeLater(() -> TimerFrame.this.setTime(time), true);
        }

        @Override
        public long getTime() {
            long time[] = new long[1];
            try {
                EDTHelper.invokeAndWait(() -> time[0] = TimerFrame.this.getTime(), true);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return time[0];
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }


    public void setTime(long time) {
        long millis = time % 1000;
        time /= 1000;
        long sec = time % 60;
        time /= 60;
        long mins = time % 60;
        time /= 60;
        long hours = time % 100;
        timerText.setText(String.format("%02d:%02d:%02d.%03d", hours, mins, sec, millis));
    }

    public long getTime() {
        String text[] = timerText.getText().split("[:\\.]");
        long time = Integer.parseInt(text[0]) * 60;
        time += Integer.parseInt(text[1]);
        time *= 60;
        time += Integer.parseInt(text[2]);
        time *= 1000;
        time += Integer.parseInt(text[3]);
        return time;
    }
}
