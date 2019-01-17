package com.ziems.neat_driving.neat;

import com.ziems.neat_driving.neat.pool.Pool;

/**
 * Created by noahziems on 4/10/16.
 */
public class Neat {

    double perturbChance = 0.90;
    double crossoverChance = 0.70;//TODO: CHANGE LATER PLS

    int inputs;
    int outputs;
    int maxNodes;
    int globalInnovationNumber;

    Pool pool;

    public Neat(int inputs, int outputs){
        this.inputs = inputs;
        this.outputs = outputs;
        maxNodes = 500;
        globalInnovationNumber = outputs;
        pool = new Pool(this);
    }

    public float[] runCurrentNetwork(float[] inputs){
        return pool.runCurrentNetwork(inputs);
    }

    public void cycleGenome(int fitnessOfCurrent){
        pool.cycleGenome(fitnessOfCurrent);
    }

    public int getInputs(){
        return inputs;
    }

    public int getOutputs(){
        return outputs;
    }

    public int maxNodes(){
        return maxNodes;
    }

    public int newInnovation(){
        globalInnovationNumber++;
        return globalInnovationNumber;
    }

    public int getGenomeNumber(){
        return pool.currentGenomeIndex;
    }

    public int getSpecieNumber(){
        return pool.currentSpeciesIndex;
    }

    public int getGenerationNumber(){
        return pool.generation;
    }

    public int maxFitness(){
        return pool.maxFitness;
    }

    public double getPerturbChance(){
        return perturbChance;
    }

    public double getCrossoverChance() {return crossoverChance;}

    public int getGlobalInnovationNumber(){
        return globalInnovationNumber;
    }
}
