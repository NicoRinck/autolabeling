package preprocess_data;

import com.google.gson.JsonArray;
import org.datavec.api.conf.Configuration;
import org.datavec.api.records.Record;
import org.datavec.api.records.metadata.RecordMetaData;
import org.datavec.api.records.reader.BaseRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.api.writable.Writable;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonRecordReader extends BaseRecordReader {

    private final TrialDataManager trialDataManager;
    //TODO: Manager, der bereits vervielfältigte, geshuffelte und gelabelte Daten liefert, über die nur noch iteriert wird
    private Configuration configuration;
    private Iterator<JsonArray> fileIterator;
    private Iterator<ArrayList<Writable>> fileContentIterator;

    public JsonRecordReader(TrialDataManager trialDataManager) {
        this.trialDataManager = trialDataManager;
    }

    //only accept File inputSplit
    public void initialize(InputSplit inputSplit) throws IOException, InterruptedException, IllegalArgumentException {
        if (!(inputSplit instanceof FileSplit)) {
            throw new IllegalArgumentException("JsonRecordReader is for file Input only");
        }
        fileIterator = new TrialFileIterator((FileSplit) inputSplit);
        fileContentIterator = trialDataManager.getTrialDataFromJson(fileIterator.next(),false).iterator();
    }

    public void initialize(Configuration configuration, InputSplit inputSplit) throws IOException, InterruptedException, IllegalArgumentException {
        this.configuration = configuration;
        initialize(inputSplit);
    }

    public List<Writable> next() {
        return null;
    }

    public boolean hasNext() {
        return !(!fileIterator.hasNext() && !fileContentIterator.hasNext());

    }

    public List<String> getLabels() {
        return null;
    }

    public void reset() {

    }

    public boolean resetSupported() {
        return false;
    }

    public List<Writable> record(URI uri, DataInputStream dataInputStream) throws IOException {
        return null;
    }

    public Record nextRecord() {
        return null;
    }

    public Record loadFromMetaData(RecordMetaData recordMetaData) throws IOException {
        return null;
    }

    public List<Record> loadFromMetaData(List<RecordMetaData> list) throws IOException {
        return null;
    }

    public void close() throws IOException {

    }

    public void setConf(Configuration configuration) {

    }

    public Configuration getConf() {
        return null;
    }
}
