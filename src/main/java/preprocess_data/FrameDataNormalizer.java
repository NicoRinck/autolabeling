package preprocess_data;

import preprocess_data.data_model.Frame;

public class FrameDataNormalizer {

    private final MarkerNormalizationStrategy normalizationStrategy;

    public FrameDataNormalizer(MarkerNormalizationStrategy normalizationStrategy) {
        this.normalizationStrategy = normalizationStrategy;
    }

    public Frame normalizeFrame(final Frame frame) {
        //TODO: Normalisierung eines kompletten Frames unter Anwendung der Strategie
        return frame;
    }


}
