package deep_learning;

import datavec.JsonTrialRecordReader;
import org.datavec.api.split.FileSplit;
import preprocess_data.JsonToTrialParser;
import preprocess_data.TrialDataManager;
import preprocess_data.TrialDataTransformation;
import preprocess_data.data_manipulaton.FrameDataManipulationStrategy;
import preprocess_data.data_manipulaton.FrameShuffleManipulator;
import preprocess_data.labeling.FrameLabelingStrategy;
import preprocess_data.labeling.OneTargetLabeling;

import java.io.File;
import java.io.IOException;

public class TestNN {

    public static void main(String[] args) {

        //Input Data
        File file = new File("C:\\Users\\nico.rinck\\Desktop\\stack1\\01_SS_O1_S1_AR.json");
        FileSplit fileSplit = new FileSplit(file);

        FrameLabelingStrategy frameLabelingStrategy = new OneTargetLabeling("RASI");
        JsonToTrialParser jsonToTrialParser = new JsonToTrialParser();

        FrameDataManipulationStrategy manipulationStrategy = new FrameShuffleManipulator(5);
        TrialDataTransformation transformation = new TrialDataTransformation(frameLabelingStrategy,manipulationStrategy);

        TrialDataManager trialDataManager = new TrialDataManager(transformation,jsonToTrialParser);
        JsonTrialRecordReader jsonTrialRecordReader = new JsonTrialRecordReader(trialDataManager);
        try {
            jsonTrialRecordReader.initialize(fileSplit);

            while (jsonTrialRecordReader.hasNext()) {
                System.out.println(jsonTrialRecordReader.next());
            }
            /*DataSetIterator iterator = new RecordReaderDataSetIterator.Builder(jsonTrialRecordReader,10)*/

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}