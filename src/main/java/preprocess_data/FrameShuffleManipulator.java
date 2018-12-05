package preprocess_data;

import org.datavec.api.writable.Writable;

import java.util.ArrayList;

public class FrameShuffleManipulator implements FrameDataManipulator {

    private final int amountOfShuffles;

    public FrameShuffleManipulator(int amountOfShuffles) {
        this.amountOfShuffles = amountOfShuffles;
    }

    public ArrayList<Frame> manipulateFrame(Frame frame) {
        return null;
    }
}
