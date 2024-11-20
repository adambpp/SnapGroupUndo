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

    // TODO: implement contains in a way that works for a line (look in the course slides)
    public boolean contains(double x, double y) {
        return true;
    }

    /**
     * Looks for the first entity that contains the given x y coordinates
     *
     * @param x: x position to check
     * @param y: y position to check
     * @return: the first entity found with the coords
     */
    public DLine whichEntity(double x, double y) {
        return lines.stream()
                .filter(e -> e.contains(x, y))
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
