package datavec;

import org.datavec.api.records.SequenceRecord;
import org.datavec.api.records.metadata.RecordMetaData;
import org.datavec.api.records.reader.SequenceRecordReader;
import org.datavec.api.split.InputSplit;
import org.datavec.api.writable.Writable;
import preprocess_data.TrialDataManager;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class SequenceJsonTrialRecordReader extends JsonTrialRecordReader implements SequenceRecordReader {

    private final int sequenceLength;
    private int currentTrialAmountOfFrames;
    private int currentFrameIndex;

    public SequenceJsonTrialRecordReader(TrialDataManager trialDataManager, int sequenceLength) {
        super(trialDataManager);
        this.sequenceLength = sequenceLength;
    }

    @Override
    public void initialize(InputSplit inputSplit) throws IOException, InterruptedException, IllegalArgumentException {
        super.initialize(inputSplit);
        this.currentTrialAmountOfFrames = trialDataManager.getAmountOfFrames();//UPDATE!!
        this.currentFrameIndex = 0;
    }

    @Override
    //einzeln für jeden Marker --> storage (shuffle?)
    //erster Frame verwerfern, (2ter Frame für Labeling benötigt
    //letzer Frame --> ist der vorletzte
    //wo befinden sich die Labels im finalen sequentiellen DS
    //da variable Anzahl an Zeitschritten möglich sind --> wohl im DS des Zeitpunkts
    public List<List<Writable>> sequenceRecord() {
        int framesLeftInTrial = framesLeftInTrial();
        if (framesLeftInTrial >= sequenceLength) {
            this.currentFrameIndex += sequenceLength;
            return getNextTrialSequence(sequenceLength);
        } else if (framesLeftInTrial > 0) {
            return getNextTrialSequence(framesLeftInTrial);
        } else if (fileIterator.hasNext()) {
            trialDataManager.setTrialContent(fileIterator.next());
            resetIndexes();
            return sequenceRecord();
        } else
            throw new NoSuchElementException();
    }

    private void resetIndexes() {
        this.currentFrameIndex = 0;
        this.currentTrialAmountOfFrames = trialDataManager.getAmountOfFrames();
    }

    private List<List<Writable>> getNextTrialSequence(int sequenceLength) {
        ArrayList<List<Writable>> resultList = new ArrayList<>();
        for (int length = sequenceLength; length > 0; length--) {
            resultList.add(super.next());
        }
        return resultList;
    }

    private int framesLeftInTrial() {
        return currentTrialAmountOfFrames - (currentFrameIndex + sequenceLength);
    }

    @Override
    public List<List<Writable>> sequenceRecord(URI uri, DataInputStream dataInputStream) throws IOException {
        return null;
    }

    @Override
    public SequenceRecord nextSequence() {
        return null;
    }

    @Override
    public SequenceRecord loadSequenceFromMetaData(RecordMetaData recordMetaData) throws IOException {
        return null;
    }

    @Override
    public List<SequenceRecord> loadSequenceFromMetaData(List<RecordMetaData> list) throws IOException {
        return null;
    }
}
