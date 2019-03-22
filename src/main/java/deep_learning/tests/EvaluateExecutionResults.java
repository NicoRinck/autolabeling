package deep_learning.tests;

import deep_learning.execution.AutomaticConfigExecutor;
import deep_learning.execution.LogFileEvaluator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;

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

        File file = new File("C:\\Users\\Nico Rinck\\Desktop\\Daten_Studienarbeit\\logs\\logfile(-1,1)-v3.txt");
        LogFileEvaluator logFileEvaluator = new LogFileEvaluator(file);

        ArrayList<String> bestModelConfigs = logFileEvaluator.getBestModelConfigs(0.14);
        System.out.println(bestModelConfigs.size());

        ArrayList<MultiLayerConfiguration> configsFromJSON = Helper.getConfigsFromJSON(bestModelConfigs);
        AutomaticConfigExecutor automaticConfigExecutor = ExecutorConfigs.smallDataSetNegRange();
        automaticConfigExecutor.executeConfigs(configsFromJSON,10,3);

    }
}
