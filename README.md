# Laugh Finder
A machine learning program for human laughter detection.

## Setup
1. Install Python 3.4.3 or later
2. Install Python's [SciPy package](http://www.scipy.org/) and related [NumPy package](http://www.numpy.org/)
3. Install [WEKA](http://www.cs.waikato.ac.nz/ml/weka/)
4. Install NodeJS (work automated model generation)

## Using the Programs

### Training

#### Generate .arff File for WEKA
1. Run *main.py* under the training folder
2. In the GUI, click "Browse" next to "File with laughter audio file names"
3. Select the file containing a series of paths to laughter training files
4. Click "Browse" next to "File with non-laughter audio file names"
5. Select the file containing a series of paths to non-laughter training files
6. Click "Prepare" to generate a *wekaFile.arff* file within the same directory as *main.py*

#### Using WEKA
1. Run WEKA using the following command:
  - `java -Xmx512m -classpath /location/of/weka/jar/weka.jar weka.gui.GUIChooser`
2. In the GUI, click the "Explorer" button
3. Under the "Preprocess" tab, click the "Open file..." button
4. Select the **wekaFile.arff** file generated earlier
5. Under the "Classify" tab, select "Choose" under "Classifier"
6. To use K-nearest neigbors algorithm: under "lazy", select "IBk"
7. Press "Start"
8. After the classification is finished, right-click the output in "Result List" and click "Save Model"

##### In Progress: WEKA automated
Within `weka-automation/` directory, run `npm install`.

### Testing

#### Python
To be explained

#### Java
1. Change file paths in *Constants.java* accordingly, relative to the values of your machine
2. Compile and run the *WavReader* main class (include the *weka.jar* to your classpath)
3. Run the main Java file
4. In the GUI, select the audio file containig the laughter segments to be identified
5. Click the "Get Laughter Segments" button
6. You will be presented with a series of snippets identified as laughter, select each one that is correctly identified
7. Click the retrain model process
8. The re-trained model will be placed in the same directory as the old model (see *Constants.java*)

## Useful Commands
If you are using a Macintosh, you can install `ffmpeg` using [Homebrew](http://brew.sh/)
- `brew install ffmpeg`

Using that application, you can do the following:

- Split a wav file into segments
  - `ffmpeg -i audio.wav -f segment -segment_time 0.5 -c copy parts/out%03d.wav`

- Find the exact duration of a wav file
  - `ffmpeg -i audio.wav 2>&1 | grep Duration | awk '{print $2}' | tr -d ,`

- Cut for a given duration of a wav file
  - `ffmpeg -i audio.wav -ss 00:00:07.100 -t 00:00:01.1 audio.wav`

- Convert .mp4 to .wav
  - `ffmpeg -i sourcefile.mp4 destination.wav`
