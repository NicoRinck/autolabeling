package deep_learning.execution;

import datavec.JsonTrialRecordReader;
import deep_learning.execution.result_logging.ResultLogger;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.api.TrainingListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerMinMaxScaler;
import preprocess_data.TrialDataManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AutomaticConfigExecutor {

    private static final String[] ALLOWED_FILE_FORMAT = {"json"};
    private final RecordReaderDataSetIterator trainIterator;
    private final RecordReaderDataSetIterator testIterator;
    private final ResultLogger resultLogger;
    private final ArrayList<TrainingListener> listeners = new ArrayList<>();

    public AutomaticConfigExecutor(File train, File test, File logFile, TrialDataManager dataManager, int batchSize,
                                   NormalizerMinMaxScaler normalizer) throws IOException, InterruptedException, InstantiationException, IllegalAccessException {
        this(train, test, logFile, dataManager, batchSize);
        addPreProcessors(normalizer,trainIterator);
        addPreProcessors(normalizer,testIterator);
        this.resultLogger.logNormalizer(normalizer);
    }

    private void addPreProcessors(DataNormalization normalizer, RecordReaderDataSetIterator iterator) throws IllegalAccessException, InstantiationException {
        DataNormalization normalizerCopy = normalizer.getClass().newInstance();
        normalizer.fit(iterator);
        iterator.setPreProcessor(normalizerCopy);
    }

    public AutomaticConfigExecutor(File train, File test, File logFile, TrialDataManager dataManager, int batchSize) throws IOException, InterruptedException {
        this.resultLogger = new ResultLogger(logFile);
        this.trainIterator = initIterator(train, dataManager, batchSize);
        this.testIterator = initIterator(test, dataManager, batchSize);
        this.resultLogger.logDataInfo(dataManager, batchSize);
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

        final DL4JNetworkTrainer networkExecutor = new DL4JNetworkTrainer(trainIterator);
        for (MultiLayerConfiguration config : configs) {
            trainAndEvaluateNetwork(config, repeats, epochsPerExecution, networkExecutor);
        }
    }
    
    private void trainAndEvaluateNetwork(MultiLayerConfiguration config, int repeats, int epoch, DL4JNetworkTrainer executor) {
        for (int i = 0; i < repeats; i++) {
            MultiLayerNetwork multiLayerNetwork = executor.trainNetwork(config, epoch);
            Evaluation evaluation = multiLayerNetwork.evaluate(testIterator);
            resultLogger.log(multiLayerNetwork, evaluation);
            testIterator.reset();
            trainIterator.reset();
        }
    }
}
