package preprocess_data.data_conversion;

import org.datavec.api.writable.Writable;
import preprocess_data.data_model.Frame;

import java.util.ArrayList;

//describes how to convert a frame to a array of writables
public interface FrameDataConversionStrategy {

    //convert a single Frame
    ArrayList<ArrayList<Writable>> convertFrameToListOfWritables(Frame frame);

    //convert multiple Frames (in case of data augmentation
    ArrayList<ArrayList<Writable>> convertFramesToListOfWritables(ArrayList<Frame> frames);
}
