package org.adam.gui.asn4;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.List;
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
                Groupable clickedElement = model.whichEntity(e.getX(), e.getY(), 5);

                // if selection list is empty then we can add the clicked line to it
                if (imodel.getSelection().isEmpty()) {
                    imodel.clearSelection();
                    imodel.addToSelection(clickedElement);
                    //imodel.setSelected(clickedElement);
                } else if (!imodel.getSelection().contains(clickedElement)) {
                    // if it's not empty and doesn't contain the clicked line then
                    // clear it first since we are not holding down control here
                    imodel.clearSelection();
                    imodel.addToSelection(clickedElement);
                    //imodel.setSelected(clickedElement);
                }
                // else if the clicked line is already part of the selection then we don't need to do anything


                // check if we clicked on an endpoint before trying to move a line
                for (Groupable element: imodel.getSelection()) {
                    if (element instanceof DLine line) {
                    if (isWithinEndpoint(e.getX(), e.getY(), line.getX1(), line.getY1(), 5)) {
                        System.out.println("Current endpoint set to endpoint 1");
                        line.setCurEndpoint(0);
                        currentState = endpointAdjusting;
                    } else if (isWithinEndpoint(e.getX(), e.getY(), line.getX2(), line.getY2(), 5)) {
                        System.out.println("Current endpoint set to endpoint 2");
                        line.setCurEndpoint(1);
                        currentState = endpointAdjusting;

                    }                    }
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
                    for (Groupable element: imodel.getSelection()) {
                        model.removeElement(element);
                        if (element instanceof DLine line) {
                            DeleteCommand cmd = new DeleteCommand(model, line);
                            imodel.clearRedoStack();
                            imodel.addToUndoStack(cmd);
                        }
                    }
                }
            // CHECKING SHIFT FOR LINE CREATION
            } else if (e.isShiftDown()) {
                System.out.println("shift key pressed, about to create new line");
                currentState = creating;

            }
            // CHECKING CONTROL FOR MULTIPLE SELECT
            else if (e.isControlDown()) {
                currentState = multipleSelect;


            }
            // CHECKING G/U FOR GROUPING
            else if (Objects.requireNonNull(e.getCode()) == KeyCode.G) {
                List<Groupable> selection = imodel.getSelection();
                if (!selection.isEmpty()) {
                    DGroup newGroup = model.group(selection);
                    imodel.clearSelection();
                    imodel.addToSelection(newGroup);
                }
            } else if (Objects.requireNonNull(e.getCode()) == KeyCode.U){
                if (imodel.getSelection() != null) {
                    for (Groupable element: imodel.getSelection()) {
                        if (element instanceof DGroup group) {
                            model.ungroup(group);
                        }
                    }
                }
            }
            // CHECKING UP/DOWN FOR SCALING
            else if (Objects.requireNonNull(e.getCode()) == KeyCode.UP) {
                if (imodel.getSelection() != null) {
                    model.scaleLine(imodel.getSelection(), 0.05, 0);
                }
            }else if (Objects.requireNonNull(e.getCode()) == KeyCode.DOWN) {
                if (imodel.getSelection() != null) {
                    model.scaleLine(imodel.getSelection(), 0.05, 1);
                }

            }

            // CHECKING LEFT/RIGHT FOR ROTATING
            else if (Objects.requireNonNull(e.getCode()) == KeyCode.LEFT) {
                if (imodel.getSelection() != null) {
                    model.rotateElement(imodel.getSelection(), 5);
                }
            } else if (Objects.requireNonNull(e.getCode()) == KeyCode.RIGHT) {
                if (imodel.getSelection() != null) {
                    model.rotateElement(imodel.getSelection(), -5);
                }
            } else if (Objects.requireNonNull(e.getCode()) == KeyCode.Z) {
                imodel.handleUndo();
            } else if (Objects.requireNonNull(e.getCode()) == KeyCode.R) {
                imodel.handleRedo();
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
            model.initRubberband(e.getX(), e.getY());
            prevX = e.getX();
            prevY = e.getY();
            if (model.contains(e.getX(), e.getY(), 5)) {
                Groupable clickedElement = model.whichEntity(e.getX(), e.getY(), 5);

                // add line to selection if not already in selection
                if (!imodel.getSelection().contains(clickedElement)) {
                    imodel.addToSelection(clickedElement);
                } else {
                    // remove line from selection if already in selection list
                    imodel.removeFromSelection(clickedElement);
                }

            }
        }

        @Override
        void handleDragged(MouseEvent e) {
            dX = e.getX() - prevX;
            dY = e.getY() - prevY;
            model.resizeRubberband(dX, dY);
        }

        @Override
        void handleKeyReleased(KeyEvent e) {
            // get list of lines within the rectangle
            List<Groupable> lines = model.rubberBandLineSelect();
            for (Groupable line: lines) {
                // remove line from selection if already in selection list
                if (imodel.getSelection().contains(line)) {
                    imodel.removeFromSelection(line);
                } else {
                    // add line to selection if not already in selection
                    imodel.addToSelection(line);
                }
            }
            model.clearRubberband();
            currentState = ready;
        }
    };

    /**
     *  In this state we either resize the rubber band or deselect all lines and clear the rubber band
     *  (yes this means I am always creating a rubberband box even when there is no mouse drag)
     */
    ControllerState rubberBandOrDeselect = new ControllerState() {

        @Override
        void handleDragged(MouseEvent e) {
            // I could initialize the rubber band here instead of in ready state perhaps
            currentState = rubberBandSelectOnly;
        }

        @Override
        void handleReleased(MouseEvent e) {
            System.out.println("mouse released rubberBandOrDeselect, deselecting and going back to ready");
            imodel.clearSelection();
            currentState = ready;
        }
    };

    // THIS STATE SHOULD ONLY SELECT WHAT IS IN THE RUBBER BAND BOX
    // AND THEN CTRL CLICK DRAG WILL BOTH SELECT AND UNSELECT, SO I NEED TO MAKE ANOTHER STATE
    ControllerState rubberBandSelectOnly = new ControllerState() {

        @Override
        void handleDragged(MouseEvent e) {
            dX = e.getX() - prevX;
            dY = e.getY() - prevY;
            model.resizeRubberband(dX, dY);
        }

        @Override
        void handleReleased(MouseEvent e) {
            // get list of lines within the rectangle
            List<Groupable> lines = model.rubberBandLineSelect();
            for (Groupable element: lines) {
                if (!imodel.getSelection().contains(element)) {
                    imodel.addToSelection(element);
                }
            }
            model.clearRubberband();
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
            CreateCommand cmd = new CreateCommand(model, line);
            // clearing the redo stack first so that I don't kill my grandmother (joke made by Carl Gutwin in during a lecture iirc)
            imodel.clearRedoStack();
            imodel.addToUndoStack(cmd);
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
