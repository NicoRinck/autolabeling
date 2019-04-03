package test.tests.lstm;

import datavec.JsonTrialRecordReader;
import datavec.SequentialMarkerwiseTrialRecordReader;
import org.datavec.api.records.reader.SequenceRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.writable.Writable;
import preprocess_data.TrialDataManager;
import preprocess_data.builders.TrialDataManagerBuilder;
import preprocess_data.builders.TrialDataTransformationBuilder;
import preprocess_data.labeling.FrameLabelingStrategy;
import preprocess_data.labeling.NoLabeling;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

public class TestSequentialRecordReader {

    public static void main(String[] args) throws IOException, InterruptedException {
        File trainDirectory = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\trainData\\train\\01_SS_O1_S1_Abd.json");
        String[] markerLabels = {"C7", "CLAV", "LASI", "LELB", "LELBW", "LHUM4", "LHUMA", "LHUMP", "LHUMS", "LRAD", "LSCAP1", "LSCAP2", "LSCAP3", "LSCAP4", "LULN", "RASI", "RELB", "RELBW", "RHUM4", "RHUMA", "RHUMP", "RHUMS", "RRAD", "RSCAP1", "RSCAP2", "RSCAP3", "RSCAP4", "RULN", "SACR", "STRN", "T10", "THRX1", "THRX2", "THRX3", "THRX4"};
        TreeSet<String> selectedLabels = new TreeSet<>(Arrays.asList(markerLabels));
        int sequenceLength = 20;

        FrameLabelingStrategy frameLabelingStrategy = new NoLabeling();
        TrialDataManager dataManager = TrialDataManagerBuilder
                .addTransformation(TrialDataTransformationBuilder.addLabelingStrategy(frameLabelingStrategy).build())
                .build();
        SequenceRecordReader recordReader = new SequentialMarkerwiseTrialRecordReader(dataManager, selectedLabels, sequenceLength);
        recordReader.initialize(new FileSplit(trainDirectory));

        //Standard Reader
        TreeSet<String> filterMarker = new TreeSet<>();
        filterMarker.add(selectedLabels.iterator().next());
        TrialDataManager dataManager1 = TrialDataManagerBuilder
                .addTransformation(TrialDataTransformationBuilder.addLabelingStrategy(frameLabelingStrategy).build())
                .filterMarkers(filterMarker)
                .build();
        JsonTrialRecordReader recordReader1 = new JsonTrialRecordReader(dataManager1);
        recordReader1.initialize(new FileSplit(trainDirectory));

        for (List<Writable> writables : recordReader.sequenceRecord()) {
            System.out.println(writables);
        }
        System.out.println("--------------------");
        for (List<Writable> writables : recordReader.sequenceRecord()) {
            System.out.println(writables);
        }

        System.out.println("Original:");
        for (int i = 0; i < 2; i++) {
            for (int length = sequenceLength; length > 0; length--) {
                System.out.println(recordReader1.next());
            }
            System.out.println("-----------------------");
        }
    }
}
