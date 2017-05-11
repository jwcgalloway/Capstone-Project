from scipy import stats
import numpy as np
import csv
import pandas as pd
csvFile = "walking.csv"
import matplotlib.pyplot as plt
df = pd.read_csv(csvFile)
listOfDf = []
# print df.iloc[:,3:6]
TRAINING = "train.csv"
TEST = "test.csv"
OUTPUT_NAME = "Features3.csv"
ACTIVITY = "4"

#r2m: 1 RAR: 2 WR: 3 WALKING: 4

def separateAndExtract(csvFile):
    with open(csvFile, 'rb') as f:
        reader = csv.reader(f)
        dataList = list(reader)
        return dataList

testData = separateAndExtract(csvFile)


def splitList(list):
    splitUpList = []
    counter = 0
    for items in range(0,len(list),1):
        if list[items][0] =='x' or items == len(list)-1:
            if items != 0:
                splitUpList.append(list[items-counter:items])
                counter = 0
        counter += 1
    return splitUpList

listSplit = splitList(testData)

def createDataFrames(listSplit):
    dataframeList = []
    for movements in listSplit:
        dataframeList.append(pd.DataFrame(movements,columns = movements.pop(0)).astype(float))
    return dataframeList

listOfDf = createDataFrames(listSplit)

print listOfDf

def createFeatures(dataframeList):
    featureList = []
    axises = [0,1,2]
    for frames in dataframeList:
        accum = ""
        for axis in axises:
            subset = frames.ix[0:,axis]
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
            accum += featureAxis
        featureList.append(accum)
        accum = ""



    return featureList



featureList = createFeatures(listOfDf)
labels = "varx, stdx, skewx, minx, maxx , avgx,vary,stdy,skewy,miny,maxy,avgy,varz,stdz,skewz,minz,maxz,avgz,CLASS \n"

with open(OUTPUT_NAME, "a") as text_file:
    text_file.write(labels)
    for rows in featureList:
        text_file.write(rows + "," + ACTIVITY + "\n")





