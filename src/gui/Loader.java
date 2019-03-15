package gui;

import javax.swing.*;

/**
 * Created by Ompro on 15.04.2019.
 */
public class Loader {
    public static void main(String[] args) {

        for(UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            if(laf.getName().equals("Nimbus")){
                try {
                    UIManager.setLookAndFeel(laf.getClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TimerFrame timerFrame = new TimerFrame();
                timerFrame.setVisible(true);
            }
        });
    }
}
