package StorageManagementSystem;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class AddSaleWindow {

    private ExecuteOrderList executeOrderList = new ExecuteOrderList();

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
        executeOrderList.createTransaction = "INSERT INTO sales (sales_date) VALUES (now()) ";
        executeOrderList.orderList += "INSERT INTO products_sold (id_sale, id_product, quantity) VALUES (special_id, " + asm.productBoxItem().id() + ", " + asm.amountFieldItem() + "); ";
        executeOrderList.testTransaction();
        addSaleMenuController.setNetPriceLabel(executeOrderList.netPrice);
        addSaleMenuController.setWholePriceLabel(executeOrderList.grossPrice);
        ProductRepr modifiedRepr = new ProductRepr(asm.productBoxItem().id(), asm.productBoxItem().brand(), asm.productBoxItem().name(), asm.productBoxItem().category(), asm.amountFieldItem(), asm.productBoxItem().netPrice());
        flattenedProductAnchorController.add(modifiedRepr);
    }

    public void addSaleTransaction(ActionEvent actionEvent) {

    }
}

