package org.adam.gui.asn4;

public class DLine {
    private double x1, y1, x2, y2;

    public DLine(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public boolean contains(double x, double y, double threshold) {
        // length = pythagorean theorem
        // ratioA = vertical change (rise) of the line
        // ratioB = horizontal change (run) of the line
        double length = Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
        double ratioA = (y1-y2) / length;
        double ratioB = (x2 - x1) / length;
        double ratioC = -1 * ((y1 - y2) * x1 + (x2 - x1) * y1) / length;
        double distFromLine = Math.abs((ratioA * x) + (ratioB * y) + ratioC); // Ax + By + C
        System.out.println("distFromLine: " + distFromLine);
        return distFromLine <= threshold || distFromLine == 0;
    }

    public void adjust(double x2, double y2) {
        this.x2 = x2;
        this.y2 = y2;
    }

    public void move(double nX, double nY) {
        this.x1 += nX;
        this.y1 += nY;
        this.x2 += nX;
        this.y2 += nY;
    }

    /*
    ######################
    GETTERS
    ######################
     */
    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }

    public double getX2() {
        return x2;
    }

    public double getY2() {
        return y2;
    }
}
