package deep_learning.execution;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.evaluation.classification.Evaluation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

class ResultLogger {

    private static final String MODEL_LINE_PREFIX = "-";
    private static final String RESULT_LINE_PREFIX = "   Ergebnis: ";
    private final LogFileManager logFileManager;
    private final DL4JModelConverter modelConverter = new DL4JModelConverter();
    private int modelCounter = 0;
    private String currentModelLine = "";

    ResultLogger(File logFile) {
        this.logFileManager = new LogFileManager(logFile);
    }

    public ResultLogger() {
        logFileManager = null;
    }

    void log(MultiLayerNetwork model, Evaluation evaluation) {
        String modelString = modelConverter.modelToString(model);
        System.out.println("Model: " + modelString);
        System.out.println(evaluation.stats(false, true));
        if (!modelString.equalsIgnoreCase(currentModelLine) && logFileManager != null) {
            modelCounter++;
            logFileManager.writeInFile(MODEL_LINE_PREFIX + modelCounter + ". " + modelString);
            currentModelLine = modelString;
        }
        if (logFileManager != null) {
            logFileManager.writeInFile(getEvaluationLogString(evaluation));
        }
    }


    private ArrayList<String> getLoggedLines() {
        if (logFileManager != null && logFileManager.hasValidState()) {
            return logFileManager.getLines();
        }
        return null;
    }

    private String getEvaluationLogString(Evaluation evaluation) {
        return RESULT_LINE_PREFIX + evaluation.accuracy();

    }

    HashMap<Integer, ArrayList<Double>> getModelsAndResults() {
        if (logFileManager != null) {
            HashMap<Integer, ArrayList<Double>> resultMap = new HashMap<>();
            ArrayList<String> lines = logFileManager.getLines();
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).substring(0, 1).equalsIgnoreCase(MODEL_LINE_PREFIX)) {
                    ArrayList<Double> modelResults = getNextResultLines(i, lines);
                    resultMap.put(i,modelResults);
                    i+=modelResults.size();
                }
            }
            return resultMap;
        }
        return null;
    }

    private ArrayList<Double> getNextResultLines(int currentIndex, ArrayList<String> lines) {
        ArrayList<Double> resultList = new ArrayList<>();
        while (lines.get(++currentIndex).equalsIgnoreCase(RESULT_LINE_PREFIX)) {
            String line = lines.get(currentIndex);
            resultList.add(Double.valueOf(line.substring(RESULT_LINE_PREFIX.length())));
        }
        return resultList;
    }
}
