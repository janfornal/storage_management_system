package StorageManagementSystem.menuView;

import StorageManagementSystem.fxmlAssistants.GeneralWindow;
import StorageManagementSystem.deliveryView.LeftAnchorPaneMenu;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ProductsTableMenu implements GeneralWindow {

    @FXML
    private LeftAnchorPaneMenu leftAnchorPaneMenuController;

    @FXML
    private AnchorPane optionsAnchorPane;

    @FXML
    private ProductAnchor productAnchorController;

    @FXML
    private VBox rightVBox;

    @FXML
    void initialize() {
    }

    public void actualizeList() {
        productAnchorController.actualizeList();
    }
}
