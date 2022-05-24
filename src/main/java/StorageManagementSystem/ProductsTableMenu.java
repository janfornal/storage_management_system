package StorageManagementSystem;

import javafx.fxml.FXML;

public class ProductsTableMenu {

    private GUIPresenter presenter;

    @FXML
    private LeftAnchorPaneMenu leftAnchorPaneMenuController;

    @FXML
    void initialize() {

    }

    void setGUIPresenter(GUIPresenter guiPresenter) {
        presenter = guiPresenter;
    }

}
