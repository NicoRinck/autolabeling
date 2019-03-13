package preprocess_data.labeling;

import org.datavec.api.writable.DoubleWritable;
import org.datavec.api.writable.Text;
import org.datavec.api.writable.Writable;
import preprocess_data.data_model.Coordinate3D;
import preprocess_data.data_model.Frame;

import java.util.ArrayList;
import java.util.List;

public class OneTargetDistanceLabeling implements FrameLabelingStrategy {

    private final Coordinate3D targetPoint;
    private final String targetLabel;
    private final int amountOfLabels;

    public OneTargetDistanceLabeling(final Coordinate3D targetPoint, String targetLabel) {
        this.targetPoint = targetPoint;
        this.targetLabel = targetLabel;
        this.amountOfLabels = -1;
    }
    public OneTargetDistanceLabeling(final Coordinate3D targetPoint, String targetLabel, int amountOfLabels) {
        this.targetPoint = targetPoint;
        this.targetLabel = targetLabel;
        this.amountOfLabels = amountOfLabels;
    }

    public ArrayList<Writable> getLabeledWritableList(Frame frame) {
        final ArrayList<Writable> resultList = new ArrayList<Writable>();
        int indexOfTarget = -1;
        for (int i = 0; i < frame.getMarkers().size(); i++) {
            final double distance = DistanceCalculator.getDistanceToCoordinate(frame.getMarkers().get(i),targetPoint);
            resultList.add(new DoubleWritable(distance));
            if (frame.getMarkers().get(i).getLabel().equalsIgnoreCase(targetLabel)) {
                indexOfTarget = i;
            }
        }
        resultList.add(new Text(indexOfTarget + ""));
        return resultList;
    }

    public List<String> getLabels() {
        if (amountOfLabels > 0) {
            final ArrayList<String> resultList = new ArrayList<>();
            for (int i = 0; i < amountOfLabels; i++) {
                resultList.add(i + "");
            }
            return resultList;
        }
        return null;
    }

    @Override
    public String getInfoString() {
        return null;
    }
}
