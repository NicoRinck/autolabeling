package deep_learning.tests;

import datavec.JsonTrialRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import preprocess_data.TrialDataManager;
import preprocess_data.TrialDataTransformation;
import preprocess_data.data_manipulaton.FrameShuffleManipulator;
import preprocess_data.data_normalization.CentroidNormalization;
import preprocess_data.data_normalization.TrialNormalizationStrategy;
import preprocess_data.labeling.DistanceToMarkerLabeling;
import preprocess_data.labeling.FrameLabelingStrategy;

import java.io.File;

public class TestDistanceToMarkerLabeling {

    public static void main(String[] args) throws Exception {
        String[] allowedFileFormat = {"json"};
        //Input Data
        File trainDirectory = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\train\\01_SS_O1_S1_Abd.json");
        File testDirectory = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\test\\01_SS_O1_S1_Abd.json");
        FileSplit fileSplitTrain = new FileSplit(trainDirectory, allowedFileFormat);
        FileSplit fileSplitTest = new FileSplit(testDirectory, allowedFileFormat);

        //Strategies/Assets
        //falsche Reihenfolge:
        String[] orderedLabels = {"RULN", "LSCAP4", "RHUMS", "RRAD", "T10", "THRX4", "RSCAP3", "LELB", "LSCAP3", "C7", "RHUMP", "THRX3", "LHUMA", "RSCAP4", "RHUM4", "CLAV", "STRN", "RELB", "RHUMA", "LULN", "LSCAP2", "LHUM4", "SACR", "THRX2", "RSCAP1", "LASI", "LRAD", "LSCAP1", "LHUMP", "RELBW", "RASI", "THRX1", "LELBW", "RSCAP2", "LHUMS"};
        //richtige (default) Reihenfolge;
        String[] orderedLabels2 = {"C7", "CLAV", "LASI", "LELB", "LELBW", "LHUM4", "LHUMA", "LHUMP", "LHUMS", "LRAD", "LSCAP1", "LSCAP2", "LSCAP3", "LSCAP4", "LULN", "RASI", "RELB", "RELBW", "RHUM4", "RHUMA", "RHUMP", "RHUMS", "RRAD", "RSCAP1", "RSCAP2", "RSCAP3", "RSCAP4", "RULN", "SACR", "STRN", "T10", "THRX1", "THRX2", "THRX3", "THRX4"};
        FrameLabelingStrategy frameLabelingStrategy = new DistanceToMarkerLabeling(orderedLabels2);
        TrialNormalizationStrategy normalizationStrategy = new CentroidNormalization();
        FrameShuffleManipulator shuffleManipulator = new FrameShuffleManipulator(3);
        TrialDataTransformation transformation = new TrialDataTransformation(frameLabelingStrategy, null);
        TrialDataManager trialDataManager = new TrialDataManager(transformation, normalizationStrategy);

        //DataSet Iterators
        JsonTrialRecordReader trainDataReader = new JsonTrialRecordReader(trialDataManager);
        trainDataReader.initialize(fileSplitTrain);
        JsonTrialRecordReader testDataReader = new JsonTrialRecordReader(trialDataManager);
        testDataReader.initialize(fileSplitTest);

        DataSetIterator trainData = new RecordReaderDataSetIterator(trainDataReader,20);
        DataSetIterator testData = new RecordReaderDataSetIterator(testDataReader,20);


    }
}
