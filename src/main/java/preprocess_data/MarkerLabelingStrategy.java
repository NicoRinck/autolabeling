package preprocess_data;

import org.datavec.api.writable.Writable;

public interface MarkerLabelingStrategy {

    //e.g  has hash-list with labels as key
    Writable getMarkerLabel(String markerLabel);
}
