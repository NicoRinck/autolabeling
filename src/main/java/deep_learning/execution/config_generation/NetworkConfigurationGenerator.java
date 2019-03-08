package deep_learning.execution.config_generation;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.IUpdater;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.util.ArrayList;

public class NetworkConfigurationGenerator {

    private ArrayList<NeuralNetConfiguration.Builder> configBuilders = new ArrayList<>();
    /*private ValueContainer<Integer> amountOfEvaluations;
    private ValueContainer<OptimizationAlgorithm> optimizationAlgorithm;
    private ValueContainer<IUpdater> updater; //including learning rate*/
    //erstesLayer
    //alle Dense Layers
    //OutputLayer
    //keine VariableValues, sondern Arrays mit einem Element!

    //nächster schritt --> aufsplitten --> Klasse, die die konkreten methdoden zum hinzufügen der Elemente implementiert (vererbung?)
                    //layer generierung
                    //finale klasse, die die Generatoren verwendet und n-mal ausführt.


    //amount of Layers variabel (dense Layer variieren in der Anzahl
    public NetworkConfigurationGenerator(final Integer seed) {
        configBuilders.add(new NeuralNetConfiguration.Builder().seed(seed));
    }


    private <T> void addValueToBuilder(T[] values, ConfigurationBuilderMethod<T> method) {
        ArrayList<NeuralNetConfiguration.Builder> newBuilders = new ArrayList<>();
        for (T value : values) {
            for (NeuralNetConfiguration.Builder configBuilder : configBuilders) {
                NeuralNetConfiguration.Builder builder = configBuilder.clone();
                newBuilders.add(method.addToBuilder(builder, value));
            }
        }
        configBuilders = newBuilders;
    }

    public ArrayList<MultiLayerConfiguration> getConfigs() {
        final ArrayList<MultiLayerConfiguration> resultList = new ArrayList<>();
        configBuilders.forEach(builder -> resultList.add(builder.list().layer(new DenseLayer()).build()));
        return resultList;
    }

    public static void main(String[] args) {


        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(2435)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Sgd())
                .list()
                .layer(0, new DenseLayer.Builder().
                        nIn(105).nOut(53).
                        weightInit(WeightInit.SIGMOID_UNIFORM).
                        activation(Activation.SIGMOID).build())
                .layer(1, new DenseLayer.Builder()
                        .nIn(53).nOut(35)
                        .weightInit(WeightInit.RELU)
                        .activation(Activation.LEAKYRELU).build())
                .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(35).nOut(35)
                        .weightInit(WeightInit.NORMAL)
                        .activation(Activation.SOFTMAX).build())
                .build();

        NetworkConfigurationGenerator generator = new NetworkConfigurationGenerator(2134);
        IUpdater[] updaters = {new Sgd(0.1), new Sgd(0.2), new Sgd(0.3)};
        generator.addValueToBuilder(updaters, NeuralNetConfiguration.Builder::updater);
        ArrayList<MultiLayerConfiguration> configs = generator.getConfigs();
        System.out.println(configs.size());
        configs.forEach(configuration -> {
            System.out.println(configuration.toString());
        });

    }
}
