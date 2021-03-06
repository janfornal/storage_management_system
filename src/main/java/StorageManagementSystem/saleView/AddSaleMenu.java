package StorageManagementSystem.saleView;

import StorageManagementSystem.records.CategoryRecord;
import StorageManagementSystem.GUIPresenter;
import StorageManagementSystem.records.ProductRepr;
import StorageManagementSystem.records.ProductWithProblemRepr;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

public class AddSaleMenu {

    @FXML
    private ComboBox<CategoryRecord> categoryComboBox;

    @FXML
    private ComboBox<ProductWithProblemRepr> idReturnComboBox;

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
        Callback<ListView<ProductWithProblemRepr>, ListCell<ProductWithProblemRepr>> problemCellFactory = new Callback<>() {
            @Override
            public ListCell<ProductWithProblemRepr> call(ListView<ProductWithProblemRepr> l) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(ProductWithProblemRepr item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.id_product() + ": " + item.description() + ", " + item.quantity());
                        }
                    }
                };
            }
        };
        idReturnComboBox.setCellFactory(problemCellFactory);
        idReturnComboBox.setVisibleRowCount(10);
        idReturnComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(ProductWithProblemRepr item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setText(item.id_product() + ": " + item.description() + ", " + item.quantity());
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

    public void chosenProductHandler(ActionEvent actionEvent) {
        final int[] selectedId = new int[1];
        nameComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(ProductRepr item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setText(item.id() + ": " + item.brand() + " " + item.name());
                    selectedId[0] = item.id();
                }
            }
        });
        idReturnComboBox.setItems(FXCollections.observableArrayList(GUIPresenter.databaseManager.getProductsWithProblems(selectedId[0])));
    }

    public CategoryRecord categoryBoxItem() {
        return categoryComboBox.getValue();
    }

    public ProductRepr productBoxItem() {
        return nameComboBox.getValue();
    }

    public ProductWithProblemRepr productProblemBoxItem() {
        return idReturnComboBox.getValue();
    }

    public Double amountFieldItem() {    // TODO dodaj jaki?? exception
        if(selectAmountArea.getText() == null) return null;
        return Double.parseDouble(selectAmountArea.getText());
    }

    public void setNetPriceLabel(Double price) {
        this.netPriceLabel.setText("Net price: " + price);
    }

    public void setWholePriceLabel(Double price) {
        this.wholePriceLabel.setText("Whole price: " + price);
    }
}
