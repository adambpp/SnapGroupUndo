package org.adam.gui.asn4;

public class DeleteCommand implements DCommand{
    DLine line;
    LineModel linemodel;

    public DeleteCommand(LineModel lm, DLine dl) {
        line = dl;
        linemodel = lm;
    }

    @Override
    public void doit() {
        linemodel.removeElement(line);
    }

    @Override
    public void undo() {
        linemodel.addLine(line);
    }
}
