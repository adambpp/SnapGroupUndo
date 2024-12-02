package org.adam.gui.asn4;

public class DLine {
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

    public void adjustEndpoint(Endpoints endpoint, double nX, double nY) {
        if (endpoint == Endpoints.ENDPOINT_1) {
            this.x1 = nX;
            this.y1 = nY;
        } else if (endpoint == Endpoints.ENDPOINT_2) {
            this.x2 = nX;
            this.y2 = nY;
        }
    }

    public void move(double nX, double nY) {
        this.x1 += nX;
        this.y1 += nY;
        this.x2 += nX;
        this.y2 += nY;
    }

    public void scaleLine(double lineScale) {

    }

    public void rotateLine(double rotationAmount) {
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
