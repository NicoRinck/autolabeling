package deep_learning.execution;

import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.api.TrainingListener;
import org.nd4j.evaluation.classification.Evaluation;

public class DL4JNetworkExecutor {

    private final RecordReaderDataSetIterator trainData;
    private final RecordReaderDataSetIterator testData;
    private final ResultLogger resultLogger;

    public DL4JNetworkExecutor(final RecordReaderDataSetIterator trainData,
                               final RecordReaderDataSetIterator testData,
                               final ResultLogger resultLogger) {
        this.trainData = trainData;
        this.testData = testData;
        this.resultLogger = resultLogger;
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
        final MultiLayerNetwork model = new MultiLayerNetwork(config);
        model.init();
        return model;
    }
}
