package deep_learning.tests;

import deep_learning.execution.AutomaticConfigExecutor;
import deep_learning.execution.LogFileEvaluator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;

import java.io.File;
import java.util.ArrayList;

public class EvaluateExecutionResults {

    public static void main(String[] args) throws Exception {

        File file1 = new File("C:\\Users\\Nico Rinck\\Desktop\\Daten_Studienarbeit\\logs\\logfile-v1.txt");
        File file2 = new File("C:\\Users\\Nico Rinck\\Desktop\\Daten_Studienarbeit\\logs\\logfile(0,1)-v2.txt");
        File file3 = new File("C:\\Users\\Nico Rinck\\Desktop\\Daten_Studienarbeit\\logs\\logfile(-1,1)-v1.txt");
        File file4 = new File("C:\\Users\\Nico Rinck\\Desktop\\Daten_Studienarbeit\\logs\\logfile(-1,1)-v2.txt");
        LogFileEvaluator logFileEvaluator1 = new LogFileEvaluator(file1);
        LogFileEvaluator logFileEvaluator2 = new LogFileEvaluator(file2);
        LogFileEvaluator logFileEvaluator3 = new LogFileEvaluator(file3);
        LogFileEvaluator logFileEvaluator4 = new LogFileEvaluator(file4);

        Helper.logHashMap(logFileEvaluator1.getBestResult());
        Helper.logHashMap(logFileEvaluator2.getBestResult());
        Helper.logHashMap(logFileEvaluator3.getBestResult());
        Helper.logHashMap(logFileEvaluator4.getBestResult());

        ArrayList<String> bestModelConfigs1 = logFileEvaluator1.getBestModelConfigs(0.075);
        ArrayList<String> bestModelConfigs2 = logFileEvaluator2.getBestModelConfigs(0.09);
        ArrayList<String> bestModelConfigs3 = logFileEvaluator3.getBestModelConfigs(0.011);
        ArrayList<String> bestModelConfigs4 = logFileEvaluator4.getBestModelConfigs(0.011);

        logConfig(bestModelConfigs1);
        System.out.println("--------------------------------------------");
        logConfig(bestModelConfigs2);
        System.out.println("--------------------------------------------");
        logConfig(bestModelConfigs3);
        System.out.println("--------------------------------------------");
        logConfig(bestModelConfigs4);

    }

    static void logConfig(ArrayList<String> arrayList) {
        for (String s : arrayList) {
            System.out.println(s);
        }
    }

}
