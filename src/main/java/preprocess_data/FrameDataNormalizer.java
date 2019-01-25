package preprocess_data;

import preprocess_data.data_model.Frame;

public class FrameDataNormalizer {

    private final FrameNormalizationStrategy normalizationStrategy;

    public FrameDataNormalizer(FrameNormalizationStrategy normalizationStrategy) {
        this.normalizationStrategy = normalizationStrategy;
    }

    public Frame normalizeFrame(final Frame frame) {
        //TODO: Normalisierung eines kompletten Frames unter Anwendung der Strategie
        return frame;
    }


}
