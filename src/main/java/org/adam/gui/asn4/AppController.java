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

    public void handleMouseMoved(MouseEvent e) {
        currentState.handleMouseMoved(e);
    }

    private void snapToGrid(DLine line, double lineX, double lineY) {
        double roundedX = Math.round(lineX / 20) * 20;
        double roundedY = Math.round(lineY / 20) * 20;
        model.adjustLine(line, roundedX, roundedY);
    }

    private abstract class ControllerState {
        void handlePressed(MouseEvent e) {}
        void handleDragged(MouseEvent e) {}
        void handleReleased(MouseEvent e) {}
        void handleKeyPressed(KeyEvent e) {}
        void handleKeyReleased(KeyEvent e) {}
        void handleMouseMoved(MouseEvent e) {}
    }

    /**
     * Ready state. This is the state we are in when a mouse event first happens, and then we
     * switch states based on the next user action
     */
    ControllerState ready = new ControllerState() {

        @Override
        void handleMouseMoved(MouseEvent e) {
            if (model.contains(e.getX(), e.getY(), 5)) {
                System.out.println("mouse movement in range of a line");
                iModel.setHovered(model.whichEntity(e.getX(), e.getY(), 5));
            } else {
                iModel.clearHovered();
            }
        }

        @Override
        void handlePressed(MouseEvent e) {
            prevX = e.getX();
            prevY = e.getY();
            System.out.println("mouse pressed in ready state");

            // first check if the mouse click is close enough to a line
            if (model.contains(e.getX(), e.getY(), 5)) {
                iModel.setSelected(model.whichEntity(e.getX(), e.getY(), 5));

                // check if we clicked on an endpoint before trying to move a line
                DLine line = iModel.getSelected();
                if (isWithinEndpoint(e.getX(), e.getY(), line.getX1(), line.getY1(), 5)) {
                    System.out.println("Current endpoint set to endpoint 1");
                    line.setCurEndpoint(0);
                    currentState = endpointAdjusting;
                } else if (isWithinEndpoint(e.getX(), e.getY(), line.getX2(), line.getY2(), 5)) {
                    System.out.println("Current endpoint set to endpoint 2");
                    line.setCurEndpoint(1);
                    currentState = endpointAdjusting;

                // move the line if we were not on an endpoint
                } else {
                    currentState = moving;
                }
            // clear selection otherwise
            } else {
                iModel.clearSelection();
                }
        }

        @Override
        void handleKeyPressed(KeyEvent e) {
            if (Objects.requireNonNull(e.getCode()) == KeyCode.DELETE || e.getCode() == KeyCode.BACK_SPACE) {
                System.out.println("delete or backspace was pressed, deleting line");
                if (iModel.getSelected() != null) {
                    model.removeLine(iModel.getSelected());
                }
            } else if (e.isShiftDown()) {
                System.out.println("shift key pressed, about to create new line");
                currentState = createOrDeselect;
            }
        }

        private boolean isWithinEndpoint(double mouseX, double mouseY, double endpX, double endpY, double r) {
            double diffX = mouseX - endpX;
            double diffY = mouseY - endpY;
            double dist = Math.sqrt((diffX * diffX) + (diffY * diffY));
            return dist <= r * 2;
        }
    };

    /**
     *  This is an intermediary state. If a drag is detected after the initial press, we go to creating, otherwise
     *  we clear selection and go back to ready state
     */
    //TODO: This should be changed to something like "SelectionBoxOrDeselect" (for part 2)
    ControllerState createOrDeselect = new ControllerState() {
        @Override
        void handleKeyReleased(KeyEvent e) {
            if (Objects.requireNonNull(e.getCode()) == KeyCode.SHIFT) {
                currentState = ready;
            }
        }

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
            DLine line = iModel.getSelected();
            System.out.println("mouse released in creating state, going back to ready");
            double roundedX = Math.round(line.getX2() / 20) * 20;
            double roundedY = Math.round(line.getY2() / 20) * 20;
            model.adjustLine(line, roundedX, roundedY);
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

    ControllerState endpointAdjusting = new ControllerState() {
        @Override
        void handleDragged(MouseEvent e) {
            System.out.println("adjusting endpoint");
            DLine line = iModel.getSelected();
            model.adjustEndpoint(line,e.getX(),e.getY());
        }

        @Override
        void handleReleased(MouseEvent e) {
            System.out.println("mouse released, stopping endpoint adjustment");

            // this code lowkey messy but whatever
            DLine line = iModel.getSelected();
            double roundedX;
            double roundedY;
            if (line.getCurEndpoint() == DLine.Endpoints.ENDPOINT_1) {
                roundedX = Math.round(line.getX1() / 20) * 20;
                roundedY = Math.round(line.getY1() / 20) * 20;
            } else {
                roundedX = Math.round(line.getX2() / 20) * 20;
                roundedY = Math.round(line.getY2() / 20) * 20;
            }
            model.adjustEndpoint(line,roundedX,roundedY);
            iModel.getSelected().clearEndpointSelection();
            currentState = ready;
        }
    };
}
