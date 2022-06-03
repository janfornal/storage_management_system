package StorageManagementSystem;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.sql.SQLException;

public class AddSaleWindow implements FunctonalityWindow {

    public int idOfSale;

    @FXML
    private AddSaleMenu addSaleMenuController;

    @FXML
    private FlattenedProductAnchor flattenedProductAnchorController;

    @FXML
    private Button addProductButton;

    @FXML
    private Button addSaleButton;

    @FXML
    void initialize() {
        idOfSale = GUIPresenter.databaseManager.addNewSale(new java.sql.Date(System.currentTimeMillis()));

    }

    public void close() {
        String cleaner = "DELETE FROM products_sold WHERE id_sale = " + idOfSale
                + "; DELETE FROM sales WHERE id_sale = " + idOfSale;
        try {
            GUIPresenter.databaseManager.updateFromString(cleaner);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addProductToList(ActionEvent actionEvent) {
        AddSaleMenu asm = addSaleMenuController;
        if(asm.amountFieldItem() == null || asm.productBoxItem() == null || asm.categoryBoxItem() == null) {
            Platform.runLater(
                    () -> new Alert(Alert.AlertType.ERROR, "Please provide all of data").showAndWait()
            );
            return;
        }
        try {
            GUIPresenter.databaseManager.addNewSaleProduct(idOfSale, asm.productBoxItem().id(), asm.amountFieldItem());
            addSaleMenuController.setNetPriceLabel(GUIPresenter.databaseManager.getPriceOfProductFromSale(idOfSale));
            addSaleMenuController.setWholePriceLabel(GUIPresenter.databaseManager.getGrossOfProductFromSale(idOfSale));
            ProductRepr modifiedRepr = new ProductRepr(asm.productBoxItem().id(), asm.productBoxItem().brand(), asm.productBoxItem().name(), asm.productBoxItem().category(), asm.amountFieldItem(), asm.productBoxItem().netPrice());
            flattenedProductAnchorController.add(modifiedRepr);
        } catch (SQLException e) {
            Platform.runLater(
                    () -> new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait()
            );
            return;
        }
    }

    public void addSaleTransaction(ActionEvent actionEvent) {

    }
}

