package process_data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class JsonToMarkerConverter {

    private final JsonParser jsonParser = new JsonParser();
    private static final String TRIAL_PROPERTY = "trial";
    private static final String FRAMES_PROPERTY = "frames";

    public ArrayList<ArrayList<Marker>> getMarkersFromJson(String fileName) {
        JsonObject jsonObject = getJsonObjectFromFile(fileName);
        ArrayList<ArrayList<Marker>> markers = new ArrayList<ArrayList<Marker>>();
        if (jsonObject != null && jsonObject.has(TRIAL_PROPERTY)) {
            JsonArray jsonArray = getFramesArray(jsonObject);
            for (JsonElement jsonElement : jsonArray) {
                markers.add(getMarkersFromJsonObject(jsonElement.getAsJsonObject()));
            }
        }
        return markers;
    }

    private ArrayList<Marker> getMarkersFromJsonObject(JsonObject jsonObject) {
        final Set<String> markerLabels = getMarkerLabels(jsonObject);
        final ArrayList<Marker> markerList = new ArrayList<Marker>();
        for (String markerLabel : markerLabels) {
            markerList.add(new Marker(
                    markerLabel,
                    jsonObject.get(markerLabel + "_x").getAsDouble(),
                    jsonObject.get(markerLabel + "_y").getAsDouble(),
                    jsonObject.get(markerLabel + "_z").getAsDouble()
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

    private JsonArray getFramesArray(JsonObject jsonObject) {
        return jsonObject.getAsJsonObject(TRIAL_PROPERTY).getAsJsonArray(FRAMES_PROPERTY);
    }

    private InputStream getJSONFromResource(String name) {
        return this.getClass().getResourceAsStream("/" + name + ".json");
    }

    private JsonObject getJsonObjectFromFile(String fileName) {
        JsonElement jsonElement = jsonParser.parse(new InputStreamReader(getJSONFromResource(fileName)));
        return jsonElement.getAsJsonObject();
    }
}