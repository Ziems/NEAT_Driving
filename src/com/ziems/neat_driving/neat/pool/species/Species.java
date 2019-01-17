package com.ziems.neat_driving.neat.pool.species;


import com.ziems.neat_driving.neat.Neat;
import com.ziems.neat_driving.neat.pool.species.genome.Genome;

/**
 * Created by noahziems on 4/10/16.
 */
public class Species {

    private int topFitness;
    private int staleness;
    private Genome[] genomes;
    private float averageFitness;
    private Neat neat;

    public Species(Genome[] genomes, Neat neat){
        setGenomes(genomes);
        staleness = 0;
        topFitness = 0;
        averageFitness = 0;
        this.neat = neat;
    }

    public Genome breedChild(){
        Genome child;
        if(Math.random() < neat.getCrossoverChance()){
            Genome g1 = getGenomes()[(int)(Math.random() * getGenomes().length)];
            Genome g2 = getGenomes()[(int)(Math.random() * getGenomes().length)];
            child = new Genome(g1, g2);
        }else{
            Genome g = getGenomes()[(int)(Math.random() * getGenomes().length)];
            child = g.copyGenome();
        }
        //System.out.println("Old child: " + child);
        child.mutate();
        //System.out.println("New child: " + child);
        return child;
    }

    public int getTopFitness() {
        return topFitness;
    }

    public void setTopFitness(int topFitness) {
        this.topFitness = topFitness;
    }

    public int getStaleness() {
        return staleness;
    }

    public void setStaleness(int staleness) {
        this.staleness = staleness;
    }

    public Genome[] getGenomes() {
        return genomes;
    }

    public void setGenomes(Genome[] genomes) {
        this.genomes = genomes;
    }

    public float getAverageFitness() {
        return averageFitness;
    }

    public Genome[] sortGenomes(){
        Genome temp;
        for(int i = 0; i < genomes.length; i++){
            if(genomes[i] == null)
            System.out.println("GENOME IS NULL[" + i + "]");
        }
        for(int i=0; i < getGenomes().length-1; i++){
            for(int j=1; j < getGenomes().length-i; j++){
                if(getGenomes()[j-1].getFitness() < getGenomes()[j].getFitness()){
                    temp=getGenomes()[j-1];
                    getGenomes()[j-1] = getGenomes()[j];
                    getGenomes()[j] = temp;
                }
            }
        }
        return getGenomes();
    }

    public void addGenome(Genome newGenome){
        if(newGenome == null)
            System.out.println("NEW GENOME IS NULL");
        if(genomes == null || genomes.length == 0){
            genomes = new Genome[]{newGenome};
            return;
        }
        Genome[] newGenomes = new Genome[genomes.length+1];
        for(int i = 0; i < genomes.length; i++)
            newGenomes[i] = genomes[i];
        newGenomes[genomes.length] = newGenome;
        genomes = newGenomes;
    }

    public boolean isTested(){
        for(int i = 0; i < genomes.length; i++)
            if(genomes[i].getFitness() == -1)
                return false;
        return true;
    }

    public void setAverageFitness(float averageFitness) {
        this.averageFitness = averageFitness;
    }
}
