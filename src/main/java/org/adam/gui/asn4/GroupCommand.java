package org.adam.gui.asn4;

import java.util.ArrayList;
import java.util.List;

public class GroupCommand implements DCommand{
    private DGroup group;
    private List<Groupable> groupchildren = new ArrayList<>();
    private LineModel linemodel;

    public GroupCommand(LineModel lm, DGroup g) {
        this.group = g;
        groupchildren.addAll(g.getChildren());
        this.linemodel = lm;
    }

    @Override
    public void doit() {
        linemodel.group(groupchildren);
    }

    @Override
    public void undo() {
        linemodel.ungroup(group);
    }
}
