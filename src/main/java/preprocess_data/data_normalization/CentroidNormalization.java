package preprocess_data.data_normalization;

import preprocess_data.data_model.Coordinate3D;
import preprocess_data.data_model.Frame;
import preprocess_data.data_model.Marker;

import java.util.ArrayList;

public class CentroidNormalization implements TrialNormalizationStrategy {

    private final Coordinate3D addedValues = new Coordinate3D(0,0,0);
    private int counter = 0;
    private Coordinate3D centroid;

    public Frame normalizeFrame(final Frame frame) {
        calculateCentroid();
        final ArrayList<Marker> newMarkers = new ArrayList<Marker>();
        for (final Marker marker : frame.getMarkers()) {
            newMarkers.add(normalizeMarker(marker));
        }
        return new Frame(newMarkers);
    }

    private void calculateCentroid() {
        if (centroid == null) {
            centroid = new Coordinate3D(
                    addedValues.getX()/counter,
                    addedValues.getY()/counter,
                    addedValues.getZ()/counter);
        }
    }

    private Marker normalizeMarker(Marker marker) {
        return new Marker(marker.getLabel(),
                marker.getX() - centroid.getX(),
                marker.getY() - centroid.getY(),
                marker.getZ() - centroid.getZ());
    }

    public void collectMarkerData(Marker marker) {
        addedValues.add(marker.getX(),marker.getY(),marker.getZ());
        counter++;
    }

    public TrialNormalizationStrategy getNewInstance() {
        return new CentroidNormalization();
    }
}
