package org.adam.gui.asn4;

public class CreateCommand implements DCommand {
    DLine line;
    LineModel linemodel;

    public CreateCommand(LineModel lm, DLine dl) {
        line = dl;
        linemodel = lm;
    }


    @Override
    public void doit() {
        linemodel.addElement(line);
    }

    @Override
    public void undo() {
        linemodel.removeElement(line);
    }
}
