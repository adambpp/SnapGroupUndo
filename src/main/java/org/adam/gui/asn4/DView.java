package org.adam.gui.asn4;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.List;

public class DView extends StackPane implements Subscriber{

    private double width, height;
    private int r = 5;
    private GraphicsContext gc;
    private Canvas myCanvas;
    private LineModel model;
    private iModel imodel;

    public DView(){
        width = 1000;
        height = 800;
        myCanvas = new Canvas(width, height);
        gc = myCanvas.getGraphicsContext2D();

        Platform.runLater(this::draw);
        Platform.runLater(myCanvas::requestFocus);

        this.getChildren().add(myCanvas);
    }

    public void setModel(LineModel model) {
        this.model = model;
    }

    public void setIModel(iModel imodel) {
        this.imodel = imodel;
    }

    public void setupEvents(AppController controller) {
        setOnMousePressed(controller::handlePressed);
        setOnMouseDragged(controller::handleDragged);
        setOnMouseReleased(controller::handleReleased);
        setOnKeyPressed(controller::handleKeyPressed);
        setOnKeyReleased(controller::handleKeyReleased);
        setOnMouseMoved(controller::handleMouseMoved);
    }

    /**
     * This is the main draw method that gets called everytime we notifySubs. It will draw every line with their
     * updated values and whatever else.
     */
    private void draw() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        drawGrid();
        for (Groupable element : model.getElements() ) {
            if (element instanceof DLine line) {
                drawline(line);
            } else if (element instanceof DGroup group) {
                drawGroup(group);
            }
        }
        drawRubberBand();
    }

    private void drawline(DLine dl) {
        // draw hover line
        if (dl == imodel.getHovered()) {
            gc.setLineWidth(10);
            gc.setStroke(Color.rgb(211, 211, 211, 0.6));
            gc.strokeLine(dl.getX1(), dl.getY1(), dl.getX2(), dl.getY2());
        }

        // draw normal line
        gc.setLineWidth(2);
//            gc.setStroke(dl == imodel.getSelection() ? Color.PINK : Color.PURPLE);
        gc.setStroke(imodel.getSelection().contains(dl) ? Color.PINK : Color.PURPLE);
        gc.strokeLine(dl.getX1(), dl.getY1(), dl.getX2(), dl.getY2());

        drawEndpoints(dl, dl.getX1(), dl.getY1(), dl.getX2(), dl.getY2());
    }

    private void drawGroup(DGroup group) {
        group.makeBoundingBoxDimensions();
        gc.setLineWidth(4);
        gc.setStroke(Color.PINK);
        gc.strokeRect(group.getMinX(), group.getMinY(), (group.getMaxX() - group.getMinX()), (group.getMaxY() - group.getMinY()));

        List<Groupable> children = group.getChildren();
        for (Groupable child : children) {
            if (child instanceof DLine line) {
                gc.setLineWidth(2);
                gc.setStroke(Color.PURPLE);
                gc.strokeLine(line.getX1(), line.getY1(), line.getX2(), line.getY2());
            }
        }
    }

    /**
     * Draws the grid that helps visualize where the snap points are.
     */
    private void drawGrid() {
        gc.setStroke(Color.rgb(0, 0, 0, 0.5));
        gc.setLineWidth(0.5);
        // x axis
        for (int i = 0; i <= 1000; i+= 20) {
            gc.strokeLine(i, 0, i, 800);
        }
        // y axis
        for (int j = 0; j <= 800; j+= 20) {
            gc.strokeLine(0, j, 1000, j);
        }
    }

    public void drawRubberBand() {
        RubberBand box = model.getRubberband();
        if (box != null) {
            gc.save();
            gc.setStroke(Color.RED);
            gc.setLineWidth(2);
            gc.setLineDashes(10, 10);
            gc.strokeRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());
            gc.restore();
        }
    }

    /**
     * Draws an endpoint on each end of the line, and then sets the endpoint object with the properties of
     * where it was drawn.
     *
     * @param line: line to draw endpoints on
     * @param x1: x pos for endpoint1
     * @param y1: y pos for endpoint1
     * @param x2: x pos for endpoint2
     * @param y2: y pos for endpoint2
     */
    private void drawEndpoints(DLine line, double x1, double y1, double x2, double y2) {
        if (imodel.getSelection().contains(line)) {
            //gc.setFill(Color.LIGHTGRAY);
            gc.setStroke(line == imodel.getSelection() ? Color.PINK : Color.PURPLE);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);

            // endpoint 1
            //                                  Accessing the enum like this is prob not a good idea, should change later
            gc.setFill(line.getCurEndpoint() == DLine.Endpoints.ENDPOINT_1 ? Color.LIGHTGREEN : Color.LIGHTGRAY);
            gc.fillOval(x1 - r, y1 - r, r * 2, r * 2);
            gc.strokeOval(x1 - r, y1 - r, r * 2, r * 2);
            line.setEndpoint1(x1 - r, y1 - r, r * 2);

            // endpoint 2
            gc.setFill(line.getCurEndpoint() == DLine.Endpoints.ENDPOINT_2 ? Color.LIGHTGREEN : Color.LIGHTGRAY);
            gc.fillOval(x2 - r, y2 - r, r * 2, r * 2);
            gc.strokeOval(x2 - r, y2 - r, r * 2, r * 2);
            line.setEndpoint2(x2 - r, y2 - r, r * 2);
        }
    }

    @Override
    public void modelChanged() {
        draw();
    }
}
