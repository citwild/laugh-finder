from numpy import *
from math import *

def getDFT(signal, Fs, PLOT=None):

    #
    # function [FFT, Freq] = getDFT(signal, Fs, PLOT)
    #
    # This function returns the DFT of a discrete signal and the
    # respective frequency range.
    #
    # ARGUMENTS:
    # - signal: vector containing the samples of the signal
    # - Fs:     the sampling frequency
    # - PLOT:   use this argument if the FFT (and the respective
    #           frequency values) need to be returned in the
    #           [-fs/2..fs/2] range. Otherwise, only half of
    #           the spectrum is returned.
    #
    # RETURNS:
    # - FFT:    the magnitude of the DFT coefficients
    # - Freq:   the corresponding frequencies (in Hz)
    #

    N = len(signal)# length of signal
    # compute the magnitude of the spectrum
    # (and normalize by the number of samples):
    FFT = abs(fft.fft(signal)) / N

    if PLOT == None:    # return the first half of the spectrum:
        FFT = FFT[0 : ceil(N/2)]
        Freq = (Fs/2) * arange(1,ceil(N/2)+2) / (ceil(N/2)+1)   # define the frequency axis
    else:
      # ... or return the whole spectrum
      #     (in the range -fs/2 to fs/2)
      FFT = fftshift(FFT)
      if mod(N, 2) == 0:            # define the frequency axis:
        Freq = arange(-N/2, N/2)    # if N is even
      else:
        Freq = arange(-(N-1)/2, (N-1)/2+1)    # if N is odd
      Freq = (Fs/2) * Freq / ceil(N/2)
    return FFT
