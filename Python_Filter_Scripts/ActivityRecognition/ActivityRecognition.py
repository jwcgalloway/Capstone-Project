
# Sample Decision Tree Classifier
from sklearn import datasets
from sklearn import metrics
from sklearn.tree import DecisionTreeClassifier
from sklearn import svm
from pandas import Series
from matplotlib import pyplot
from sklearn.preprocessing import MinMaxScaler
import numpy as np
#Load the Dataset
myData = np.recfromcsv("ReachToMouthFeatures.csv",delimiter=",")

print myData
#print np.var(myData)ss