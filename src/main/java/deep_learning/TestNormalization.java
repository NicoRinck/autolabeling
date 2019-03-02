package deep_learning;

import datavec.JsonTrialRecordReader;
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
import org.nd4j.evaluation.IEvaluation;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import preprocess_data.TrialDataManager;
import preprocess_data.TrialDataTransformation;
import preprocess_data.data_manipulaton.FrameDataManipulationStrategy;
import preprocess_data.data_manipulaton.FrameShuffleManipulator;
import preprocess_data.data_normalization.CentroidNormalization;
import preprocess_data.data_normalization.TrialNormalizationStrategy;
import preprocess_data.labeling.FrameLabelingStrategy;
import preprocess_data.labeling.OneTargetLabeling;

import java.io.File;

public class TestNormalization {

    public static void main(String[] args) throws Exception {
        String[] allowedFileFormat = {"json"};
        //Input Data
        File trainDirectory = new File("C:\\Users\\nicor\\Desktop\\Daten_Studienarbeit\\train");
        File testDirectory = new File("C:\\Users\\nicor\\Desktop\\Daten_Studienarbeit\\test");
        FileSplit fileSplitTrain = new FileSplit(trainDirectory, allowedFileFormat);
        FileSplit fileSplitTest = new FileSplit(testDirectory,allowedFileFormat);

        //Strategies/Assets
        FrameLabelingStrategy frameLabelingStrategy = new OneTargetLabeling("LELB", 35);
        FrameDataManipulationStrategy manipulationStrategy = new FrameShuffleManipulator(20);
        TrialNormalizationStrategy normalizationStrategy = new CentroidNormalization();
        TrialDataTransformation transformation = new TrialDataTransformation(frameLabelingStrategy, manipulationStrategy);
        TrialDataManager trialDataManager = new TrialDataManager(transformation, normalizationStrategy);

        //DataSet Iterators
        JsonTrialRecordReader trainDataReader = new JsonTrialRecordReader(trialDataManager);
        JsonTrialRecordReader testDataReader = new JsonTrialRecordReader(trialDataManager);
        trainDataReader.initialize(fileSplitTrain);
        testDataReader.initialize(fileSplitTrain);

        //NN Config
        final int numInputs = 30;
        final int outputNum = 10;
        final long seed = 1014L;

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .activation(Activation.TANH)
                .weightInit(WeightInit.NORMAL)
                .updater(new Sgd(0.4))
                .list()
                .layer(0, new DenseLayer.Builder().nIn(numInputs).nOut(15).build())
                .layer(1, new DenseLayer.Builder().nIn(15).nOut(15).build())
                .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS).nIn(15).nOut(outputNum).build())
                .build();

        //dataset iterator
        RecordReaderDataSetIterator trainIterator = new RecordReaderDataSetIterator(trainDataReader, 50);
        RecordReaderDataSetIterator testIterator = new RecordReaderDataSetIterator(testDataReader,50);

        //Normalization
        /*NormalizerMinMaxScaler normalizerMinMaxScaler = new NormalizerMinMaxScaler(-1, 1);
        normalizerMinMaxScaler.fit(trainIterator);
        trainIterator.setPreProcessor(normalizerMinMaxScaler);*/

        //init nn
        MultiLayerNetwork nn = new MultiLayerNetwork(conf);
        nn.init();
        EvaluativeListener evaluativeListener = new EvaluativeListener(testIterator,1,InvocationType.EPOCH_END);
        nn.setListeners(new ScoreIterationListener(10000), evaluativeListener);

        //Training
        nn.fit(trainIterator, 5);

        //Eval (full test data)
        Evaluation evaluation = new Evaluation(10);
        testIterator.reset();
        DataSet testData = testIterator.next();
        INDArray features = testData.getFeatures();
        INDArray prediction = nn.output(features);
        evaluation.eval(testData.getLabels(), prediction);
        evaluation.accuracy();
        System.out.println(evaluation.stats(false, true)); //prints confusion matrix

        //epochs
        IEvaluation[] evaluations = evaluativeListener.getEvaluations();
        for (IEvaluation singleEvaluation : evaluations) {
            String s = singleEvaluation.stats();
            String[] split = s.split("\n");
            int i = 0;
            for (String s1 : split) {
                if (s1.contains("Accuracy")) {
                    System.out.println("Präzision über die Epochen:");
                    System.out.println("Epoche " + i++ + ": " + s1);
                }
            }
        }
    }
}
