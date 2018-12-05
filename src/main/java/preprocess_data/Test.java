package preprocess_data;

import org.datavec.api.split.FileSplit;

import java.io.File;
import java.io.IOException;

public class Test {

    private static final String testString = "01_SS_O1_S1_Abd-TEST";

    public static void main(String[] args) {
        JsonRecordReader jsonRecordReader = new JsonRecordReader(
                new TrialDataManager(new OneTargetLabelingStrategy("RASI"),null));
        File file = new File("C:\\Users\\Nico Rinck\\IdeaProjects\\autolabeling\\src\\main\\resources\\01_SS_O1_S1_Abd-TEST.json");
        FileSplit fileSplit = new FileSplit(file);
        try {
            jsonRecordReader.initialize(fileSplit);
            while (jsonRecordReader.hasNext()) {
                System.out.println("Dataset: " + jsonRecordReader.next());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
