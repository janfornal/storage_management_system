package StorageManagementSystem.returnView;

import StorageManagementSystem.GUIPresenter;
import StorageManagementSystem.deliveryView.AddDeliveryMenu;
import StorageManagementSystem.records.CategoryRecord;
import StorageManagementSystem.records.ProductRepr;
import StorageManagementSystem.records.SaleRepr;
import StorageManagementSystem.records.SupplierRecord;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.sql.Date;
import java.util.Calendar;

public class ReturnMenu {

    @FXML
    private Label chosenProductLabel;

    @FXML
    private Button clientsReturnButton;

    @FXML
    private Button complaintButton;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private ComboBox<SaleRepr> saleComboBox;

    @FXML
    private TextField selectAmountField;

    @FXML
    void initialize() {
        saleComboBox.setItems(FXCollections.observableArrayList(GUIPresenter.databaseManager.getAllSales()));
        Callback<ListView<SaleRepr>, ListCell<SaleRepr>> cellFactory = new Callback<>() {
            @Override
            public ListCell<SaleRepr> call(ListView<SaleRepr> l) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(SaleRepr item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText("" + item.id_sale() + ": " + item.data().toString());
                        }
                    }
                };
            }
        };
        saleComboBox.setCellFactory(cellFactory);
        saleComboBox.setVisibleRowCount(10);
        saleComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(SaleRepr item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setText("" + item.id_sale() + ": " + item.data());
                }
            }
        });
    }

    public void chosenSaleHandler(ActionEvent actionEvent) {
        if(GUIPresenter.functionalityController instanceof ReturnWindow windowController) {
            final Integer[] chosenIdSale = new Integer[1];
            saleComboBox.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(SaleRepr item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setGraphic(null);
                    } else {
                        setText(item.id_sale() + ": " + item.data());
                        chosenIdSale[0] = item.id_sale();
                    }
                }
            });
            windowController.actualizeList(chosenIdSale[0]);
        }
    }

    public void chosenReturnButton(ActionEvent actionEvent) {
        if(GUIPresenter.functionalityController instanceof ReturnWindow windowController) {
            if(descriptionTextArea.getText() == null || selectAmountField.getText() == null || windowController.selectedItem() == null) {
                Platform.runLater(
                        () -> new Alert(Alert.AlertType.ERROR, "Please provide all of data").showAndWait()
                );
            }
            else {
                Double quantity;
                try
                {
                    quantity = Double.parseDouble(selectAmountField.getText());
                }
                catch(NumberFormatException e)
                {
                    Platform.runLater(
                            () -> new Alert(Alert.AlertType.ERROR, "You provided wrong data in amount field").showAndWait()
                    );
                    return;
                }
                GUIPresenter.databaseManager.addNewReturn(saleComboBox.getValue().id_sale(), windowController.selectedItem().id(), quantity);
                GUIPresenter.standardCloseFunctionalityStage();
            }
        }
        else {
            throw new RuntimeException("application recognizes wrong functionality window");
        }
    }

    public void chosenComplaintButton(ActionEvent actionEvent) {
        if(GUIPresenter.functionalityController instanceof ReturnWindow windowController) {
            if(descriptionTextArea.getText() == null || selectAmountField.getText() == null || windowController.selectedItem() == null) {
                Platform.runLater(
                        () -> new Alert(Alert.AlertType.ERROR, "Please provide all of data").showAndWait()
                );
            }
            else {
                Double quantity;
                try
                {
                    quantity = Double.parseDouble(selectAmountField.getText());
                }
                catch(NumberFormatException e)
                {
                    Platform.runLater(
                            () -> new Alert(Alert.AlertType.ERROR, "You provided wrong data in amount field").showAndWait()
                    );
                    return;
                }
                GUIPresenter.databaseManager.addComplaint(windowController.selectedItem().id(), saleComboBox.getValue().id_sale() , quantity.intValue(), new Date(System.currentTimeMillis()),descriptionTextArea.getText());
                GUIPresenter.standardCloseFunctionalityStage();
            }
        }
        else {
            throw new RuntimeException("application recognizes wrong functionality window");
        }
    }
}

