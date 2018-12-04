package preprocess_data;

import org.datavec.api.writable.Writable;

import java.util.ArrayList;

public interface FrameDataManipulator {

    //takes frame data and returns one or more manipulated instances of it
    ArrayList<ArrayList<Writable>> manipulateFrameData(ArrayList<Writable> frameData);
}
