package org.adam.gui.asn4;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.Objects;

public class AppController {
    private LineModel model;
    private iModel imodel;
    private ControllerState currentState;
    private double prevX, prevY, dX, dY;

    public AppController() {
        currentState = ready;
    }

    public void setModel(LineModel m) {
        model = m;
    }

    public void setIModel(iModel im) {
        imodel = im;
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
                imodel.setHovered(model.whichEntity(e.getX(), e.getY(), 5));
            } else {
                imodel.clearHovered();
            }
        }

        @Override
        void handlePressed(MouseEvent e) {
            prevX = e.getX();
            prevY = e.getY();
            System.out.println("mouse pressed in ready state");

            // first check if the mouse click is close enough to a line
            if (model.contains(e.getX(), e.getY(), 5)) {
                DLine clickedLine = model.whichEntity(e.getX(), e.getY(), 5);

                // if selection list is empty then we can add the clicked line to it
                if (imodel.getSelection().isEmpty()) {
                    imodel.clearSelection();
                    imodel.addToSelection(clickedLine);
                    imodel.setSelected(clickedLine);
                } else if (!imodel.getSelection().contains(clickedLine)) {
                    // if it's not empty and doesn't contain the clicked line then
                    // clear it first since we are not holding down control here
                    imodel.clearSelection();
                    imodel.addToSelection(clickedLine);
                    imodel.setSelected(clickedLine);
                }
                // else if the clicked line is already part of the selection then we don't need to do anything


                // check if we clicked on an endpoint before trying to move a line
                for (DLine line: imodel.getSelection()) {
                    if (isWithinEndpoint(e.getX(), e.getY(), line.getX1(), line.getY1(), 5)) {
                        System.out.println("Current endpoint set to endpoint 1");
                        line.setCurEndpoint(0);
                        currentState = endpointAdjusting;
                    } else if (isWithinEndpoint(e.getX(), e.getY(), line.getX2(), line.getY2(), 5)) {
                        System.out.println("Current endpoint set to endpoint 2");
                        line.setCurEndpoint(1);
                        currentState = endpointAdjusting;
                    }
                }
                // move the line if we were not on an endpoint
                if (model.findLineWithCurEndpoint(imodel.getSelection()) == null) {
                    currentState = moving;
                }
                // clear selection otherwise
            } else {
                model.initRubberband(e.getX(), e.getY());
                currentState = rubberBandOrDeselect;
            }

        }

        @Override
        void handleKeyPressed(KeyEvent e) {
            if (Objects.requireNonNull(e.getCode()) == KeyCode.DELETE || e.getCode() == KeyCode.BACK_SPACE) {
                System.out.println("delete or backspace was pressed, deleting line");
                if (imodel.getSelection() != null) {
                    for (DLine line: imodel.getSelection()) {
                        model.removeLine(line);
                    }
                }
            } else if (e.isShiftDown()) {
                System.out.println("shift key pressed, about to create new line");
                currentState = creating;

            } else if (e.isControlDown()) {
                currentState = multipleSelect;
            } else if (Objects.requireNonNull(e.getCode()) == KeyCode.UP) {
                if (imodel.getSelection() != null) {
                    model.scaleLine(imodel.getSelection(), 0.05, 0);
                }
            }else if (Objects.requireNonNull(e.getCode()) == KeyCode.DOWN) {
                if (imodel.getSelection() != null) {
                    model.scaleLine(imodel.getSelection(), 0.05, 1);
                }
            } else if (Objects.requireNonNull(e.getCode()) == KeyCode.LEFT) {
                if (imodel.getSelection() != null) {
                    model.rotateLine(imodel.getSelection(), 25);
                }
            } else if (Objects.requireNonNull(e.getCode()) == KeyCode.RIGHT) {
                if (imodel.getSelection() != null) {
                    model.rotateLine(imodel.getSelection(), -25);
                }
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
     * Multiple Select state. This is where we go after control is held down, to add multiple lines to our selection
     */
    ControllerState multipleSelect = new ControllerState() {
        @Override
        void handleMouseMoved(MouseEvent e) {
            if (model.contains(e.getX(), e.getY(), 5)) {
                imodel.setHovered(model.whichEntity(e.getX(), e.getY(), 5));
            } else {
                imodel.clearHovered();
            }
        }

        @Override
        void handlePressed(MouseEvent e) {
            if (model.contains(e.getX(), e.getY(), 5)) {
                DLine clickedLine = model.whichEntity(e.getX(), e.getY(), 5);

                // add line to selection if not already in selection
                if (!imodel.getSelection().contains(clickedLine)) {
                    imodel.addToSelection(clickedLine);
                } else {
                    // remove line from selection if already in selection list
                    imodel.removeFromSelection(clickedLine);
                }

            }
        }

        @Override
        void handleKeyReleased(KeyEvent e) {
            currentState = ready;
        }
    };

    /**
     *  This is an intermediary state. If a drag is detected after the initial press, we go to creating, otherwise
     *  we clear selection and go back to ready state
     */
    //TODO: This should be changed to something like "SelectionBoxOrDeselect" (for part 2)
    ControllerState rubberBandOrDeselect = new ControllerState() {

        @Override
        void handleDragged(MouseEvent e) {
            dX = e.getX() - prevX;
            dY = e.getY() - prevY;
            model.resizeRubberband(dX, dY);
        }

        @Override
        void handleReleased(MouseEvent e) {
            System.out.println("mouse released rubberBandOrDeselect, deselecting and going back to ready");
            model.clearRubberband();
            imodel.clearSelection();
            currentState = ready;
        }
    };

    /**
     * Creating state. This is the state where lines get created (obviously). Line is being created/constantly
     * resized until the mouse is released.
     */
    ControllerState creating = new ControllerState() {
        @Override
        void handleKeyReleased(KeyEvent e) {
            if (e.getCode() == KeyCode.SHIFT) {
                currentState = ready;
            }
        }

        @Override
        void handleDragged(MouseEvent e) {
            System.out.println("creating line in creating state");
            model.adjustLine(imodel.getSelected(),e.getX(),e.getY());
        }

        @Override
        void handlePressed(MouseEvent e) {
            DLine line = model.addLine(e.getX(), e.getY(), e.getX(), e.getY());
            // clear selection array, the set single selection to the new line, and then add it to selection array
            imodel.clearSelection();
            imodel.setSelected(line);
            imodel.addToSelection(line);

            // this is messy af, change later
            double roundedX = Math.round(line.getX1() / 20) * 20;
            double roundedY = Math.round(line.getY1() / 20) * 20;
            line.setCurEndpoint(0);
            model.adjustEndpoint(line, roundedX, roundedY);
            line.clearEndpointSelection();
        }

        @Override
        void handleReleased(MouseEvent e) {
            DLine line = imodel.getSelected();
            System.out.println("mouse released in creating state, going back to ready");
            double roundedX = Math.round(line.getX2() / 20) * 20;
            double roundedY = Math.round(line.getY2() / 20) * 20;
            model.adjustLine(line, roundedX, roundedY);
            //imodel.addToSelection(line);
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

            model.moveLine(imodel.getSelection(),dX,dY);
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
            DLine line = model.findLineWithCurEndpoint(imodel.getSelection());
            model.adjustEndpoint(line,e.getX(),e.getY());
        }

        @Override
        void handleReleased(MouseEvent e) {
            System.out.println("mouse released, stopping endpoint adjustment");

            // this code lowkey messy but whatever
            DLine line = model.findLineWithCurEndpoint(imodel.getSelection());
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
            model.clearEndpointSelection(imodel.getSelection());
            currentState = ready;
        }
    };
}
