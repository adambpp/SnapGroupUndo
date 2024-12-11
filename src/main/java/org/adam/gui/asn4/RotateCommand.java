package org.adam.gui.asn4;

import java.util.ArrayList;
import java.util.List;

public class RotateCommand implements DCommand{
    List<Groupable> elements = new ArrayList<>();
    LineModel linemodel;
    double rotation;

    public RotateCommand(LineModel lm, List<Groupable> e, double rotationAmount) {
        this.elements = e;
        this.linemodel = lm;
        this.rotation = rotationAmount;
    }

    @Override
    public void doit() {
        linemodel.rotateElement(elements, rotation);
    }

    @Override
    public void undo() {
        linemodel.rotateElement(elements, -rotation);
    }
}
