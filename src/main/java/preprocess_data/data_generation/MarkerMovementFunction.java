package preprocess_data.data_generation;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import preprocess_data.data_model.Marker;

public class MarkerMovementFunction {

    //3D-function: f(x,y) = coefficientX*x^exponentX + coefficientY*y^exponentY
    private final double coefficientX;
    private final double exponentX;
    private final double coefficientY;
    private final double exponentY;

    MarkerMovementFunction(double coefficientX, double exponentX, double coefficientY, double exponentY) {
        this.coefficientX = coefficientX;
        this.exponentX = exponentX;
        this.coefficientY = coefficientY;
        this.exponentY = exponentY;
    }

    MarkerMovementFunction(double coefficientX, double coefficientY) {
        this(coefficientX, 1, coefficientY, 1);
    }

    Point3D getNextMarker(Point2D newXYPosition) {
        return new Point3D(newXYPosition.getX(), newXYPosition.getY(), getNextZ(newXYPosition));
    }

    private double getNextZ(Point2D xy) {
        return coefficientX * Math.pow(xy.getX(), exponentX) + coefficientY * Math.pow(xy.getY(), exponentY);
    }
}
