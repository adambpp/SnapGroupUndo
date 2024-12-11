package org.adam.gui.asn4;

import java.util.ArrayList;
import java.util.List;

public class UngroupCommand implements DCommand{
    private List<Groupable> stuff;
    private DGroup group;
    private LineModel linemodel;

    public UngroupCommand(LineModel lm, List<Groupable> lines) {
        this.stuff = lines;
        this.linemodel = lm;
    }

    @Override
    public void doit() {
        linemodel.ungroup(group);
    }

    @Override
    public void undo() {
        stuff.forEach(e -> linemodel.removeElement(e));
        group = linemodel.group(stuff);
    }
}
