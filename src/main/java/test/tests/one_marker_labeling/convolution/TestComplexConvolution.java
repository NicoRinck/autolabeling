package test.tests.one_marker_labeling.convolution;

import datavec.RandomizedTrialRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.ComputationGraphConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.graph.ReshapeVertex;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.api.InvocationType;
import org.deeplearning4j.optimize.listeners.EvaluativeListener;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import preprocess_data.TrialDataManager;
import preprocess_data.builders.TrialDataManagerBuilder;
import preprocess_data.builders.TrialDataTransformationBuilder;
import preprocess_data.data_manipulaton.FrameShuffleManipulator;
import preprocess_data.data_normalization.CentroidNormalization;
import preprocess_data.labeling.OneTargetLabeling;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class TestComplexConvolution {
    public static void main(String[] args) throws IOException, InterruptedException {

        File trainDirectory = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\trainData\\train");
        File testDirectory = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\testData\\test");
        String[] markerLabels = {"C7", "CLAV", "LASI", "LELB", "LELBW", "LHUM4", "LHUMA", "LHUMP", "LHUMS", "LRAD", "LSCAP1", "LSCAP2", "LSCAP3", "LSCAP4", "LULN", "RASI", "RELB", "RELBW", "RHUM4", "RHUMA", "RHUMP", "RHUMS", "RRAD", "RSCAP1", "RSCAP2", "RSCAP3", "RSCAP4", "RULN", "SACR", "STRN", "T10", "THRX1", "THRX2", "THRX3", "THRX4"};
        TreeSet<String> selectedLabels = new TreeSet<>();
        selectedLabels.add(markerLabels[1]);
        selectedLabels.add(markerLabels[2]);
        selectedLabels.add(markerLabels[3]);
        selectedLabels.add(markerLabels[4]);
        selectedLabels.add(markerLabels[5]);
        int batchSize = 20;

        TrialDataManager trialDataManager = TrialDataManagerBuilder.addTransformation(TrialDataTransformationBuilder
                .addLabelingStrategy(new OneTargetLabeling("LELB", selectedLabels.size()))
                .withManipulation(new FrameShuffleManipulator(10))
                .build())
                .withNormalization(new CentroidNormalization(-1, 1))
                .filterMarkers(selectedLabels)
                .build();

        RandomizedTrialRecordReader train = new RandomizedTrialRecordReader(trialDataManager, 50000);
        RandomizedTrialRecordReader test = new RandomizedTrialRecordReader(trialDataManager, 50000);
        train.initialize(new FileSplit(trainDirectory));
        test.initialize(new FileSplit(testDirectory));

        RecordReaderDataSetIterator trainIterator = new RecordReaderDataSetIterator(train, batchSize);
        RecordReaderDataSetIterator testIterator = new RecordReaderDataSetIterator(test, batchSize);

        ComputationGraph graph = new ComputationGraph(nnConfig(selectedLabels, batchSize));
        System.out.println();
        graph.init();
        EvaluativeListener evaluativeListener = new EvaluativeListener(testIterator, 1, InvocationType.EPOCH_END);
        graph.setListeners(new ScoreIterationListener(10000), evaluativeListener);

        graph.fit(trainIterator, 5);
        Evaluation evaluate = graph.evaluate(testIterator);
        System.out.println(evaluate.stats(false, true));
    }

    private static ComputationGraphConfiguration nnConfig(Set<String> selectedLabels, int batchSize) {
        int inputSize = selectedLabels.size() * 3; //equals height of convolution input --> spatial: [inputSize,1]
        int cnnOutputSize = inputSize / 3; //CNN kernel lowers amount of data (3 marker-coordinates to one value)
        int cnn1Channels = 50; //amount of kernel-operations on input data in cnn1
        int cnn2Channels = 20;
        //kernelSize of CNN1 = [3,1] --> 3 for x,y,z of each marker, 1 because width of data = 1
        //stride = [3,1] --> step over 3 elements to filter each marker individually

        int[] newShape = {batchSize, 1, 1, cnn2Channels * cnnOutputSize}; //final shape of outputs
        //weitere Ideen: convertierung zu 1D mit Vertex selbst überprüfen um ordnung zu überwachen
        //noch mehr convolution (output cnn2 vermehren?)
        //beides testen (1 CNN + richtige convertierung; mehrere CNNS + richtige Converierung (und ohne))
        return new NeuralNetConfiguration.Builder()
                .seed(523)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Sgd(0.01))
                .graphBuilder()
                .addInputs("1")
                .layer("CNN1", new ConvolutionLayer.Builder().nIn(1).nOut(cnn1Channels)
                        .kernelSize(3, 1).stride(3, 1).build(), "1")
                .addVertex("Reshape1",
                        new ReshapeVertex(batchSize, 1, cnn1Channels, cnnOutputSize), "CNN1")
                .layer("CNN2", new ConvolutionLayer.Builder().nIn(1).nOut(cnn2Channels)
                        .kernelSize(cnn1Channels, 1).stride(1, 1).build(), "Reshape1")
                //vertex to convert the inputs in order of markers
                .addVertex("Reshape2",
                        new ReshapeVertex('f', newShape, null), "CNN2")
                .layer("Layer 2", new DenseLayer.Builder().activation(Activation.TANH).weightInit(WeightInit.XAVIER)
                        .nIn(cnnOutputSize * cnn2Channels).nOut(cnnOutputSize * cnn2Channels).build(), "Reshape2")
                .layer("Layer 3", new DenseLayer.Builder().activation(Activation.TANH).weightInit(WeightInit.XAVIER)
                        .nIn(cnnOutputSize * cnn2Channels).nOut(cnnOutputSize).build(), "Layer 2")
                .layer("Layer 4", new OutputLayer.Builder(LossFunctions.LossFunction.MCXENT)
                        .activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER)
                        .nIn(cnnOutputSize).nOut(5).build(), "Layer 3")
                .setOutputs("Layer 4")
                .setInputTypes(InputType.convolutionalFlat(inputSize, 1, 1))
                .build();
    }

    /* feedforward and log path to test layer outputs.
        To test with same in each run use JsonTrialRecordReader and FrameShuffleManipulator with seed.
        DataSet next = trainIterator.next();
        INDArray features = next.getFeatures();
        Map<String, INDArray> indArrays = graph.feedForward(features, false);
        for (String s : indArrays.keySet()) {
            System.out.println(s);
        }
        System.out.println("Batch 0: _________________________________");
        System.out.println("    Inputs:");
        System.out.println(indArrays.get("1").getRow(0));
        System.out.println("    CNN 1:");
        System.out.println(indArrays.get("CNN1").getRow(0));
        System.out.println("    mergeChannels:");
        System.out.println(indArrays.get("mergeChannels").getRow(0));
        System.out.println("    CNN2:");
        System.out.println(indArrays.get("CNN2").getRow(0));
        System.out.println("    Layer 2:");
        System.out.println(indArrays.get("Layer 2").getRow(0));*/

    //Current Best (5 Markers) --> 99%
    //batchsize = 20
    //RandomizedRR(50000)
    //FrameShuffles(10)
    //CentroidNorm(-1,1)
    //channels = 10
   /* return new NeuralNetConfiguration.Builder()
            .seed(523)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Sgd(0.01))
            .graphBuilder()
                .addInputs("1")
                .layer("CNN1", new ConvolutionLayer.Builder().nIn(1).nOut(cnn1Channels).kernelSize(3, 1).stride(3, 1).build(), "1")
            .addVertex("mergeChannels",
                               new ReshapeVertex(batchSize, 1, cnn1Channels, cnnOutputSize), "CNN1")
            .layer("CNN2", new ConvolutionLayer.Builder().nIn(1).nOut(1).kernelSize(cnn1Channels, 1).stride(1, 1).build(), "mergeChannels")
            .layer("Layer 2", new DenseLayer.Builder().activation(Activation.TANH).weightInit(WeightInit.XAVIER)
                        .nIn(cnnOutputSize).nOut(cnnOutputSize).build(), "CNN2")
            .layer("Layer 3", new DenseLayer.Builder().activation(Activation.TANH).weightInit(WeightInit.XAVIER)
                        .nIn(cnnOutputSize).nOut(cnnOutputSize).build(), "Layer 2")
            .layer("Layer 4", new OutputLayer.Builder(LossFunctions.LossFunction.MCXENT)
                        .activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER)
                        .nIn(cnnOutputSize).nOut(5).build(), "Layer 3")
            .setOutputs("Layer 4")
                .setInputTypes(InputType.convolutionalFlat(inputSize, 1, 1))
            .build();*/
}