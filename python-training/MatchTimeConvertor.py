#! /usr/bin/python
#
# Copyright 2014 All Rights Reserved
# Author: Robert Bezirganyan (robbez@uw.edu)

import queue


class MatchConverter:
    def __init__(self, file, offset):
        self.matchFile = file
        self.matchOffset = offset


    def toMilliseconds(self, time):
        totalMilliseconds = 0
        timeComponents = time.split(":")
        hours = int(timeComponents[0])
        minutes = int(timeComponents[1])
        secondsAndMillis = timeComponents[2].split(",")

        seconds = int(secondsAndMillis[0])
        milliseconds = int(secondsAndMillis[1])

        totalMilliseconds = hours * 3600000 + minutes * 60000 + seconds * 1000 + milliseconds

        return totalMilliseconds


    def convert(self):
        print("Convert Called")
        with open(self.matchFile, 'r') as match:

            timesList = queue.Queue()

            for line in match:
                splitLine = line.split("-->");

                if len(splitLine) > 1:
                    startTime = splitLine[0].rstrip();
                    endTime = splitLine[1].rstrip();
                    # print startTime + "---->" + endTime
                    startMills = self.toMilliseconds(startTime) - self.matchOffset
                    endMills = self.toMilliseconds(endTime) - self.matchOffset

                    print(str(startMills) + "\t" + str(endMills))

                    timesList.put([startMills, endMills])

        return timesList










