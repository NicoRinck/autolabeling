package preprocess_data;

import preprocess_data.data_model.Frame;

public interface FrameNormalizationStrategy {

    //takes frame and returns normalized instance of it
    Frame normalizeMarker(final Frame frame);
}
