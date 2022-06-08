package StorageManagementSystem.menuView;

import StorageManagementSystem.GUIPresenter;
import StorageManagementSystem.fxmlAssistants.FunctionalityValues;
import StorageManagementSystem.fxmlAssistants.GeneralWindow;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ProductsTableMenu implements GeneralWindow {

    @FXML
    public Button addProductButton;
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

    public void provideProductWindow(javafx.event.ActionEvent actionEvent){
        GUIPresenter.enterFunctionalityWindow(FunctionalityValues.PRODUCT);
    }

}
