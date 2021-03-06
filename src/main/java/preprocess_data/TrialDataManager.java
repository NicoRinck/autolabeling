package preprocess_data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.datavec.api.writable.DoubleWritable;
import org.datavec.api.writable.Writable;
import preprocess_data.data_conversion.FrameDataConversionStrategy;
import preprocess_data.data_manipulator.FrameDataManipulationStrategy;
import preprocess_data.data_model.Frame;
import preprocess_data.data_model.Marker;
import preprocess_data.labeling.MarkerLabelingStrategy;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

//TODO: split into TrialDataJsonParser and TrialDataTransformation (Konvertierung und Manipulation?)
public class TrialDataManager {

    private Set<String> markerLabels;
    //TODO: Normalisierung? --> analog wie labels holen? Normalisation-Klasse, die eine Normaliserungsstrategie entgegennimmt.
    private final MarkerLabelingStrategy labelingStrategy;
    private final FrameDataManipulationStrategy manipulator;
    private final FrameDataConversionStrategy converter;

    public TrialDataManager(MarkerLabelingStrategy labelingStrategy, FrameDataManipulationStrategy manipulator, FrameDataConversionStrategy converter) {
        this.labelingStrategy = labelingStrategy;
        this.manipulator = manipulator;
        this.converter = converter;
    }

    public ArrayList<ArrayList<Writable>> getTrialDataFromJson(JsonArray trialData) {
        /*for (JsonElement frameData : trialData) {
            Frame frame = getLabeledMarkerDataFromFrame(frameData.getAsJsonObject());
            if (manipulator != null) {
                ArrayList<Frame> manipulatedFrames = manipulator.manipulateFrame(frame);
                features.addAll(getResultList(manipulatedFrames));
            } else {
                features.add(converter.convertFrameToWritables(frame));
            }
        }
        return features.addAll(labels);*/
        return null;
    }


    private Frame getLabeledMarkerDataFromFrame(JsonObject frameJson) {
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