package org.adam.gui.asn4;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;

public class DView extends StackPane implements Subscriber{

    private double width, height;
    private GraphicsContext gc;
    private Canvas myCanvas;
    private LineModel model;
    private iModel iModel;

    public DView(){
        width = 800;
        height = 800;
        myCanvas = new Canvas(width, height);
        gc = myCanvas.getGraphicsContext2D();

        this.getChildren().add(myCanvas);
    }

    public void setModel(LineModel model) {
        this.model = model;
    }

    public void setIModel(iModel imodel) {
        this.iModel = imodel;
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
        for (DLine dl : model.getLines() ) {
            gc.strokeLine(dl.getX1(), dl.getY1(), dl.getX2(), dl.getY2());
        }
    }

    @Override
    public void modelChanged() {
        draw();
    }
}
