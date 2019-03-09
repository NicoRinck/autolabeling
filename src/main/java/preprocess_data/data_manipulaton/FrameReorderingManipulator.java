package preprocess_data.data_manipulaton;

import preprocess_data.data_model.Frame;
import preprocess_data.data_model.Marker;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class FrameReorderingManipulator implements FrameManipulationStrategy {

    private final int reorderedFrames;
    private final int originalFrames;

    public FrameReorderingManipulator(int reorderedFrames, int originalFrames) {
        this.reorderedFrames = reorderedFrames;
        this.originalFrames = originalFrames;
    }

    @Override
    public ArrayList<Frame> manipulateFrame(Frame frame) {
        final ArrayList<Frame> resultList = new ArrayList<>();
        for (int i = 0; i < reorderedFrames; i++) {
            ArrayList<Marker> markers = new ArrayList<>(frame.getMarkers());
            Collections.shuffle(markers);
            resultList.add(new Frame(markers));
        }
        for (int i = 0; i < originalFrames; i++) {
            resultList.add(frame);
        }
        Collections.shuffle(resultList);
        return resultList;
    }

}
