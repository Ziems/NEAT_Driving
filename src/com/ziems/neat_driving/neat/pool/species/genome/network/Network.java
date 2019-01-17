package com.ziems.neat_driving.neat.pool.species.genome.network;

/**
 * Created by noahziems on 4/10/16.
 */
public class Network {

    Neuron[] allNeurons;
    Neuron[] inputNeurons;
    Neuron[] outputNeurons;
    Neuron[] hiddenNeurons;

    public Network(){
        Neuron biasNeuron = new Neuron(0);
        biasNeuron.setValue(1f);
        allNeurons = addNeuronToArray(allNeurons, biasNeuron);
        inputNeurons = new Neuron[]{biasNeuron};
        hiddenNeurons = new Neuron[]{};
        outputNeurons = new Neuron[]{};

    }

    public boolean containsNeuron(int index){
        for(Neuron neuron: allNeurons)
            if(neuron.getIndex() == index)
                return true;
        return false;
    }

    public Neuron getNeuron(int index){
        for(Neuron neuron: allNeurons)
            if(neuron.getIndex() == index)
                return neuron;
        return null;
    }

    public Neuron getBiasNeuron(){
        return getNeuron(0);
    }

    public void addInputNeuron(Neuron neuron){
        inputNeurons = addNeuronToArray(inputNeurons, neuron);
        allNeurons = addNeuronToArray(allNeurons, neuron);
    }

    public void addOutputNeuron(Neuron neuron){
        outputNeurons = addNeuronToArray(outputNeurons, neuron);
        allNeurons = addNeuronToArray(allNeurons, neuron);
    }

    public void addHiddenNeuron(Neuron neuron){
        hiddenNeurons = addNeuronToArray(hiddenNeurons, neuron);
        allNeurons = addNeuronToArray(allNeurons, neuron);
    }

    public float[] run(float[] inputs){
        for(int i = 0; i < inputNeurons.length-1; i++)
            inputNeurons[i].setValue(inputs[i]);
        float[] out = new float[outputNeurons.length];
        for(int i = 0; i < outputNeurons.length; i++) {
            out[i] = outputNeurons[i].getValue(System.currentTimeMillis());
        }
        return out;
    }

    public Neuron[] addNeuronToArray(Neuron[] neurons, Neuron neuron){
        if(neurons == null || neurons.length == 0)
            return new Neuron[]{neuron};
        Neuron[] newNeurons = new Neuron[neurons.length+1];
        for(int i = 0; i < neurons.length; i++)
            newNeurons[i] = neurons[i];
        newNeurons[neurons.length] = neuron;
        return newNeurons;
    }

    public Neuron getRandomNeuronForInput(){
        Neuron[] acceptableNeurons = new Neuron[inputNeurons.length + hiddenNeurons.length];
        for(int i = 0; i < inputNeurons.length; i++)
            acceptableNeurons[i] = inputNeurons[i];
        for(int i = 0; i < hiddenNeurons.length; i++)
            acceptableNeurons[i + inputNeurons.length] = hiddenNeurons[i];
        Neuron n = acceptableNeurons[(int)(Math.random() * acceptableNeurons.length)];
        return n;
    }

    public Neuron getRandomNeuronForOutput(){
        Neuron[] acceptableNeurons = new Neuron[hiddenNeurons.length + outputNeurons.length];
        for(int i = 0; i < hiddenNeurons.length; i++)
            acceptableNeurons[i] = hiddenNeurons[i];
        for(int i = 0; i < outputNeurons.length; i++)
            acceptableNeurons[i + hiddenNeurons.length] = outputNeurons[i];
        return acceptableNeurons[(int)(Math.random() * acceptableNeurons.length)];
    }

    public int getNextIndex(){
        if(hiddenNeurons != null && hiddenNeurons.length != 0)
            return inputNeurons.length-1 + hiddenNeurons.length-1;
        return inputNeurons.length-1;
    }

    public void print(){
        for(Neuron n: inputNeurons){
            System.out.println(n.getIndex());
        }
        //for(Neuron n:allNeurons)
        //    n.print();
    }


}
