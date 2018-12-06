package deep_learning;

import datavec.JsonTrialRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import preprocess_data.TrialDataManager;
import preprocess_data.data_manipulator.FrameShuffleManipulator;
import preprocess_data.labeling.OneTargetLabeling;

import java.io.File;
import java.io.IOException;

public class TestNN {

    public static void main(String[] args) {
        JsonTrialRecordReader jsonTrialRecordReader = new JsonTrialRecordReader(
                new TrialDataManager(new OneTargetLabeling("RASI"),new FrameShuffleManipulator(3),null));
        File file = new File("C:\\Users\\Nico Rinck\\IdeaProjects\\autolabeling\\src\\main\\resources\\01_SS_O1_S1_Abd-TEST.json");
        FileSplit fileSplit = new FileSplit(file);
        try {
            jsonTrialRecordReader.initialize(fileSplit);
            DataSetIterator iterator = new RecordReaderDataSetIterator.Builder(jsonTrialRecordReader,100).build();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
