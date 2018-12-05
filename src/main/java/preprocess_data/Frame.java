package preprocess_data;

import org.datavec.api.writable.Writable;

import java.util.ArrayList;

public class Frame {

    private final ArrayList<Marker> markers;

    public Frame(ArrayList<Marker> markers) {
        this.markers = markers;
    }

    public ArrayList<Writable> getFrameAsWritables() {
        final ArrayList<Writable> resultList = new ArrayList<Writable>();
        for (Marker marker : markers) {
            resultList.addAll(marker.getMarkerDataAsWritables());
        }
        return resultList;
    }
}
