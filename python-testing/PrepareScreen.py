__author__ = 'Rober_001'

from tkinter import *
from tkinter.filedialog import askopenfilename

"""
Prepares the UI frame.
"""
class PrepareFrame(Frame):
    def __init__(self, parent, onPrepare):
        self.onPrepare = onPrepare

        Frame.__init__(self, parent)

        self.audioEntryLabel = Label(self, text="Audio File:")
        self.audioEntryLabel.grid(row=0, column=0)
        self.audioEntry = Entry(self, width=50)
        self.audioEntry.grid(row=0, column=1)
        self.selectAudioBtn = Button(self, text="Browse", command=lambda: self.select_file(self.audioEntry))
        self.selectAudioBtn.grid(row=0, column=2)

        self.matchEntryLabel = Label(self, text="Match File:")
        self.matchEntryLabel.grid(row=1, column=0)
        self.matchEntry = Entry(self, width=50)
        self.matchEntry.grid(row=1, column=1)
        self.selectMatch = Button(self, text="Browse", command=lambda: self.select_file(self.matchEntry))
        self.selectMatch.grid(row=1, column=2)

        # multiple audio and text label file inputs
        self.multiFileLabel = Label(self, text="File with list of audio and match files:")
        self.multiFileLabel.grid(row=2, column=0)
        self.multiFilePath = Entry(self, width=50)
        self.multiFilePath.grid(row=2, column=1)
        self.multiFileBrowse = Button(self, text="Browse", command=lambda: self.select_file(self.multiFilePath))
        self.multiFileBrowse.grid(row=2, column=2)

        self.windowSizeLabel = Label(self, text="Window Size:")
        self.windowSizeLabel.grid(row=3, column=0)
        self.windowSizeEntry = Entry(self, width=4)
        self.windowSizeEntry.grid(row=3, column=1, columnspan=2,  sticky=W)

        self.stepSizeLabel = Label(self, text="Step Size (%):")
        self.stepSizeLabel.grid(row=4, column=0)
        self.stepSizeEntry = Entry(self, width=4)
        self.stepSizeEntry.grid(row=4, column=1, columnspan=2,  sticky=W)

        self.isTest = IntVar()
        self.isTestCheckbox = Checkbutton(self, text="Test data", variable=self.isTest)
        self.isTestCheckbox.grid(row=5, column=1, columnspan=2, sticky=W)

        self.amplitudeFilter = IntVar()
        self.amplitudeFilterCheckbox = Checkbutton(self, text="Apply Amplitude Filter", variable=self.amplitudeFilter)
        self.amplitudeFilterCheckbox.grid(row=6, column=1, columnspan=2, sticky=W)

        self.diffFilter = IntVar()
        self.diffFilterCheckbox = Checkbutton(self, text="Apply Repeat Audio Filter", variable=self.diffFilter)
        self.diffFilterCheckbox.grid(row=7, column=1, columnspan=2, sticky=W)

        self.prepareBttn = Button(self, text="Prepare",
                                  command=lambda: self.callOnPrepare())
        self.prepareBttn.grid(row=8, column=1, columnspan=2)

    def select_file(self, entry):
        filename = askopenfilename()

        entry.delete(0, END)
        entry.insert(0, filename)

        return filename

    def callOnPrepare(self):
        if (self.audioEntry.get()):
            self.onPrepare(self.audioEntry.get(), self.matchEntry.get(),
                           self.windowSizeEntry.get(), self.stepSizeEntry.get(),
                           self.isTest.get() == 1, self.amplitudeFilter.get() == 1, self.diffFilter.get() == 1)
        else:
            self.onPrepare(self.multiFilePath.get(), None,
                           self.windowSizeEntry.get(), self.stepSizeEntry.get(),
                           self.isTest.get() == 1, self.amplitudeFilter.get() == 1, self.diffFilter.get() == 1)

