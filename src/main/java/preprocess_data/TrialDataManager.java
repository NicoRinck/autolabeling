package preprocess_data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.datavec.api.writable.DoubleWritable;
import org.datavec.api.writable.Writable;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class TrialDataManager {

    private Set<String> markerLabels;
    //TODO: Normalisierung? --> analog wie labels holen? Normalisation-Klasse, die eine Normaliserungsstrategie entgegennimmt.
    private final MarkerLabelingStrategy markerLabelingStrategy;
    private final FrameDataManipulator manipulator;

    public TrialDataManager(MarkerLabelingStrategy markerLabelingStrategy, FrameDataManipulator manipulator) {
        this.markerLabelingStrategy = markerLabelingStrategy;
        this.manipulator = manipulator;
    }

    public ArrayList<ArrayList<Writable>> getTrialDataFromJson(JsonArray trialData) {
        ArrayList<ArrayList<Writable>> resultList = new ArrayList<ArrayList<Writable>>();
        for (JsonElement jsonElement : trialData) {
            Frame frame = getLabeledMarkerDataFromFrame(jsonElement.getAsJsonObject());
            if (manipulator != null) {
                ArrayList<Frame> manipulatedFrames = manipulator.manipulateFrame(frame);
                resultList.addAll(getResultList(manipulatedFrames));
            } else {
                resultList.add(frame.getFrameAsWritables());
            }
        }
        return resultList;
    }

    private ArrayList<ArrayList<Writable>> getResultList(ArrayList<Frame> frames) {
        ArrayList<ArrayList<Writable>> resultList = new ArrayList<ArrayList<Writable>>();
        for (Frame frame : frames) {
            resultList.add(frame.getFrameAsWritables());
        }
        return resultList;
    }

    private Frame getLabeledMarkerDataFromFrame(JsonObject frameJson) {
        if (markerLabels == null) {//only get labels once per file
            markerLabels = getMarkerLabels(frameJson);
        }
        final ArrayList<Marker> result = new ArrayList<Marker>();
        for (String markerLabel : markerLabels) {
            result.add(new Marker(markerLabelingStrategy.getMarkerLabel(markerLabel),
                    new DoubleWritable(frameJson.get(markerLabel + "_x").getAsDouble()),
                    new DoubleWritable(frameJson.get(markerLabel + "_y").getAsDouble()),
                    new DoubleWritable(frameJson.get(markerLabel + "_z").getAsDouble())));
        }
        return new Frame(result);
    }

    private Set<String> getMarkerLabels(JsonObject sampleObject) {
        Set<String> labels = new TreeSet<String>();
        for (Map.Entry<String, JsonElement> jsonPropertyEntry : sampleObject.entrySet()) {
            int indexOfSeparator = jsonPropertyEntry.getKey().indexOf("_");
            labels.add(jsonPropertyEntry.getKey().substring(0, indexOfSeparator));
        }

        return labels;
    }
}