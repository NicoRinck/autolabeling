package deep_learning.tests;

import deep_learning.execution.result_logging.ResultReader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class EvaluateExecutionResults {

    public static void main(String[] args) {

        File file = new File("C:\\Users\\Nico Rinck\\Desktop\\Daten_Studienarbeit\\logs\\logfile-v1.txt");
        ResultReader resultReader = new ResultReader(file);

        HashMap<Integer, ArrayList<Double>> results = resultReader.getModelIndexAndResults();
        results.keySet().forEach(System.out::println);
        /*results.keySet().forEach(key -> System.out.println(results.get(key)));*/

    }

}
