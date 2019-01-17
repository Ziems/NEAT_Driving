package com.ziems.neat_driving.neat.pool;

import com.ziems.neat_driving.neat.Neat;
import com.ziems.neat_driving.neat.pool.species.Species;
import com.ziems.neat_driving.neat.pool.species.genome.Genome;
import com.ziems.neat_driving.neat.pool.species.genome.genes.Gene;
import com.ziems.neat_driving.neat.pool.species.genome.network.Connection;
import com.ziems.neat_driving.neat.pool.species.genome.network.Network;
import com.ziems.neat_driving.neat.pool.species.genome.network.Neuron;

import java.util.ArrayList;

/**
 * Created by noahziems on 4/10/16.
 */
public class Pool {

    private final float DELTA_DISJOINT = 2.0f;
    private final float WEIGHT_DIFFERENCE = 2.0f;
    private final float SPECIES_THRESHOLD = 6f;
    private final int STALENESS_THRESHOLD = 15;
    public final int POPULATION_SIZE = 10;

    Species[] species;
    public int generation;
    public int maxFitness;
    Species currentSpecies;
    Genome currentGenome;
    public int currentSpeciesIndex;
    public int currentGenomeIndex;
    Neat neat;

    public Pool(Neat neat){
        this.neat = neat;
        generation = 0;
        maxFitness = 0;
        currentGenome = null;
        currentSpecies = null;
        species = new Species[]{};
        startPool();
    }

    public float[] runCurrentNetwork(float[] inputs){
        if(currentGenome == null) {
            System.out.println("CANNOT RUN NETWORK: GENOME IS NULL");
            return null;
        }
        return currentGenome.generateNetwork().run(inputs);
    }

    public void cycleGenome(int fitnessOfCurrent){
        if(currentGenome != null) {
            currentGenome.setFitness(fitnessOfCurrent);
            if(fitnessOfCurrent > maxFitness)
                maxFitness = fitnessOfCurrent;
        }

        for(int x = 0; x < species.length; x++) {
            Species s = species[x];
            if (!s.isTested()) {
                currentSpecies = s;
                currentSpeciesIndex = x;
                for(int y = 0; y < s.getGenomes().length; y++) {
                    Genome g = s.getGenomes()[y];
                    if (g.getFitness() == -1) {
                        currentGenome = g;
                        currentGenomeIndex = y;
                        System.out.println(g);
                        return;
                    }
                }
            }
        }
        newGeneration();
        /*
        if(currentSpecies == null){
            currentSpeciesIndex = 0;
            currentSpecies = species[currentSpeciesIndex];
            cycleGenome(fitnessOfCurrent);
        } else if(currentGenome == null){
            if(currentSpecies.getGenomes().length > 0) {
                currentGenomeIndex = 0;
                currentGenome = currentSpecies.getGenomes()[currentGenomeIndex];
            }else {
                currentSpeciesIndex++;
                currentSpecies = species[currentSpeciesIndex];
                cycleGenome(fitnessOfCurrent);
            }
            return;
        }
        currentGenome.setFitness(fitnessOfCurrent);
        if(currentSpecies.getGenomes().length > currentGenomeIndex +1){
            currentGenomeIndex++;
            currentGenome = currentSpecies.getGenomes()[currentGenomeIndex];
        }else if(species.length > currentSpeciesIndex +1){
            currentSpeciesIndex++;
            currentSpecies = species[currentSpeciesIndex];
            currentGenomeIndex = 0;
            currentGenome = currentSpecies.getGenomes()[currentGenomeIndex];
        } else {
            System.out.println("NEW GENERATION");
            currentSpecies = null;
            currentGenome = null;
            newGeneration();
            cycleGenome();
        }*/
    }

    private void cycleGenome(){
        cycleGenome(0);//number doesnt matter
    }

    public void startPool(){
        for(int i = 0; i < POPULATION_SIZE; i++) {
            speciateGenome(basicGenome());
        }
        cycleGenome();
        //System.out.println("#Species: " + species.length + "(" + species[0].getGenomes().length + ")");
    }

