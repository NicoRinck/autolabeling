package deep_learning;

import datavec.JsonTrialRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import preprocess_data.EuclideanDistanceNormalizer;
import preprocess_data.JsonToTrialParser;
import preprocess_data.TrialDataManager;
import preprocess_data.TrialDataTransformation;
import preprocess_data.data_manipulaton.FrameDataManipulationStrategy;
import preprocess_data.data_manipulaton.FrameShuffleManipulator;
import preprocess_data.labeling.FrameLabelingStrategy;
import preprocess_data.labeling.OneTargetLabeling;

import java.io.File;

public class TestNN {

    public static void main(String[] args) throws Exception {

        //Input Data
        File trainDirectory = new File("C:\\Users\\nico.rinck\\Desktop\\Daten_Studienarbeit\\ABD_train");
        FileSplit fileSplitTrain = new FileSplit(trainDirectory);

        //Test Data
        File file = new File("C:\\Users\\nico.rinck\\Desktop\\Daten_Studienarbeit\\ABD_test");
        FileSplit fileSplitTest = new FileSplit(file);

        //Strategies/Assets
        FrameLabelingStrategy frameLabelingStrategy = new OneTargetLabeling("RASI", 35);
        EuclideanDistanceNormalizer euclideanDistanceNormalizer = new EuclideanDistanceNormalizer();
        FrameDataManipulationStrategy manipulationStrategy = new FrameShuffleManipulator(5);
        JsonToTrialParser jsonToTrialParser = new JsonToTrialParser();
        TrialDataTransformation transformation = new TrialDataTransformation(frameLabelingStrategy,manipulationStrategy, euclideanDistanceNormalizer);
        TrialDataManager trialDataManager = new TrialDataManager(transformation,jsonToTrialParser);


        //DataSet Iterators
        JsonTrialRecordReader trainDataReader = new JsonTrialRecordReader(trialDataManager);
        trainDataReader.initialize(fileSplitTrain);
        JsonTrialRecordReader testDataReader = new JsonTrialRecordReader(trialDataManager);
        testDataReader.initialize(fileSplitTest);

        //more is not needed. datavec assumes the last index of the data-array (record) as label.
        //To get output-labels of the NN the method RecordReader.getLabels() has to be implemented!
        RecordReaderDataSetIterator trainDataIterator = new RecordReaderDataSetIterator(trainDataReader,10);
        RecordReaderDataSetIterator testDataIterator = new RecordReaderDataSetIterator(testDataReader,10);

        //NN Config
        final int numInputs = 105;
        final int outputNum = 35;
        final long seed = 6;

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .activation(Activation.SIGMOID)
                .list()
                .layer(new DenseLayer.Builder().nIn(numInputs).nOut(53).build())
                .layer(new DenseLayer.Builder().nIn(53).nOut(33).build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS).activation(Activation.SIGMOID).nIn(33).nOut(outputNum).build())
                .build();

        MultiLayerNetwork nn = new MultiLayerNetwork(conf);
        nn.init();

        //Training
        int epochs = 10;
        for (int i = 0; i < epochs; i++) {
            nn.fit(trainDataIterator);
        }

        //Evaluation
        Evaluation evaluation = nn.evaluate(testDataIterator);
        System.out.println(evaluation.stats());
        System.out.println(evaluation.confusionToString());

       /* Evaluation evaluation = new Evaluation();
        INDArray output = nn.output();
        System.out.println("Output: " + output);
        evaluation.eval(testData.getLabels(), output);
        System.out.println(evaluation.stats());*/
    }
}