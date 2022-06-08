package StorageManagementSystem.Products;

import StorageManagementSystem.GUIPresenter;
import StorageManagementSystem.fxmlAssistants.FunctionalityWindow;
import StorageManagementSystem.records.BrandRecord;
import StorageManagementSystem.records.CategoryRecord;
import StorageManagementSystem.records.ProductRepr;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class addProduct implements FunctionalityWindow {
    @FXML
    public TextField nameButton;
    @FXML
    public ComboBox<CategoryRecord> categoryComboBox;
    @FXML
    public ComboBox<BrandRecord> brandComboBox;
    @FXML
    public Button addButton;
    @FXML
    public AnchorPane addAnchorPane;

    @FXML
    void initialize(){
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
        brandComboBox.setItems(FXCollections.observableArrayList(GUIPresenter.databaseManager.getAllBrands()));
        Callback<ListView<BrandRecord>, ListCell<BrandRecord>> cellFactory2 = new Callback<>() {
            @Override
            public ListCell<BrandRecord> call(ListView<BrandRecord> l) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(BrandRecord item, boolean empty) {
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
        brandComboBox.setCellFactory(cellFactory2);
        brandComboBox.setVisibleRowCount(10);
        Callback<ListView<ProductRepr>, ListCell<ProductRepr>> productCellFactory2 = new Callback<>() {
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
    }

    public void chosenBrandHandler(ActionEvent actionEvent) {
        final String[] nameOfBrand= new String[1];
        brandComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(BrandRecord item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setText(item.id() + ": " + item.name());
                    nameOfBrand[0] = item.name();
                }
            }
        });
    }

    public void addProductToTable(ActionEvent actionEvent){
        if(brandComboBox.getValue() == null || categoryComboBox.getValue() == null || nameButton.getText() == null){
            Platform.runLater(
                    () -> new Alert(Alert.AlertType.ERROR, "Please provide all of data").showAndWait()
            );
            return;
        }
        try{
            GUIPresenter.databaseManager.addNewProduct(categoryComboBox.getValue().id(),nameButton.getText(),brandComboBox.getValue().id());
            GUIPresenter.standardCloseFunctionalityStage();
        } catch (IllegalStateException e) {
            Platform.runLater(
                    () -> new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait()
            );
            return;
        }
        catch (Exception e){
            GUIPresenter.standardCloseFunctionalityStage();
        }
    }
}
