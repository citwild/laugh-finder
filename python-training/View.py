__author__ = 'Rober_001'

from tkinter import *

from PrepareScreen import PrepareFrame
from TestModelScreen import TestModelFrame


class View:
    def __init__(self, width, height):
        print(str(width) + " : " + str(height))
        self.root = Tk()
        self.root.title("Training process")
        self.root.geometry(str(width) + "x" + str(height))
        self.currFrame = None

    def start(self):
        self.root.mainloop()
        print("starting loop")

    def stop(self):
        self.root.destroy()

    def get_root(self):
        return self.root

    def showPrepareScreen(self, onPrepare):
        if self.currFrame is not None:
            self.currFrame.destroy()
            self.currFrame.pack_forget()
        self.currFrame = PrepareFrame(self.root, onPrepare)
        self.currFrame.pack()

    def showTestModelScreen(self):
        if self.currFrame is not None:
            self.currFrame.destroy()
            self.currFrame.pack_forget()
        self.currFrame = TestModelFrame(self.root)
        self.currFrame.pack()
