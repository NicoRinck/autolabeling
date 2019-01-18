package preprocess_data;

import org.datavec.api.writable.Writable;
import preprocess_data.data_conversion.FrameDataConversionStrategy;
import preprocess_data.data_manipulaton.FrameDataManipulationStrategy;
import preprocess_data.data_model.Frame;

import java.util.ArrayList;

public class TrialDataTransformation {

    private final FrameDataManipulationStrategy manipulator;
    private final FrameDataConversionStrategy converter;

    public TrialDataTransformation(FrameDataConversionStrategy converter, FrameDataManipulationStrategy manipulator) {
        this.converter = converter;
        this.manipulator = manipulator;
    }

    public TrialDataTransformation(FrameDataConversionStrategy converter) {
        this.converter = converter;
        manipulator = null;
    }

    public ArrayList<ArrayList<Writable>> transformFrameData(Frame frame) {
        if (manipulator != null) {
            return converter.convertFramesToListOfWritables(manipulator.manipulateFrame(frame));
        }
        return converter.convertFrameToListOfWritables(frame);
    }
}