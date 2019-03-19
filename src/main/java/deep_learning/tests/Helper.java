package deep_learning.tests;

import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

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

    public static <T,V> void logHashMap(HashMap<T,V> hashMap) {
        for (T t : hashMap.keySet()) {
            System.out.println("Key: " + t + ", Value: " + hashMap.get(t));
        }
    }
}
