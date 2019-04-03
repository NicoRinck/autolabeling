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
import java.util.*;

public class SequentialMarkerwiseTrialRecordReader extends JsonTrialRecordReader implements SequenceRecordReader {

    private final boolean hasSequenceLength;
    private int sequenceLength = -1;
    private Set<String> markerStrings;
    private Iterator<String> markerStringsIterator;
    private int currentTrialAmountOfFrames;
    private int currentFrameIndex;

    public SequentialMarkerwiseTrialRecordReader(TrialDataManager dataManager, Set<String> markerStrings, int sequenceLength) {
        super(dataManager);
        this.markerStrings = markerStrings;
        this.sequenceLength = sequenceLength;
        this.hasSequenceLength = false;
    }

    public SequentialMarkerwiseTrialRecordReader(TrialDataManager dataManager, Set<String> markerStrings) {
        super(dataManager);
        this.markerStrings = markerStrings;
        this.hasSequenceLength = false;
    }

    @Override
    public void initialize(InputSplit inputSplit) throws IOException, InterruptedException, IllegalArgumentException {
        super.initialize(inputSplit);
        initNewTrial();
    }

    @Override
    public List<List<Writable>> sequenceRecord() {
        int framesLeftInTrial = framesLeftInTrial();
        if (framesLeftInTrial >= sequenceLength) {
            this.currentFrameIndex += sequenceLength;
            return getNextTrialSequence(sequenceLength);
        } else if (framesLeftInTrial > 0) {
            return getNextTrialSequence(framesLeftInTrial);
        } else if (markerStringsIterator.hasNext()) {
            setMarkerFilter(markerStringsIterator.next());
            return sequenceRecord();
        } else if (fileIterator.hasNext()) {
            trialDataManager.setTrialContent(fileIterator.next());
            resetForNextTrial();
            return sequenceRecord();
        } else
            throw new NoSuchElementException();
    }

    private void setMarkerFilter(String markerString) {
        Set<String> newSet = new HashSet<>();
        newSet.add(markerString);
        this.trialDataManager.setNewFilter(newSet);
    }

    private void resetForNextTrial() {
        this.markerStringsIterator = markerStrings.iterator();
        initNewTrial();
    }

    private void initNewTrial() {
        this.currentFrameIndex = 0;
        this.currentTrialAmountOfFrames = trialDataManager.getAmountOfFrames();
        if (!hasSequenceLength) {
            sequenceLength = currentTrialAmountOfFrames;
        }
        setMarkerFilter(markerStringsIterator.next()); //set first Marker as Filter
    }

    private List<List<Writable>> getNextTrialSequence(int sequenceLength) {
        ArrayList<List<Writable>> resultList = new ArrayList<>();
        for (int length = sequenceLength; length > 0; length--) {
            resultList.add(super.next());
        }
        return resultList;
    }

    private int framesLeftInTrial() {
        if (!hasSequenceLength) {
            return sequenceLength;
        }
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
