package preprocess_data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import preprocess_data.data_model.Frame;
import preprocess_data.data_model.Marker;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class JsonToTrialParser {

    private Set<String> markerLabels;

    Frame getFrameFromJson(JsonObject frameJson) {
        //only get labels once
        if (markerLabels == null) {
            markerLabels = getMarkerLabels(frameJson);
        }
        final ArrayList<Marker> result = new ArrayList<Marker>();
        for (String markerLabel : markerLabels) {
            result.add(new Marker(markerLabel,
                    frameJson.get(markerLabel + "_x").getAsDouble(),
                    frameJson.get(markerLabel + "_y").getAsDouble(),
                    frameJson.get(markerLabel + "_z").getAsDouble()));
        }
        return new Frame(result);
    }

    Set<String> getMarkerLabels(@NotNull JsonObject sampleObject) {
        Set<String> labels = new TreeSet<String>();
        for (Map.Entry<String, JsonElement> jsonPropertyEntry : sampleObject.entrySet()) {
            int indexOfSeparator = jsonPropertyEntry.getKey().indexOf("_");
            labels.add(jsonPropertyEntry.getKey().substring(0, indexOfSeparator));
        }
        return labels;
    }
}
