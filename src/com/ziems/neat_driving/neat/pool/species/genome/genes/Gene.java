package com.ziems.neat_driving.neat.pool.species.genome.genes;

/**
 * Created by noahziems on 4/10/16.
 */
public abstract class Gene {

    /**
     * is historical marking of node
     */
    int innovation_num;

    /**
     * is a flag: is TRUE the gene is enabled FALSE otherwise.
     */
    boolean enable;

    public Gene(int innovation_num, boolean enable){
        setInnovation_num(innovation_num);
        setEnabled(enable);
    }

    public int getInnovation_num() {
        return innovation_num;
    }

    public void setInnovation_num(int innovation_num) {
        this.innovation_num = innovation_num;
    }

    public boolean isEnabled() {
        return enable;
    }

    public void setEnabled(boolean enable) {
        this.enable = enable;
    }

    public abstract String toString();
}
