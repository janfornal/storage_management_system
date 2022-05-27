package StorageManagementSystem;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class AddDeliveryWindow {

    @FXML
    private AddDeliveryMenu addDeliveryMenuController;

    @FXML
    private FlattenedProductAnchor flattenedProductAnchorController;

    @FXML
    private Button addDeliveryButton;

    @FXML
    private Button addProductButton;

    @FXML
    private VBox centralVBox;

    @FXML
    void initialize() {

    }

    public void addProductToList(ActionEvent actionEvent) {
        AddDeliveryMenu adm = addDeliveryMenuController;
        if(adm.amountFieldItem() == null || adm.productBoxItem() == null || adm.categoryBoxItem() == null) {
            Platform.runLater(
                    () -> new Alert(Alert.AlertType.ERROR, "Please provide all of data").showAndWait()
            );
            return;
        }
        ProductRepr modifiedRepr = new ProductRepr(adm.productBoxItem().id(), adm.productBoxItem().brand(), adm.productBoxItem().name(), adm.productBoxItem().category(), adm.amountFieldItem(), adm.productBoxItem().netPrice()); // nie chce mi sie robić nowych klas xd
        flattenedProductAnchorController.add(modifiedRepr);
    }
}
