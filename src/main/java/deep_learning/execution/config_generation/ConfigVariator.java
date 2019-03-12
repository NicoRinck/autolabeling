package deep_learning.execution.config_generation;

import deep_learning.execution.DL4JNetworkExecutor;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.Layer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.IUpdater;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.util.ArrayList;

public class ConfigVariator {

    private final ConfigGenerator configGenerator;
    private final LayerConfigVariator layerConfigVariator;
    private int[] epochs;

    public ConfigVariator(Integer seed, LayerConfigVariator layerConfigVariator) {
        this.configGenerator = new ConfigGenerator(seed);
        this.layerConfigVariator = layerConfigVariator;
    }

    void setEpochs(int... epochs) {
        this.epochs = epochs;
    }

    void addUpdater(IUpdater... updaters) {
        configGenerator.addValueToBuilder(updaters, NeuralNetConfiguration.Builder::updater);
    }

    void addOptimizationAlgorithm(OptimizationAlgorithm... optimizationAlgorithms) {
        configGenerator.addValueToBuilder(optimizationAlgorithms, NeuralNetConfiguration.Builder::optimizationAlgo);
    }

    public void executeVariations(int iterations, DL4JNetworkExecutor executor) {
        for (MultiLayerConfiguration config : configGenerator.getConfigs(layerConfigVariator.getLayerVariants())) {
            for (int epoch : epochs) {
                executeNetwork(config, epoch, iterations, executor);
            }
        }
    }

    private void executeNetwork(MultiLayerConfiguration config, int iterations, int epoch, DL4JNetworkExecutor executor) {
        if (epoch > 0) {
            for (int i = 0; i < iterations; i++) {
                executor.executeAndTrainNetwork(config, epoch);
            }
        }
    }

    public static void main(String[] args) {
        LayerConfigVariator layerConfigVariator = new LayerConfigVariator(4, 4, 5, 6, 7);
        layerConfigVariator.addInputLayers(
                new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.SIGMOID).weightInit(WeightInit.SIGMOID_UNIFORM).build(),
                new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.RELU).weightInit(WeightInit.RELU_UNIFORM).build(),
                /*new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.RELU).weightInit(WeightInit.RELU).build(),
                new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.LEAKYRELU).weightInit(WeightInit.RELU_UNIFORM).build(),
                new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.LEAKYRELU).weightInit(WeightInit.RELU).build(),
                new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.TANH).weightInit(WeightInit.NORMAL).build(),
                new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.TANH).weightInit(WeightInit.XAVIER).build(),*/
                new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.TANH).weightInit(WeightInit.XAVIER_UNIFORM).build());
        layerConfigVariator.addOutputLayers(
                new OutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS).nIn(55).nOut(35).activation(Activation.SIGMOID).weightInit(WeightInit.SIGMOID_UNIFORM).build(),
                new OutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS).nIn(55).nOut(35).activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER_UNIFORM).build(),
               /* new OutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS).nIn(55).nOut(35).activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER).build(),
                new OutputLayer.Builder(LossFunctions.LossFunction.MSE).nIn(55).nOut(35).activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER).build(),*/
                new OutputLayer.Builder(LossFunctions.LossFunction.MCXENT).nIn(55).nOut(35).activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER).build(),
                new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD).nIn(55).nOut(35).activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER).build(),
                new OutputLayer.Builder(LossFunctions.LossFunction.MEAN_SQUARED_LOGARITHMIC_ERROR).nIn(55).nOut(35).activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER).build());
        layerConfigVariator.addHiddenLayers(
                new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.SIGMOID).weightInit(WeightInit.SIGMOID_UNIFORM).build(),
               /* new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.RELU).weightInit(WeightInit.RELU_UNIFORM).build(),*/
               /* new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.RELU).weightInit(WeightInit.RELU).build(),
                new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.LEAKYRELU).weightInit(WeightInit.RELU_UNIFORM).build(),
                new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.LEAKYRELU).weightInit(WeightInit.RELU).build(),
                new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.TANH).weightInit(WeightInit.NORMAL).build(),
                new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.TANH).weightInit(WeightInit.XAVIER).build(),*/
                new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.TANH).weightInit(WeightInit.XAVIER_UNIFORM).build());
        ArrayList<ArrayList<Layer>> layerVariants = layerConfigVariator.getLayerVariants();
        for (ArrayList<Layer> layerVariant : layerVariants) {
            System.out.println("_______________________________");
            for (Layer layer : layerVariant) {
                System.out.println(layer.toString());
            }
            System.out.println("_______________________________");
        }
        System.out.println(layerConfigVariator.getLayerVariants().size());
    }


}
