package preprocess_data;

import java.util.ArrayList;

public interface FrameDataManipulator {

    //takes frame and returns one or more manipulated instances of it
    ArrayList<Frame> manipulateFrame(Frame frame);
}
