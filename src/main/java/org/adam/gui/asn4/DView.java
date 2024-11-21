package org.adam.gui.asn4;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class DView extends StackPane implements Subscriber{

    private double width, height;
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
    }

    private void draw() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        drawGrid();
        for (DLine dl : model.getLines() ) {
            gc.setLineWidth(2);
            gc.setStroke(dl == imodel.getSelected() ? Color.PINK : Color.GREY);
            gc.strokeLine(dl.getX1(), dl.getY1(), dl.getX2(), dl.getY2());
        }
    }

    private void drawGrid() {
        gc.setStroke(Color.rgb(0, 0, 0, 0.5));
        gc.setLineWidth(0.5);
        // x axis
        for (int i = 0; i <= 1000; i+= 20) {
            gc.strokeLine(i, 0, i, 800);
        }
        // y axis
        for (int j = 0; j <= 1000; j+= 20) {
            gc.strokeLine(0, j, 1000, j);
        }
    }

    @Override
    public void modelChanged() {
        draw();
    }
}
