package org.adam.gui.asn4;

import java.util.ArrayList;
import java.util.List;

public class iModel {
    private ArrayList<Groupable> selection;
    private Groupable hovered;
    private DLine Selected;
    private ArrayList<Subscriber> subs;

    public iModel() {
        selection = new ArrayList<>();
        subs = new ArrayList<>();
    }

    /**
     * Add a line to the selection ArrayList
     *
     * @param element: A line or group object to be added to the selection list
     */
    public void addToSelection(Groupable element) {
        selection.add(element);
        notifySubscribers();
    }

    /**
     * Remove a line from the selection list
     *
     * @param line: A line object to be removed
     */
    public void removeFromSelection(DLine line) {
        selection.remove(line);
        notifySubscribers();
    }

    public void setHovered(Groupable line) {
        hovered = line;
        notifySubscribers();
    }

    public void setSelected(DLine line) {
        Selected = line;
        notifySubscribers();
    }

    /**
     * Gets the selected line so that the view and controller can perform operations on it
     *
     * @return: The selected line
     */
    public List<Groupable> getSelection() {
        return selection;
    }

    public Groupable getHovered() {
        return hovered;
    }

    public DLine getSelected() {
        return Selected;
    }

    /**
     * Clear the currently selected line by setting selected to null
     */
    public void clearSelection() {
        selection.clear();
        hovered = null;
        notifySubscribers();
    }

    public void clearHovered() {
        hovered = null;
        notifySubscribers();
    }

    public void somethingChanged() {
        notifySubscribers();
    }

    /**
     * Adds a subscriber to the publishing/subscribe pattern
     * @param s: subscriber to add
     */
    public void addSubscriber(Subscriber s) {
        subs.add(s);
    }

    /**
     * Notifies the subscribers that the iModel has changed, which then causes
     * the subscribers to update their views/states
     */
    private void notifySubscribers() {
        subs.forEach(Subscriber::modelChanged);
    }

}
