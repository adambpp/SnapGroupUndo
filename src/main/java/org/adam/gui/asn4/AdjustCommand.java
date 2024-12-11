package org.adam.gui.asn4;

public class AdjustCommand implements DCommand {
    DLine line;
    LineModel linemodel;
    double originalX1, originalY1, originalX2, originalY2;
    double newX1, newX2, newY1, newY2;

    public AdjustCommand(LineModel lm, DLine dl, double originalX1, double originalY1, double originalX2, double originalY2) {
        line = dl;
        this.originalX1 = originalX1;
        this.originalY1 = originalY1;
        this.originalX2 = originalX2;
        this.originalY2 = originalY2;
        linemodel = lm;
    }

    @Override
    public void doit() {
        linemodel.adjustAll(line, newX1, newY1, newX2, newY2);
    }

    @Override
    public void undo() {
        newX1 = line.getX1();
        newX2 = line.getX2();
        newY1 = line.getY1();
        newY2 = line.getY2();
        linemodel.adjustAll(line, originalX1, originalY1, originalX2, originalY2);
    }
}
