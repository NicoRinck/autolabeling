package preprocess_data.labeling;

import org.datavec.api.writable.Writable;
import preprocess_data.data_model.Frame;

import java.util.ArrayList;

//defines how the frame-data is labeled
public interface FrameLabelingStrategy {

    ArrayList<Writable> getLabeledWritableList(Frame frame);
}
