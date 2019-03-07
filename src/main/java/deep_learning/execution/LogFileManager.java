package deep_learning.execution;

import java.io.*;
import java.util.ArrayList;

class LogFileManager {

    private final File logFile;
    private boolean valid = true;

    LogFileManager(File logFile) {
        this.logFile = logFile;
        createFileIfNotExist(logFile);
    }

    void writeInFile(String logString) {
        if (hasValidState()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true))) {
                bw.write(logString);
            } catch (IOException e) {
                System.err.println("Fehler beim Schreiben: " + e.getMessage());
            }
        }
    }

    ArrayList<String> getLines() {
        ArrayList<String> resultList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
            while (br.ready()) {
                String s = br.readLine();
                resultList.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    private void createFileIfNotExist(File logFile) {
        if (!logFile.exists()) {
            try {
                this.valid = logFile.createNewFile();
            } catch (IOException e) {
                this.valid = false;
            }
            writeInFile(getInitialHeader());
        }
    }

    private String getInitialHeader() {
        return "# Best Result: [0,-1] #";
    }

    boolean hasValidState() {
        return valid;
    }
}
