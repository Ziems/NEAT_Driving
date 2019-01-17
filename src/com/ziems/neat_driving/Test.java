package com.ziems.neat_driving;

import com.ziems.neat_driving.neat.pool.species.genome.network.Connection;
import com.ziems.neat_driving.neat.pool.species.genome.network.Network;
import com.ziems.neat_driving.neat.pool.species.genome.network.Neuron;

/**
 * Created by noahziems on 3/8/17.
 */
public class Test {

    public static void main(String[] args){
        Neuron inputNeuron = new Neuron(0);
        Neuron outputNeuron = new Neuron(1);
        Connection connection = new Connection(0, true, inputNeuron, outputNeuron, -2f);


        Network network = new Network();
        network.addInputNeuron(inputNeuron);
        network.addOutputNeuron(outputNeuron);
        outputNeuron.addInput(connection);

        System.out.println(network.run(new float[]{2.0f})[0]);
    }
}
