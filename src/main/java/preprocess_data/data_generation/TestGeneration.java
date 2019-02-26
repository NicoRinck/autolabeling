package preprocess_data.data_generation;

import preprocess_data.data_model.Marker;

import java.util.ArrayList;

public class TestGeneration {

    public static void main(String[] args) {

        Marker marker1 = new Marker("1", 5,10,5);
        Marker marker2 = new Marker("2", 1,1,1);
        Marker marker3 = new Marker("3", 1,1,1);
        Marker marker4 = new Marker("4", 1,1,1);
        Marker marker5 = new Marker("5", 1,1,1);
        MarkerMovementGenerator markerGenerator1 = new MarkerMovementGenerator(marker1,
                new MarkerMovementFunction(2,2),
                new DirectionFunction(1,1));
        MarkerMovementGenerator markerGenerator2 = new MarkerMovementGenerator(marker2,
                new MarkerMovementFunction(2,2),
                new DirectionFunction(1,1));
        MarkerMovementGenerator markerGenerator3 = new MarkerMovementGenerator(marker3,
                new MarkerMovementFunction(2,2),
                new DirectionFunction(1,1));
        MarkerMovementGenerator markerGenerator4 = new MarkerMovementGenerator(marker4,
                new MarkerMovementFunction(2,2),
                new DirectionFunction(1,1));
        MarkerMovementGenerator markerGenerator5 = new MarkerMovementGenerator(marker5,
                new MarkerMovementFunction(2,2),
                new DirectionFunction(1,1));
        ArrayList<MarkerMovementGenerator> markerGenerators = new ArrayList<MarkerMovementGenerator>();
        markerGenerators.add(markerGenerator1);/*
        markerGenerators.add(markerGenerator2);
        markerGenerators.add(markerGenerator3);
        markerGenerators.add(markerGenerator4);
        markerGenerators.add(markerGenerator5);*/

        FrameGenerator frameGenerator = new FrameGenerator(markerGenerators,3);

        String generationPath = "C:\\Users\\Nico Rinck\\Desktop\\Daten_Studienarbeit";
        TrialDataGenerator trialDataGenerator = new TrialDataGenerator(frameGenerator,generationPath);
        trialDataGenerator.generateTrial("test.json");
    }
}