    public void newGeneration(){
        generation++;
        System.out.println("New Generation: " + generation);
        //System.out.println("#Species1: "+ species.length + "(" + species[0].getGenomes().length + ")");
        cullSpecies(false);
        //System.out.println("#Species2: "+ species.length + "(" + species[0].getGenomes().length + ")");
        rankGlobally();
        //System.out.println("#Species3: "+ species.length + "(" + species[0].getGenomes().length + ")");
        removeStaleSpecies();
        //System.out.println("#Species4: "+ species.length + "(" + species[0].getGenomes().length + ")");
        rankGlobally();
        //System.out.println("#Species5: "+ species.length + "(" + species[0].getGenomes().length + ")");
        evaluateGeneration();
        //System.out.println("#Species6: "+ species.length + "(" + species[0].getGenomes().length + ")");
        removeWeakSpecies();
        float sum = totalAverageFitness();
        ArrayList<Genome> children = new ArrayList<>();
        for(Species species: this.species){
            int breed = (int)Math.floor(species.getAverageFitness() / sum * POPULATION_SIZE);//-1
            for(int i = 0; i < breed; i++)
                children.add(species.breedChild());
        }
        cullSpecies(true);
        System.out.println("#Species7: "+ species.length);

        while(children.size() + species.length < POPULATION_SIZE){
            Species specie = this.species[(int)(Math.random() * this.species.length)];
            children.add(specie.breedChild());
        }
        for(Genome child: children)
            speciateGenome(child);
    }

    public void rankGlobally(){
        Genome[] global = new Genome[]{};
        for(Species specie: species)
            for(Genome genome: specie.getGenomes())
                if(genome != null)
                    global = addGenomeToArray(genome, global);
        global = sortGenomes(global);
        for(int i = 0; i < global.length; i++)
            global[i].setGlobalRank(i+1);
    }

    public void removeStaleSpecies(){
        Species[] survived = new Species[]{};
        for(Species specie: species){
            if(specie.getGenomes().length > 0) {
                specie.sortGenomes();
                if (specie.getGenomes()[0].getFitness() > specie.getTopFitness()) {
                    specie.setTopFitness(specie.getGenomes()[0].getFitness());
                    specie.setStaleness(0);
                } else
                    specie.setStaleness(specie.getStaleness() + 1);
                if (specie.getStaleness() < STALENESS_THRESHOLD)
                    survived = addSpecieToArray(specie, survived);
            }
        }
        species = survived;
    }

    public void removeWeakSpecies(){
        ArrayList<Species> survived = new ArrayList<>();
        float sum = totalAverageFitness();
        System.out.println("#Species before weak species: " + species.length);
        for(Species specie: species) {
            System.out.println("Average Specie Fitness: " + specie.getAverageFitness());
            int breed = (int) ((specie.getAverageFitness() / sum) * POPULATION_SIZE);
            System.out.println("BREED STATS:\taverageFitness: " + specie.getAverageFitness() + "\tsum: " + sum + "\ttoInt: " + breed);
            if (breed >= 1)
                survived.add(specie);
        }
        species = survived.toArray(new Species[survived.size()]);
    }

    public float totalAverageFitness(){
        float sum = 0;
        for(Species specie: species)
            sum += specie.getAverageFitness();
        return sum;
    }

    public void speciateGenome(Genome genome){
        //System.out.println("Species Length at SpeciateGenome: " + species.length);
        for(Species species: this.species){
            if(species.getGenomes().length>0) {
                if (inSameSpecies(genome, species.getGenomes()[0])) {
                    species.addGenome(genome);
                    return;
                }
            }
        }
        Species childSpecies = new Species(new Genome[]{}, getNeat());
        childSpecies.addGenome(genome);
        addNewSpecies(childSpecies);
        //System.out.println("New Species has been created");
    }

    public void cullSpecies(boolean everyoneExceptTop){
        for(int i = 0; i < species.length; i++){
            Species specie = this.species[i];
            if(specie.getGenomes().length > 0) {
                specie.sortGenomes();
                int remainingIndex = specie.getGenomes().length / 2;/**Only the top 50% survive*/
                if(specie.getGenomes().length == 1)
                    remainingIndex = 1;
                //System.out.println("Specie size: " + specie.getGenomes().length + "\tremainingIndex: " + remainingIndex);
                if (everyoneExceptTop)
                    remainingIndex = 1;
                Genome[] genomes = new Genome[remainingIndex];
                for (int o = 0; o < genomes.length; o++) {
                    genomes[o] = specie.getGenomes()[o];
                }
                specie.setGenomes(genomes);
                for (Genome g : specie.getGenomes())
                    if (g == null)
                        System.out.println("g is null");
            }
        }
    }

    public void evaluateGeneration(){
        for(Species species: this.species) {
            int totalSpeciesFitness = 0;
            for (Genome genome : species.getGenomes())
                totalSpeciesFitness += genome.getFitness();
            species.setAverageFitness(totalSpeciesFitness/species.getGenomes().length);
        }
    }

