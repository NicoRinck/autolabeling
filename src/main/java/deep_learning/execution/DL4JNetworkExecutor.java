package deep_learning.execution;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.api.TrainingListener;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

public class DL4JNetworkExecutor {

    private final DataSetIterator trainData;

    public DL4JNetworkExecutor(DataSetIterator trainData) {
        this.trainData = trainData;
    }

    public MultiLayerNetwork executeAndTrainNetwork(final MultiLayerConfiguration config, int epochs, TrainingListener[] listeners) {
        final MultiLayerNetwork model = initModel(config);
        model.setListeners(listeners);
        executeModel(model,epochs);
        return model;
    }

    public MultiLayerNetwork executeAndTrainNetwork(final MultiLayerConfiguration config, int epochs) {
        final MultiLayerNetwork model = initModel(config);
        executeModel(model,epochs);
        return model;
    }

    private void executeModel(final MultiLayerNetwork model, int epochs) {
        model.fit(trainData, epochs);
    }

    private MultiLayerNetwork initModel(final MultiLayerConfiguration config) {
        final MultiLayerNetwork model = new MultiLayerNetwork(config.clone());
        model.init();
        return model;
    }
}
