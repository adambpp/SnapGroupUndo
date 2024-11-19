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

    public void addLine(double x, double y) {
        //lines.add(new DLine(x, y));
        notifySubscribers();
    }

    public void removeLine(DLine line) {
        lines.remove(line);
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
//    public DLine whichEntity(double x, double y) {
//        return lines.stream()
//                .filter(e -> e.contains(x, y))
//                .findFirst()
//                .orElse(null);
//    }


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
