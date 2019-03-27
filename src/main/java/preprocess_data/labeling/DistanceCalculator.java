package preprocess_data.labeling;

import preprocess_data.data_model.Coordinate3D;
import preprocess_data.data_model.Marker;

class DistanceCalculator {

    static double getDistanceToMarker(Marker startMarker, Marker endMarker) {
        return Math.sqrt(Math.pow(startMarker.getX() - endMarker.getX(), 2)
                + Math.pow(startMarker.getX() - endMarker.getX(), 2)
                + Math.pow(startMarker.getX() - endMarker.getX(), 2));
    }

    static double getDistanceToCoordinate(Marker startMarker, Coordinate3D endPoint) {
        return Math.sqrt(Math.pow(startMarker.getX() - endPoint.getX(), 2)
                + Math.pow(startMarker.getX() - endPoint.getX(), 2)
                + Math.pow(startMarker.getX() - endPoint.getX(), 2));
    }
}
