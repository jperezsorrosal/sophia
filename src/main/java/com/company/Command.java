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


}
