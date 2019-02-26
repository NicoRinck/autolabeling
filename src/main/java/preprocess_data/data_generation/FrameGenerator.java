package preprocess_data.data_generation;

import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;

//forward frame includes the first Frame
class FrameGenerator {

    private final ArrayList<MarkerMovementGenerator> markerGenerators;
    private final MarkerToJsonConverter markerToJsonConverter = new MarkerToJsonConverter();
    private final int amountOfFrames;
    private final int backwardFrames;
    private final int forewardFrames;

    FrameGenerator(ArrayList<MarkerMovementGenerator> markerGenerators, int amountOfFrames) {
        this.markerGenerators = markerGenerators;
        this.amountOfFrames = amountOfFrames;
        this.forewardFrames = -1;
        this.backwardFrames = -1;
    }

    FrameGenerator(ArrayList<MarkerMovementGenerator> markerGenerators, int forwardFrames, int backwardFrames) {
        this.markerGenerators = markerGenerators;
        this.amountOfFrames = forwardFrames + backwardFrames;
        this.forewardFrames = forwardFrames;
        this.backwardFrames = backwardFrames;
    }

    void generateFrames(JsonWriter jsonWriter) throws IOException {
        jsonWriter.beginArray();
        generateFrameObjects(jsonWriter);
        jsonWriter.endArray();
    }

    private void generateFrameObjects(JsonWriter jsonWriter) throws IOException {
        for (int i = 0; i < amountOfFrames; i++) {
            jsonWriter.beginObject();
            if (forewardFrames != -1 && backwardFrames != -1 && i >= forewardFrames) {
                addFrameObject(jsonWriter, countBackwards(i));
            } else {
                addFrameObject(jsonWriter, i);
            }
            jsonWriter.endObject();
        }
    }

    private void addFrameObject(JsonWriter jsonWriter, int counter) throws IOException {
        for (MarkerMovementGenerator markerGenerator : markerGenerators) {
            markerToJsonConverter.addToJsonObject(jsonWriter, markerGenerator.getNextMarker(counter));
        }
    }

    private int countBackwards(int counter) {
        return forewardFrames - 2 - (counter - forewardFrames);
    }

}
