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
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import preprocess_data.JsonToTrialParser;
import preprocess_data.TrialDataManager;
import preprocess_data.TrialDataTransformation;
import preprocess_data.data_manipulaton.FrameDataManipulationStrategy;
import preprocess_data.data_manipulaton.FrameShuffleManipulator;
import preprocess_data.labeling.FrameLabelingStrategy;
import preprocess_data.labeling.OneTargetLabeling;

import java.io.File;
import java.util.Iterator;

public class SimpleTest {
    public static void main(String[] args) throws Exception {

        String[] allowedFileFormat = {"json"};
        //Input Data
        File trainDirectory = new File("C:\\Users\\nico.rinck\\Desktop\\Daten_Studienarbeit\\ABD_train\\04_MH_O2_S1_Abd.json");
        FileSplit fileSplitTrain = new FileSplit(trainDirectory, allowedFileFormat);

        //Strategies/Assets
        FrameLabelingStrategy frameLabelingStrategy = new OneTargetLabeling("LELB", 35);
        FrameDataManipulationStrategy manipulationStrategy = new FrameShuffleManipulator(5);
        JsonToTrialParser jsonToTrialParser = new JsonToTrialParser();
        TrialDataTransformation transformation = new TrialDataTransformation(frameLabelingStrategy, manipulationStrategy);
        TrialDataManager trialDataManager = new TrialDataManager(transformation, jsonToTrialParser);

        //DataSet Iterators
        JsonTrialRecordReader trainDataReader = new JsonTrialRecordReader(trialDataManager);
        trainDataReader.initialize(fileSplitTrain);

        //NN Config
        final int numInputs = 105;
        final int outputNum = 35;
        final long seed = 1014L;

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .activation(Activation.SIGMOID)
                .weightInit(WeightInit.SIGMOID_UNIFORM)
                .updater(new Sgd(0.1))
                .list()
                .layer(0, new DenseLayer.Builder().nIn(numInputs).nOut(53).build())
                .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS).nIn(53).nOut(outputNum).build())
                .build();

        //data
        RecordReaderDataSetIterator trainDataIterator = new RecordReaderDataSetIterator(trainDataReader, 1000);

        DataSet dataSet = trainDataIterator.next(); //1000 datasets (batchsize) every dataset consists of 105 features and 35 labels (as array --> 0 0 0 ... 1 0 0)
        SplitTestAndTrain testAndTrain = dataSet.splitTestAndTrain(0.8);

        org.nd4j.linalg.dataset.DataSet train = testAndTrain.getTrain();
        org.nd4j.linalg.dataset.DataSet test = testAndTrain.getTest();

        //init nn
        MultiLayerNetwork nn = new MultiLayerNetwork(conf);
        nn.init();
        nn.setListeners(new ScoreIterationListener(100));

        //Training
        nn.fit(train);

        //Eval
        System.out.println("start evaluation");
        Evaluation eval = new Evaluation(35);
        Iterator<org.nd4j.linalg.dataset.DataSet> testIterator = test.iterator();
        org.nd4j.linalg.dataset.DataSet testDataSet = testIterator.next();
        System.out.println("Features: " + testDataSet.getFeatures());
        System.out.println("Labels: " + testDataSet.getLabels());
        INDArray prediction = nn.output(testDataSet.getFeatures());
        System.out.println("Prediction: " + prediction);

        eval.eval(testDataSet.getLabels(),prediction);
        System.out.println(eval.stats(false,true)); //prints confusion matrix
    }
}
