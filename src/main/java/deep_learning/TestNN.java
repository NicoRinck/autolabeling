package deep_learning;

import datavec.JsonTrialRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.lossfunctions.LossFunctions;
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
        File file = new File("C:\\Users\\nico.rinck\\Desktop\\stack1\\01_SS_O1_S1_AR.json");
        FileSplit fileSplit = new FileSplit(file);

        FrameLabelingStrategy frameLabelingStrategy = new OneTargetLabeling("RASI", 35);
        JsonToTrialParser jsonToTrialParser = new JsonToTrialParser();

        FrameDataManipulationStrategy manipulationStrategy = new FrameShuffleManipulator(5);
        TrialDataTransformation transformation = new TrialDataTransformation(frameLabelingStrategy,manipulationStrategy);

        TrialDataManager trialDataManager = new TrialDataManager(transformation,jsonToTrialParser);
        JsonTrialRecordReader jsonTrialRecordReader = new JsonTrialRecordReader(trialDataManager);
        jsonTrialRecordReader.initialize(fileSplit);


        //more is not needed. datavec assumes the last index of the data-array (record) as label.
        //To get output-labels of the NN the method RecordReader.getLabels() has to be implemented!
        RecordReaderDataSetIterator dataSetIterator = new RecordReaderDataSetIterator(jsonTrialRecordReader,10);

        final int numInputs = 106;
        final int outputNum = 35;
        final long seed = 6;

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .activation(Activation.SIGMOID)
                .list()
                .layer(new DenseLayer.Builder().nIn(numInputs).nOut(53).build())
                .layer(new DenseLayer.Builder().nIn(numInputs).nOut(53).build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS).activation(Activation.SIGMOID).nIn(53).nOut(outputNum).build())
                .build();

        MultiLayerNetwork nn = new MultiLayerNetwork(conf);
        nn.init();

        System.out.println(dataSetIterator.hasNext());
        System.out.println(dataSetIterator.next());

        /*List<Writable> writable = jsonTrialRecordReader.next();
        Writable label = writable.get(105);
        System.out.println(label.getType());

        DataSet allData = dataSetIterator.next();
        SplitTestAndTrain testAndTrain = allData.splitTestAndTrain(0.8);

        DataSet trainingData = testAndTrain.getTrain();

        for (int i = 0; i < 10; i++) {
            nn.fit(trainingData);
        }*/
    }
}