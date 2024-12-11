package org.adam.gui.asn4;

import java.util.List;

public interface Groupable {
    boolean isGroup();

    List<Groupable> getChildren();

    void scale(double scaleFactor, Integer upOrDown);

    void scale(double scaleFactor, double centerX, double centerY, Integer upOrDown);

    void rotate(double rotationAmount);

    void rotate(double rotationAmount, double centerX, double centerY);

    void move(double nX, double nY);

    boolean contains(double x, double y, double threshold);

    Groupable deepcopy();

}
