package preprocess_data.data_generation;

public class TestGeneration {

    public static void main(String[] args) {

        MarkerGenerator markerGenerator = new MarkerGenerator();
        FrameGenerator frameGenerator = new FrameGenerator(markerGenerator);

        String generationPath = "C:\\Users\\Nico Rinck\\Desktop\\Daten_Studienarbeit";
        TrialDataGenerator trialDataGenerator = new TrialDataGenerator(frameGenerator,generationPath);
        trialDataGenerator.generateTrial("test.json");
    }
}
