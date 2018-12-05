package preprocess_data;

public interface MarkerNormalizationStrategy {

    //takes marker and returns normalized instance of it
    Marker normalizeMarker(final Marker marker);
}
