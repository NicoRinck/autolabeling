package preprocess_data;

import org.datavec.api.split.FileSplit;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.Buffer;

public class Test {

    private static final String testString = "01_SS_O1_S1_Abd-TEST";

    public static void main(String[] args) {
        JsonRecordReader jsonRecordReader = new JsonRecordReader(null, true);
        File file = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\stack1");
        FileSplit fileSplit = new FileSplit(file);
        try {
            InputStream inputStream = fileSplit.openInputStreamFor(fileSplit.locations()[0].toString());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while (bufferedReader.ready()) {
                System.out.println(bufferedReader.readLine());
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

}
