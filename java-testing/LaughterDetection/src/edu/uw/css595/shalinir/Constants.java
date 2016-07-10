package edu.uw.css595.shalinir;

/**
 * The Constants used in the application.
 *
 * @author Shalini Ramachandra
 */
public class Constants {

    public static final long WINDOW_SIZE_IN_MS = 800;
    // location of where all the outputs/inputs can be found
    public static final String TEST_DIR = "/Users/milesdowe/Documents/workspace/school/lf-output/";
    // .arff file from training
    public static final String TEST_ARFF_FILE = TEST_DIR + "wekaFile.arff";
    // file to write test results
    public static final String TEST_INDEX_FILE = TEST_DIR + "index.txt";
    // Location of python testing script
    public static final String PYTHON_MAIN_SCRIPT_PATH = "/Users/milesdowe/Documents/workspace/school/laugh-finder/python-testing/main.py";
    // .arff file resulting from testing
    public static final String TRAIN_ARFF_FILE = TEST_DIR + "wekaFile.arff";
    // the model to be read and modified
    public static final String MODEL_FILE = TEST_DIR + "laugh.model";

}
