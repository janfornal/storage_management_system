package StorageManagementSystem.deliveryView;

import StorageManagementSystem.records.CategoryRecord;
import StorageManagementSystem.GUIPresenter;
import StorageManagementSystem.records.ProductRepr;
import StorageManagementSystem.records.SupplierRecord;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

public class AddDeliveryMenu {

    @FXML
    private ComboBox<CategoryRecord> categoryComboBox;

    @FXML
    private ComboBox<ProductRepr> nameComboBox;

    @FXML
    private TextField selectAmountArea;

    @FXML
    private ComboBox<SupplierRecord> supplierComboBox;

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
        supplierComboBox.setItems(FXCollections.observableArrayList(GUIPresenter.databaseManager.getAllSuppliers()));
        Callback<ListView<SupplierRecord>, ListCell<SupplierRecord>> supplierCellFactory = new Callback<>() {
            @Override
            public ListCell<SupplierRecord> call(ListView<SupplierRecord> l) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(SupplierRecord item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.id() + ": " + item.name());
                        }
                    }
                };
            }
        };
        supplierComboBox.setCellFactory(supplierCellFactory);
        supplierComboBox.setVisibleRowCount(10);
        supplierComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(SupplierRecord item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setText(item.id() + ": " + item.name());
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

    public CategoryRecord categoryBoxItem() {
        return categoryComboBox.getValue();
    }

    public ProductRepr productBoxItem() {
        return nameComboBox.getValue();
    }

    public SupplierRecord supplierBoxItem() {
        return supplierComboBox.getValue();
    }

    public Double amountFieldItem() {    // TODO dodaj jakiś exception
        if(selectAmountArea.getText() == null) return null;
        return Double.parseDouble(selectAmountArea.getText());
    }
}
