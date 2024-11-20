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

    public void handlePressed(MouseEvent e) {
        currentState.handlePressed(e);
    }

    public void handleDragged(MouseEvent e) {
        currentState.handleDragged(e);
    }

    public void handleReleased(MouseEvent e) {
        currentState.handleReleased(e);
    }

    public void handleKeyPressed(KeyEvent e) {
        currentState.handleKeyPressed(e);
    }
    public void handleKeyReleased(KeyEvent e) {
        currentState.handleKeyReleased(e);
    }


    private abstract class ControllerState {
        void handlePressed(MouseEvent e) {}
        void handleDragged(MouseEvent e) {}
        void handleReleased(MouseEvent e) {}
        void handleKeyPressed(KeyEvent e) {}
        void handleKeyReleased(KeyEvent e) {}
    }

    /**
     * Ready state. This is the state we are in when a mouse event first happens, and then we
     * switch states based on the next user action
     */
    ControllerState ready = new ControllerState() {
        @Override
        void handlePressed(MouseEvent e) {
            System.out.println("mouse pressed in ready state");
            DLine line = model.addLine(e.getX(),e.getY(),e.getX(),e.getY());
            iModel.setSelected(line);
            currentState = creating;
        }
    };

    /**
     * Creating state. This is the state where lines get created (obviously). Line is being created/constantly
     * resized until the mouse is released.
     */
    ControllerState creating = new ControllerState() {
        @Override
        void handleDragged(MouseEvent e) {
            System.out.println("creating line in creating state");
            model.adjustLine(iModel.getSelected(),e.getX(),e.getY());
        }

        @Override
        void handleReleased(MouseEvent e) {
            System.out.println("mouse released in creating state, going back to ready");
            iModel.clearSelection();
            currentState = ready;
        }
    };
}
