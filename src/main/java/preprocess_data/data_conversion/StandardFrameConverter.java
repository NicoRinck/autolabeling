package preprocess_data.data_conversion;

import org.datavec.api.writable.Writable;
import preprocess_data.data_model.Frame;
import preprocess_data.data_model.Marker;

import java.util.ArrayList;

public class StandardFrameConverter implements FrameDataConversionStrategy {

    public ArrayList<Writable> convertFrameToWritables(final Frame frame) {
        final ArrayList<Writable> featureList = new ArrayList<Writable>();
        final ArrayList<Writable> labelList = new ArrayList<Writable>();
        for (Marker marker : frame.getMarkers()) {
            featureList.addAll(marker.getCoordinates());
            labelList.add(marker.getLabel());
        }
        featureList.addAll(labelList);
        return featureList;
    }

    public ArrayList<ArrayList<Writable>> convertFrameToListOfWritables(Frame frame) {
        ArrayList<ArrayList<Writable>> resultList = new ArrayList<ArrayList<Writable>>();
        resultList.add(convertFrameToWritables(frame));
        return resultList;
    }

    public ArrayList<ArrayList<Writable>> convertFramesToListOfWritables(ArrayList<Frame> frames) {
        ArrayList<ArrayList<Writable>> resultList = new ArrayList<ArrayList<Writable>>();
        for (Frame frame : frames) {
            resultList.add(convertFrameToWritables(frame));
        }
        return resultList;
    }
}
