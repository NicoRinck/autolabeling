package preprocess_data.data_generation;

import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class FrameGenerator {

    private final MarkerGenerator markerGenerator;

    public FrameGenerator(MarkerGenerator markerGenerator) {
        this.markerGenerator = markerGenerator;
    }

    public void generateFrames(JsonWriter jsonWriter) throws IOException {
        jsonWriter.beginArray();
    }
}
