package edu.uw.css595.shalinir;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

/**
 * The test engine which is used for producing the laughter segments from an
 * ARFF file.
 *
 * @author Shalini Ramachandra
 *
 */
public class TestEngine {

    private String arffPath;
    private Instances instances;
    private List<Boolean> isPresentList;

    /**
     * Initializes a new instance of TestEngine
     *
     * @param arffPath
     *            - The path to the ARFF file
     */
    public TestEngine(String arffPath) {
        this.arffPath = arffPath;
        System.out.println("ARFF path: " + this.arffPath);
    }

    /**
     * Get the laughter segments from the ARFF files.
     *
     * @return - List of arrays of size 2, with start and end time in
     *         milliseconds.
     */
    public List<long[]> getLaughters() {
        String indexFileName = Constants.TEST_INDEX_FILE;
        isPresentList = this.getIsPresentList(indexFileName);
        List<long[]> laughtersInMilliSecs = new ArrayList<long[]>();

        try {
            BufferedReader arffReader = new BufferedReader(new FileReader(
                    this.arffPath));
            instances = new Instances(arffReader);
            instances.setClassIndex(instances.numAttributes() - 1);
            Classifier model = (Classifier) weka.core.SerializationHelper
                    .read(Constants.MODEL_FILE);

            // Test the model
            Evaluation test = new Evaluation(instances);
            double[] predictions = test.evaluateModel(model, instances);

            boolean[] isLaughterList = this.merge(predictions, isPresentList);

            System.out.println("Laughter time frames:");
            int start, end;

            for (int i = 0; i < isLaughterList.length; i++) {

                if (isLaughterList[i]) {
                    start = i;
                    while (isLaughterList[i]) {
                        i++;
                    }
                    end = i;
                    if (end != start + 1) {
                        end--;
                    }
                    long windowSize = Constants.WINDOW_SIZE_IN_MS;
                    laughtersInMilliSecs.add(new long[] { start * windowSize,
                            end * windowSize });
                    System.out.println(getDisplayTime(start * windowSize)
                            + " to " + getDisplayTime(end * windowSize));
                }
            }

            arffReader.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return laughtersInMilliSecs;
    }

    /**
     * The WEKA instances parsed from the ARFF file.
     *
     * @return - The instances.
     */
    public Instances getInstances() {
        return this.instances;
    }

    /**
     * The list of boolean value indicating if the instances in the ARFF file
     * was returned as a laughter segment.
     *
     * @return The list of boolean value indicating if the segment was returned
     *         as laughter.
     */
    public List<Boolean> isPresentList() {
        return this.isPresentList;
    }

    /**
     * Merges the predications with existing instances.
     *
     * @param predictions
     *            - The predictions.
     * @param isPresentList
     *            - List indicating if the original instances were returned by
     *            the model as laughter.
     * @return - Merged instances.
     */
    private boolean[] merge(double[] predictions, List<Boolean> isPresentList) {
        boolean[] isLaughterList = new boolean[isPresentList.size()];
        int predictionsIndex = 0;

        for (int i = 0; i < isLaughterList.length; i++) {
            if (isPresentList.get(i)) {
                if (predictions[predictionsIndex] == 0) {
                    isLaughterList[i] = true;
                }
                predictionsIndex++;
            }
        }

        return isLaughterList;
    }

    /**
     * Produces a list of booleans for each instance from the test set
     * indicating if they were identified as laughter.
     *
     * @param indexFileName
     *            - The index file name.
     * @return - List of booleans indicating if an instance is laughter.
     */
    private List<Boolean> getIsPresentList(String indexFileName) {
        List<Boolean> isPresentList = new ArrayList<Boolean>();
        BufferedReader indexFileReader;
        try {
            indexFileReader = new BufferedReader(new FileReader(indexFileName));
            String line;
            while ((line = indexFileReader.readLine()) != null) {
                isPresentList.add(line.trim().equalsIgnoreCase("YES") ? true
                        : false);
            }
            indexFileReader.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return isPresentList;
    }

    /**
     * Get the human readable display time as a string.
     *
     * @param timeInMilliseconds
     *            - duration in milliseconds
     * @return - Duration as a human readable string.
     */
    private String getDisplayTime(long timeInMilliseconds) {
        String displayTime = "";
        double seconds = timeInMilliseconds / 1000.0;
        int minute = (int) (seconds / 60);
        double second = seconds % 60;
        displayTime = minute + ":" + String.format("%.3f", second);

        return displayTime;
    }
}
