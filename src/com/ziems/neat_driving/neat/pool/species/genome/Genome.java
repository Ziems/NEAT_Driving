package com.ziems.neat_driving.neat.pool.species.genome;

import com.ziems.neat_driving.neat.Neat;
import com.ziems.neat_driving.neat.pool.species.genome.genes.Gene;
import com.ziems.neat_driving.neat.pool.species.genome.network.Connection;
import com.ziems.neat_driving.neat.pool.species.genome.network.Network;
import com.ziems.neat_driving.neat.pool.species.genome.network.Neuron;

import java.util.ArrayList;


/**
 * Created by noahziems on 4/10/16.
 */
public class Genome {

    Gene[] genes = new Gene[]{};
    int fitness = -1;

    int adjustedFitness;
    Network network;
    int globalRank;

    Neat neat;
    MutationRates mutationRates;

    public Network generateNetwork(){
        Network network = new Network();
        for(int i = 1; i < getNeat().getInputs()+1; i++) {//Starts at 1 because bias neuron has index of 0;
            network.addInputNeuron(new Neuron(i));
        }
        for(int i = 0; i < getNeat().getOutputs(); i++)
            network.addOutputNeuron(new Neuron(getNeat().maxNodes() + i));
        for(int i = 0; i < getGenes().length; i++){
            Connection gene = (Connection)getGenes()[i];
            if(gene.isEnabled()){
                Neuron out;
                Neuron in;
                if(!network.containsNeuron(gene.getOutput().getIndex())) {
                    out = new Neuron(gene.getOutput().getIndex());
                    network.addHiddenNeuron(out);
                } else
                    out = network.getNeuron(gene.getOutput().getIndex());
                if(!network.containsNeuron(gene.getInput().getIndex())) {
                    in = new Neuron(gene.getInput().getIndex());
                    network.addHiddenNeuron(in);
                }else
                    in = network.getNeuron(gene.getInput().getIndex());

                if(in != null && out != null)
                    out.addInput(new Connection(gene.getInnovation_num(), true, in, out, gene.getWeight()));
            }
        }
        this.network = network;
        return network;
    }

    public void mutate(){
        getMutationRates().mutateMutationRates();

        if(Math.random() < getMutationRates().getConnectionsMutationChance())
            pointMutate();

        double p = getMutationRates().getLinkMutationChance();
        while(p > 0){
            if(Math.random() < p)
                linkMutate(false);
            p--;
        }

        p = getMutationRates().getBiasMutationChance();
        while(p > 0){
            if(Math.random() < p)
                linkMutate(true);
            p--;
        }

        p = getMutationRates().getNodeMutationChance();
        while(p > 0){
            if(Math.random() < p)
                nodeMutate();
            p--;
        }

        p = getMutationRates().getEnableMutationChance();
        while(p > 0){
            if(Math.random() < p)
                enableDisableMutate(true);
            p--;
        }

        p = getMutationRates().getDisableMutationChance();
        while(p > 0){
            if(Math.random() < p)
                enableDisableMutate(false);
            p--;
        }
    }

    public void pointMutate(){
        float step = (float)getMutationRates().getStepSize();
        for(int i = 0; i < getGenes().length; i++){
            Connection gene = (Connection)getGenes()[i];
            if(Math.random() < getNeat().getPerturbChance())
                gene.setWeight(gene.getWeight() + ((float)Math.random()) * step*2 - step);
            else
                gene.setWeight((float)(Math.random()) * 4 - 2);
        }
    }

    public void linkMutate(boolean forceBias){
        generateNetwork();
        Neuron neuron1;
        if(forceBias)
            neuron1 = network.getBiasNeuron();
        else
            neuron1 = network.getRandomNeuronForInput();
        Neuron neuron2 = network.getRandomNeuronForOutput();
        if(neuron1.getIndex() == neuron2.getIndex())
            return;
        Connection newConnection = new Connection(getNeat().newInnovation(), true, neuron1, neuron2, (float)(Math.random() * 15 - 7.5));
        for(int i = 0; i < getGenes().length; i++){
            Connection gene = (Connection)getGenes()[i];
            if(gene.getInput().equals(neuron1) || gene.getOutput().equals(neuron2))
                return;
        }
        neuron2.addInput(newConnection);
        addGene(newConnection);
    }

