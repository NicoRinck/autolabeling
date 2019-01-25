package preprocess_data;

import org.datavec.api.writable.Writable;
import preprocess_data.data_manipulaton.FrameDataManipulationStrategy;
import preprocess_data.data_model.Frame;
import preprocess_data.labeling.FrameLabelingStrategy;

import java.util.ArrayList;

public class TrialDataTransformation {

    private final FrameDataManipulationStrategy manipulator;
    private final FrameConverter converter;
    private final FrameNormalizationStrategy normalizationStrategy;

    //defines how a frame of marker-data is converted to a list of writables
    public TrialDataTransformation(FrameLabelingStrategy frameLabelingStrategy,
                                   FrameDataManipulationStrategy manipulator,
                                   FrameNormalizationStrategy normalizationStrategy) {
        this.converter = new FrameConverter(frameLabelingStrategy);
        this.manipulator = manipulator;
        this.normalizationStrategy = normalizationStrategy;
    }

    public TrialDataTransformation(FrameLabelingStrategy frameLabelingStrategy) {
        this(frameLabelingStrategy,null,null);
    }

    ArrayList<ArrayList<Writable>> transformFrameData(final Frame frame) {
        if (normalizationStrategy != null) {
            return convertFrameData(normalizationStrategy.normalizeMarker(frame));
        }
        return convertFrameData(frame);
    }

    private ArrayList<ArrayList<Writable>> convertFrameData(final Frame frame) {
        if (manipulator != null) {
            return converter.convertFramesToListOfWritables(manipulator.manipulateFrame(frame));
        }
        return converter.convertFrameToListOfWritables(frame);
    }

    public FrameConverter getConverter() {
        return converter;
    }
}