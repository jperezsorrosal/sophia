package com.company;

import java.util.List;

public class Command {
    private String id;
    private String command;
    private List<Input> inputs;
    private List<Output> outputs;

    public Command(String id, String command) {
        this.id = id;

        this.command = command;
    }

    public String getId() {
        return id;
    }

    public String getCommand() {
        return command;
    }

    public List<Input> getInputs() {
        return inputs;
    }

    public void setInputs(List<Input> inputs) {
        this.inputs = inputs;
    }

    public List<Output> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Output> outputs) {
        this.outputs = outputs;
    }


}
