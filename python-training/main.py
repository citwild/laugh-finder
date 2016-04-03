__author__ = 'Shalini Ramachandra'

from View import View
from MatchTimeConvertor import MatchConverter
from Weka_MatchGenerator_Local import LableGenerator
from file_feature_extraction import file_feature_extraction

def createWekaFile(features, labels, fileName):
    with open(fileName, 'w+') as wekaFile:
        wekaFile.write(generateWekaHeader() + "\n\n")
        positive = 0
        negative = 0
        filtered = 0
        positives_filtered = 0
        for x in range(len(features)):
            wekaFile.write(' '.join(str(val) for val in features[x]) + " " + labels[x] + "\n")

    print("Total: ", len(features))
    print("+ves: ", positive)
    print("-ves: ", negative)
    print("Filtered: ", filtered)
    print("+ves Filtered: ", positives_filtered)

def generateWekaHeader():


    header = """% Title: MFCC, Energy, Zero Crossing rate Features
    %
    % Creator: Shalini Ramachandra
    % Date: 10/30/2015
    %
    @relation Laughter_detection_capture_training

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

    return header;

def showTestModel(laughterFileName, nonLaughterFileName):
    print(laughterFileName)
    print(nonLaughterFileName)

    laughterFile = open(laughterFileName, 'r')
    nonLaughterFile = open(nonLaughterFileName, 'r')
    labels = []
    featureArray = []
    for line in laughterFile:
        line = line.strip("\n")
        featureArray.append(file_feature_extraction(line))
        labels.append('YES')

    for line in nonLaughterFile:
        line = line.strip("\n")
        featureArray.append(file_feature_extraction(line))
        labels.append('NO')

    #print(labels)
    createWekaFile(featureArray, labels, "wekaFile.arff")
    my_view.showTestModelScreen()


my_view = View(800, 500)

my_view.showPrepareScreen(showTestModel)

my_view.start()


