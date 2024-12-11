package org.adam.gui.asn4;

import java.util.ArrayList;
import java.util.List;

public class ScaleCommand implements DCommand {
    List<Groupable> elements = new ArrayList<>();
    LineModel linemodel;
    double scale;
    Integer scaleType;

    public ScaleCommand(LineModel lm, List<Groupable> e, double scaleFactor, Integer upOrDown) {
        this.elements = e;
        this.linemodel = lm;
        this.scale = scaleFactor;
        this.scaleType = upOrDown;
    }

    @Override
    public void doit() {
        linemodel.scaleLine(elements, scale, scaleType);
    }

    @Override
    public void undo() {
        // apply the inverse scale: the reciprocal of scale
        double inverseScale = 1.0 / scale;

        // invert the direction
        int inverseType = (scaleType == 0) ? 1 : 0;

        linemodel.scaleLine(elements, inverseScale, inverseType);
    }
}

