package preprocess_data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.datavec.api.writable.Writable;
import preprocess_data.data_model.Frame;

import java.util.ArrayList;

public class TrialDataManager {

    private final TrialDataTransformation dataTransformer;
    private final JsonToTrialParser jsonToTrialParser;

    public TrialDataManager(TrialDataTransformation dataTransformer, JsonToTrialParser jsonToTrialParser) {
        this.dataTransformer = dataTransformer;
        this.jsonToTrialParser = jsonToTrialParser;
    }

    public ArrayList<ArrayList<Writable>> getTrialDataFromJson(JsonArray trialData) {
        ArrayList<ArrayList<Writable>> resultList = new ArrayList<ArrayList<Writable>>();
        for (JsonElement trialDatum : trialData) {
            ArrayList<Frame> frames = new ArrayList<Frame>();
            Frame frame = jsonToTrialParser.getLabeledFrameFromJson(trialData.getAsJsonObject());
            resultList.addAll(dataTransformer.transformFrameData(frame));
        }
        return resultList;
    }
}
