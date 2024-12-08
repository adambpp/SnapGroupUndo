package org.adam.gui.asn4;

public class RubberBand {
    private double x, y, width, height;

    public RubberBand(double x, double y) {
        this.x = x;
        this.y = y;
        this.width = 0.0;
        this.height = 0.0;
    }

    public boolean contains(double mx, double my) {
        return mx >= x && mx <= (x + width) && my >= y && my <= (y + height);
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

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
