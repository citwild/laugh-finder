package edu.uw.css595.shalinir;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;

/**
 * @author Shalini Ramachandra #1465487
 *
 */
public class FeedbackIncorporator {

    private Instances feedbackInstances;

    public FeedbackIncorporator(Instances feedbackInstances) {
        this.feedbackInstances = feedbackInstances;
    }

    public void backupAndGenerateArffFile() {
        Utils.backupFileWithTimestamp(Constants.TRAIN_ARFF_FILE);

        BufferedReader arffReader;
        try {
            arffReader = new BufferedReader(new FileReader(
                    Constants.TRAIN_ARFF_FILE));
            Instances existingInstances = new Instances(arffReader);
            existingInstances
                    .setClassIndex(existingInstances.numAttributes() - 1);
            for (int i = 0; i < this.feedbackInstances.numInstances(); i++) {
                existingInstances.add(this.feedbackInstances.instance(i));
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(
                    Constants.TRAIN_ARFF_FILE));
            writer.write(existingInstances.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void backupAndGenerateModelFile() {
        Utils.backupFileWithTimestamp(Constants.MODEL_FILE);

        BufferedReader arffReader;
        try {
            arffReader = new BufferedReader(new FileReader(
                    Constants.TRAIN_ARFF_FILE));
            Instances instances = new Instances(arffReader);
            instances.setClassIndex(instances.numAttributes() - 1);

            Classifier ibk = new IBk(2);
            Evaluation eval = new Evaluation(instances);
            eval.crossValidateModel(ibk, instances, 10, new Random(1));
            ibk.buildClassifier(instances);
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(Constants.MODEL_FILE));
            oos.writeObject(ibk);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
