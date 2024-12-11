package org.adam.gui.asn4;

import java.util.ArrayList;
import java.util.List;

public class DeleteCommand implements DCommand{
    List<Groupable> elements = new ArrayList<>();
    LineModel linemodel;

    public DeleteCommand(LineModel lm, List<Groupable> g) {
        //elements = g;
        for (Groupable child: g) {
            elements.add(child.deepcopy());
        }
        linemodel = lm;
    }

    @Override
    public void doit() {
        for (Groupable element: elements) {
            linemodel.removeElement(element);
        }
    }

    @Override
    public void undo() {
        for (Groupable element: elements) {
            linemodel.addElement(element);
        }
    }
}
