package preprocess_data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class TrialDataParser {

    private final JsonParser jsonParser = new JsonParser();
    private Set<String> markerLabels;


    public ArrayList<ArrayList<Marker>> getTrialDataFromJson(JsonArray trialData) {
        ArrayList<ArrayList<Marker>> markers = new ArrayList<ArrayList<Marker>>();
        for (JsonElement jsonElement : trialData) {
            markers.add(getMarkersFromFrameObject(jsonElement.getAsJsonObject()));
        }
        return markers;
    }

    private ArrayList<Marker> getMarkersFromFrameObject(JsonObject frameJson) {
        if (markerLabels == null) {//only get lanels once per file
            markerLabels = getMarkerLabels(frameJson);
        }
        final ArrayList<Marker> markerList = new ArrayList<Marker>();
        for (String markerLabel : markerLabels) {
            markerList.add(new Marker(markerLabel,
                    frameJson.get(markerLabel + "_x").getAsDouble(),
                    frameJson.get(markerLabel + "_y").getAsDouble(),
                    frameJson.get(markerLabel + "_z").getAsDouble()
            ));
        }
        return markerList;
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