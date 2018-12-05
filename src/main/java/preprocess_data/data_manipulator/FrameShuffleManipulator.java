package preprocess_data.data_manipulator;

import preprocess_data.data_model.Frame;
import preprocess_data.data_model.Marker;

import java.util.ArrayList;
import java.util.Collections;

public class FrameShuffleManipulator implements FrameDataManipulator {

    private final int amountOfShuffles;

    public FrameShuffleManipulator(int amountOfShuffles) {
        this.amountOfShuffles = amountOfShuffles;
    }

    public ArrayList<Frame> manipulateFrame(Frame frame) {
        ArrayList<Frame> resultFrames = new ArrayList<Frame>();
        for (int i = 0; i < amountOfShuffles; i++) {
            resultFrames.add(getShuffledFrame(frame));
        }
        return resultFrames;
    }

    private Frame getShuffledFrame(Frame frame) {
        ArrayList<Marker> newList = new ArrayList<Marker>(frame.getMarkers());
        Collections.shuffle(newList);
        return new Frame(newList);
    }
}
