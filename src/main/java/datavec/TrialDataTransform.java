package datavec;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.transform.TransformProcess;
import org.datavec.api.transform.schema.Schema;
import org.datavec.api.writable.Writable;

import java.util.List;

public class TrialDataTransform {

    private final Schema schema;
    private final RecordReader recordReader;

    public TrialDataTransform(Schema schema, RecordReader recordReader) {
        this.schema = schema;
        this.recordReader = recordReader;
    }

    private TransformProcess getTransformProzess() {
        return new TransformProcess.Builder(schema).build();
    }

    public List<List<Writable>> executeTransformation() {
        return null; //current status: reader returns optimal data --> this function is not needed
    }

}
