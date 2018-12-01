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


    public ArrayList<Marker> getMarkersFromJson(String fileName) {
        JsonObject jsonObject = getJsonObjectFromFile(fileName);
        if (jsonObject != null && jsonObject.has(TRIAL_PROPERTY)) {
            JsonArray jsonArray = getFramesArray(jsonObject);
            getMarkersFromJsonObject(jsonArray.get(0).getAsJsonObject());
        }
        return null;
    }

    private ArrayList<Marker> getMarkersFromJsonObject(JsonObject jsonObject) {
        for (String s : getLabels(jsonObject)) {
            System.out.println(s);
        }
        final Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            /*System.out.println(entry.getKey());*/
        }
        return null;
    }

    private Set<String> getLabels(JsonObject sampleObject) {
        Set<String> labels = new TreeSet<String>();
        for (Map.Entry<String, JsonElement> jsonPropertyEntry : sampleObject.entrySet()) {
            int indexOfSeperator = jsonPropertyEntry.getKey().indexOf("_");
            labels.add(jsonPropertyEntry.getKey().substring(0,indexOfSeperator +1));
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
