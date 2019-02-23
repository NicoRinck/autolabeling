package preprocess_data;

import preprocess_data.data_model.Frame;
import preprocess_data.data_model.Marker;

public interface TrialNormalizationStrategy {

    Frame normalizeFrame(Frame frame);

    //Method to set data for the normalization process from outside
    void collectMarkerData(Marker marker);

    //returns new instance of the strategy to reset values and calculations
    TrialNormalizationStrategy getNewInstance();
}
