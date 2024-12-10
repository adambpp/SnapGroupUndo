package org.adam.gui.asn4;

import java.util.ArrayList;
import java.util.List;

public class LineModel {
    private ArrayList<Subscriber> subs;
    private List<Groupable> elements;
    private RubberBand rubberband;

    public LineModel() {
        subs = new ArrayList<>();
        elements = new ArrayList<>();
        rubberband = null;
    }

    /**
     * Get the list of lines currently in the model
     * @return: The Arraylist of lines
     */

    public List<Groupable> getElements() {
        return elements;
    }

    public DLine addLine(double x1, double y1, double x2, double y2) {
        DLine line = new DLine(x1, y1, x2, y2);
        elements.add(line);
        return line;
    }

    public DGroup group(List<Groupable> sel) {
        DGroup linegroup = new DGroup(sel);
        sel.forEach(this::removeElement);

        elements.add(linegroup);
        notifySubscribers();
        return linegroup;
    }

    // make the ungrouped lines be selected after in controller (maybe? look at video first)
    public void ungroup(DGroup linegroup) {
        List<Groupable> elements = linegroup.getChildren();
        removeElement(linegroup);

        for(Groupable g : elements) {
            if(g instanceof DLine line) {
                addLine(line.getX1(), line.getY1(), line.getX2(), line.getY2());
            }
            else if (g instanceof DGroup group) {
                group(group.getChildren());
            }
        }
        notifySubscribers();
    }

    public void removeElement(Groupable element) {
        elements.remove(element);
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

    public void moveLine(List<Groupable> lines, double nX, double nY) {
        lines.forEach(line -> line.move(nX, nY));
        notifySubscribers();
    }

    public void scaleLine(List<Groupable> lines, double scale, int UpOrDown) {
        lines.forEach(line -> line.scale(scale, UpOrDown));
        notifySubscribers();
    }

    public void rotateElement(List<Groupable> elements, double rotation_amount) {
        elements.forEach(e -> e.rotate(rotation_amount));
        notifySubscribers();
    }

    public DLine findLineWithCurEndpoint(List<Groupable> lines) {
        for(Groupable element : lines ) {
            if (element instanceof DLine line) {
                if (line.getCurEndpoint() != null) {
                    return line;
                }
            }
        }
        return null;
    }

    public void clearEndpointSelection(List<Groupable> elements) {
        for(Groupable element : elements ) {
            if (element instanceof DLine line) {
                line.clearEndpointSelection();
            }
        }
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

    public List<Groupable> rubberBandLineSelect() {
        List<Groupable> linesWithinBounds = new ArrayList<>();


        for(Groupable element : elements) {

            // check if a line object is within bounds
            if (element instanceof DLine line) {
                if (rubberband.contains(line.getX1(), line.getY1()) || rubberband.contains(line.getX2(), line.getY2())) {
                    linesWithinBounds.add(line);
                }
            } else if (element instanceof DGroup group) {
                // loop through groups children to see if any of its lines are within bounds
                for (Groupable groupchild : group.getChildren()) {
                    if (groupchild instanceof DLine line) {
                        if (rubberband.contains(line.getX1(), line.getY1()) || rubberband.contains(line.getX2(), line.getY2())) {
                            linesWithinBounds.add(element);
                            break;
                        }
                    }
                }
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
        return elements.stream().anyMatch(e -> e.contains(x, y, threshold));
    }

    /**
     * Looks for the first entity that contains the given x y coordinates
     *
     * @param x: x position to check
     * @param y: y position to check
     * @param threshold: distance between line and click that is valid
     * @return: the first entity found with the coords
     */
    public Groupable whichEntity(double x, double y, double threshold) {
        return elements.stream()
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
