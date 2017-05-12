package com.company;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.SMO;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.classifiers.trees.J48;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws Exception {
        ActivityRecognition classifier = new ActivityRecognition();

        //System.out.println(Arrays.toString(classifier.CreateFeatures(classifier.ExtractAxis()).toArray()));
        System.out.println(classifier.getClassifiedInstance());


    }

}
