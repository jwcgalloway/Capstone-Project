#min, max, variance, mean
from scipy import stats
import numpy as np
import csv
from PreprocessingData import *
FREQUENCY = 8
EPOCH = 3
SAMPLES = EPOCH * FREQUENCY
ACTIVITY = "3"
DATASET = "walking.csv"
FILTEREDSET = "filteredSets.csv"
OUTPUT_NAME = "randomTestFeatures.csv"
data = np.loadtxt("randomTest.csv",delimiter= ",")






x = data[:, 0]
y = data[:, 1]
z = data[:, 2]
axises = [x, y, z]

output = []
counter = 0
xyz = []

for points in range(0, len(x) - 1):
    if counter == int(SAMPLES):
        accum = ""
        zCounter = 0
        for axis in axises:
            counter = 0
            zCounter += 1
            slice = points - SAMPLES
            subset = axis[slice:points]
            var = np.var(subset)
            std = np.std(subset)
            skew = stats.skew(subset)
            min = np.min(subset)
            max = np.max(subset)
            avg = np.average(subset)
            if(zCounter % 3 == 0):
                featureAxis = "{0},{1},{2},{3},{4},{5}".format(var, std, skew, min, max, avg)
            else:
                featureAxis = "{0},{1},{2},{3},{4},{5},".format(var, std, skew, min, max, avg)
            accum += featureAxis
        xyz.append(accum)
        accum = ""
    counter += 1

print xyz
print np.array(xyz)


#Write to TExtfile
labels = "varx, stdx, skewx, minx, maxx , avgx,vary,stdy,skewy,miny,maxy,avgy,varz,stdz,skewz,minz,maxz,avgz,CLASS \n"


# with open(OUTPUT_NAME, "a") as text_file:
#     text_file.write(labels)
#     for rows in xyz:
#         text_file.write(rows + "," + ACTIVITY + "\n")
#
