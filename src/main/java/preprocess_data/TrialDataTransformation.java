package preprocess_data;

import org.datavec.api.writable.Writable;
import preprocess_data.data_manipulaton.FrameDataManipulationStrategy;
import preprocess_data.data_model.Frame;
import preprocess_data.labeling.FrameLabelingStrategy;

import java.util.ArrayList;

public class TrialDataTransformation {

    private final FrameDataManipulationStrategy manipulator;
    private final FrameConverter converter;

    //defines how a frame of marker-data is converted to a list of writables
    public TrialDataTransformation(FrameLabelingStrategy frameLabelingStrategy, FrameDataManipulationStrategy manipulator) {
        this.converter = new FrameConverter(frameLabelingStrategy);
        this.manipulator = manipulator;
    }

    public TrialDataTransformation(FrameLabelingStrategy frameLabelingStrategy) {
        this.converter = new FrameConverter(frameLabelingStrategy);
        manipulator = null;
    }

    ArrayList<ArrayList<Writable>> transformFrameData(Frame frame) {
        if (manipulator != null) {
            return converter.convertFramesToListOfWritables(manipulator.manipulateFrame(frame));
        }
        return converter.convertFrameToListOfWritables(frame);
    }

    public FrameConverter getConverter() {
        return converter;
    }
}