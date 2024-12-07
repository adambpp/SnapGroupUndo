package org.adam.gui.asn4;

import java.util.ArrayList;
import java.util.List;

public class iModel {
    private ArrayList<DLine> selection;
    private DLine hovered;
    private DLine Selected;
    private ArrayList<Subscriber> subs;

    public iModel() {
        selection = new ArrayList<>();
        subs = new ArrayList<>();
    }

    /**
     * Add a line to the selection ArrayList
     *
     * @param line: A line object to be added to the selection list
     */
    public void addToSelection(DLine line) {
        selection.add(line);
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

    public void setHovered(DLine line) {
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
    public List<DLine> getSelection() {
        return selection;
    }

    public DLine getHovered() {
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
