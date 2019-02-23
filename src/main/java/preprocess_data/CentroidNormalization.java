package preprocess_data;

import javafx.geometry.Point3D;
import preprocess_data.data_model.Frame;
import preprocess_data.data_model.Marker;

import java.util.ArrayList;

public class CentroidNormalization implements TrialNormalizationStrategy {

    private final Point3D addedValues = new Point3D(0,0,0);
    private int counter = 0;
    private Point3D centroid;

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
            centroid = new Point3D(
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
