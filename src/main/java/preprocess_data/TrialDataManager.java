package preprocess_data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.datavec.api.writable.Writable;
import preprocess_data.data_model.Frame;

import java.util.ArrayList;

//manages json-parsing, normalization and transformation of trial-data
public class TrialDataManager {

    private final TrialDataTransformation dataTransformer;
    private final JsonToTrialParser jsonToTrialParser;
    private final TrialNormalizationStrategy normalizationStrategy;

    public TrialDataManager(TrialDataTransformation dataTransformer, TrialNormalizationStrategy normalizationStrategy) {
        this.dataTransformer = dataTransformer;
        jsonToTrialParser = new JsonToTrialParser(normalizationStrategy);
        this.normalizationStrategy = normalizationStrategy;
    }

    public TrialDataManager(TrialDataTransformation dataTransformer) {
        this(dataTransformer, null);
    }

    public ArrayList<ArrayList<Writable>> getTrialDataFromJson(JsonArray trialData) {
        final ArrayList<ArrayList<Writable>> resultList = new ArrayList<ArrayList<Writable>>();
        ArrayList<Frame> frames = getFramesFromJson(trialData);
        for (Frame frame : frames) {
            resultList.addAll(transformFrameToWritable(frame));
        }
        return resultList;
    }

    private ArrayList<ArrayList<Writable>> transformFrameToWritable(Frame frame) {
        if (normalizationStrategy != null) {
            return dataTransformer.transformFrameData(normalizationStrategy.normalizeFrame(frame));
        }
        return dataTransformer.transformFrameData(frame);
    }

    private ArrayList<Frame> getFramesFromJson(JsonArray trialData) {
        final ArrayList<Frame> frames = new ArrayList<Frame>();
        for (JsonElement trialDatum : trialData) {
            Frame frame = jsonToTrialParser.getFrameFromJson(trialDatum.getAsJsonObject());
            frames.add(frame);
        }
        return frames;
    }

    public TrialDataTransformation getDataTransformer() {
        return dataTransformer;
    }
}
