package test.tests;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Helper {

    static void printINDArray(INDArray indArray) {
        double[] array = indArray.toDoubleVector();
        for (int i = 0; i < array.length; i++) {
            System.out.print(i + ": " + array[i] + " | ");
        }
        System.out.println();
    }

    public static File getNextPossibleFile(File modelSaveFile) {
        String currentPath = modelSaveFile.getPath();
        int indexOfVersion = currentPath.indexOf("-v");
        int integer = Integer.valueOf(currentPath.substring(indexOfVersion + 2, currentPath.lastIndexOf(".")));
        return new File(modelSaveFile.getParent() + ++integer + getFileExtension(modelSaveFile));
    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf);
    }

    public static <T, V> void logHashMap(HashMap<T, V> hashMap) {
        for (T t : hashMap.keySet()) {
            System.out.println("Key: " + t + ", Value: " + hashMap.get(t));
        }
    }

    public static ArrayList<MultiLayerConfiguration> getConfigsFromJSON(ArrayList<String> jsonStrings) {
        ArrayList<MultiLayerConfiguration> configurations = new ArrayList<>();
        for (String s : jsonStrings) {
            configurations.add(MultiLayerConfiguration.fromJson(s));
        }
        return configurations;
    }
}
