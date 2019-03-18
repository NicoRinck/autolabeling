package deep_learning.tests;

import deep_learning.execution.AutomaticConfigExecutor;
import deep_learning.execution.LogFileEvaluator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;

import java.io.File;
import java.util.ArrayList;

public class EvaluateExecutionResults {

    public static void main(String[] args) throws Exception {

        File file1 = new File("C:\\Users\\Nico Rinck\\Desktop\\Daten_Studienarbeit\\logs\\logfile(-1,1)-v1.txt");
        /*File file2 = new File("C:\\Users\\Nico Rinck\\Desktop\\Daten_Studienarbeit\\logs\\logfile(-1,1)-v2.txt");*/
        LogFileEvaluator logFileEvaluator1 = new LogFileEvaluator(file1);
        /*LogFileEvaluator logFileEvaluator2 = new LogFileEvaluator(file2);*/

        ArrayList<String> bestModelConfigs1 = logFileEvaluator1.getBestModelConfigs(0.011);
        System.out.println(bestModelConfigs1.size());
        /*ArrayList<String> bestModelConfigs2 = logFileEvaluator2.getBestModelConfigs(0.06);*/

        ArrayList<MultiLayerConfiguration> configs = new ArrayList<>();
        for (String s : bestModelConfigs1) {
            configs.add(MultiLayerConfiguration.fromJson(s));
        }

        AutomaticConfigExecutor automaticConfigExecutor = ExecutorConfigs.smallDataNegRange();
        automaticConfigExecutor.executeConfigs(configs,1,1);
    }

}
