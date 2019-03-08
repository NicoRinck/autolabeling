package deep_learning.execution.result_logging;

import java.io.*;
import java.util.ArrayList;

class FileManager {

    private final File file;
    private boolean valid = true;

    FileManager(String filePath) {
        file = createFileIfNotExist(filePath);
    }

    ArrayList<String> getFileContent() {
        ArrayList<String> resultList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                String s = br.readLine();
                resultList.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }
    private File createFileIfNotExist(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                this.valid = file.createNewFile();
            } catch (IOException e) {
                this.valid = false;
            }
        }
        return file;
    }
    boolean hasValidState() {
        return valid;
    }

    void writeInFile(String content) {
        if (hasValidState()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
