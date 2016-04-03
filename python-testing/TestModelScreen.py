__author__ = 'Rober_001'

from tkinter import *
from tkinter.filedialog import askopenfilename

"""
Displays the test model frame.
"""
class TestModelFrame(Frame):
    def __init__(self, parent):
        Frame.__init__(self, parent)
        self.pathEntry = Entry(self, width=50)
        self.pathEntry.grid(row=0, column=0)
        self.selectFileBtn = Button(self, text="Browse Model", command=lambda: self.select_file(self.pathEntry))
        self.selectFileBtn.grid(row=0, column=1)

    def select_file(self, entry):
        filename = askopenfilename()

        entry.delete(0, END)
        entry.insert(0, filename)

        return filename
