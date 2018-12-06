package preprocess_data.data_model;

import java.util.ArrayList;

public class Frame {

    private final ArrayList<Marker> markers;

    public Frame(ArrayList<Marker> markers) {
        this.markers = markers;
    }

    public ArrayList<Marker> getMarkers() {
        return markers;
    }
}
