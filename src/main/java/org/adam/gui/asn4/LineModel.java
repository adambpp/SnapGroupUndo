package org.adam.gui.asn4;

import java.util.ArrayList;
import java.util.List;

public class LineModel {
    private ArrayList<Subscriber> subs;
    private ArrayList<DLine> lines;
    private RubberBand rubberband;

    public LineModel() {
        subs = new ArrayList<>();
        lines = new ArrayList<>();
        rubberband = null;
    }

    /**
     * Get the list of lines currently in the model
     * @return: The Arraylist of lines
     */

    public List<DLine> getLines() {
        return lines;
    }

    public DLine addLine(double x1, double y1, double x2, double y2) {
        DLine line = new DLine(x1, y1, x2, y2);
        lines.add(line);
        return line;
    }

    public void removeLine(DLine line) {
        lines.remove(line);
        notifySubscribers();
    }

    public void adjustLine(DLine line, double x2, double y2) {
        line.adjust(x2, y2);
        notifySubscribers();
    }

    public void adjustEndpoint(DLine line, double nX, double nY) {
        line.adjustEndpoint(line.getCurEndpoint(), nX, nY);
        notifySubscribers();
    }

    public void moveLine(List<DLine> lines, double nX, double nY) {
        lines.forEach(line -> line.move(nX, nY));
        notifySubscribers();
    }

    public void scaleLine(List<DLine> lines, double scale, int UpOrDown) {
        lines.forEach(line -> line.scaleLine(scale, UpOrDown));
        notifySubscribers();
    }

    public void rotateLine(List<DLine> lines, double rotation_amount) {
        lines.forEach(line -> line.rotateLine(rotation_amount));
        notifySubscribers();
    }

    public DLine findLineWithCurEndpoint(List<DLine> lines) {
        for(DLine line : lines ) {
            if(line.getCurEndpoint() != null) {
                return line;
            }
        }
        return null;
    }

    public void clearEndpointSelection(List<DLine> lines) {
        lines.forEach(DLine::clearEndpointSelection);
    }

    public void initRubberband(double x, double y) {
        rubberband = new RubberBand(x, y);
    }

    public RubberBand getRubberband() {
        return rubberband;
    }

    public void resizeRubberband(double nWidth, double nHeight) {
        rubberband.setWidth(nWidth);
        rubberband.setHeight(nHeight);
        notifySubscribers();
    }

    public List<DLine> rubberBandLineSelect() {
        ArrayList<DLine> linesWithinBounds = new ArrayList<>();
        for(DLine line : lines) {
            if (rubberband.contains(line.getX1(), line.getY1()) || rubberband.contains(line.getX2(), line.getY2())) {
                linesWithinBounds.add(line);
            }
        }
        return linesWithinBounds;
    }

    public void clearRubberband() {
        rubberband = null;
    }

    /**
     * Checks to see any lines in the model are within bounds of an x and y and threshold
     *
     * @param x: x bounds
     * @param y: y bounds
     * @param threshold: distance between line and click that is valid
     * @return: true if any lines are within bounds, false otherwise
     */

    public boolean contains(double x, double y, double threshold) {
        return lines.stream().anyMatch(e -> e.contains(x, y, threshold));
    }

    /**
     * Looks for the first entity that contains the given x y coordinates
     *
     * @param x: x position to check
     * @param y: y position to check
     * @param threshold: distance between line and click that is valid
     * @return: the first entity found with the coords
     */
    public DLine whichEntity(double x, double y, double threshold) {
        return lines.stream()
                .filter(e -> e.contains(x, y, threshold))
                .findFirst()
                .orElse(null);
    }


    /**
     * Adds a subscriber to the publishing/subscribe pattern
     * @param s: subscriber to add
     */
    public void addSubscriber(Subscriber s) {
        subs.add(s);
    }

    /**
     * Notifies the subscribers that the model has changed, which then causes
     * the subscribers to update their views/states
     */
    private void notifySubscribers() {
        subs.forEach(Subscriber::modelChanged);
    }

}
