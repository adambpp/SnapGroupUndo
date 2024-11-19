package org.adam.gui.asn4;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class AppController {
    private LineModel model;
    private iModel iModel;
    private ControllerState currentState;

    public AppController() {
        currentState = ready;
    }

    public void setModel(LineModel m) {
        model = m;
    }

    public void setIModel(iModel im) {
        iModel = im;
    }

    public void handlePressed(MouseEvent event) {
        currentState.handlePressed(event);
    }

    public void handleDragged(MouseEvent event) {
        currentState.handleDragged(event);
    }

    public void handleReleased(MouseEvent event) {
        currentState.handleReleased(event);
    }

    public void handleKeyPressed(KeyEvent event) {
        currentState.handleKeyPressed(event);
    }
    public void handleKeyReleased(KeyEvent event) {
        currentState.handleKeyReleased(event);
    }


    private abstract class ControllerState {
        void handlePressed(MouseEvent event) {}
        void handleDragged(MouseEvent event) {}
        void handleReleased(MouseEvent event) {}
        void handleKeyPressed(KeyEvent event) {}
        void handleKeyReleased(KeyEvent event) {}
    }


    ControllerState ready = new ControllerState() {

    };
}
