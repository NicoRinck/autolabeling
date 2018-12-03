package preprocess_data;

public class Marker {

    private final String label;
    private final double x;
    private final double y;
    private final double z;

    public Marker(String label, double x, double y, double z) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return label + ": (" + x + "," + y + "," + z + ")";
    }

    public String getLabel() {
        return label;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