    public boolean inSameSpecies(Genome genome1, Genome genome2){
        float deltaDisjoint = DELTA_DISJOINT * getDisjointGenes(genome1, genome2);
        if(Float.isNaN(deltaDisjoint))
            deltaDisjoint = 0f;
        //System.out.println("Delta Disjoint: " + deltaDisjoint);
        float meanWeightDifference = WEIGHT_DIFFERENCE * getMeanWeightDifference(genome1, genome2);
        if(Float.isNaN(meanWeightDifference))
            meanWeightDifference = 0f;
        /*if(deltaDisjoint + meanWeightDifference >= SPECIES_THRESHOLD)
            System.out.println("Difference Evaluation: " + (deltaDisjoint + meanWeightDifference));*/
        return deltaDisjoint + meanWeightDifference <   SPECIES_THRESHOLD;
    }

    public float getDisjointGenes(Genome genome1, Genome genome2){
        boolean[] genes1 = new boolean[getNeat().getGlobalInnovationNumber()+1];
        boolean[] genes2 = new boolean[getNeat().getGlobalInnovationNumber()+1];
        for(int i = 0; i < genes1.length; i++)
            genes1[i] = false;
        for(int i = 0; i < genes2.length; i++)
            genes2[i] = false;
        for(int i = 0; i < genome1.getGenes().length; i++){
            Connection gene = (Connection)genome1.getGenes()[i];
            genes1[gene.getInnovation_num()] = true;
        }
        for(int i = 0; i < genome2.getGenes().length; i++){
            Connection gene = (Connection)genome2.getGenes()[i];
            genes2[gene.getInnovation_num()] = true;
        }
        float disjointGenes = 0.0f;
        for(int i = 0; i < genes1.length; i++){
            if(!genes2[i] && genes1[i])
                disjointGenes++;
        }
        for(int i = 0; i < genes2.length; i++){
            if(!genes1[i] && genes2[i])
                disjointGenes++;
        }
        float maxNumberOfGenes = Math.max(genome1.getGenes().length, genome2.getGenes().length);
        return disjointGenes/maxNumberOfGenes;
    }

    public float getMeanWeightDifference(Genome genome1, Genome genome2){
        int coincident = 0;
        float sum = 0;
        Connection[] genes1 = new Connection[getNeat().getGlobalInnovationNumber()+1];
        Connection[] genes2 = new Connection[getNeat().getGlobalInnovationNumber()+1];
        for(Connection gene: genes1)
            gene = null;
        for(Connection gene: genes2)
            gene = null;
        for(int i = 0; i < genome1.getGenes().length; i++){
            Connection gene = (Connection)genome1.getGenes()[i];
            genes1[gene.getInnovation_num()] = gene;
        }
        for(int i = 0; i < genome2.getGenes().length; i++){
            Connection gene = (Connection)genome2.getGenes()[i];
            genes2[gene.getInnovation_num()] = gene;
        }
        for(int i = 0; i < genes1.length; i++){
            if(genes1[i] != null && genes2[i] != null){
                sum += Math.abs(genes1[i].getWeight() - genes2[i].getWeight());
                coincident++;
            }
        }
        return sum/coincident;
    }

    public void addNewSpecies(Species newSpecies){
        if(species == null || species.length == 0){
            species = new Species[]{newSpecies};
            return;
        }
        Species[] returnSpecies = new Species[species.length+1];
        for(int i = 0; i < species.length; i++)
            returnSpecies[i] = species[i];
        returnSpecies[species.length] = newSpecies;
        species = returnSpecies;
    }

    public Genome[] sortGenomes(Genome[] genomes){
        for(int i = 0; i < genomes.length; i++){
            if(genomes[i] == null)
                System.out.println("GENOME IS NULL[" + i + "]");
        }
        Genome temp;
        for(int i=0; i < genomes.length-1; i++){
            for(int j=1; j < genomes.length-i; j++){
                if(genomes[j-1].getFitness() < genomes[j].getFitness()){
                    temp=genomes[j-1];
                    genomes[j-1] = genomes[j];
                    genomes[j] = temp;
                }
            }
        }
        return genomes;
    }

    public Genome basicGenome(){
        Genome genome = new Genome(getNeat(), new Gene[]{});
        System.out.println("Neat inputs: " + neat.getInputs());
        genome.mutate();
        return genome;
    }

    public Genome[] addGenomeToArray(Genome genome, Genome[] genomes){
        Genome[] newGenomes = new Genome[genomes.length+1];
        for(int i = 0; i < genomes.length; i++)
            newGenomes[i] = genomes[i];
        newGenomes[genomes.length] = genome;
        return newGenomes;
    }

    public Species[] addSpecieToArray(Species specie, Species[] species){
        Species[] newSpecies = new Species[species.length+1];
        for(int i = 0; i < species.length; i++)
            newSpecies[i] = species[i];
        newSpecies[species.length] = specie;
        return newSpecies;
    }

    private Neat getNeat(){
        return neat;
    }
}
