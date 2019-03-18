package deep_learning.tests;

import deep_learning.execution.AutomaticConfigExecutor;
import deep_learning.execution.config_generation.ConfigVariator;
import deep_learning.execution.config_generation.LayerConfigVariator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.nd4j.linalg.learning.config.IUpdater;
import org.nd4j.linalg.learning.config.Sgd;

import java.util.ArrayList;

public class AutomaticExecution {

    public static void main(String[] args) throws Exception {

        LayerConfigVariator layerConfigVariator = new LayerConfigVariator(3, 5, 7);
        layerConfigVariator.addInputLayers(LayerConfigs.getInputLayersFromIndexes(0, 2, 3));
        layerConfigVariator.addHiddenLayers(LayerConfigs.getHiddenLayersFromIndexes(0, 2, 5));
        layerConfigVariator.addOutputLayers(LayerConfigs.getOutputLayersFromIndexes(0, 2, 4, 5, 6));

        ConfigVariator configVariator = new ConfigVariator(24, layerConfigVariator);
        IUpdater[] updaters = {new Sgd(0.01), new Sgd(0.001)};
        configVariator.addUpdater(updaters);

        AutomaticConfigExecutor automaticConfigExecutor = ExecutorConfigs.smallDataNegRange();
        automaticConfigExecutor.executeConfigs(configVariator.getConfigs(), 1, 3);
    }
}
