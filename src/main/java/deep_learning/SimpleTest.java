package deep_learning;

import datavec.JsonTrialRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.ExistingDataSetIterator;
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
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerMinMaxScaler;
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

public class SimpleTest {
    public static void main(String[] args) throws Exception {

        String[] allowedFileFormat = {"json"};
        //Input Data
        File trainDirectory = new File("C:\\Users\\nico.rinck\\Desktop\\Daten_Studienarbeit\\ABD_train\\04_MH_O2_S1_Abd.json");
        FileSplit fileSplitTrain = new FileSplit(trainDirectory, allowedFileFormat);

        //Strategies/Assets
        FrameLabelingStrategy frameLabelingStrategy = new OneTargetLabeling("LELB", 35);
        FrameDataManipulationStrategy manipulationStrategy = new FrameShuffleManipulator(10);
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
                .activation(Activation.TANH)//TANH inklusive neuer Normalisierung auf -1,1 sorgt für drastische Verbesserung
                .weightInit(WeightInit.NORMAL)//beste Wahl: XAVIER (ca. 13-14%) oder Normal (bis zu 15% - mit LR: 0,2), Sigmoid Uniform führt zu übermäßigen Fokussierung auf ein Neuron im Ausgabelayer
                .updater(new Sgd(0.2))//Bisher beste Ergebnisse: 0.2 --> 12,5%
                .list()
                .layer(0, new DenseLayer.Builder().nIn(numInputs).nOut(53).build())//Mehr Neuronen in den Hidden Layer --> bessere Ergebnisse --> 13-14%
                .layer(1, new DenseLayer.Builder().nIn(53).nOut(45).build())
                .layer(2, new DenseLayer.Builder().nIn(45).nOut(45).build())
                .layer(3, new OutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS).nIn(35).nOut(outputNum).build())
                .build();

        //data
        RecordReaderDataSetIterator trainDataIterator = new RecordReaderDataSetIterator(trainDataReader, 47500);// mehr Daten --> klare Verbeserung --> stärkeres durchmischen der Marker (shuffle)

        org.nd4j.linalg.dataset.DataSet dataSet = trainDataIterator.next();//14000 datasets (batchsize) every dataset consists of 105 features and 35 labels (as array --> 0 0 0 ... 1 0 0)
        dataSet.shuffle(235L);

        //Normalization
        NormalizerMinMaxScaler normalizerMinMaxScaler = new NormalizerMinMaxScaler(-1,1); //ohne Normalisierung keine guten Ergebnisse (TANH)
        normalizerMinMaxScaler.fit(dataSet);
        normalizerMinMaxScaler.transform(dataSet);
        /*NormalizerStandardize normalizerStandardize = new NormalizerStandardize();
        normalizerStandardize.fit(dataSet);
        normalizerStandardize.transform(dataSet);*/

        //split
        SplitTestAndTrain testAndTrain = dataSet.splitTestAndTrain(0.8);

        org.nd4j.linalg.dataset.DataSet train = testAndTrain.getTrain();
        org.nd4j.linalg.dataset.DataSet test = testAndTrain.getTest();

        //init nn
        MultiLayerNetwork nn = new MultiLayerNetwork(conf);
        nn.init();
        nn.setListeners(new ScoreIterationListener(50000), new EvaluativeListener(test,5, InvocationType.EPOCH_END));
        //Training
        DataSetIterator dataSetIterator = new ExistingDataSetIterator(train);
        nn.fit(dataSetIterator, 50);

        //Eval
        System.out.println("start evaluation");
        Evaluation eval = new Evaluation(35);

        INDArray features = test.getFeatures();
        INDArray prediction = nn.output(features);
        eval.eval(test.getLabels(),prediction);
        System.out.println(eval.stats(false,true)); //prints confusion matrix

        //single eval
        System.out.println("single eval:");
        Evaluation evaluation = new Evaluation(35);
        evaluation.eval(test.getLabels().getRow(0), prediction.getRow(0));
        System.out.println(evaluation.stats(false,true));
        System.out.println("Datensatz 1 --> Features: ");
        printINDArray(features.getRow(0));
        System.out.println("Datensatz 1 --> Prediction: ");
        printINDArray(prediction.getRow(0));
        System.out.println("geschätzter Wert: ");
        System.out.println(prediction.getRow(0).maxNumber());

        /*Problem aktuelle Config --> keine Veränderung der Präzision --> passiert da überhaupt was?
        * weiteres Vorgehen --> andere NN-Modelle testen
        * NN gibt immer nur einen Output. Daher sind die Prediction Values immer gleich dem Zufallswert
        *  --> wird nur ein Output-Neuron trainiert?
        *
        *
        * Erste Erkenntnisse:
        * hohe Lernrate --> starke fixierung auf einen Output
        * mehr Layer --> bessere Verteilung
        * TANH und eine Normalisierung aud -1,1 führt zu den besten Ergebnissen
        * */
    }

    private static void printINDArray(INDArray indArray) {
        double[] array = indArray.toDoubleVector();
        for (int i = 0; i < array.length; i++) {
            System.out.print(i + ": " + array[i] + " | ");
        }
        System.out.println("");
    }
}
