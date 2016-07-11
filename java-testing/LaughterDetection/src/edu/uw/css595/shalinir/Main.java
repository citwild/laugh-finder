package edu.uw.css595.shalinir;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import weka.core.Instance;
import weka.core.Instances;
import edu.uw.css595.shalinir.Utils.FeedbackLabel;
import edu.uw.css595.shalinir.player.SwingAudioPlayer;

/**
 * This is the main class for the application. It inherits from JFrame in order
 * to display the main window.
 *
 * @author Shalini Ramachandra
 */
public class Main extends JFrame {

    private JPanel topPanel;
    private JPanel bottomPanel;
    private JButton getLaughterSegments;
    private static SwingAudioPlayer player;

    private ImageIcon iconPlay = new ImageIcon(getClass().getResource(
            "/images/Play.gif"));

    private static FeedbackLabel[] feedback;

    /**
     * Create the GUI
     */
    public Main() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Laughter Detection");

        // Create Grid layout, create top panel and its layout
        setLayout(new FlowLayout());

        // Bottom Panel
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        // Top Panel
        topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        player = new SwingAudioPlayer();
        getLaughterSegments = new JButton("Get Laughter Segments");
        getLaughterSegments.setFont(new Font("Sans", Font.BOLD, 16));
        getLaughterSegments.addActionListener(new GetLaughterSegmentsListener(
                this, bottomPanel));
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20,
                5));
        panelButtons.setPreferredSize(new Dimension(1200, 100));
        panelButtons.add(getLaughterSegments);
        topPanel.add(player);
        topPanel.add(panelButtons);

        // Main Frame
        add(topPanel);
        add(bottomPanel);

        setSize(1300, 900);
    }

    /**
     * The listener that is invoked when Get Laughter Segments button is
     * clicked. Implements the ActionListener.
     */
    public class GetLaughterSegmentsListener implements ActionListener {
        private JFrame frame;
        private JPanel bottomPanel;

        /**
         * Initializes a new instance of GetLaughterSegmentsListener
         *
         * @param frame
         *            - The frame for displaying the message box.
         * @param bottomPanel
         *            - The bottom panel in which the laughter segments are to
         *            be displayed.
         */
        public GetLaughterSegmentsListener(JFrame frame, JPanel bottomPanel) {
            this.frame = frame;
            this.bottomPanel = bottomPanel;
        }

        /**
         * The method that is invoked when the button is clicked. It calls the
         * python code for feature extraction and displays the output.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String audioFilePath = player.getAudioFilePath();
            if (audioFilePath == null || audioFilePath.trim().length() == 0) {
                JOptionPane.showMessageDialog(frame,
                        "Please select a file first.");
                return;
            }

            bottomPanel.removeAll();
            Utils.backupFileWithTimestamp(Constants.TEST_ARFF_FILE);
            Utils.backupFileWithTimestamp(Constants.TEST_INDEX_FILE);
            String command = "/usr/local/bin/python3 "
                    + Constants.PYTHON_MAIN_SCRIPT_PATH + " --audio "
                    + audioFilePath + " --arff " + Constants.TEST_DIR
                    + " --phase 0";
            Runtime runTime = Runtime.getRuntime();
            try {
                Process process = runTime.exec(command);
                // retrieve output from Python script
                BufferedReader bfr = new BufferedReader(new InputStreamReader(
                        process.getInputStream()));
                String line;
                while ((line = bfr.readLine()) != null) {
                    // display each output line from Python script
                    System.out.println(line);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            String wekaFileName = Constants.TEST_ARFF_FILE;
            TestEngine engine = new TestEngine(wekaFileName);
            List<long[]> laughtersInMilliSecs = engine.getLaughters();
            Instances instances = engine.getInstances();
            List<Boolean> isPresentList = engine.isPresentList();
            feedback = new FeedbackLabel[laughtersInMilliSecs.size()];

            this.bottomPanel.setLayout(new FlowLayout());
            JPanel laughterGridPanel = new JPanel();
            laughterGridPanel.setLayout(new GridLayout(laughtersInMilliSecs
                    .size() / 3 + 1, 3, 5, 5));

            for (int i = 0; i < laughtersInMilliSecs.size(); i++) {
                long start = laughtersInMilliSecs.get(i)[0];
                long end = laughtersInMilliSecs.get(i)[1];
                String durationText = Utils.displayTime(start) + " - "
                        + Utils.displayTime(end);

                JCheckBox laughterCheckbox = new JCheckBox("L");
                laughterCheckbox
                        .addActionListener(new FeedbackLabelCheckboxHandler(i,
                                true));
                JCheckBox nonLaughterCheckbox = new JCheckBox("NL");
                nonLaughterCheckbox
                        .addActionListener(new FeedbackLabelCheckboxHandler(i,
                                false));
                JLabel durationLabel = new JLabel(durationText);
                JButton button = new JButton("Play");
                button.setFont(new Font("Sans", Font.BOLD, 14));
                button.setIcon(iconPlay);
                button.addActionListener(new PlayButton(start, end));

                JPanel panelForOneEntry = new JPanel(new FlowLayout(
                        FlowLayout.CENTER, 20, 5));
                panelForOneEntry.add(laughterCheckbox);
                panelForOneEntry.add(nonLaughterCheckbox);
                panelForOneEntry.add(durationLabel);
                panelForOneEntry.add(button);
                laughterGridPanel.add(panelForOneEntry);
            }

            JCheckBox balanceCheckbox = new JCheckBox("Balance instances");

            JButton retrainButton = new JButton("Re-Train Model");
            retrainButton.setFont(new Font("Sans", Font.BOLD, 16));
            retrainButton.addActionListener(new RetrainButtonListener(
                    laughtersInMilliSecs, instances, isPresentList,
                    balanceCheckbox, this.bottomPanel, this.frame));
            JPanel panelRetrainControls = new JPanel(new FlowLayout(
                    FlowLayout.CENTER, 20, 5));
            panelRetrainControls.add(balanceCheckbox);
            panelRetrainControls.add(retrainButton);

            // Scroll bar in laughter segments grids
            JScrollPane scrollPane = new JScrollPane(laughterGridPanel);
            scrollPane
                    .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane
                    .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setPreferredSize(new Dimension(1250, 500));

            this.bottomPanel.setLayout(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 6;
            this.bottomPanel.add(scrollPane, constraints);

            constraints.gridy = 1;
            constraints.gridwidth = 6;
            this.bottomPanel.add(panelRetrainControls, constraints);

            this.frame.revalidate();
        }
    }

    /**
     * The listener that is invoked when the play button is clicked for each of
     * the laughter segments identified.
     *
     * @author Shalini Ramachandra
     *
     */
    public class PlayButton implements ActionListener {
        private long startInMs;
        private long endInMs;

        /**
         * Initializes a new instance of type PlayButton.
         *
         * @param start
         *            - The start time of the segment.
         * @param end
         *            - The end time of the segment.
         */
        public PlayButton(long start, long end) {
            this.startInMs = start;
            this.endInMs = end;
        }

        /**
         * Calls the player in order to play between the time range required.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            player.playBetween(startInMs, endInMs);
        }
    }

    /**
     * This class handles the action for the check box which determines if the
     * user has selected the instance as laughter or non-laughter
     *
     * @author Shalini Ramachandra #1465487
     *
     */
    private class FeedbackLabelCheckboxHandler implements ActionListener {
        private int feedbackIndex;
        private boolean isLaughter;

        /**
         * Initializes a new instance of FeedbackLabelCheckboxHandler
         *
         * @param feedbackIndex
         *            - The index for the segment.
         * @param isLaughter
         *            - Value indicating if the check box is laughter or
         *            non-laughter
         */
        public FeedbackLabelCheckboxHandler(int feedbackIndex,
                boolean isLaughter) {
            this.feedbackIndex = feedbackIndex;
            this.isLaughter = isLaughter;
        }

        /**
         * This method handles the action performed for the check box.
         */
        public void actionPerformed(ActionEvent e) {
            JCheckBox checkbox = (JCheckBox) e.getSource();
            if (checkbox.isSelected()) {
                feedback[this.feedbackIndex] = new FeedbackLabel(
                        this.isLaughter);
            } else {
                feedback[this.feedbackIndex] = null;
            }
        }
    }

    /**
     * Listener for the retrain button for submitting human feedback.
     *
     * @author Shalini Ramachandra
     *
     */
    private class RetrainButtonListener implements ActionListener {
        private List<long[]> laughtersInMilliSecs;
        private Instances instances;
        private List<Boolean> isPresentList;
        private JCheckBox balanceCheckbox;
        private JPanel bottomPanel;
        private JFrame frame;

        /**
         * Initializes a new instance of RetrainButtonListener.
         *
         * @param laughtersInMilliSecs
         *            The laughter result produced by testing.
         * @param instances
         *            - The instances that are to be added to previous training
         *            model.
         */
        public RetrainButtonListener(List<long[]> laughtersInMilliSecs,
                Instances instances, List<Boolean> isPresentList,
                JCheckBox balanceCheckbox, JPanel bottomPanel, JFrame frame) {
            this.laughtersInMilliSecs = laughtersInMilliSecs;
            this.instances = instances;
            this.isPresentList = isPresentList;
            this.balanceCheckbox = balanceCheckbox;
            this.bottomPanel = bottomPanel;
            this.frame = frame;
        }

        /**
         * Invoked when the button is clicked. Grabs the new instances that were
         * labelled by the user and combines them with the previous train.arff
         * file in order to produce a new train.arff and train.model files.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Instances retrainInstances = new Instances(this.instances, 0);
            Instances noInstances = new Instances(this.instances, 0);

            Map<Integer, Boolean> indexToIsLaughter = new HashMap<Integer, Boolean>();
            for (int i = 0; i < this.laughtersInMilliSecs.size(); i++) {
                long startInMs = this.laughtersInMilliSecs.get(i)[0];
                long endInMs = this.laughtersInMilliSecs.get(i)[1];
                int startIndex = (int) (startInMs / Constants.WINDOW_SIZE_IN_MS);
                int endIndex = (int) (endInMs / Constants.WINDOW_SIZE_IN_MS);
                for (int j = startIndex; j < endIndex; j++) {
                    FeedbackLabel label = feedback[i];
                    if (label != null) {
                        indexToIsLaughter.put(j, label.isLaugher());
                    }
                }
            }

            Instance instance;
            int instanceIndex = 0;
            for (int i = 0; i < this.isPresentList.size(); i++) {
                if (this.isPresentList.get(i)) {
                    instance = this.instances.instance(instanceIndex);
                    if (indexToIsLaughter.containsKey(i)) {
                        instance.setClassValue(indexToIsLaughter.get(i) ? "YES" : "NO");
                        if (indexToIsLaughter.get(i)) {
                            retrainInstances.add(instance);
                        } else {
                            noInstances.add(instance);
                        }
                    }

                    instanceIndex++;
                }
            }

            int yesCount = retrainInstances.numInstances();
            int noIncrement = 1;

            // Set noIncrement appropriately if balancing is selected.
            if (this.balanceCheckbox.isSelected()) {
                // FIXME: divide by zero error if processing no laughter instances
                noIncrement = noInstances.numInstances() / yesCount;

                // Making sure noIncrement is not zero when number of no
                // instances <
                // yes instances
                if (noIncrement == 0) {
                    noIncrement = 1;
                }
            }

            for (int i = 0; i < noInstances.numInstances(); i += noIncrement) {
                retrainInstances.add(noInstances.instance(i));
            }

            System.out.println("Feedback Instances Total: "
                    + retrainInstances.numInstances());
            System.out.println("YES: " + yesCount + ", NO: "
                    + (retrainInstances.numInstances() - yesCount));

            FeedbackIncorporator feedbackIncorporator = new FeedbackIncorporator(
                    retrainInstances);
            feedbackIncorporator.backupAndGenerateArffFile();
            feedbackIncorporator.backupAndGenerateModelFile();

            JOptionPane.showMessageDialog(null, "Re-training completed."
                    + " Please re-open to use the new model.");
            System.exit(0);
        }
    }

    /**
     * The main function that is invoked.
     * @param args - The command line arguments (not used).
     */
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Main app = new Main();
                app.setVisible(true);
            }
        });
    }

}
