package deep_learning.tests;

import deep_learning.execution.LogFileEvaluator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.util.ArrayList;

public class EvaluateExecutionResults {

    public static void main(String[] args) throws Exception {

        /*MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(423)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .activation(Activation.SIGMOID)
                .weightInit(WeightInit.SIGMOID_UNIFORM)
                .updater(new Sgd(0.1))
                .list()
                .layer(0,new DenseLayer.Builder().nIn(105).nOut(53).build())
                .layer(1,new OutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS).nIn(53).nOut(35).build())
                .build();

        MultiLayerNetwork network = new MultiLayerNetwork(conf);
        MultiLayerConfiguration multiLayerConfiguration = MultiLayerConfiguration.fromJson(network.getLayerWiseConfigurations().toJson());
        System.out.println(multiLayerConfiguration.equals(conf));*/

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

        ArrayList<String> bestModelConfigs1 = logFileEvaluator1.getBestModelConfigs(0.09);
        ArrayList<String> bestModelConfigs2 = logFileEvaluator2.getBestModelConfigs(0.07);
        ArrayList<String> bestModelConfigs3 = logFileEvaluator3.getBestModelConfigs(0.105);
        ArrayList<String> bestModelConfigs4 = logFileEvaluator4.getBestModelConfigs(0.08);
        System.out.println(bestModelConfigs1.size());
        System.out.println(bestModelConfigs2.size());
        System.out.println(bestModelConfigs3.size());
        System.out.println(bestModelConfigs4.size());

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
