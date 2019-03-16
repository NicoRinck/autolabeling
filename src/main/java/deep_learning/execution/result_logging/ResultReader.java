package deep_learning.execution.result_logging;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ResultReader {

    private final FileManager logFileManager;
    private final FileManager configFileManager;

    public ResultReader(File logFile) {
        this.logFileManager = new FileManager(logFile);
        this.configFileManager = ResultLogger.getConfigFileManager(logFile);
    }

    HashMap<Integer, ArrayList<Double>> getModelIndexAndResults() {
        HashMap<Integer, ArrayList<Double>> resultMap = new HashMap<>();
        ArrayList<String> lines = logFileManager.getFileContent();
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).substring(0, LinePrefixes.MODEL_LINE_PREFIX.getLinePrefix().length())
                    .equalsIgnoreCase(LinePrefixes.MODEL_LINE_PREFIX.getLinePrefix())) {
                ArrayList<String> modelResultsLines = getNextLinesFromPrefix(i, lines,
                        LinePrefixes.RESULT_LINE_PREFIX.getLinePrefix());
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
            if (fileContent.get(i).contains(LinePrefixes.MODEL_LINE_PREFIX.getLinePrefix() + index)) {
                StringBuilder result = new StringBuilder(fileContent.get(i));
                ArrayList<String> nextLinesFromPrefix = getNextLinesFromPrefix(i, fileContent,
                        LinePrefixes.LAYER_LINE_PREFIX.getLinePrefix());
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
}
