__author__ = 'Rober_001 & Shalini Ramachandra'

from tkinter import *
from tkinter.filedialog import askopenfilename


class PrepareFrame(Frame):
    def __init__(self, parent, onPrepare):
        self.onPrepare = onPrepare

        Frame.__init__(self, parent)

        self.audioEntryLabel = Label(self, text="File with laughter audio file names:")
        self.audioEntryLabel.grid(row=0, column=0)
        self.audioEntry = Entry(self, width=50)
        self.audioEntry.grid(row=0, column=1)
        self.selectAudioBtn = Button(self, text="Browse", command=lambda: self.select_file(self.audioEntry))
        self.selectAudioBtn.grid(row=0, column=2)

        self.matchEntryLabel = Label(self, text="File with non-laughter audio file names:")
        self.matchEntryLabel.grid(row=1, column=0)
        self.matchEntry = Entry(self, width=50)
        self.matchEntry.grid(row=1, column=1)
        self.selectMatch = Button(self, text="Browse", command=lambda: self.select_file(self.matchEntry))
        self.selectMatch.grid(row=1, column=2)

        self.prepareBttn = Button(self, text="Prepare",
                                  command=lambda: self.callOnPrepare())
        self.prepareBttn.grid(row=2, column=1, columnspan=2)

    def select_file(self, entry):
        filename = askopenfilename()

        entry.delete(0, END)
        entry.insert(0, filename)

        return filename

    def callOnPrepare(self):
        self.onPrepare(self.audioEntry.get(), self.matchEntry.get())
