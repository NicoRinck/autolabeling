package preprocess_data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.datavec.api.writable.Writable;
import preprocess_data.data_model.Frame;

import java.util.ArrayList;
import java.util.List;

public class TrialDataManager {

    private final TrialDataTransformation dataTransformer;
    private final JsonToTrialParser jsonToTrialParser;

    public TrialDataManager(TrialDataTransformation dataTransformer, JsonToTrialParser jsonToTrialParser) {
        this.dataTransformer = dataTransformer;
        this.jsonToTrialParser = jsonToTrialParser;
    }

    public ArrayList<ArrayList<Writable>> getTrialDataFromJson(JsonArray trialData) {
        final ArrayList<ArrayList<Writable>> resultList = new ArrayList<ArrayList<Writable>>();
        for (JsonElement trialDatum : trialData) {
            Frame frame = jsonToTrialParser.getFrameFromJson(trialDatum.getAsJsonObject());
            resultList.addAll(dataTransformer.transformFrameData(frame));
        }
        return resultList;
    }

    public TrialDataTransformation getDataTransformer() {
        return dataTransformer;
    }
}
