package com.company;

/**
 * Created by arjaireynolds on 11/5/17.
 */

import com.opencsv.CSVReader;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.*;
import weka.core.converters.ConverterUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public  class ActivityRecognition {
    //put classifier up here
    //access classifier
    //get result
    //method for classifying
    private SMO smo;
    private Evaluation eval;
    private int ClassifiedInstance;
    private  int epoch = 3 * 8;

    public ActivityRecognition() throws Exception {
            ConverterUtils.DataSource source = new ConverterUtils.DataSource("Features3.arff");
            Instances data = source.getDataSet();
            if (data.classIndex() == -1)
                data.setClassIndex(data.numAttributes() - 1);

            String[] options = new String[1];
            options[0] = "";            // unpruned tree
            smo = new SMO();         // new instance of tree
            smo.setOptions(options);     // set the options
            smo.buildClassifier(data);

            eval = new Evaluation(data);
            eval.crossValidateModel(smo, data, 10, new Random(1));

        }


    /*
        Extracts x,y,z from text file then poip
     */
    private List ExtractAxis() throws IOException {
        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();
        List<Double> z = new ArrayList<>();
        List<List<Double>> axisList = new ArrayList<>();


        CSVReader reader = new CSVReader(new FileReader("randomTest.csv"));
        String [] nextLine;
        int i = 0;
        while ((nextLine = reader.readNext()) != null && i < epoch) {
            // nextLine[] is an array of values from the line
            i++;
            x.add(Double.parseDouble(nextLine[0]));
            y.add(Double.parseDouble(nextLine[1]));
            z.add(Double.parseDouble(nextLine[2]));
        }
        axisList.add(x);
        axisList.add(y);
        axisList.add(z);
        return axisList;
    }

    /*
        Extract features from arrays
        varx,stdx,skewx,minx,maxx,avgx,vary,stdy,skewy,miny,maxy,avgy,varz,stdz,skewz,minz,maxz,avgz,CLASS
     */
    private List CreateFeatures(List<List<Double>> axisList){
        DescriptiveStatistics stats = new DescriptiveStatistics();
        List<Double> features = new ArrayList();
        for (List<Double> axis: axisList) {
            stats.clear();
            for(double dataPoints:axis){
                stats.addValue(dataPoints);

            }
            features.add(stats.getVariance());
            features.add(stats.getStandardDeviation());
            features.add(stats.getSkewness());
            features.add(stats.getMin());
            features.add(stats.getMax());
            features.add(stats.getMean());
            stats.clear();
       }
        System.out.println(Arrays.toString(features.toArray()));
        return features;
    }

    /*
        Returns an Instance of a given array of datapoints
    */
    private Instance createInstance(List<Double> featureVector) {
        //read in the txt file

        String [] labels = new String []{"varx", "stdx", "skewx", "minx", "maxx" , "avgx","vary","stdy","skewy","miny","maxy","avgy","varz","std","skewz","minz","maxz","avgz","ClASS"};

        ArrayList<Attribute> attributeList = new ArrayList<Attribute>(2);
        ArrayList<String> classVal = new ArrayList<String>();
        classVal.add("1");
        classVal.add("2");
        classVal.add("3");
        classVal.add("4");


        for (int i = 0; i < labels.length-1; i++){
            attributeList.add(new Attribute(labels[i]));

        }
        attributeList.add(new Attribute("CLASS",classVal));


        Instances dataset = new Instances("MyRelation", attributeList, 0);

        double [] features = new double [18];
        int index = 0;
        for (Double vals:featureVector) {
            features[index] = vals;
            index++;
        }
        Instance inst_co = new DenseInstance(1.0,features);

        dataset.setClassIndex(18);
        dataset.add(inst_co);
        inst_co.setDataset(dataset);

        return inst_co;
    }

    public String getClassifiedInstance() throws Exception {
        List extractedAxis = ExtractAxis();
        List features = CreateFeatures(extractedAxis);
        Instance instance = createInstance(features);

        double clsLabel = smo.classifyInstance(instance);
        return instance.classAttribute().value((int) clsLabel);


    }
}
