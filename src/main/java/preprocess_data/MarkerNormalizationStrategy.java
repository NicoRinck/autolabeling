package preprocess_data;

import preprocess_data.data_model.Marker;

public interface MarkerNormalizationStrategy {

    //takes marker and returns normalized instance of it
    Marker normalizeMarker(final Marker marker);
}
