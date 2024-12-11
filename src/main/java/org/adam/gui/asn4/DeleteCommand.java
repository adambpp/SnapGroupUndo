package org.adam.gui.asn4;

public class DeleteCommand implements DCommand{
    Groupable element;
    LineModel linemodel;

    public DeleteCommand(LineModel lm, Groupable g) {
        element = g;
        linemodel = lm;
    }

    @Override
    public void doit() {
        linemodel.removeElement(element);
    }

    @Override
    public void undo() {
        linemodel.addElement(element);
    }
}
