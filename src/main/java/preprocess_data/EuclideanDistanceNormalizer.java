package preprocess_data;

import preprocess_data.data_model.Frame;
import preprocess_data.data_model.Marker;

import java.util.ArrayList;

public class EuclideanDistanceNormalizer implements FrameNormalizationStrategy {

    public Frame normalizeMarker(final Frame frame) {
        final ArrayList<Marker> normalizedMarkers = new ArrayList<Marker>();

        for (Marker marker : frame.getMarkers()) {
            double distance = getEuclideanDistance(marker);
            normalizedMarkers.add(getNormalizedMarker(marker,distance));
        }
        return new Frame(normalizedMarkers);
    }

    private Marker getNormalizedMarker(final Marker oldMarker, double distance) {
        return new Marker(oldMarker.getLabel(),
                oldMarker.getX()/distance,
                oldMarker.getY()/distance,
                oldMarker.getZ()/distance);
    }

    private double getEuclideanDistance(final Marker marker) {
        return Math.sqrt(getSquareOf(marker.getX()) + getSquareOf(marker.getY()) + getSquareOf(marker.getZ()));
    }

    private double getSquareOf(final double d) {
        return Math.pow(d,2);
    }
}
