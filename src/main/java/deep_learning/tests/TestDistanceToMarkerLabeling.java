package deep_learning.tests;

import datavec.JsonTrialRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Sgd;
import preprocess_data.TrialDataManager;
import preprocess_data.TrialDataTransformation;
import preprocess_data.data_manipulaton.FrameReorderingManipulator;
import preprocess_data.data_normalization.CentroidNormalization;
import preprocess_data.data_normalization.TrialNormalizationStrategy;
import preprocess_data.labeling.DistanceToMarkerLabeling;

import java.io.File;

public class TestDistanceToMarkerLabeling {

    public static void main(String[] args) throws Exception {
        String[] allowedFileFormat = {"json"};
        //Input Data
        File trainDirectory = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\trainData\\trainDistanceSimple");
        File testDirectory = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\testData\\testDistanceSimple");
        FileSplit fileSplitTrain = new FileSplit(trainDirectory, allowedFileFormat);
        FileSplit fileSplitTest = new FileSplit(testDirectory, allowedFileFormat);

        //Strategies/Assets
        //falsche Reihenfolge:
        String[] orderedLabels = {"RULN", "LSCAP4", "RHUMS", "RRAD", "T10", "THRX4", "RSCAP3", "LELB", "LSCAP3", "C7", "RHUMP", "THRX3", "LHUMA", "RSCAP4", "RHUM4", "CLAV", "STRN", "RELB", "RHUMA", "LULN", "LSCAP2", "LHUM4", "SACR", "THRX2", "RSCAP1", "LASI", "LRAD", "LSCAP1", "LHUMP", "RELBW", "RASI", "THRX1", "LELBW", "RSCAP2", "LHUMS"};
        //richtige (default) Reihenfolge;
        String[] orderedLabels2 = {"C7", "CLAV", "LASI", "LELB", "LELBW", "LHUM4", "LHUMA", "LHUMP", "LHUMS", "LRAD", "LSCAP1", "LSCAP2", "LSCAP3", "LSCAP4", "LULN", "RASI", "RELB", "RELBW", "RHUM4", "RHUMA", "RHUMP", "RHUMS", "RRAD", "RSCAP1", "RSCAP2", "RSCAP3", "RSCAP4", "RULN", "SACR", "STRN", "T10", "THRX1", "THRX2", "THRX3", "THRX4"};
        DistanceToMarkerLabeling frameLabelingStrategy = new DistanceToMarkerLabeling(orderedLabels2);
        TrialNormalizationStrategy normalizationStrategy = new CentroidNormalization();
        FrameReorderingManipulator frameReorderingManipulator = new FrameReorderingManipulator(2,2);
        TrialDataTransformation transformation = new TrialDataTransformation(frameLabelingStrategy, frameReorderingManipulator);
        TrialDataManager trialDataManager = new TrialDataManager(transformation, normalizationStrategy);

        //DataSet Iterators
        JsonTrialRecordReader trainDataReader = new JsonTrialRecordReader(trialDataManager);
        trainDataReader.initialize(fileSplitTrain);
        JsonTrialRecordReader testDataReader = new JsonTrialRecordReader(trialDataManager);
        testDataReader.initialize(fileSplitTest);

        DataSetIterator trainData = new RecordReaderDataSetIterator(trainDataReader,20);
        DataSetIterator testData = new RecordReaderDataSetIterator(testDataReader,20);


        MultiLayerConfiguration multiLayerConfiguration = new NeuralNetConfiguration.Builder()
                .seed(234)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Sgd(0.1))
                .list()
                .layer(new DenseLayer.Builder().nIn(595).nOut(119).build())
                .layer(new DenseLayer.Builder().nIn(119).nOut(17).build())
                .layer(new OutputLayer.Builder().nIn(17).nOut(2).build())
                .build();

        MultiLayerNetwork multiLayerNetwork = new MultiLayerNetwork(multiLayerConfiguration);
        multiLayerNetwork.init();

        multiLayerNetwork.fit(trainData,1);
        frameLabelingStrategy.logCount();
        frameLabelingStrategy.resetCount();

        Evaluation evaluate = multiLayerNetwork.evaluate(testData);
        String stats = evaluate.stats(false, true);
        System.out.println(stats);
        frameLabelingStrategy.logCount();

        testData.reset();
        DataSet test = testData.next();
        INDArray features = test.getFeatures();
        INDArray prediction = multiLayerNetwork.output(features);
        System.out.println("single eval:");
        Evaluation evaluation = new Evaluation(2);
        evaluation.eval(test.getLabels().getRow(0), prediction.getRow(0));
        System.out.println(evaluation.stats(false, true));
        System.out.println("Datensatz 1 --> Features: ");
        TestV3.printINDArray(features.getRow(0));
        System.out.println("Datensatz 1 --> Prediction: ");
        TestV3.printINDArray(prediction.getRow(0));
        System.out.println("geschätzter Wert: ");
        System.out.println(prediction.getRow(0).maxNumber());

        for (Layer layer : multiLayerNetwork.getLayers()) {
            System.out.println(layer.toString());
        }
        System.out.println(multiLayerNetwork.getLayerWiseConfigurations().toString());
    }

}
