package deep_learning.tests;

import deep_learning.execution.AutomaticConfigExecutor;
import deep_learning.execution.config_generation.ConfigVariator;
import deep_learning.execution.config_generation.LayerConfigVariator;
import org.nd4j.linalg.learning.config.IUpdater;
import org.nd4j.linalg.learning.config.Sgd;
import preprocess_data.TrialDataManager;
import preprocess_data.TrialDataTransformation;
import preprocess_data.data_manipulaton.FrameManipulationStrategy;
import preprocess_data.data_manipulaton.FrameShuffleManipulator;
import preprocess_data.data_normalization.CentroidNormalization;
import preprocess_data.data_normalization.TrialNormalizationStrategy;
import preprocess_data.labeling.FrameLabelingStrategy;
import preprocess_data.labeling.OneTargetLabeling;

import java.io.File;

public class TestAutomaticExecution {

    public static void main(String[] args) throws Exception {
        File trainDirectory = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\trainData\\train\\01_SS_O1_S1_Abd - Kopie.json");
        File testDirectory = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\testData\\test\\01_SS_O1_S1_Abd.json");
        File logFile = new File("C:\\Users\\Nico Rinck\\Documents\\DHBW\\Studienarbeit\\Daten_Studienarbeit\\logs\\logFile-v1.txt");

        //Strategies/Assets
        FrameLabelingStrategy frameLabelingStrategy = new OneTargetLabeling("LELB", 35);
        FrameManipulationStrategy manipulationStrategy = new FrameShuffleManipulator(1);
        TrialNormalizationStrategy normalizationStrategy = new CentroidNormalization(0, 1);
        TrialDataTransformation transformation = new TrialDataTransformation(frameLabelingStrategy, manipulationStrategy);
        TrialDataManager trialDataManager = new TrialDataManager(transformation, normalizationStrategy);

        LayerConfigVariator layerConfigVariator = new LayerConfigVariator(3, 5, 7);
        layerConfigVariator.addInputLayers(LayerConfigs.getInputLayersFromIndexes(1, 2));
        layerConfigVariator.addHiddenLayers(LayerConfigs.getHiddenLayersFromIndexes(1, 2));
        layerConfigVariator.addOutputLayers(LayerConfigs.getOutputLayersFromIndexes(1, 2));

        ConfigVariator configVariator = new ConfigVariator(24, layerConfigVariator);
        IUpdater[] updaters = {new Sgd(0.1), new Sgd(0.01)};
        configVariator.addUpdater(updaters);

        AutomaticConfigExecutor configExecutor = new AutomaticConfigExecutor(trainDirectory, testDirectory, logFile,
                trialDataManager, 20);
    }
}
