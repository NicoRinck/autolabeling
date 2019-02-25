package preprocess_data.data_generation;


import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TrialDataGenerator {

    private final FrameGenerator frameGenerator;
    private final String generationPath;

    public TrialDataGenerator(FrameGenerator frameGenerator, String generationPath) {
        this.frameGenerator = frameGenerator;
        this.generationPath = generationPath;
    }


    public boolean generateTrial(String filename) {

        try {
            JsonWriter jsonWriter = new JsonWriter(new FileWriter(createFile(filename)));
            jsonWriter.beginObject().name("trial").beginObject().name("frames");
            frameGenerator.generateFrames(jsonWriter);
            jsonWriter.endObject().endObject(); //close remaining Objects
            jsonWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private File createFile(String filename) {
        File file = new File(generationPath + "\\" + filename);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
