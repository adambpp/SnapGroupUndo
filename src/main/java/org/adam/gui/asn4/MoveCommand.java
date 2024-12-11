package org.adam.gui.asn4;

import java.util.ArrayList;
import java.util.List;

public class MoveCommand implements DCommand {
    List<Groupable> elements;
    List<Groupable> elementsOriginalState;
    List<Groupable> elementsNewState;
    LineModel linemodel;

    public MoveCommand(LineModel lm, List<Groupable> selection) {
        this.elements = selection;
        this.elementsOriginalState = new ArrayList<>();
        this.elementsNewState = new ArrayList<>();
        for (Groupable child: elements) {
            elementsOriginalState.add(child.deepcopy());
        }
        this.linemodel = lm;
    }

    @Override
    public void doit() {
        joe(elements, elementsNewState);
    }

    @Override
    public void undo() {
        for (Groupable child: elements) {
            elementsNewState.add(child.deepcopy());
        }

        joe(elements, elementsOriginalState);
    }

    private void joe(List<Groupable> actualList, List<Groupable> listToGrabFrom) {
        for (int i = 0; i < actualList.size(); i++) {
            Groupable element = actualList.get(i);
            Groupable changedState = listToGrabFrom.get(i);

            linemodel.adjustFromCopy(element, changedState);
        }
    }
}
