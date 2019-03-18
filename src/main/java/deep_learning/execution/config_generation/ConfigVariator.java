package deep_learning.execution.config_generation;

import deep_learning.tests.LayerConfigs;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.IUpdater;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.util.ArrayList;

public class ConfigVariator {

    private final ConfigGenerator configGenerator;
    private final LayerConfigVariator layerConfigVariator;

    public ConfigVariator(Integer seed, LayerConfigVariator layerConfigVariator) {
        this.configGenerator = new ConfigGenerator(seed);
        this.layerConfigVariator = layerConfigVariator;
    }

    public void addUpdater(IUpdater... updaters) {
        configGenerator.addValueToBuilder(updaters, NeuralNetConfiguration.Builder::updater);
    }

    void addOptimizationAlgorithm(OptimizationAlgorithm... optimizationAlgorithms) {
        configGenerator.addValueToBuilder(optimizationAlgorithms, NeuralNetConfiguration.Builder::optimizationAlgo);
    }

    public ArrayList<MultiLayerConfiguration> getConfigs() {
        return configGenerator.getConfigs(layerConfigVariator.getLayerVariants());
    }

    public static void main(String[] args) {
        LayerConfigVariator layerConfigVariator = new LayerConfigVariator(3);
        layerConfigVariator.addInputLayers(LayerConfigs.getInputLayersFromIndexes(0));
        layerConfigVariator.addHiddenLayers(LayerConfigs.getHiddenLayersFromIndexes(0));
        layerConfigVariator.addOutputLayers(LayerConfigs.getOutputLayersFromIndexes(0));

        ConfigVariator configVariator = new ConfigVariator(213, layerConfigVariator);
        IUpdater[] updaters = {new Sgd(0.1), new Sgd(0.01), new Sgd(0.001)};
        configVariator.addUpdater(updaters);
        configVariator.getConfigs();
    }
}
