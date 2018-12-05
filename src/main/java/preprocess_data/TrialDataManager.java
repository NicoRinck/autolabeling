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
    //TODO: Normalisierung? --> analog wie labels holen?
    private final MarkerLabelingStrategy markerLabelingStrategy;
    private final FrameDataManipulator manipulator;

    public TrialDataManager(MarkerLabelingStrategy markerLabelingStrategy, FrameDataManipulator manipulator) {
        this.markerLabelingStrategy = markerLabelingStrategy;
        this.manipulator = manipulator;
    }

    public ArrayList<ArrayList<Writable>> getTrialDataFromJson(JsonArray trialData, boolean manipulateData) {
        ArrayList<ArrayList<Writable>> resultList = new ArrayList<ArrayList<Writable>>();
        for (JsonElement jsonElement : trialData) {
            ArrayList<Writable> writables = getLabeledMarkerDataFromFrame(jsonElement.getAsJsonObject());
            if (manipulateData && manipulator != null) {
                resultList.addAll(manipulator.manipulateFrameData(writables));
            } else {
                resultList.add(writables);
            }
        }
        return resultList;
    }

    private ArrayList<Writable> getLabeledMarkerDataFromFrame(JsonObject frameJson) {
        if (markerLabels == null) {//only get labels once per file
            markerLabels = getMarkerLabels(frameJson);
        }
        final ArrayList<Writable> result = new ArrayList<Writable>();
        for (String markerLabel : markerLabels) {
            result.add(markerLabelingStrategy.getMarkerLabel(markerLabel));
            result.add(new DoubleWritable(frameJson.get(markerLabel + "_x").getAsDouble()));
            result.add(new DoubleWritable(frameJson.get(markerLabel + "_y").getAsDouble()));
            result.add(new DoubleWritable(frameJson.get(markerLabel + "_z").getAsDouble()));
        }
        return result;
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