# Laugh Finder
A machine learning program for human laughter detection.

## Setup
1. Install Python 3.4.3 or later
2. Install Python's [SciPy package](http://www.scipy.org/) and related [NumPy package](http://www.numpy.org/)
3. Install [WEKA](http://www.cs.waikato.ac.nz/ml/weka/)
  - If you want to use SVM classifier you have to:
    - Download libSVM
    - Copy the libsvm.jar into ~/Library/Java/libsvm.jar (assuming you are using a Macintosh
    - Run:
      - `export CLASSPATH="/location/of/weka/jar/weka.jar:/Library/Java/*"`

## Using the Programs

### Training

#### Generate .arff File for WEKA
1. Run *main.py* under the training folder
2. In the GUI, click "Browse" next to "File with laughter audio file names"
3. Select the file containing a series of paths to laughter training files
4. Click "Browse" next to "File with non-laughter audio file names"
5. Select the file containing a series of paths to non-laughter training files 
6. Click "Prepare" to generate a `weka.arff` file within the same directory as *main.py*

#### Using WEKA
1. Run WEKA using the following command:
  - `java -Xmx512m -classpath /location/of/weka/jar/weka.jar:/Library/Java/libsvm.jar weka.gui.GUIChooser`
2. Load the generated `weka.arff` file perform classifications

### Testing
1. Change file paths in *Constants.java* accordingly, relative to the values of your machine
2. Run the main Java file
3. In the GUI, select the audio file containig the laughter segments to be identified
4. Click the "Get Laughter Segments" button
5. You will be presented with a series of snippets identified as laughter, select each one that is correctly identified
6. Click the retrain model process
7. The re-trained model will be placed in the same directory as the old model (see *Constants.java*)

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
