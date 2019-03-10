package preprocess_data.data_manipulaton;

import preprocess_data.TrialDataTransformation;
import preprocess_data.data_model.Frame;
import preprocess_data.data_model.Marker;
import preprocess_data.labeling.DistanceToMarkerLabeling;

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
            final ArrayList<Marker> markers = new ArrayList<>(frame.getMarkers());
            Collections.shuffle(markers);
            resultList.add(new Frame(markers));
        }
        for (int i = 0; i < originalFrames; i++) {
            resultList.add(frame);
        }
        Collections.shuffle(resultList);
        return resultList;
    }

    public static void main(String[] args) {
        ArrayList<Marker> markers = new ArrayList<>();
        markers.add(new Marker("1", 1, 1, 1));
        markers.add(new Marker("2", 2, 2, 2));
        markers.add(new Marker("3", 3, 3, 3));
        markers.add(new Marker("4", 4, 4, 4));
        markers.add(new Marker("5", 5, 5, 5));
        markers.add(new Marker("6", 6, 6, 6));
        markers.add(new Marker("7", 7, 7, 7));
        markers.add(new Marker("8", 8, 8, 8));
        markers.add(new Marker("9", 9, 9, 9));
        Frame frame = new Frame(markers);
        ArrayList<Marker> markers2 = new ArrayList<>();
        markers2.add(new Marker("1", 10, 10, 10));
        markers2.add(new Marker("2", 20, 20, 20));
        markers2.add(new Marker("3", 30, 30, 30));
        markers2.add(new Marker("4", 40, 40, 40));
        markers2.add(new Marker("5", 50, 50, 50));
        markers2.add(new Marker("6", 60, 60, 60));
        markers2.add(new Marker("7", 70, 70, 70));
        markers2.add(new Marker("8", 80, 80, 80));
        markers2.add(new Marker("9", 90, 90, 90));
        Frame frame2 = new Frame(markers2);
        ArrayList<Marker> markers3 = new ArrayList<>();
        markers3.add(new Marker("1", 100, 100, 100));
        markers3.add(new Marker("2", 200, 200, 200));
        markers3.add(new Marker("3", 300, 300, 300));
        markers3.add(new Marker("4", 400, 400, 400));
        markers3.add(new Marker("5", 500, 500, 500));
        markers3.add(new Marker("6", 600, 600, 600));
        markers3.add(new Marker("7", 700, 700, 700));
        markers3.add(new Marker("8", 800, 800, 800));
        markers3.add(new Marker("9", 900, 900, 900));
        Frame frame3 = new Frame(markers3);

        ArrayList<Frame> frames = new ArrayList<>();
        frames.add(frame);
        frames.add(frame2);
        frames.add(frame3);

        FrameReorderingManipulator frameReorderingManipulator = new FrameReorderingManipulator(5, 2);
        String[] orderedLabels2 = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        DistanceToMarkerLabeling distanceToMarkerLabeling = new DistanceToMarkerLabeling(orderedLabels2);
        TrialDataTransformation trialDataTransformation = new TrialDataTransformation(distanceToMarkerLabeling, frameReorderingManipulator);
        for (Frame frame1 : frames) {
            System.out.println("_____________");
            trialDataTransformation.transformFrameData(frame1).forEach(System.out::println);
            System.out.println("_____________");
            System.out.println();
        }
    }
}
