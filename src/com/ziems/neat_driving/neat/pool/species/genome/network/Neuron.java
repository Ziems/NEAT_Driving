package com.ziems.neat_driving.neat.pool.species.genome.network;

/**
 * Created by noahziems on 4/10/16.
 */
public class Neuron {

    Connection[] inputs;

    float value = 0f;
    int index;
    long lastRunTime = 0;

    public Neuron(Connection[] inputs, int index){
        this(index);
        setInputs(inputs);
    }

    public float getValue(long lastRunTime){
        float output = value;
        if(this.lastRunTime != lastRunTime)
            this.lastRunTime = lastRunTime;
        else
            return 1.0f;
        if(getInputs() != null) {
            for (Connection c : getInputs()) {
                output += c.getValue(lastRunTime);
            }
        }
        return Math.max(0f, (float)Math.cos(output + Math.PI/3));
    }

    public void addInput(Connection connection){
        if(inputs == null || inputs.length == 0)
            inputs = new Connection[]{connection};
        Connection[] newInputs = new Connection[inputs.length+1];
        for(int i = 0; i < inputs.length; i++)
            newInputs[i] = inputs[i];
        newInputs[inputs.length] = connection;
    }

    public void setValue(float value){
        this.value = value;
    }

    public Connection[] getInputs(){
        return inputs;
    }

    public void setInputs(Connection[] inputs){
        this.inputs = inputs;
    }

    float sigmoid(float x) {
        return (1/( 1 + (float)Math.pow(Math.E,(-1*x))));
    }

    public int getIndex(){
        return index;
    }

    public Neuron(int index){
        this.index = index;
    }

    public void print(){
        if(inputs != null) {
            System.out.println("=============");
            for (int i = 0; i < inputs.length; i++) {
                System.out.println(inputs[i]);
            }
            System.out.println("=============");
        }
    }

}
