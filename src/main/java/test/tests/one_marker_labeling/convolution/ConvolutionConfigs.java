package test.tests.one_marker_labeling.convolution;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.ComputationGraphConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.graph.ReshapeVertex;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.util.Set;

public class ConvolutionConfigs {

    private static int inputSize;   //equals height of convolution input --> spatial: [inputSize,1]
    private static int outputSize;  //CNN kernel lowers amount of data (3 marker-coordinates to one value)
    //cnnChannels --> amount of kernel-operations on input data in cnn1
    //kernelSize of CNN1 = [3,1] --> 3 for x,y,z of each marker, 1 because width of data = 1
    //stride = [3,1] --> step over 3 elements to filter each marker individually

    static ComputationGraphConfiguration multipleReshapes(Set<String> selectedLabels, int batchSize, int cnn1Channels, int cnn2Channels ) {
        initInputSize(selectedLabels);
        int[] newShape = {batchSize, 1, 1, cnn2Channels * outputSize}; //final shape of outputs

        return new NeuralNetConfiguration.Builder()
                .seed(523)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Sgd(0.01))
                .graphBuilder()
                .addInputs("1")
                .layer("CNN1", new ConvolutionLayer.Builder().nIn(1).nOut(cnn1Channels)
                        .kernelSize(3, 1).stride(3, 1).build(), "1")
                .addVertex("Reshape1",
                        new ReshapeVertex(batchSize, 1, cnn1Channels, outputSize), "CNN1")
                .layer("CNN2", new ConvolutionLayer.Builder().nIn(1).nOut(cnn2Channels)
                        .kernelSize(cnn1Channels, 1).stride(1, 1).build(), "Reshape1")
                //vertex to convert the inputs in order of markers
                .addVertex("Reshape2",
                        new ReshapeVertex('f', newShape, null), "CNN2")
                .layer("DL2", new DenseLayer.Builder().activation(Activation.TANH).weightInit(WeightInit.XAVIER)
                        .nIn(outputSize * cnn2Channels).nOut(outputSize * cnn2Channels).build(), "Reshape2")
                .layer("DL3", new DenseLayer.Builder().activation(Activation.TANH).weightInit(WeightInit.XAVIER)
                        .nIn(outputSize * cnn2Channels).nOut(outputSize).build(), "DL2")
                .layer("DL4", new OutputLayer.Builder(LossFunctions.LossFunction.MCXENT)
                        .activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER)
                        .nIn(outputSize).nOut(outputSize).build(), "DL3")
                .setOutputs("DL4")
                .setInputTypes(InputType.convolutionalFlat(inputSize, 1, 1))
                .build();
    }

    static ComputationGraphConfiguration singleReshape(Set<String> selectedLabels, int batchSize, int cnnChannels) {
        initInputSize(selectedLabels);
        int[] newShape = {batchSize, 1, 1, cnnChannels * outputSize}; //final shape of outputs
        return new NeuralNetConfiguration.Builder()
                .seed(523)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Sgd(0.01))
                .graphBuilder()
                .addInputs("1")
                .layer("CNN1", new ConvolutionLayer.Builder().nIn(1).nOut(cnnChannels)
                        .kernelSize(3, 1).stride(3, 1).build(), "1")
                //vertex to convert the inputs in order of markers
                .addVertex("Reshape",
                        new ReshapeVertex('f', newShape, null), "CNN1")
                .layer("DL2", new DenseLayer.Builder().activation(Activation.TANH).weightInit(WeightInit.XAVIER)
                        .nIn(outputSize * cnnChannels).nOut(outputSize * cnnChannels).build(), "Reshape")
                .layer("DL3", new DenseLayer.Builder().activation(Activation.TANH).weightInit(WeightInit.XAVIER)
                        .nIn(outputSize * cnnChannels).nOut(outputSize).build(), "DL2")
                .layer("DL4", new OutputLayer.Builder(LossFunctions.LossFunction.MCXENT)
                        .activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER)
                        .nIn(outputSize).nOut(outputSize).build(), "DL3")
                .setOutputs("DL4")
                .setInputTypes(InputType.convolutionalFlat(inputSize, 1, 1))
                .build();
    }

    private static void initInputSize(Set<String> selectedLabels) {
        inputSize = selectedLabels.size() * 3;
        outputSize = selectedLabels.size();
    }
}
