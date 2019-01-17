package com.ziems.neat_driving.neat.pool.species.genome.network;

import com.ziems.neat_driving.neat.pool.species.genome.genes.Gene;

/**
 * Created by noahziems on 4/10/16.
 */
public class Connection extends Gene {

    Neuron input;
    Neuron output;

    float weight;
    long lastRunTime = 0;

    public Connection(int innovation_num, boolean enabled, Neuron input, Neuron output, float weight){
        super(innovation_num,enabled);
        setInput(input);
        setOutput(output);
        setWeight(weight);
    }

    public float getValue(long lastRunTime){
        if(this.lastRunTime == lastRunTime)
            return 1.0f;
        this.lastRunTime = lastRunTime;
        return getInput().getValue(lastRunTime) * getWeight();
    }

    public void setWeight(float newWeight){
        this.weight = newWeight;
    }

    public void setInput(Neuron inputNeuron){
        this.input = inputNeuron;
    }

    public void setOutput(Neuron outputNeuron){
        this.output = outputNeuron;
    }

    public float getWeight(){
        return weight;
    }

    public Neuron getInput(){
        return input;
    }

    public Neuron getOutput(){
        return output;
    }

    public Connection copy(){
        return new Connection(getInnovation_num(), isEnabled(), input, output, weight);
    }

    @Override
    public String toString() {
        String string = new String("Node[" + input.getIndex() + "] to Node[" + output.getIndex() + "]\tWeight: " + getWeight());
        if(isEnabled())
            return string;
        else return string + "\t(DISABLED)";
    }
}
