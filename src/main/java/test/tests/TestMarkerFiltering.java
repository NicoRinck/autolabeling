package test.tests;

import datavec.JsonTrialRecordReader;
import datavec.RandomizedTrialRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.api.InvocationType;
import org.deeplearning4j.optimize.listeners.EvaluativeListener;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import preprocess_data.TrialDataManager;
import preprocess_data.builders.TrialDataManagerBuilder;
import preprocess_data.builders.TrialDataTransformationBuilder;
import preprocess_data.data_manipulaton.FrameShuffleManipulator;
import preprocess_data.data_normalization.CentroidNormalization;
import preprocess_data.labeling.OneTargetLabeling;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class TestMarkerFiltering {

    public static void main(String[] args) throws IOException, InterruptedException {

        File trainDirectory = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\trainData\\trainDistanceSimple");
        File testDirectory = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\testData\\testDistanceSimple");
        File logFile = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\logs\\logFile(5-Markers)-v1.txt");
        String[] markerLabels = {"C7", "CLAV", "LASI", "LELB", "LELBW", "LHUM4", "LHUMA", "LHUMP", "LHUMS", "LRAD", "LSCAP1", "LSCAP2", "LSCAP3", "LSCAP4", "LULN", "RASI", "RELB", "RELBW", "RHUM4", "RHUMA", "RHUMP", "RHUMS", "RRAD", "RSCAP1", "RSCAP2", "RSCAP3", "RSCAP4", "RULN", "SACR", "STRN", "T10", "THRX1", "THRX2", "THRX3", "THRX4"};
        TreeSet<String> selectedLabels = new TreeSet<>();
        selectedLabels.add(markerLabels[1]);
        selectedLabels.add(markerLabels[2]);
        selectedLabels.add(markerLabels[3]);
        selectedLabels.add(markerLabels[4]);
        selectedLabels.add(markerLabels[5]);
        selectedLabels.add(markerLabels[6]);
        selectedLabels.add(markerLabels[7]);
        selectedLabels.add(markerLabels[8]);

        TrialDataManager trialDataManager = TrialDataManagerBuilder.addTransformation(TrialDataTransformationBuilder
                .addLabelingStrategy(new OneTargetLabeling("LELB", selectedLabels.size()))
                .withManipulation(new FrameShuffleManipulator(10))
                .build())
                .withNormalization(new CentroidNormalization(-1,1))
                .filterMarkers(selectedLabels)
                .build();

        /*RandomizedTrialRecordReader train = new RandomizedTrialRecordReader(trialDataManager, 50000);
        RandomizedTrialRecordReader test = new RandomizedTrialRecordReader(trialDataManager, 50000);*/
        JsonTrialRecordReader train = new JsonTrialRecordReader(trialDataManager);
        JsonTrialRecordReader test = new JsonTrialRecordReader(trialDataManager);
        train.initialize(new FileSplit(trainDirectory));
        test.initialize(new FileSplit(testDirectory));

        RecordReaderDataSetIterator trainIterator = new RecordReaderDataSetIterator(train, 10);
        RecordReaderDataSetIterator testIterator = new RecordReaderDataSetIterator(test, 10);

        MultiLayerNetwork multiLayerNetwork = new MultiLayerNetwork(nnConfig(selectedLabels));
        multiLayerNetwork.init();
        EvaluativeListener evaluativeListener = new EvaluativeListener(testIterator, 1, InvocationType.EPOCH_END);
        multiLayerNetwork.setListeners(new ScoreIterationListener(10000), evaluativeListener);
        multiLayerNetwork.fit(trainIterator, 3);

        Evaluation evaluate = multiLayerNetwork.evaluate(testIterator);
        System.out.println(evaluate.stats(false, true));
        Helper.logSingleEvaluationDetails(multiLayerNetwork, testIterator);
    }

    private static MultiLayerConfiguration nnConfig(Set<String> selectedLabels) {
        int inputSize = selectedLabels.size() * 3;
        int hiddenSize = inputSize / 2;
        int outputSize = selectedLabels.size();

        return new NeuralNetConfiguration.Builder()
                .seed(523)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Sgd(0.05))
                .list()
                .layer(new DenseLayer.Builder().activation(Activation.TANH).weightInit(WeightInit.NORMAL)
                        .nIn(inputSize).nOut(hiddenSize).build())
                .layer(new DenseLayer.Builder().activation(Activation.RELU).weightInit(WeightInit.RELU_UNIFORM)
                        .nIn(hiddenSize).nOut(hiddenSize).build())
                .layer(new DenseLayer.Builder().activation(Activation.RELU).weightInit(WeightInit.RELU_UNIFORM)
                        .nIn(hiddenSize).nOut(hiddenSize).build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.MCXENT)
                        .activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER)
                        .nIn(hiddenSize).nOut(outputSize).build())
                .build();
    }
}
