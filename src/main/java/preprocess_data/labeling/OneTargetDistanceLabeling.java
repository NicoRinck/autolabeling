package preprocess_data.labeling;

import org.datavec.api.writable.DoubleWritable;
import org.datavec.api.writable.Text;
import org.datavec.api.writable.Writable;
import preprocess_data.data_model.Coordinate3D;
import preprocess_data.data_model.Frame;
import preprocess_data.data_model.Marker;

import java.util.ArrayList;
import java.util.List;

public class OneTargetDistanceLabeling implements FrameLabelingStrategy {

    private final Coordinate3D targetPoint;
    private final String targetLabel;

    public OneTargetDistanceLabeling(final Coordinate3D targetPoint, String targetLabel) {
        this.targetPoint = targetPoint;
        this.targetLabel = targetLabel;
    }

    public ArrayList<Writable> getLabeledWritableList(Frame frame) {
        final ArrayList<Writable> resultList = new ArrayList<Writable>();
        int indexOfTarget = -1;
        for (int i = 0; i < frame.getMarkers().size(); i++) {
            final double distance = getDistanceToTarget(frame.getMarkers().get(i));
            resultList.add(new DoubleWritable(distance));
            if(frame.getMarkers().get(i).getLabel().equalsIgnoreCase(targetLabel))  {
                indexOfTarget = i;
            }
        }
        resultList.add(new Text(indexOfTarget + ""));
        return resultList;
    }

    private double getDistanceToTarget(Marker marker) {
        return Math.sqrt(Math.pow(marker.getX() - targetPoint.getX(), 2)
                + Math.pow(marker.getX() - targetPoint.getX(),2)
                + Math.pow(marker.getX() - targetPoint.getX(),2));
    }

    public List<String> getLabels() {
        return null;
    }
}
