#! /usr/bin/python
#
# Copyright 2014 All Rights Reserved
# Author: Robert Bezirganyan (robbez@uw.edu)

"""
Produces the label based on the match queue.
"""
class LableGenerator:
    def __init__(self, matchQueue, totalRows, timeInterval, isTest):
        self.matchQueue = matchQueue
        self.totalRows = totalRows
        self.timeInterval = timeInterval
        self.isTest = isTest
        self.counter = 0
        self.match = None
        self.nextLabel = 'NO'


    def generate(self):

        self.match = self.matchQueue.get()
        labels = [self.nextLabel] * self.totalRows


        for row in range(self.totalRows):
            windowStart = self.counter
            windowEnd = self.counter + self.timeInterval
            if windowStart >= self.match[0] and windowEnd <= self.match[1]:
                self.nextLabel = 'YES'
            elif windowEnd <= self.match[0]:
                self.nextLabel = 'NO'
            else:
                if self.isTest:
                    self.nextLabel = 'YES'
                else:
                    self.nextLabel = 'IGNORE'

            labels[row] = self.nextLabel

            self.counter += self.timeInterval
            if self.counter > self.match[1]:
                if not self.matchQueue.empty():
                    self.match = self.matchQueue.get()
                else:
                    break

        return labels