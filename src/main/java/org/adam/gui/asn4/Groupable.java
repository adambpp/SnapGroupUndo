package org.adam.gui.asn4;

import java.util.List;

public interface Groupable {
    boolean isGroup();

    List<Groupable> getChildren();

}
