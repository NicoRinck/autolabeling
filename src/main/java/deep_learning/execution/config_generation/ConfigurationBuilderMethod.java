package deep_learning.execution.config_generation;

import org.deeplearning4j.nn.conf.NeuralNetConfiguration;

public interface ConfigurationBuilderMethod<T> {


    NeuralNetConfiguration.Builder addToBuilder(NeuralNetConfiguration.Builder builder, T value);

}
