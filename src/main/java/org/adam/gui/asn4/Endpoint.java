package org.adam.gui.asn4;

public class Endpoint {

    private double x, y;
    private int radius;

    public Endpoint(double x, double y, int r) {
        this.x = x;
        this.y = y;
        this.radius = r;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    public int getRadius() {
        return radius;
    }

    public void setRadius(int r) {
        this.radius = r;
    }
}
