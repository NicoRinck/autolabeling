package datavec;

import org.datavec.api.transform.schema.Schema;

public class TrialDataSchema {

    private final int amountOfMarkers;

    public TrialDataSchema(int amountOfMarkers) {
        this.amountOfMarkers = amountOfMarkers;
    }

    public Schema getSchema() {
        Schema.Builder builder = new Schema.Builder().addColumnInteger("0_label", 0, 1);
        for (int i = 0; i < amountOfMarkers; i++) {
            builder.addColumnsDouble(i + "_x", i + "_y", i + "_z");
            if (!(i == amountOfMarkers-1)) { //last iteration does no need a label
                builder.addColumnInteger((i+1) + "_label");
            }
        }
        return builder.build();
    }
}
