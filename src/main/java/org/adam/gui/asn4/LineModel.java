package org.adam.gui.asn4;

import java.util.ArrayList;
import java.util.List;

public class LineModel {
    private ArrayList<Subscriber> subs;
    private ArrayList<DLine> lines;

    public LineModel() {
        subs = new ArrayList<>();
        lines = new ArrayList<>();
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

    public void moveLine(DLine line, double nX, double nY) {
        line.move(nX, nY);
        notifySubscribers();
    }

    public void scaleLine(DLine line, double scale) {
        line.scaleLine(scale);
        notifySubscribers();
    }

    public void rotateLine(DLine line, double rotation_amount) {
        line.rotateLine(rotation_amount);
        notifySubscribers();
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
