package org.adam.gui.asn4;

import java.util.ArrayList;
import java.util.List;

public class DGroup implements Groupable{

    private List<Groupable> children;
    private double minX = Double.MAX_VALUE;
    private double maxX = Double.MIN_VALUE;
    private double minY = Double.MAX_VALUE;
    private double maxY = Double.MIN_VALUE;

    public DGroup(List<Groupable> items) {
        children = new ArrayList<>(items);
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxY() {
        return maxY;
    }

    public void makeBoundingBoxDimensions() {
        resetBoxDimensions();
        for (Groupable child : children) {
            if (child instanceof DLine line) {
                double lineXmin = Math.min(line.getX1(), line.getX2());
                double lineYmin = Math.min(line.getY1(), line.getY2());
                double lineXmax = Math.max(line.getX1(), line.getX2());
                double lineYmax = Math.max(line.getY1(), line.getY2());

                minX = Math.min(lineXmin, minX);
                maxX = Math.max(lineXmax, maxX);
                minY = Math.min(lineYmin, minY);
                maxY = Math.max(lineYmax, maxY);
            } else if (child instanceof DGroup group) {
                group.makeBoundingBoxDimensions();
                minX = Math.min(group.getMinX(), minX);
                maxX = Math.max(group.getMaxX(), maxX);
                minY = Math.min(group.getMinY(), minY);
                maxY = Math.max(group.getMaxY(), maxY);
            }
        }
    }


    private void resetBoxDimensions() {
        minX = Double.MAX_VALUE;
        maxX = Double.MIN_VALUE;
        minY = Double.MAX_VALUE;
        maxY = Double.MIN_VALUE;
    }

    @Override
    public boolean isGroup() {
        return true;
    }

    @Override
    public List<Groupable> getChildren() {
        return children;
    }

    // this method does not get used in this class
    @Override
    public void scale(double scaleFactor, Integer upOrDown) {}

    @Override
    public void scale(double scaleFactor, double centerX, double centerY, Integer upOrDown) {

    }

    @Override
    public void rotate(double rotationAmount) {
        double centerX = (minX + maxX) / 2;
        double centerY = (minY + maxY) / 2;

        for (Groupable child : children) {
            if (child instanceof DLine line) {
                line.rotate(rotationAmount, centerX, centerY);

            } else if (child instanceof DGroup group) {
                group.rotate(rotationAmount, centerX, centerY);
            }
        }
    }

    @Override
    public void rotate(double rotationAmount, double centerX, double centerY) {
        for (Groupable child: children) {
            if (child instanceof DLine line) {
                line.rotate(rotationAmount, centerX, centerY);
            }
        }
    }

    @Override
    public void move(double nX, double nY) {
        children.forEach(child -> child.move(nX, nY));
        //resetBoxDimensions();
    }

    @Override
    public boolean contains(double x, double y, double threshold) {
        for (Groupable child : children) {
            if (child instanceof DLine line) {
                if (line.contains(x, y, threshold)) return true;
            }
            else if (child instanceof DGroup group) {
                if (group.contains(x, y, threshold)) return true;
            }
        }
        return false;
    }
}
