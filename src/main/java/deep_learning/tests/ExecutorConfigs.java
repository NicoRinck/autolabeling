package deep_learning.tests;

import deep_learning.execution.AutomaticConfigExecutor;
import preprocess_data.TrialDataManager;
import preprocess_data.TrialDataTransformation;
import preprocess_data.data_manipulaton.FrameManipulationStrategy;
import preprocess_data.data_manipulaton.FrameShuffleManipulator;
import preprocess_data.data_normalization.CentroidNormalization;
import preprocess_data.data_normalization.TrialNormalizationStrategy;
import preprocess_data.labeling.FrameLabelingStrategy;
import preprocess_data.labeling.OneTargetLabeling;

import java.io.File;

public class ExecutorConfigs {

    static AutomaticConfigExecutor smallDataSetNegRange() throws Exception {
        File trainDirectory = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\trainData\\trainDistanceSimple");
        File testDirectory = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\testData\\testDistanceSimple");
        File logFile = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\logs\\logFile(-1,1)-v3.txt");

        //Strategies/Assets
        FrameLabelingStrategy frameLabelingStrategy = new OneTargetLabeling("LELB", 35);
        FrameManipulationStrategy manipulationStrategy = new FrameShuffleManipulator(10);
        TrialNormalizationStrategy normalizationStrategy = new CentroidNormalization(-1, 1);
        TrialDataTransformation transformation = new TrialDataTransformation(frameLabelingStrategy, manipulationStrategy);
        TrialDataManager trialDataManager = new TrialDataManager(transformation, normalizationStrategy);
        return new AutomaticConfigExecutor(trainDirectory, testDirectory, logFile,
                trialDataManager, 20);
    }

    public static AutomaticConfigExecutor largeDataSetNegRange() throws Exception {
        File trainDirectory = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\trainData\\train");
        File testDirectory = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\testData\\test");
        File logFile = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\logs\\logFile(-1,1)-v4.txt");

        //Strategies/Assets
        FrameLabelingStrategy frameLabelingStrategy = new OneTargetLabeling("LELB", 35);
        FrameManipulationStrategy manipulationStrategy = new FrameShuffleManipulator(25);
        TrialNormalizationStrategy normalizationStrategy = new CentroidNormalization(-1, 1);
        TrialDataTransformation transformation = new TrialDataTransformation(frameLabelingStrategy, manipulationStrategy);
        TrialDataManager trialDataManager = new TrialDataManager(transformation, normalizationStrategy);
        return new AutomaticConfigExecutor(trainDirectory, testDirectory, logFile,
                trialDataManager, 20);
    }
}
