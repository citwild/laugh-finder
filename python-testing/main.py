import argparse
from MatchTimeConvertor import MatchConverter
from Weka_MatchGenerator_Local import LableGenerator
from file_feature_extraction import file_feature_extraction
from get_s3_object import get_wav_file

__author__ = 'Shalini Ramachandra'


"""
Creates a weka file given the features and labels.
"""


def create_weka_file(features, labels, filePath, isTest, isFilterApplied):
    fileName = filePath + "/test.arff"
    originalIndexFileName = filePath + "/index.txt"

    with open(originalIndexFileName, 'w+', encoding="utf-8") as originalIndexFile:
        with open(fileName, 'w+', encoding="utf-8") as wekaFile:
            wekaFile.write(generateWekaHeader() + "\n\n")
            positive = 0
            negative = 0
            filtered = 0
            positives_filtered = 0
            for x in range(len(features)):
                if labels[x] == 'IGNORE':
                    continue

                if isFilterApplied:
                    # write in original index file: index, if filtered ? YES: NO
                    if features[x][-1] == 1.0: # to be filtered
                        is_present = "NO"
                    else:
                        is_present = "YES"
                    originalIndexFile.write(is_present + "\n")
                    if features[x][-1] == 1.0: # to be filtered
                        filtered += 1
                        if labels[x] == 'YES':
                            positives_filtered += 1
                        continue

                if labels[x] == 'NO':
                    if positive < negative and isTest is False:
                        continue
                    negative += 1
                else:
                    positive += 1
                wekaFile.write(' '.join(str(val) for val in features[x][:-1]) + " " + labels[x] + "\n")

    print("Total: ", len(features))
    print("Filtered: ", filtered)

"""
Generates and returns the WEKA file header as a string.
"""
def generateWekaHeader():

    """
    % Title: MFCC, Energy, Zero Crossing rate Features
    %
    % Creator: Shalini Ramachandra
    % Date: 10/30/2015
    %
    """

    header = """@relation Laughter_detection_capture_training

@attribute MFCC1 NUMERIC
@attribute MFCC2 NUMERIC
@attribute MFCC3 NUMERIC
@attribute MFCC4 NUMERIC
@attribute MFCC5 NUMERIC
@attribute MFCC6 NUMERIC
@attribute MFCC7 NUMERIC
@attribute MFCC8 NUMERIC
@attribute MFCC9 NUMERIC
@attribute MFCC10 NUMERIC
@attribute MFCC11 NUMERIC
@attribute MFCC12 NUMERIC
@attribute MFCC13 NUMERIC
@attribute ENERGY NUMERIC
@attribute ZCR NUMERIC
@attribute ENERGY_ENTROPY NUMERIC
@attribute SPECTRAL_CENTROID NUMERIC
@attribute SPECTRAL_SPREAD NUMERIC
@attribute SPECTRAL_ENTROPY NUMERIC
@attribute SPECTRAL_ROLLOFF NUMERIC
@attribute class {YES,NO}

@data"""

    #@attribute SPECTRAL_FLUX NUMERIC

    return header

"""
Main method that extracts the features and produces the ARFF file.
"""


def showTestModel(bucket, key, arffFilePath, matchFile=None, windowSize=800, stepSize=100, isTest=True, amplitudeFilter=True, diffFilter=False):
    params = 'windowSize: %s, stepSize: %s, isTest: %s, applyFilter: %s, diffFilter: %s' % (windowSize, stepSize, isTest, amplitudeFilter, diffFilter)
    print(params)

    # get audio file from S3
    audio_file = get_wav_file(bucket, key)

    window_size_in_sec = int(windowSize) / 1000.0
    step_size_in_sec = (int(stepSize) / 100.0) * window_size_in_sec
    print("Window Size (in sec): ", window_size_in_sec)
    print("Step Size (in sec): ", step_size_in_sec)

    # get features from WAV file
    feature_array = file_feature_extraction(audio_file, window_size_in_sec, step_size_in_sec, amplitudeFilter, diffFilter)

    if matchFile != None:
        my_matchConverter = MatchConverter(matchFile, 0)
        timesList = my_matchConverter.convert()

        my_labelGenerator = LableGenerator(timesList, len(feature_array), int(windowSize), isTest)
        labels = my_labelGenerator.generate()
    else:
        labels = ["NO"] * len(feature_array)

    isFilterApplied = amplitudeFilter or diffFilter
    create_weka_file(feature_array, labels, arffFilePath, isTest, isFilterApplied)

"""
my_view.showTestModelScreen()

my_view = View(800, 500)

my_view.showPrepareScreen(showTestModel)

my_view.start()
"""
parser = argparse.ArgumentParser(description='Arguments to generate ARFF file for train/test.')
parser.add_argument('--phase', dest='phase', type=int, help='test = 0 train = 1', required=True)
parser.add_argument('--bucket', dest='bucket', help="The bucket to search for in S3.", required=True)
parser.add_argument('--key', dest='key', help="The key to search for within the bucket.", required=True)
parser.add_argument('--arff', dest='arff', help="Absolute path to the output ARFF to be generated.", required=True)

args = parser.parse_args()

showTestModel(args.bucket, args.key, args.arff)


