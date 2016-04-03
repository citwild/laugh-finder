/**
 *
 */
package edu.uw.css595.shalinir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The utility methods used in the application.
 *
 * @author Shalini Ramachandra
 *
 */
public class Utils {

    /**
     * Get the human readable display time.
     * @param milliseconds - The duration in milliseconds.
     * @return - String representation of the duration.
     */
    public static String displayTime(long milliseconds) {
        String displayTime = "";
        double seconds = milliseconds / 1000.0;
        int minute = (int) (seconds / 60);
        double second = seconds % 60;
        displayTime = String.format("%02d:%06.3f", minute, second);

        return displayTime;
    }

    /**
     * Backs up the given file with current timestamp as suffix
     *
     * @param fileName - The file name to be backed up.
     */
    public static void backupFileWithTimestamp(String fileName) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String[] parts = fileName.split("\\.");
        if (parts.length != 2) {
            try {
                throw new Exception("File name must have one and only one dot");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else {
            String newFileName = parts[0] + "_" + timeStamp + "." + parts[1];
            Path source = Paths.get(fileName);
            Path target = Paths.get(newFileName);
            try {
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * The feedback label structure.
     *
     * @author Shalini Ramachandra
     *
     */
    public static class FeedbackLabel {
        private boolean isLaughter;

        /**
         * Intializes a new instance of FeedbackLabel
         * @param isLaugher - Value indicating if the label is laughter.
         */
        public FeedbackLabel(boolean isLaugher) {
            this.isLaughter = isLaugher;
        }

        /**
         * Value indicating if the label is laughter.
         * @return - Value indicating if the label is laughter.
         */
        public boolean isLaugher() {
            return this.isLaughter;
        }
    }
}
