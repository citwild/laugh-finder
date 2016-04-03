# __author__ = 'Robert'
#
# import os
# from tkinter import *
# from tkinter.filedialog import askopenfilename
#
#
# def select_file(entry):
# filename = askopenfilename()
#     #filePath = os.path.dirname(filename)
#     #print(filePath)
#
#     entry.delete(0, END)
#     entry.insert(0, filename)
#
#     return filename
#
#
#
# def prepare():
#     audioFile = pathEntry.get()
#     matchTextFile = pathEntry2.get()
#
#     print(audioFile)
#     print(matchTextFile)
#     print("done!")
#
#
#
#
#
# root = Tk()
#
# globalFrame = Frame(root, width = "500", height = "500")
# globalFrame.pack(side = LEFT)
#
#
#
# pathEntry = Entry(globalFrame, width=50)
# pathEntry.grid(row = 0, column = 0)
#
# selectFileBtn = Button(globalFrame, text="Browse", command = lambda : select_file(pathEntry))
# selectFileBtn.grid(row = 0, column = 1)
#
# pathEntry2 = Entry(globalFrame, width=50)
# pathEntry2.grid(row = 1, column = 0)
#
# selectFileBtn2 = Button(globalFrame, text="Browse", command = lambda : select_file(pathEntry2))
# selectFileBtn2.grid(row = 1, column = 1)
#
# prepareBtn = Button(globalFrame, text="Prepare", command = prepare)
# prepareBtn.grid(row = 3, column = 0, columnspan = 2)

#root.mainloop()