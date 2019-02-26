package preprocess_data.data_generation;

import javafx.geometry.Point2D;

//function which decides in which direction the marker can move --> x,y
//f(x) = coefficient * x ^ exponent
//stepWidth to get new X
class DirectionFunction {

    private final double stepWidth;
    private final double coefficient;
    private final double exponent;

    DirectionFunction(double stepWidth, double coefficient, double exponent) {
        this.stepWidth = stepWidth;
        this.coefficient = coefficient;
        this.exponent = exponent;
    }

    DirectionFunction(double stepWidth, double coefficient) {
        this(stepWidth, coefficient, 1);
    }

    Point2D getNextPosition(int stepCounter) {
        double newX = stepWidth * stepCounter;
        return new Point2D(newX, calcY(newX));
    }

    private double calcY(double newX) {
        return coefficient * Math.pow(newX, exponent);
    }
}