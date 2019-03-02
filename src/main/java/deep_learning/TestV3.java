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
import preprocess_data.TrialDataManager;
import preprocess_data.TrialDataTransformation;
import preprocess_data.data_manipulaton.FrameDataManipulationStrategy;
import preprocess_data.data_manipulaton.FrameShuffleManipulator;
import preprocess_data.data_normalization.CentroidNormalization;
import preprocess_data.data_normalization.TrialNormalizationStrategy;
import preprocess_data.labeling.FrameLabelingStrategy;
import preprocess_data.labeling.OneTargetLabeling;

import java.io.File;

public class TestV3 {
    public static void main(String[] args) throws Exception {

        String[] allowedFileFormat = {"json"};
        //Input Data
        File trainDirectory = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\stack1");
        FileSplit fileSplitTrain = new FileSplit(trainDirectory, allowedFileFormat);

        //Strategies/Assets
        FrameLabelingStrategy frameLabelingStrategy = new OneTargetLabeling("LELB", 35);
        FrameDataManipulationStrategy manipulationStrategy = new FrameShuffleManipulator(53);

        TrialNormalizationStrategy normalizationStrategy = new CentroidNormalization();
        TrialDataTransformation transformation = new TrialDataTransformation(frameLabelingStrategy, manipulationStrategy);
        TrialDataManager trialDataManager = new TrialDataManager(transformation,normalizationStrategy);

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
                .activation(Activation.TANH)
                .weightInit(WeightInit.NORMAL)
                .updater(new Sgd(0.4))//LR 0.4 bei aktueller Konfig (mit mehr DS pro epoche) --> 25%!!! --> potential für mehr epochen und weitere Anpassung der LR
                .list()                 //10 epochen 32,75%
                .layer(0, new DenseLayer.Builder().nIn(numInputs).nOut(53).build())
                .layer(1, new DenseLayer.Builder().nIn(53).nOut(45).build())
                .layer(2, new DenseLayer.Builder().nIn(45).nOut(35).build())
                .layer(3, new OutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS).nIn(35).nOut(outputNum).build())
                .build();
        //bessere Ergebnisse bei höherer Anzahl an Daten pro epoche--> logisch
        //50 shuffles und 5 epochen ist besser als 10 shuffles 50 epochen (16,6%)
        //data
        RecordReaderDataSetIterator trainDataIterator = new RecordReaderDataSetIterator(trainDataReader, 250000); //mehr = mehr
        org.nd4j.linalg.dataset.DataSet dataSet = trainDataIterator.next();
        dataSet.shuffle(235L);

        //Normalization
        NormalizerMinMaxScaler normalizerMinMaxScaler = new NormalizerMinMaxScaler(-1, 1);
        normalizerMinMaxScaler.fit(dataSet);
        normalizerMinMaxScaler.transform(dataSet);

        //split
        SplitTestAndTrain testAndTrain = dataSet.splitTestAndTrain(0.8);
        org.nd4j.linalg.dataset.DataSet train = testAndTrain.getTrain();
        org.nd4j.linalg.dataset.DataSet test = testAndTrain.getTest();

        //init visualization
        /*UIServer uiServer = UIServer.getInstance();
        StatsStorage statsStorage = new InMemoryStatsStorage();
        uiServer.attach(statsStorage);*/

        //init nn
        MultiLayerNetwork nn = new MultiLayerNetwork(conf);
        nn.init();
        nn.setListeners(new ScoreIterationListener(50000),
                new EvaluativeListener(test, 1, InvocationType.EPOCH_END));
        //Training
        DataSetIterator dataSetIterator = new ExistingDataSetIterator(train);
        nn.fit(dataSetIterator, 50); //20 --> 44% //50 --> 51% (stagniert)

        //Eval (full test data)
        System.out.println("start evaluation");
        Evaluation eval = new Evaluation(35);

        INDArray features = test.getFeatures();
        INDArray prediction = nn.output(features);
        eval.eval(test.getLabels(), prediction);
        System.out.println(eval.stats(false, true)); //prints confusion matrix

        //single eval
        System.out.println("single eval:");
        Evaluation evaluation = new Evaluation(35);
        evaluation.eval(test.getLabels().getRow(0), prediction.getRow(0));
        System.out.println(evaluation.stats(false, true));
        System.out.println("Datensatz 1 --> Features: ");
        printINDArray(features.getRow(0));
        System.out.println("Datensatz 1 --> Prediction: ");
        printINDArray(prediction.getRow(0));
        System.out.println("geschätzter Wert: ");
        System.out.println(prediction.getRow(0).maxNumber());
    }

    static void printINDArray(INDArray indArray) {
        double[] array = indArray.toDoubleVector();
        for (int i = 0; i < array.length; i++) {
            System.out.print(i + ": " + array[i] + " | ");
        }
        System.out.println();
    }
}
