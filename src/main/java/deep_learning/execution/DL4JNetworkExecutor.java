package deep_learning.execution;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.api.TrainingListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

public class DL4JNetworkExecutor {

    private final DataSetIterator trainData;
    private final DataSetIterator testData;
    private final ResultLogger resultLogger;

    public DL4JNetworkExecutor(DataSetIterator trainData, DataSetIterator testData, ResultLogger logger) {
        this.trainData = trainData;
        this.testData = testData;
        this.resultLogger = logger;
    }

    public void executeAndTrainNetwork(final MultiLayerConfiguration config, int epochs, TrainingListener[] listeners) {
        final MultiLayerNetwork model = initModel(config);
        model.setListeners(listeners);
        executeModel(model, epochs);
    }

    public void executeAndTrainNetwork(final MultiLayerConfiguration config, int epochs) {
        final MultiLayerNetwork model = initModel(config);
        executeModel(model, epochs);
    }

    private void executeModel(final MultiLayerNetwork model, int epochs) {
        model.fit(trainData, epochs);
        evaluateModel(model);
    }

    private void evaluateModel(MultiLayerNetwork model) {
        Evaluation evaluation = model.evaluate(testData);
        resultLogger.log(model, evaluation);
    }

    private MultiLayerNetwork initModel(final MultiLayerConfiguration config) {
        final MultiLayerNetwork model = new MultiLayerNetwork(config.clone());
        model.init();
        return model;
    }
}
