package org.adam.gui.asn4;

import java.util.ArrayList;
import java.util.List;

public class iModel {
    private ArrayList<DLine> selected;
    private DLine hovered;
    private DLine newLine;
    private ArrayList<Subscriber> subs;

    public iModel() {
        selected = new ArrayList<>();
        subs = new ArrayList<>();
    }

    /**
     * Set the currently selected line
     *
     * @param line: A line object to be set as selected
     */
    public void addToSelection(DLine line) {
        selected.add(line);
        notifySubscribers();
    }

    public void setHovered(DLine line) {
        hovered = line;
        notifySubscribers();
    }

    public void setNewLine(DLine line) {
        newLine = line;
        notifySubscribers();
    }

    /**
     * Gets the selected line so that the view and controller can perform operations on it
     *
     * @return: The selected line
     */
    public List<DLine> getSelection() {
        return selected;
    }

    public DLine getHovered() {
        return hovered;
    }

    public DLine getNewLine() {
        return newLine;
    }

    /**
     * Clear the currently selected line by setting selected to null
     */
    public void clearSelection() {
        selected.clear();
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
