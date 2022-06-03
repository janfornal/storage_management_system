package StorageManagementSystem;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class AddSaleWindow {

    @FXML
    private AddSaleMenu addSaleMenuController;

    @FXML
    private FlattenedProductAnchor flattenedProductAnchorController;

    @FXML
    private Button addProductButton;

    @FXML
    private Button addSaleButton;

    public void addProductToList(ActionEvent actionEvent) {
        AddSaleMenu asm = addSaleMenuController;
        if(asm.amountFieldItem() == null || asm.productBoxItem() == null || asm.categoryBoxItem() == null) {
            Platform.runLater(
                    () -> new Alert(Alert.AlertType.ERROR, "Please provide all of data").showAndWait()
            );
            return;
        }
        ProductRepr modifiedRepr = new ProductRepr(asm.productBoxItem().id(), asm.productBoxItem().brand(), asm.productBoxItem().name(), asm.productBoxItem().category(), asm.amountFieldItem(), asm.productBoxItem().netPrice());
        flattenedProductAnchorController.add(modifiedRepr);
    }

    public void addSaleTransaction(ActionEvent actionEvent) {
    }
}

