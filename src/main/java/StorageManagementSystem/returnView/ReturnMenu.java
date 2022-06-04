package StorageManagementSystem.returnView;

import StorageManagementSystem.GUIPresenter;
import StorageManagementSystem.records.SaleRepr;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

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
    private Button productsProblemsButton;

    @FXML
    private ComboBox<SaleRepr> saleComboBox;

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
    }

}

