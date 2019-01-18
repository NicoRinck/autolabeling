package preprocess_data.labeling;

import org.datavec.api.writable.DoubleWritable;
import org.datavec.api.writable.IntWritable;
import org.datavec.api.writable.Writable;
import preprocess_data.data_model.Frame;
import preprocess_data.data_model.Marker;

import java.util.ArrayList;

public class OneTargetLabeling implements FrameLabelingStrategy {

    private final String targetLabel;

    public OneTargetLabeling(String targetLabel) {
        this.targetLabel = targetLabel;
    }

    public ArrayList<Writable> getLabeledWritableList(Frame frame) {
        int indexOfTarget = -1;
        final ArrayList<Writable> resultList = new ArrayList<Writable>();
        for (int i = 0; i < frame.getMarkers().size(); i++) {
            Marker currentMarker = frame.getMarkers().get(i);
            resultList.add(new DoubleWritable(currentMarker.getX()));
            resultList.add(new DoubleWritable(currentMarker.getY()));
            resultList.add(new DoubleWritable(currentMarker.getZ()));
            if (currentMarker.getLabel().equalsIgnoreCase(targetLabel)) {
                indexOfTarget = i;
            }
        }
        resultList.add(new IntWritable(indexOfTarget));
        return resultList;
    }
}
