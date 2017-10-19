package com.company;

import java.util.ArrayList;
import java.util.List;

public class Command {
    private String id;
    private String command;
    private List<Input> inputs = new ArrayList<Input>();
    private List<Output> outputs = new ArrayList<Output>();

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

    public boolean hasInputs() { return !inputs.isEmpty(); }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Command [" + id + "]: \"" + command + "\"\n");
        sb.append("Input Resources:\n");
        inputs.forEach(i -> sb. append(" - Resource: " + i.getResourceName() + " Type: " + i.getType().name() + "\n"));
        sb.append("Output Resources:\n");
        outputs.forEach(o -> sb.append(" - Resource: " + o.getResourceName() + " Type: " + o.getType().name() + "\n"));
        sb.append("\n");

        return sb.toString();
    }
}
