package org.adam.gui.asn4;

import javafx.scene.layout.StackPane;

public class MainUI extends StackPane {

    public MainUI() {
        // initialize model, view, controller
        LineModel model = new LineModel();
        iModel iModel = new iModel();
        DView view = new DView();
        AppController controller = new AppController();

        // set model controller view in their respective places
        controller.setModel(model);
        controller.setIModel(iModel);
        view.setModel(model);
        view.setIModel(iModel);
        view.setupEvents(controller);
        model.addSubscriber(view);
        iModel.addSubscriber(view);
        this.getChildren().add(view);
    }
}
