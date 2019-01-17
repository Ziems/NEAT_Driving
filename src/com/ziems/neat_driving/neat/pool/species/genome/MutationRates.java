package com.ziems.neat_driving.neat.pool.species.genome;

/**
 * Created by noahziems on 4/10/16.
 */
public class MutationRates {

    /*

MutateConnectionsChance = 0.25
PerturbChance = 0.90
LinkMutationChance = 2.0
NodeMutationChance = 0.50
BiasMutationChance = 0.40
StepSize = 0.1
DisableMutationChance = 0.4
EnableMutationChance = 0.2
     */

    private double connectionsMutationChance;
    private double linkMutationChance;
    private double nodeMutationChance;
    private double biasMutationChance;
    private double stepSize;
    private double disableMutationChance;
    private double enableMutationChance;

    public MutationRates(double connectionsMutationChance, double linkMutationChance, double nodeMutationChance, double biasMutationChance, double stepSize, double disableMutationChance, double enableMutationChance) {
        this.connectionsMutationChance = connectionsMutationChance;
        this.linkMutationChance = linkMutationChance;
        this.nodeMutationChance = nodeMutationChance;
        this.biasMutationChance = biasMutationChance;
        this.stepSize = stepSize;
        this.disableMutationChance = disableMutationChance;
        this.enableMutationChance = enableMutationChance;
    }

    public MutationRates(){
        this(0.25, 2.0, 0.1, 0.40, 0.20, 0.40, 0.20);
    }

    public void mutateMutationRates(){
        connectionsMutationChance *= getRandomMultiplier();
        linkMutationChance *= getRandomMultiplier();
        nodeMutationChance *= getRandomMultiplier();
        biasMutationChance *= getRandomMultiplier();
        stepSize *= getRandomMultiplier();
        disableMutationChance *= getRandomMultiplier();
        enableMutationChance *= getRandomMultiplier();
    }

    private double getRandomMultiplier(){
        if((int)(Math.random() * 2) + 1 == 1)
            return 0.95;
        return 1.05263;
    }

    public double getConnectionsMutationChance() {
        return connectionsMutationChance;
    }

    public void setConnectionsMutationChance(double connectionsMutationChance) {
        this.connectionsMutationChance = connectionsMutationChance;
    }

    public double getLinkMutationChance() {
        return linkMutationChance;
    }

    public void setLinkMutationChance(double linkMutationChance) {
        this.linkMutationChance = linkMutationChance;
    }

    public double getNodeMutationChance() {
        return nodeMutationChance;
    }

    public void setNodeMutationChance(double nodeMutationChance) {
        this.nodeMutationChance = nodeMutationChance;
    }

    public double getBiasMutationChance() {
        return biasMutationChance;
    }

    public void setBiasMutationChance(double biasMutationChance) {
        this.biasMutationChance = biasMutationChance;
    }

    public double getStepSize() {
        return stepSize;
    }

    public void setStepSize(double stepSize) {
        this.stepSize = stepSize;
    }

    public double getDisableMutationChance() {
        return disableMutationChance;
    }

    public void setDisableMutationChance(double disableMutationChance) {
        this.disableMutationChance = disableMutationChance;
    }

    public double getEnableMutationChance() {
        return enableMutationChance;
    }

    public void setEnableMutationChance(double enableMutationChance) {
        this.enableMutationChance = enableMutationChance;
    }
}
