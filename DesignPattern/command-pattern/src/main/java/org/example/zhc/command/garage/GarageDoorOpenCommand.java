package org.example.zhc.command.garage;

import org.example.zhc.command.inf.Command;

public class GarageDoorOpenCommand implements Command {

    GarageDoor garageDoor;
    public GarageDoorOpenCommand(GarageDoor garageDoor){
        this.garageDoor = garageDoor;
    }
    @Override
    public void execute() {
        garageDoor.open();
    }

    @Override
    public void undo() {
        garageDoor.close();
    }
}
