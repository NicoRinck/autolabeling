package preprocess_data;

public class Test {

    private static final String testString = "01_SS_O1_S1_Abd-TEST";

    public static void main(String[] args) {
        JsonToMarkerConverter jsonToMarkerConverter = new JsonToMarkerConverter();
        for (Marker marker : jsonToMarkerConverter.getTrialDataFromJson(testString).get(0)) {
            System.out.println(marker.toString());
        }
    }

}
