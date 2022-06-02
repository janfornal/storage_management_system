package StorageManagementSystem;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

public class AddSaleMenu {

    @FXML
    private ComboBox<CategoryRecord> categoryComboBox;

    @FXML
    private CheckBox chooseHistory;

    @FXML
    private ComboBox<?> idReturnComboBox;

    @FXML
    private ComboBox<ProductRepr> nameComboBox;

    @FXML
    private Label netPriceLabel;

    @FXML
    private TextField selectAmountArea;

    @FXML
    private Label wholePriceLabel;

    @FXML
    void initialize() {
        categoryComboBox.setItems(FXCollections.observableArrayList(GUIPresenter.databaseManager.getAllCategories()));
        Callback<ListView<CategoryRecord>, ListCell<CategoryRecord>> cellFactory = new Callback<>() {
            @Override
            public ListCell<CategoryRecord> call(ListView<CategoryRecord> l) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(CategoryRecord item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.name());
                        }
                    }
                };
            }
        };
        categoryComboBox.setCellFactory(cellFactory);
        categoryComboBox.setVisibleRowCount(10);
        Callback<ListView<ProductRepr>, ListCell<ProductRepr>> productCellFactory = new Callback<>() {
            @Override
            public ListCell<ProductRepr> call(ListView<ProductRepr> l) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(ProductRepr item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.brand() + " " + item.name());
                        }
                    }
                };
            }
        };
        nameComboBox.setCellFactory(productCellFactory);
        nameComboBox.setVisibleRowCount(10);
        nameComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(ProductRepr item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setText(item.id() + ": " + item.brand() + " " + item.name());
                }
            }
        });
    }


    public void chosenCategoryHandler(ActionEvent actionEvent) {
        final String[] nameOfCategory = new String[1];
        categoryComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(CategoryRecord item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setText(item.id() + ": " + item.name());
                    nameOfCategory[0] = item.name();
                }
            }
        });
        nameComboBox.setItems(FXCollections.observableArrayList(GUIPresenter.databaseManager.getProductsFromCategoryName(nameOfCategory[0])));
    }
}
