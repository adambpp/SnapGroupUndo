package org.adam.gui.asn4;

import java.util.ArrayList;

public class iModel {
    private DLine selected;
    private ArrayList<Subscriber> subs;

    public iModel() {
        selected = null;
        subs = new ArrayList<>();
    }

    /**
     * Set the currently selected line
     *
     * @param line: A line object to be set as selected
     */
    public void setSelected(DLine line) {
        selected = line;
        notifySubscribers();
    }

    /**
     * Gets the selected line so that the view and controller can perform operations on it
     *
     * @return: The selected line
     */
    public DLine getSelected() {
        return selected;
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
