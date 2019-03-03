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
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerMinMaxScaler;
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
    //alle json-dateien besitzen die richtigen marker, allerdings ist in einigen Dateien der Wert für den Marker thrx NAN
    //dadurch können die bekannten fehler entstehen

    public static void main(String[] args) throws Exception {
        String[] allowedFileFormat = {"json"};
        //Input Data
        File trainDirectory = new File("C:\\Users\\nicor\\Desktop\\Daten_Studienarbeit\\train");
        File testDirectory = new File("C:\\Users\\nicor\\Desktop\\Daten_Studienarbeit\\test");
        FileSplit fileSplitTrain = new FileSplit(trainDirectory, allowedFileFormat);
        FileSplit fileSplitTest = new FileSplit(testDirectory,allowedFileFormat);

        //Strategies/Assets
        FrameLabelingStrategy frameLabelingStrategy = new OneTargetLabeling("LELB", 35);
        FrameDataManipulationStrategy manipulationStrategy = new FrameShuffleManipulator(10);
        TrialNormalizationStrategy normalizationStrategy = new CentroidNormalization(-100,100);
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
                .activation(Activation.TANH)
                .weightInit(WeightInit.NORMAL)
                .updater(new Sgd(0.4))
                .list()
                .layer(0, new DenseLayer.Builder().nIn(numInputs).nOut(53).build())
                .layer(1, new DenseLayer.Builder().nIn(53).nOut(35).build())
                .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS).nIn(35).nOut(outputNum).build())
                .build();

        //dataset iterator
        RecordReaderDataSetIterator trainIterator = new RecordReaderDataSetIterator(trainDataReader, 1);
        RecordReaderDataSetIterator testIterator = new RecordReaderDataSetIterator(testDataReader,1);

        //Normalization
       /* int rangeMin = -1;
        int rangeMax = 1;
        NormalizerMinMaxScaler normalizerMinMaxScaler = new NormalizerMinMaxScaler(rangeMin,rangeMax);
        normalizerMinMaxScaler.fit(trainIterator);
        trainIterator.setPreProcessor(normalizerMinMaxScaler);
        NormalizerMinMaxScaler normalizerMinMaxScaler1 = new NormalizerMinMaxScaler(rangeMin,rangeMax);
        normalizerMinMaxScaler1.fit(testIterator);
        testIterator.setPreProcessor(normalizerMinMaxScaler1);*/

        //init nn
        MultiLayerNetwork nn = new MultiLayerNetwork(conf);
        nn.init();
        EvaluativeListener evaluativeListener = new EvaluativeListener(testIterator,1,InvocationType.EPOCH_END);
        nn.setListeners(new ScoreIterationListener(10000), evaluativeListener);

        //Training
        nn.fit(trainIterator, 1);

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

        System.out.println("start evaluation");
        testIterator.reset();
        Evaluation eval = nn.evaluate(testIterator);
        System.out.println(eval.stats(false, true));

        //single eval
        testIterator.reset();
        DataSet testData = testIterator.next();
        INDArray features = testData.getFeatures();
        INDArray prediction = nn.output(features);
        System.out.println("single eval:");
        Evaluation evaluation = new Evaluation(outputNum);
        eval.eval(testData.getLabels().getRow(0), prediction.getRow(0));
        System.out.println(evaluation.stats(false, true));
        System.out.println("Datensatz 1 --> Features: ");
        TestV3.printINDArray(features.getRow(0));
        System.out.println("Datensatz 1 --> Prediction: ");
        TestV3.printINDArray(prediction.getRow(0));
        System.out.println("geschätzter Wert: ");
        System.out.println(prediction.getRow(0).maxNumber());
    }
}
