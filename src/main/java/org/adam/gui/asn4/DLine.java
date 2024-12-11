package org.adam.gui.asn4;

import java.util.List;

public class DLine implements Groupable {
    private double x1, y1, x2, y2, scale, rotationVal;
    private Endpoint endpoint1, endpoint2;
    public enum Endpoints {ENDPOINT_1, ENDPOINT_2}
    private Endpoints curEndpoint;

    public DLine(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.scale = 1.0;
        this.rotationVal = 0.0;
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
//        System.out.println("distFromLine: " + distFromLine);
        return distFromLine <= threshold || distFromLine == 0;
    }

    public void adjust(double x2, double y2) {
        this.x2 = x2;
        this.y2 = y2;
    }

    public void adjustAll(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void adjustFromCopy(DLine lineToModify, DLine l) {
        lineToModify.adjustAll(l.getX1(), l.getY1(), l.getX2(), l.getY2());

    }

    public void adjustEndpoint(Endpoints endpoint, double nX, double nY) {
        if (endpoint == Endpoints.ENDPOINT_1) {
            this.x1 = nX;
            this.y1 = nY;
        } else if (endpoint == Endpoints.ENDPOINT_2) {
            this.x2 = nX;
            this.y2 = nY;
        }
    }

    @Override
    public DLine deepcopy() {
        return new DLine(x1, y1, x2, y2);
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public List<Groupable> getChildren() {
        return List.of();
    }

    @Override
    public void move(double nX, double nY) {
        this.x1 += nX;
        this.y1 += nY;
        this.x2 += nX;
        this.y2 += nY;
    }

    @Override
    public void scale(double scaleFactor, Integer upOrDown) {
        double centerX = (x1 + x2) / 2.0;
        double centerY = (y1 + y2) / 2.0;

        doScale(scaleFactor, centerX, centerY, upOrDown);
    }

    @Override
    public void scale(double scaleFactor, double centerX, double centerY, Integer upOrDown) {
        doScale(scaleFactor, centerX, centerY, upOrDown);
    }

    @Override
    public void rotate(double rotationAmount) {
        double centerX = (x1 + x2) / 2;
        double centerY = (y1 + y2) / 2;
        doRotation(rotationAmount, centerX, centerY);
    }

    @Override
    public void rotate(double rotationAmount, double centerX, double centerY) {
        // find centerX and centerY of the group and then pass that to the children
        doRotation(rotationAmount, centerX, centerY);
    }

    private void doScale(double scaleFactor, double centerX, double centerY, Integer upOrDown) {
        // Vectors from center to endpoints
        double dx1 = x1 - centerX;
        double dy1 = y1 - centerY;
        double dx2 = x2 - centerX;
        double dy2 = y2 - centerY;


//        double actualFactor = scaleFactor;
//        if (upOrDown != 0) {
//            // this means down, so we do:
//            actualFactor = 1.0 / scaleFactor;
//        }

        // scale the vectors
        dx1 *= scaleFactor;
        dy1 *= scaleFactor;
        dx2 *= scaleFactor;
        dy2 *= scaleFactor;

        // compute new endpoints
        double newX1 = centerX + dx1;
        double newY1 = centerY + dy1;
        double newX2 = centerX + dx2;
        double newY2 = centerY + dy2;

        // update endpoints
        adjustEndpoint(Endpoints.ENDPOINT_1, newX1, newY1);
        adjustEndpoint(Endpoints.ENDPOINT_2, newX2, newY2);
    }

    private void doRotation(double rotationAmount, double centerX, double centerY) {
        double dx1 = x1 - centerX;
        double dy1 = y1 - centerY;
        double dx2 = x2 - centerX;
        double dy2 = y2 - centerY;
        double angle = Math.toRadians(rotationAmount);

        x1 = (dx1 * Math.cos(angle) - dy1 * Math.sin(angle)) + centerX;
        y1 = (dx1 * Math.sin(angle) + dy1 * Math.cos(angle)) + centerY;

        x2 = (dx2 * Math.cos(angle) - dy2 * Math.sin(angle)) + centerX;
        y2 = (dx2 * Math.sin(angle) + dy2 * Math.cos(angle)) + centerY;
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

    public double getScale() {
        return scale;
    }

    public double getRotationVal() {
        return rotationVal;
    }

    public Endpoint getEndpoint1() {
        return endpoint1;
    }
    public Endpoint getEndpoint2() {
        return endpoint2;
    }

    /*
    ######################
    SETTERS
    ######################
     */

    public void setEndpoint1(double x, double y, int r) {
        this.endpoint1 = new Endpoint(x, y, r);
    }

    public void setEndpoint2(double x, double y, int r) {
        this.endpoint2 = new Endpoint(x, y, r);
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public void setRotationVal(double rotationVal) {
        this.rotationVal = rotationVal;
    }

    public void setCurEndpoint(Integer endpoint) {
        if (endpoint == 0) {
            curEndpoint = Endpoints.ENDPOINT_1;
        } else {
            curEndpoint = Endpoints.ENDPOINT_2;
        }
    }

    public Endpoints getCurEndpoint() {
        return curEndpoint;
    }

    public void clearEndpointSelection() {
        curEndpoint = null;
    }

}
