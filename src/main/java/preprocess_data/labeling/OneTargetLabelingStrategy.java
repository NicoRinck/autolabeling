package preprocess_data.labeling;

import org.datavec.api.writable.IntWritable;
import org.datavec.api.writable.Writable;

public class OneTargetLabelingStrategy implements MarkerLabelingStrategy {

    private final String targetLabel;

    public OneTargetLabelingStrategy(String targetLabel) {
        this.targetLabel = targetLabel;
    }

    public Writable getMarkerLabel(String markerLabel) {
        if (markerLabel.equalsIgnoreCase(targetLabel)) {
            return new IntWritable(1);
        }
        return new IntWritable(0);
    }
}
