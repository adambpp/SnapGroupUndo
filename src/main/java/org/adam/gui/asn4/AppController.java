package org.adam.gui.asn4;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.Objects;

public class AppController {
    private LineModel model;
    private iModel iModel;
    private ControllerState currentState;
    private double prevX, prevY, dX, dY;

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
            if (model.contains(e.getX(), e.getY(), 5)) {
                System.out.println("Mouse click within bounds of line");
                iModel.setSelected(model.whichEntity(e.getX(), e.getY(), 5));
                currentState = moving;
                prevX = e.getX();
                prevY = e.getY();

            }
        }

        @Override
        void handleKeyPressed(KeyEvent e) {
            if (e.isShiftDown()) {
                System.out.println("shift key pressed, about to create new line");
                currentState = createOrDeselect;
            }
        }
    };

    /**
     *  This is an intermediary state. If a drag is detected after the initial press, we go to creating, otherwise
     *  we clear selection and go back to ready state
     */
    ControllerState createOrDeselect = new ControllerState() {
        @Override
        void handleDragged(MouseEvent e) {
            DLine line = model.addLine(e.getX(), e.getY(), e.getX(), e.getY());
            iModel.setSelected(line);
            currentState = creating;
        }

        @Override
        void handleReleased(MouseEvent e) {
            System.out.println("mouse released createOrDeselect, deselecting and going back to ready");
            iModel.clearSelection();
            currentState = ready;
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
            currentState = ready;
        }
    };

    /**
     * Moving state. If a line is selected then the user can move that line around until the mouse click is released.
     */
    ControllerState moving = new ControllerState() {
        @Override
        void handleDragged(MouseEvent e) {
            System.out.println("moving selected line");
            dX = e.getX() - prevX;
            dY = e.getY() - prevY;
            prevX = e.getX();
            prevY = e.getY();

            model.moveLine(iModel.getSelected(),dX,dY);
        }

        @Override
        void handleReleased(MouseEvent e) {
            currentState = ready;
        }
    };
}
