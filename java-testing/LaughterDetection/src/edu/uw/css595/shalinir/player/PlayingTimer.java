package edu.uw.css595.shalinir.player;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.sound.sampled.Clip;
import javax.swing.JLabel;
import javax.swing.JSlider;

/**
 * This class counts playing time in the form of HH:mm:ss
 * It also updates the time slider
 * @author www.codejava.net
 *
 */
public class PlayingTimer extends Thread {
    private DateFormat dateFormater = new SimpleDateFormat("HH:mm:ss");
    private boolean isRunning = false;
    private boolean isPause = false;
    private boolean isReset = false;
    private long playedTime;

    private JLabel labelRecordTime;
    private JSlider slider;
    private Clip audioClip;

    public void setAudioClip(Clip audioClip) {
        this.audioClip = audioClip;
    }

    PlayingTimer(JLabel labelRecordTime, JSlider slider) {
        this.labelRecordTime = labelRecordTime;
        this.slider = slider;
    }

    public void run() {
        isRunning = true;

        while (isRunning) {
            try {
                Thread.sleep(100);
                if (!isPause) {
                    if (audioClip != null && audioClip.isRunning()) {
                        labelRecordTime.setText(toTimeString());
                        int currentSecond = (int) audioClip.getMicrosecondPosition() / 1000000;
                        slider.setValue(currentSecond);
                        playedTime += 100;
                    }
                }
            } catch (InterruptedException ex) {
                //ex.printStackTrace();
                if (isReset) {
                    slider.setValue(0);
                    labelRecordTime.setText("00:00:00");
                    isRunning = false;
                    break;
                }
            }
        }
    }

    public void setPositionAndPlay(long microsecondPosition) {
        this.pauseTimer();
        audioClip.setMicrosecondPosition(microsecondPosition);
        int currentSecond = (int) audioClip.getMicrosecondPosition() / 1000000;
        slider.setValue(currentSecond);
        playedTime = microsecondPosition / 1000;
        this.resumeTimer();
    }

    public void setPlayedTime(long timeInMs) {
        playedTime = timeInMs;
    }

    /**
     * Reset counting to "00:00:00"
     */
    void reset() {
        isReset = true;
        isRunning = false;
    }

    void pauseTimer() {
        isPause = true;
    }

    void resumeTimer() {
        isPause = false;
    }

    /**
     * Generate a String for time counter in the format of "HH:mm:ss"
     * @return the time counter
     */
    private String toTimeString() {
        Date current = new Date(playedTime);
        dateFormater.setTimeZone(TimeZone.getTimeZone("GMT"));
        String timeCounter = dateFormater.format(current);
        return timeCounter;
    }
}
