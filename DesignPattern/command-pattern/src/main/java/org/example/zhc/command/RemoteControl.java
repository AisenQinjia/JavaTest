package org.example.zhc.command;

import lombok.extern.slf4j.Slf4j;
import org.example.zhc.command.inf.Command;

@Slf4j
public class RemoteControl {
    Command[] onCommands;
    Command[] offCommands;
    int slotCount;
    Command undoCommand;
    private static NoCommand NO_COMMAND = new NoCommand();
    public RemoteControl(int slotCount){
        this.slotCount = slotCount;
        onCommands = new Command[slotCount];
        offCommands = new Command[slotCount];
        for(int i = 0;i<slotCount;i++){
            onCommands[i] = NO_COMMAND;
            offCommands[i] = NO_COMMAND;
        }
        undoCommand = NO_COMMAND;
    }

    public void setCommand(int slot,Command on,Command off){
        assert slot < slotCount-1;
        onCommands[slot] = on;
        offCommands[slot] = off;
    }

    public void onButtonPushed(int slot){
        assert slot < slotCount - 1;
        onCommands[slot].execute();
        undoCommand = onCommands[slot];
    }

    public void offButtonPushed(int slot){
        assert slot < slotCount - 1;
        offCommands[slot].execute();
        undoCommand = offCommands[slot];
    }

    public void undo(){
        undoCommand.execute();
    }

    static class NoCommand implements Command{

        @Override
        public void execute() {
            log.info("no command execute");
        }

        @Override
        public void undo() {
            log.info("no command undo execute");
        }
    }
}
