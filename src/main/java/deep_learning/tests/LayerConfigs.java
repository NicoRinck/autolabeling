package deep_learning.tests;

import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.Layer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class LayerConfigs {

    static final Layer[] INPUT_LAYERS = {
            new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.SIGMOID).weightInit(WeightInit.SIGMOID_UNIFORM).build(),
            new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.RELU).weightInit(WeightInit.RELU_UNIFORM).build(),
            new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.RELU).weightInit(WeightInit.RELU).build(),
            new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.LEAKYRELU).weightInit(WeightInit.RELU_UNIFORM).build(),
            new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.LEAKYRELU).weightInit(WeightInit.RELU).build(),
            new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.TANH).weightInit(WeightInit.NORMAL).build(),
            new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.TANH).weightInit(WeightInit.XAVIER).build(),
            new DenseLayer.Builder().nIn(105).nOut(55).activation(Activation.TANH).weightInit(WeightInit.XAVIER_UNIFORM).build()};

    static final OutputLayer[] OUTPUT_LAYERS = {
            new OutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS).nIn(55).nOut(35).activation(Activation.SIGMOID).weightInit(WeightInit.SIGMOID_UNIFORM).build(),
            new OutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS).nIn(55).nOut(35).activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER_UNIFORM).build(),
            new OutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS).nIn(55).nOut(35).activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER).build(),
            new OutputLayer.Builder(LossFunctions.LossFunction.MSE).nIn(55).nOut(35).activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER).build(),
            new OutputLayer.Builder(LossFunctions.LossFunction.MCXENT).nIn(55).nOut(35).activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER).build(),
            new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD).nIn(55).nOut(35).activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER).build(),
            new OutputLayer.Builder(LossFunctions.LossFunction.MEAN_SQUARED_LOGARITHMIC_ERROR).nIn(55).nOut(35).activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER).build()};

    static final Layer[] HIDDEN_LAYERS = {
            new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.SIGMOID).weightInit(WeightInit.SIGMOID_UNIFORM).build(),
            new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.RELU).weightInit(WeightInit.RELU_UNIFORM).build(),
            new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.RELU).weightInit(WeightInit.RELU).build(),
            new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.LEAKYRELU).weightInit(WeightInit.RELU_UNIFORM).build(),
            new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.LEAKYRELU).weightInit(WeightInit.RELU).build(),
            new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.TANH).weightInit(WeightInit.NORMAL).build(),
            new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.TANH).weightInit(WeightInit.XAVIER).build(),
            new DenseLayer.Builder().nIn(55).nOut(55).activation(Activation.TANH).weightInit(WeightInit.XAVIER_UNIFORM).build()};

    static Layer[] getInputLayersFromIndexes(int... indexes){
        return getLayers(INPUT_LAYERS, indexes);
    }

    static Layer[] getHiddenLayersFromIndexes(int... indexes){
        return getLayers(HIDDEN_LAYERS, indexes);
    }

    static OutputLayer[] getOutputLayersFromIndexes(int... indexes){
        Layer[] layers = getLayers(OUTPUT_LAYERS, indexes);
        OutputLayer[] resultList = new OutputLayer[layers.length];
        for (int i = 0; i < layers.length; i++) {
            resultList[i] = (OutputLayer) layers[i];
        }
        return resultList;
    }

    private static Layer[] getLayers(Layer[] layers, int[] indexes) {
        final Layer[] resultLayers = new Layer[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            resultLayers[i] = layers[indexes[i]];
        }
        return resultLayers;
    }
}
