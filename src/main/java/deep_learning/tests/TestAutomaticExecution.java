package deep_learning.tests;

import datavec.JsonTrialRecordReader;
import deep_learning.execution.DL4JNetworkExecutor;
import deep_learning.execution.config_generation.ConfigVariator;
import deep_learning.execution.config_generation.LayerConfigVariator;
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
import org.nd4j.linalg.learning.config.IUpdater;
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
        File trainDirectory = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\trainData\\train\\01_SS_O1_S1_Abd - Kopie.json");
        File testDirectory = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\testData\\test\\01_SS_O1_S1_Abd.json");
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

        RecordReaderDataSetIterator trainIterator = new RecordReaderDataSetIterator(trainDataReader, 20);
        RecordReaderDataSetIterator testIterator = new RecordReaderDataSetIterator(testDataReader, 20); //ändert das was?

        String logfileDestination = "C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\logs";
        File file = new File(logfileDestination + "logfile.txt");
        ResultLogger resultLogger = new ResultLogger(file);
        DL4JNetworkExecutor networkExecutor = new DL4JNetworkExecutor(trainIterator);
        LayerConfigVariator layerConfigVariator = new LayerConfigVariator(3,5,7);
        layerConfigVariator.addInputLayers(
                new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.SIGMOID).weightInit(WeightInit.SIGMOID_UNIFORM).build(),
                new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.RELU).weightInit(WeightInit.RELU_UNIFORM).build(),
                new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.RELU).weightInit(WeightInit.RELU).build(),
                new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.LEAKYRELU).weightInit(WeightInit.RELU_UNIFORM).build()
                /*new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.LEAKYRELU).weightInit(WeightInit.RELU).build(),
                new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.TANH).weightInit(WeightInit.NORMAL).build(),
                new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.TANH).weightInit(WeightInit.XAVIER).build(),
                new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.TANH).weightInit(WeightInit.XAVIER_UNIFORM).build()*/);
        layerConfigVariator.addOutputLayers(
                new OutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS).nIn(55).nOut(35).activation(Activation.SIGMOID).weightInit(WeightInit.SIGMOID_UNIFORM).build(),
                new OutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS).nIn(55).nOut(35).activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER_UNIFORM).build(),
                /*new OutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS).nIn(55).nOut(35).activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER).build(),
                new OutputLayer.Builder(LossFunctions.LossFunction.MSE).nIn(55).nOut(35).activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER).build(),
                new OutputLayer.Builder(LossFunctions.LossFunction.MCXENT).nIn(55).nOut(35).activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER).build(),
                new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD).nIn(55).nOut(35).activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER).build(),*/
                new OutputLayer.Builder(LossFunctions.LossFunction.MEAN_SQUARED_LOGARITHMIC_ERROR).nIn(55).nOut(35).activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER).build());
        layerConfigVariator.addHiddenLayers(
                new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.SIGMOID).weightInit(WeightInit.SIGMOID_UNIFORM).build(),
                new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.RELU).weightInit(WeightInit.RELU_UNIFORM).build(),
           /*     new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.RELU).weightInit(WeightInit.RELU).build(),
                new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.LEAKYRELU).weightInit(WeightInit.RELU_UNIFORM).build(),
                new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.LEAKYRELU).weightInit(WeightInit.RELU).build(),
                new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.TANH).weightInit(WeightInit.NORMAL).build(),
           */     new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.TANH).weightInit(WeightInit.XAVIER).build(),
                new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.TANH).weightInit(WeightInit.XAVIER_UNIFORM).build());
        System.out.println(layerConfigVariator.getLayerVariants().size());

        ConfigVariator configVariator = new ConfigVariator(24,layerConfigVariator);
        IUpdater[] updaters = {new Sgd(0.1), new Sgd(0.01)};
        configVariator.addUpdater(updaters);
        configVariator.executeVariations(2,networkExecutor);
    }
}
