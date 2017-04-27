
# Sample Decision Tree Classifier
from sklearn import datasets
from sklearn import metrics
from sklearn.tree import DecisionTreeClassifier
from sklearn import svm , datasets
from pandas import Series
from matplotlib import pyplot
from sklearn.preprocessing import MinMaxScaler
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
#Load the Dataset
myData = pd.read_csv("ActivitiesFeatures.csv",delimiter=",")


original_headers = list(myData.columns.values)
myData = myData._get_numeric_data()
numeric_headers = list(myData.columns.values)

#create numpy array
numpy_array = myData.as_matrix()

X = numpy_array[0:,0:16]
X.reshape(1, - 1)
y = numpy_array[0:, 18]

print X
#print X
clf = svm.SVC(gamma = 1)

iris = datasets.load_iris()
#print iris
print clf.fit(X,y)

