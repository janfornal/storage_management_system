package StorageManagementSystem;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public void addDeliveryTransaction(ActionEvent actionEvent) {
        List<Integer> idList = flattenedProductAnchorController.getObservedArray().stream().map(t -> t.id()).toList();
        List<Double> amountList = flattenedProductAnchorController.getObservedArray().stream().map(t -> t.amount()).toList();
        int id_delivery = GUIPresenter.databaseManager.addNewDelivery(1, new java.sql.Date(System.currentTimeMillis()));  // TODO - id_supplier from checkbox
        for(int i=0; i<idList.size(); i++) {
            GUIPresenter.databaseManager.addNewDeliveryProduct(id_delivery, idList.get(i), amountList.get(i));
        }
        GUIPresenter.menuController.actualizeList();
        GUIPresenter.closeFunctionalityStage();
    }
}
