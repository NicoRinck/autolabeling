package deep_learning.execution.result_logging;

import org.datavec.api.split.FileSplit;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.evaluation.classification.Evaluation;
import preprocess_data.TrialDataManager;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class ResultLogger {

    private static final String MODEL_LINE_PREFIX = "MultiLayerConfiguration ";
    private static final String LAYER_LINE_PREFIX = "   Label: ";
    private static final String RESULT_LINE_PREFIX = "       Ergebnis: ";
    private final FileManager logFileManager;
    private final FileManager configFileManager;
    private final ModelToStringConverter modelConverter = new ModelToStringConverter();
    private int modelCounter = 0;
    private String currentModelLine = "";

    public ResultLogger(File file) {
        this.logFileManager = new FileManager(file);
        this.configFileManager = new FileManager(new File(file.getParent()
                + file.getName().substring(0, file.getName().lastIndexOf(".")) + "_config.txt"));
    }

    public void log(MultiLayerNetwork model, Evaluation evaluation) {
        String modelString = modelConverter.modelToString(model);
        System.out.println(model.getLayer(model.getLayers().length - 1));
        System.out.println("Model: " + modelString);
        System.out.println(evaluation.stats(false, true));
        if (!modelString.equalsIgnoreCase(currentModelLine)) {
            modelCounter++;
            logFileManager.writeInFile("\n" + MODEL_LINE_PREFIX + modelCounter + "--> " + modelString);
            configFileManager.writeInFile(model.getDefaultConfiguration().toJson());
            currentModelLine = modelString;
            System.out.println(currentModelLine);
            System.out.println(getEvaluationLogString(evaluation));
        }
        logFileManager.writeInFile(getEvaluationLogString(evaluation));
    }

    private String getEvaluationLogString(Evaluation evaluation) {
        return "\n" + RESULT_LINE_PREFIX + evaluation.accuracy();
    }

    HashMap<Integer, ArrayList<Double>> getModelIndexAndResults() {
        HashMap<Integer, ArrayList<Double>> resultMap = new HashMap<>();
        ArrayList<String> lines = logFileManager.getFileContent();
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).substring(0, MODEL_LINE_PREFIX.length()).equalsIgnoreCase(MODEL_LINE_PREFIX)) {
                ArrayList<String> modelResultsLines = getNextLinesFromPrefix(i, lines, RESULT_LINE_PREFIX);
                ArrayList<Double> modelResults = new ArrayList<>();
                modelResultsLines.forEach(d -> modelResults.add(Double.valueOf(d)));
                resultMap.put(i, modelResults);
                i += modelResults.size();
            }
        }
        return resultMap;
    }

    private ArrayList<String> getNextLinesFromPrefix(int currentIndex, ArrayList<String> lines, String prefix) {
        ArrayList<String> resultList = new ArrayList<>();
        while (lines.get(++currentIndex).contains(prefix)) {
            String line = lines.get(currentIndex);
            resultList.add(line.substring(prefix.length()));
        }
        return resultList;
    }


    private String getModelString(int index) {
        ArrayList<String> fileContent = logFileManager.getFileContent();
        for (int i = 0; i < fileContent.size(); i++) {
            if (fileContent.get(i).contains(MODEL_LINE_PREFIX + index)) {
                StringBuilder result = new StringBuilder(fileContent.get(i));
                ArrayList<String> nextLinesFromPrefix = getNextLinesFromPrefix(i, fileContent, LAYER_LINE_PREFIX);
                for (String linesFromPrefix : nextLinesFromPrefix) {
                    result.append(linesFromPrefix);
                }
                return result.toString();
            }
        }
        return "";
    }

    private String getNetworkConfig(int index) {
        return configFileManager.getFileContent().get(index);
    }

    public void logDataInfo(TrialDataManager dataManager) {
        logFileManager.writeInFile("\n ----------------------------- \n");
        logFileManager.writeInFile(dataManager.getInfoString());
    }

    public void logFiles(FileSplit fileSplit, File file) {
        final StringBuilder builder = new StringBuilder();
        for (URI uri : fileSplit.locations()) {
            builder.append(uri).append("\n");
        }
        String testOrTrain = file.getPath().contains("train") ? "train" : "test";
        logFileManager.writeInFile(testOrTrain + "-Files:\n" + builder.toString());
        logFileManager.writeInFile("--------------------------");
    }
}
