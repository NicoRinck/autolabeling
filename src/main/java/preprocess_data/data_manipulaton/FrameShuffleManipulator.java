package preprocess_data.data_manipulaton;

import preprocess_data.data_model.Frame;
import preprocess_data.data_model.Marker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class FrameShuffleManipulator implements FrameManipulationStrategy {

    private final int amountOfShuffles;
    private long seed;
    private final boolean hasSeed;

    public FrameShuffleManipulator(int amountOfShuffles, long seed) {
        this.amountOfShuffles = amountOfShuffles;
        this.seed = seed;
        this.hasSeed = false;
    }

    public FrameShuffleManipulator(int amountOfShuffles) {
        this.amountOfShuffles = amountOfShuffles;
        this.hasSeed = false;
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
        if (hasSeed) {
            Collections.shuffle(newList,new Random(seed));
            return new Frame(newList);
        }
        Collections.shuffle(newList);
        return new Frame(newList);
    }
}
