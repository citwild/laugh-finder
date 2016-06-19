import unittest
import main
import logging
import logging.config

"""
Test the main python script
"""
class MainTest(unittest.TestCase):

    logging.config.fileConfig("../resources/logging.conf")
    log = logging.getLogger("logger")

    headerOutput = """% Title: MFCC, Energy, Zero Crossing rate Features
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

    def generateWekaHeader_MustContainCorrectFormat(self):
        self.log.info("Testing format of Weka file header")
        self.assertEquals(main.generateWekaHeader(), self.headerOutput)
