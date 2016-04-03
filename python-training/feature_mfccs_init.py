from math import *
from numpy import *

def feature_mfccs_init(windowLength, fs):
    # function mfccParams , feature_mfccs_init(windowLength, fs)
    #
    # This function is used to initalize the mfcc quantities
    # used in the MFCC calculation
    #
    # ARGUMENTS:
    # - windowLength: the length of the window analysis (in number of samples)
    # - fs:         : the sampling frequency (in Hz)
    #
    # RETURNS:
    # - mfccParams  : returns a structure with the mfcc params:
    #
    # (c) 2014 T= Giannakopoulos, A= Pikrakis

    mfccParams = dict()
    # number of cepstral coefficients:
    mfccParams = { 'cepstralCoefficients' : 13, 'fftSize': int(round(float_(windowLength) / 2)) }
    # filter parameters:
    mfccParams.update({ 'lowestFrequency': 133.3333, 'linearFilters': 13, 'linearSpacing': 66.66666666, 'logFilters': 27, 'logSpacing': 1.0711703 })
    totalFilters = mfccParams['linearFilters'] + mfccParams['logFilters']
    mfccParams.update({ 'totalFilters':  totalFilters })

    freqs = []
    for i in range(0, mfccParams['linearFilters']):
        freqs.append(mfccParams['lowestFrequency'] + i * mfccParams['linearSpacing'])

    log = []
    for i in range(1, mfccParams['logFilters'] + 3):
        log.append(pow(mfccParams['logSpacing'], i))
    for i in log:
        freqs.append(freqs[mfccParams['linearFilters'] - 1] * i)
    mfccParams.update({ 'freqs': freqs })

    lower = []
    for i in range(0, mfccParams['totalFilters']):
        lower.append(freqs[i])
    mfccParams.update({ 'lower': lower })

    center = []
    for i in range(1, mfccParams['totalFilters'] + 1):
        center.append(freqs[i])
    mfccParams.update({ 'center': center })

    upper = []
    for i in range(2, mfccParams['totalFilters'] + 2):
        upper.append(freqs[i])
    mfccParams.update({ 'upper': upper })
    weights = zeros((mfccParams['totalFilters'], mfccParams['fftSize']))

    triangleHeight = []
    for i in range(0, len(upper)):
        triangleHeight.append(2 / (upper[i] - lower[i]))
    mfccParams.update({ 'triangleHeight': triangleHeight })

    fftFreqs = []
    for i in range(0, mfccParams['fftSize']):
        fftFreqs.append(float_(i) / mfccParams['fftSize'] * fs)
    mfccParams.update({ 'fftFreqs': fftFreqs })

    for chan in range(0, mfccParams['totalFilters']): # for each filter:
        # compute the respective filter weights:
        for i in range(0, len(fftFreqs)):
            a = 1 if (fftFreqs[i] > lower[chan]) else 0
            b = 1 if (fftFreqs[i] <= center[chan]) else 0
            c = (a & b) * triangleHeight[chan]
            d = fftFreqs[i] - lower[chan]
            e = center[chan] - lower[chan]

            partOneResult = c * d / e

            a2 = 1 if (fftFreqs[i] > center[chan]) else 0
            b2 = 1 if (fftFreqs[i] < upper[chan]) else 0
            c2 = (a2 & b2) * triangleHeight[chan]
            d2 = upper[chan] - fftFreqs[i]
            e2 = upper[chan] - center[chan]

            partTwoResult = c2 * d2 / e2

            weights[chan][i] = partOneResult + partTwoResult
    mfccParams.update({ 'mfccFilterWeights': weights})

    # matrix used in the DCT calculation:
    vector = empty( (mfccParams['cepstralCoefficients'],1) )
    for i in range(0, mfccParams['cepstralCoefficients']):
        vector[i][0] = i
    array = empty( (1, mfccParams['totalFilters']) )
    for i in range(0, mfccParams['totalFilters']):
        array[0][i] = (2 * i + 1)
    matrix = 1/sqrt(mfccParams['totalFilters']/2)  * cos(vector * array * pi/2/mfccParams['totalFilters'])
    for i in range(0, len(array[0])):
        matrix[0][i] = matrix[0][i] * sqrt(2)/2
    mfccParams.update({ 'mfccDCTMatrix': matrix })
    return mfccParams
