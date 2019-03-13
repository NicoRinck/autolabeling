package deep_learning.execution;

import datavec.JsonTrialRecordReader;
import deep_learning.execution.result_logging.ResultLogger;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.api.TrainingListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import preprocess_data.TrialDataManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AutomaticConfigExecutor {

    private static final String[] ALLOWED_FILE_FORMAT = {"json"};
    private final TrialDataManager dataManager;
    private final RecordReaderDataSetIterator trainIterator;
    private final DataSetIterator testIterator;
    private final ResultLogger resultLogger;
    private final ArrayList<TrainingListener> listeners = new ArrayList<>();

    public AutomaticConfigExecutor(File train, File test, File logFile, TrialDataManager dataManager, int batchSize) throws IOException, InterruptedException {
        this.trainIterator = initIterator(train, dataManager, batchSize);
        this.testIterator = initIterator(test, dataManager, batchSize);
        this.dataManager = dataManager;
        this.resultLogger = new ResultLogger(logFile);
        this.resultLogger.logDataInfo(dataManager);
    }

    private RecordReaderDataSetIterator initIterator(File file, TrialDataManager trialDataManager, int batchSize) throws IOException, InterruptedException {
        JsonTrialRecordReader recordReader = new JsonTrialRecordReader(trialDataManager);
        FileSplit fileSplit = new FileSplit(file, ALLOWED_FILE_FORMAT);
        this.resultLogger.logFiles(fileSplit, file);
        recordReader.initialize(fileSplit);
        return new RecordReaderDataSetIterator(recordReader, batchSize);
    }

    public ArrayList<TrainingListener> getListeners() {
        return this.listeners;
    }

    public void executeConfigs(ArrayList<MultiLayerConfiguration> configs, int epochsPerExecution, int repeats) {

        final DL4JNetworkExecutor networkExecutor = new DL4JNetworkExecutor(trainIterator);
        for (MultiLayerConfiguration config : configs) {
            trainAndEvaluateNetwork(config, repeats, epochsPerExecution, networkExecutor);
        }

    }

    private void trainAndEvaluateNetwork(MultiLayerConfiguration config, int repeats, int epoch, DL4JNetworkExecutor executor) {
        for (int i = 0; i < repeats; i++) {
            MultiLayerNetwork multiLayerNetwork = executor.executeAndTrainNetwork(config, epoch);
            Evaluation evaluation = multiLayerNetwork.evaluate(testIterator);
            resultLogger.log(multiLayerNetwork,evaluation);
        }
    }

}
