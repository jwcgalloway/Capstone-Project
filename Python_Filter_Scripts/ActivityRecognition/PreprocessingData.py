from scipy import stats
import numpy as np
import csv
import pandas as pd
csvFile = "ReachToMouth10.csv"
import matplotlib.pyplot as plt
df = pd.read_csv(csvFile)
listOfDf = []
# print df.iloc[:,3:6]


def separateAndExtract(movements, dataframeList):
    counter = 1
    for num in range(1,movements * 3):
        if(counter == 3):
            counter = 0
            dataframeList.append((df.iloc[:,num-3:num]).dropna())
        counter += 1
    return dataframeList


separateAndExtract(10,listOfDf)


def createFeatures(dataframeList):
    featureList = []
    accum = []
    axises = [0,1,2]
    for frames in dataframeList:
        for axis in axises:
            subset = frames.ix[:,axis]
            var = np.var(subset)
            std = np.std(subset)
            skew = stats.skew(subset)
            min = np.min(subset)
            max = np.max(subset)
            avg = np.average(subset)
            if axis == 2:
                featureAxis = "{0},{1},{2},{3},{4},{5}".format(var, std, skew, min, max, avg)
            else:
                featureAxis = "{0},{1},{2},{3},{4},{5},".format(var, std, skew, min, max, avg)
            accum.append(featureAxis)

        featureList.append(accum)
        accum = []

    return featureList



featureList = createFeatures(listOfDf)
print featureList