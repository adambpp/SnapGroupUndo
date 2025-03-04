# **SnapGroupUndo**

## **Overview**
A JavaFX-based interactive drawing application demonstrating MVC architecture. You may encounter a few bugs while playing around with the program, mostly with undo/redo as my implementation isn't fully correct.
## **Features**
✅ Draw and manipulate lines interactively  
✅ Snap-to-grid for precise alignment  
✅ Multi-select with Ctrl-click or rubber-band selection  
✅ Group and ungroup objects  
✅ Undo/Redo with keyboard shortcuts  

## **Controls**

| Action               | Key / Mouse         |
|----------------------|--------------------|
| Create Line         | Shift + Drag       |
| Move Line           | Drag               |
| Select Line         | Click              |
| Multi-Select        | Ctrl + Click       |
| Rubber-band Selection | Drag without Shift |
| Delete              | Delete / Backspace |
| Rotate              | Left/Right Arrow   |
| Scale               | Up/Down Arrow      |
| Group               | G                  |
| Ungroup             | U                  |
| Undo                | Z                  |
| Redo                | Y                  |

## **Architecture**
Follows an **MVC (Model-View-Controller)** design:
- **Model**: Stores and manages the application state  
- **View**: Renders objects and handles UI drawing  
- **Controller**: Processes user input with a state machine

Note: This project was developed as part of my university coursework for the Implementation of Graphical User Interfaces class. It is shared for portfolio purposes and should not be used for academic submissions.
