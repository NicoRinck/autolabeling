package preprocess_data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.datavec.api.writable.Writable;
import preprocess_data.data_model.Frame;
import preprocess_data.data_normalization.TrialNormalizationStrategy;

import java.util.ArrayList;

//manages json-parsing, normalization and transformation of trial-data
public class TrialDataManager {

    private final TrialDataTransformation dataTransformer;
    private final JsonToTrialParser jsonToTrialParser = new JsonToTrialParser(normalizer);
    private TrialNormalizationStrategy normalizationStrategy;

    public TrialDataManager(TrialDataTransformation dataTransformer, TrialNormalizationStrategy normalizationStrategy) {
        this.dataTransformer = dataTransformer;
        this.normalizationStrategy = normalizationStrategy;
    }

    public TrialDataManager(TrialDataTransformation dataTransformer) {
        this.dataTransformer = dataTransformer;
        normalizationStrategy = null;
    }

    public ArrayList<ArrayList<Writable>> getTrialDataFromJson(JsonArray trialData) {
        final ArrayList<ArrayList<Writable>> resultList = new ArrayList<ArrayList<Writable>>();
        final ArrayList<Frame> frames = getFramesFromJson(trialData);
        for (Frame frame : frames) {
            resultList.addAll(transformFrameToWritable(frame));
        }
        if (normalizationStrategy != null) {
            normalizationStrategy = normalizationStrategy.getNewInstance();
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
            Frame frame = jsonToTrialParser.getFrameFromJson(trialDatum.getAsJsonObject(), normalizationStrategy);
            frames.add(frame);
        }
        return frames;
    }

    public TrialDataTransformation getDataTransformer() {
        return dataTransformer;
    }
}
