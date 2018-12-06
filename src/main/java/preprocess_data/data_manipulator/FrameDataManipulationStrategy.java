package preprocess_data.data_manipulator;

import preprocess_data.data_model.Frame;

import java.util.ArrayList;

public interface FrameDataManipulationStrategy {

    //takes frame and returns one or more manipulated instances of it
    ArrayList<Frame> manipulateFrame(Frame frame);
}
