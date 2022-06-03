package StorageManagementSystem;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

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
