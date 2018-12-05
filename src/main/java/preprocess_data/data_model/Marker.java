package preprocess_data.data_model;

import org.datavec.api.writable.DoubleWritable;
import org.datavec.api.writable.Writable;

import java.util.ArrayList;

public class Marker {

    private final Writable label;
    private final DoubleWritable x;
    private final DoubleWritable y;
    private final DoubleWritable z;

    public Marker(Writable label, DoubleWritable x, DoubleWritable y, DoubleWritable z) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ArrayList<Writable> getMarkerDataAsWritables() {
        ArrayList<Writable> result = new ArrayList<Writable>();
        result.add(label);
        result.add(x);
        result.add(y);
        result.add(z);
        return result;
    }
}
