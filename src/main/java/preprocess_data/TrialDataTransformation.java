package preprocess_data;

import org.datavec.api.writable.Writable;
import preprocess_data.data_manipulaton.FrameManipulationStrategy;
import preprocess_data.data_model.Frame;
import preprocess_data.labeling.FrameLabelingStrategy;

import java.util.ArrayList;

public class TrialDataTransformation {

    private final FrameManipulationStrategy manipulator;
    private final FrameConverter converter;

    //defines how a frame of marker-data is converted to a list of writables (datavec-format)
    public TrialDataTransformation(FrameLabelingStrategy frameLabelingStrategy,
                                   FrameManipulationStrategy manipulator) {
        this.converter = new FrameConverter(frameLabelingStrategy);
        this.manipulator = manipulator;
    }

    ArrayList<ArrayList<Writable>> transformFrameData(final Frame frame) {
        if (manipulator != null) {
            return converter.convertFramesToListOfWritables(manipulator.manipulateFrame(frame));
        }
        return converter.convertFrameToListOfWritables(frame);
    }

    public FrameConverter getConverter() {
        return converter;
    }
}