    public void nodeMutate(){
        if(getGenes().length == 0)
            return;
        Connection tempGene = (Connection)getRandomGene();
        if(!tempGene.isEnabled())
            return;
        tempGene.setEnabled(false);
        Neuron newNeuron = new Neuron(generateNetwork().getNextIndex());

        Connection gene1 = tempGene.copy();
        gene1.setOutput(newNeuron);
        gene1.setWeight(1.0f);
        gene1.setInnovation_num(getNeat().newInnovation());
        gene1.setEnabled(true);
        newNeuron.addInput(gene1);
        addGene(gene1);

        Connection gene2 = tempGene.copy();
        gene2.setInput(newNeuron);
        gene2.setInnovation_num(getNeat().newInnovation());
        gene2.setEnabled(true);
        gene2.getOutput().addInput(gene2);
        addGene(gene2);
    }

    public void enableDisableMutate(boolean enable){
        ArrayList<Connection> candidates = new ArrayList<>();
        for(int i = 0; i < getGenes().length; i++){
            Connection gene = (Connection)getGenes()[i];
                if(gene.isEnabled() != enable)
                    candidates.add(gene);
        }
        if(candidates.size() == 0)
            return;
        Connection candidate = candidates.get((int)(Math.random() * candidates.size()));
        candidate.setEnabled(!candidate.isEnabled());
    }

    public void addGene(Gene gene){
        if(genes == null || genes.length == 0) {
            genes = new Gene[]{gene};
            return;
        }
        Gene[] newGenes = new Gene[genes.length+1];
        for(int i = 0; i < genes.length; i++)
            newGenes[i] = genes[i];
        newGenes[genes.length] = gene;
        genes = newGenes;
    }

    public Gene getRandomGene(){
        return getGenes()[(int)(Math.random() * getGenes().length)];
    }

    public Genome(Neat neat, Gene[] genes, MutationRates mutationRates){
        this.genes = genes;
        this.neat = neat;
        this.mutationRates = mutationRates;
    }

    public Genome(Neat neat, Gene[] genes){
        this(neat, genes, new MutationRates());
    }

    public Genome(Genome parent1, Genome parent2){
        if(parent2.getFitness() > parent1.getFitness()) {
            Genome temp = parent1;
            parent1 = parent2;
            parent2 = temp;
        }
        neat = parent1.getNeat();
        Connection[] innovations2 = new Connection[getNeat().getGlobalInnovationNumber()+1];//Add one to avoid NPE
        for(Connection gene: innovations2)
            gene = null;
        for(int i = 0; i < parent2.getGenes().length; i++){
            Connection gene = (Connection)parent2.getGenes()[i];
            innovations2[gene.getInnovation_num()] = gene;
        }
        for(int i = 0; i < parent1.getGenes().length; i++){
            Connection gene1 = (Connection)parent1.getGenes()[i];
            Connection gene2 = innovations2[gene1.getInnovation_num()];
            if(gene2 != null && (int)(Math.random() * 2) == 1 && gene2.isEnabled())
                addGene(gene2.copy());
            else
                addGene(gene1.copy());
        }
        setMutationRates(parent1.getMutationRates());
        mutate();
    }

    public Gene[] getGenes(){
        return genes;
    }

    private Neat getNeat(){
        return neat;
    }

    public MutationRates getMutationRates(){
        return mutationRates;
    }

    public void setMutationRates(MutationRates mutationRates){
        this.mutationRates = mutationRates;
    }

    public int getGlobalRank() {
        return globalRank;
    }

    public void setGlobalRank(int globalRank) {
        this.globalRank = globalRank;
    }

    public int getAdjustedFitness() {
        return adjustedFitness;
    }

    public void setAdjustedFitness(int adjustedFitness) {
        this.adjustedFitness = adjustedFitness;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public Genome copyGenome(){
        return new Genome(getNeat(), genes.clone());
    }

    public String toString(){
        String out = "--[Genome]--";
        for(int i = 0; i < getGenes().length; i++)
            out += ("\nGene[" + i + "]: " + getGenes()[i]);
        return out;
    }
}
