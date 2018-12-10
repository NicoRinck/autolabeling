package preprocess_data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.datavec.api.writable.DoubleWritable;
import preprocess_data.data_model.Frame;
import preprocess_data.data_model.Marker;
import preprocess_data.labeling.MarkerLabelingStrategy;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class JsonToTrialParser {

    private final MarkerLabelingStrategy labelingStrategy;
    private Set<String> markerLabels;

    public JsonToTrialParser(MarkerLabelingStrategy labelingStrategy) {
        this.labelingStrategy = labelingStrategy;
    }

    public Frame getLabeledFrameFromJson(JsonObject frameJson) {
        if (markerLabels == null) {//only get labels once per file
            markerLabels = getMarkerLabels(frameJson);
        }
        final ArrayList<Marker> result = new ArrayList<Marker>();
        for (String markerLabel : markerLabels) {
            result.add(new Marker(labelingStrategy.getMarkerLabel(markerLabel),
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
