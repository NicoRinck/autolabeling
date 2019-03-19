package deep_learning.execution.result_logging;

public enum LinePrefixes {
    MODEL_LINE_PREFIX ("MultiLayerConfiguration "),
    LAYER_LINE_PREFIX ("   Layer: "),
    RESULT_LINE_PREFIX ( "       Ergebnis: "),
    CONFIG_SEPARATOR("#######\n");

    private final String linePrefix;

    LinePrefixes(String linePrefix) {
        this.linePrefix = linePrefix;
    }

    public String getLinePrefix() {
        return linePrefix;
    }
}
