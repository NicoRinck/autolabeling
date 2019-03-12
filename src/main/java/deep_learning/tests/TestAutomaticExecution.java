package deep_learning.tests;

import datavec.JsonTrialRecordReader;
import deep_learning.execution.DL4JNetworkExecutor;
import deep_learning.execution.result_logging.ResultLogger;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import preprocess_data.TrialDataManager;
import preprocess_data.TrialDataTransformation;
import preprocess_data.data_manipulaton.FrameManipulationStrategy;
import preprocess_data.data_manipulaton.FrameShuffleManipulator;
import preprocess_data.data_normalization.CentroidNormalization;
import preprocess_data.data_normalization.TrialNormalizationStrategy;
import preprocess_data.labeling.FrameLabelingStrategy;
import preprocess_data.labeling.OneTargetLabeling;

import java.io.File;

public class TestAutomaticExecution {

    public static void main(String[] args) throws Exception {
        String[] allowedFileFormat = {"json"};
        //Input Data
        File trainDirectory = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\trainData\\train");
        File testDirectory = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\testData\\test");
        FileSplit fileSplitTrain = new FileSplit(trainDirectory, allowedFileFormat);
        FileSplit fileSplitTest = new FileSplit(testDirectory, allowedFileFormat);

        //Strategies/Assets
        FrameLabelingStrategy frameLabelingStrategy = new OneTargetLabeling("LELB", 35);
        FrameManipulationStrategy manipulationStrategy = new FrameShuffleManipulator(1);
        TrialNormalizationStrategy normalizationStrategy = new CentroidNormalization(0, 1);
        TrialDataTransformation transformation = new TrialDataTransformation(frameLabelingStrategy, manipulationStrategy);
        TrialDataManager trialDataManager = new TrialDataManager(transformation, normalizationStrategy);

        //DataSet Iterators
        JsonTrialRecordReader trainDataReader = new JsonTrialRecordReader(trialDataManager);
        JsonTrialRecordReader testDataReader = new JsonTrialRecordReader(trialDataManager);
        trainDataReader.initialize(fileSplitTrain);
        testDataReader.initialize(fileSplitTest);

        //NN Config
        final int numInputs = 105;
        final int outputNum = 35;
        final long seed = 1014L;

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                /*.activation(Activation.TANH)
                .weightInit(WeightInit.XAVIER)*/
                .updater(new Sgd(0.1))
                .list()
                .layer(0, new DenseLayer.Builder().
                        nIn(numInputs).nOut(53).
                        weightInit(WeightInit.SIGMOID_UNIFORM).
                        activation(Activation.SIGMOID).build())
                .layer(1, new DenseLayer.Builder()
                        .nIn(53).nOut(35)
                        .weightInit(WeightInit.RELU)
                        .activation(Activation.LEAKYRELU).build())
                .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(35).nOut(outputNum)
                        .weightInit(WeightInit.NORMAL)
                        .activation(Activation.SOFTMAX).build())
                .build();

        RecordReaderDataSetIterator trainIterator = new RecordReaderDataSetIterator(trainDataReader, 20);
        RecordReaderDataSetIterator testIterator = new RecordReaderDataSetIterator(testDataReader, 20); //ändert das was?


        String logfileDestination = "C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit";
        ResultLogger resultLogger = new ResultLogger(logfileDestination, "logfile.txt");
        DL4JNetworkExecutor networkExecutor = new DL4JNetworkExecutor(trainIterator, testIterator, resultLogger);
        networkExecutor.executeAndTrainNetwork(conf, 1);
    }
}
