package deep_learning;

import datavec.JsonTrialRecordReader;
import org.datavec.api.split.FileSplit;
import preprocess_data.JsonToTrialParser;
import preprocess_data.TrialDataManager;
import preprocess_data.TrialDataTransformation;
import preprocess_data.data_conversion.FrameDataConversionStrategy;
import preprocess_data.data_conversion.StandardFrameConverter;
import preprocess_data.data_manipulaton.FrameDataManipulationStrategy;
import preprocess_data.data_manipulaton.FrameShuffleManipulator;
import preprocess_data.labeling.MarkerLabelingStrategy;
import preprocess_data.labeling.OneTargetLabeling;

import java.io.File;
import java.io.IOException;

public class TestNN {

    public static void main(String[] args) {

        //Input Data
        File file = new File("C:\\Users\\Nico Rinck\\IdeaProjects\\autolabeling\\src\\main\\resources\\01_SS_O1_S1_Abd-TEST.json");
        FileSplit fileSplit = new FileSplit(file);

        MarkerLabelingStrategy markerLabelingStrategy = new OneTargetLabeling("RASI");
        JsonToTrialParser jsonToTrialParser = new JsonToTrialParser(markerLabelingStrategy);

        FrameDataConversionStrategy conversionStrategy = new StandardFrameConverter();
        FrameDataManipulationStrategy manipulationStrategy = new FrameShuffleManipulator(5);
        TrialDataTransformation transformation = new TrialDataTransformation(conversionStrategy,manipulationStrategy);

        TrialDataManager trialDataManager = new TrialDataManager(transformation,jsonToTrialParser);
        JsonTrialRecordReader jsonTrialRecordReader = new JsonTrialRecordReader(trialDataManager);
        try {
            jsonTrialRecordReader.initialize(fileSplit);

            while (jsonTrialRecordReader.hasNext()) {
                System.out.println(jsonTrialRecordReader.next());
            }
            /*DataSetIterator iterator = new RecordReaderDataSetIterator.Builder(jsonTrialRecordReader,100).build();*/

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
