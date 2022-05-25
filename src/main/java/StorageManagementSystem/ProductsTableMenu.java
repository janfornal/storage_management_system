package StorageManagementSystem;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ProductsTableMenu {

    private GUIPresenter presenter;

    @FXML
    private LeftAnchorPaneMenu leftAnchorPaneMenuController;

    @FXML
    private TableColumn<ProductRepr, Double> amountColumn;

    @FXML
    private TableColumn<ProductRepr, String> brandColumn;

    @FXML
    private TableColumn<ProductRepr, String> categoryColumn;

    @FXML
    private TableColumn<ProductRepr, Integer> idColumn;

    @FXML
    private TableColumn<ProductRepr, String> nameColumn;

    @FXML
    private AnchorPane optionsAnchorPane;

    @FXML
    private TableColumn<ProductRepr, Double> priceColumn;

    @FXML
    private TableView<ProductRepr> productTableView;

    @FXML
    private VBox rightVBox;

    @FXML
    private AnchorPane tableAnchorPane;

    @FXML
    void initialize() {
        amountColumn.setCellValueFactory(
                g -> new ReadOnlyObjectWrapper<Double>(g.getValue().amount())
        );

        brandColumn.setCellValueFactory(
                g -> new ReadOnlyObjectWrapper<String>(g.getValue().brand())
        );

        categoryColumn.setCellValueFactory(
                g -> new ReadOnlyObjectWrapper<String>(g.getValue().category())
        );

        idColumn.setCellValueFactory(
                g -> new ReadOnlyObjectWrapper<Integer>(g.getValue().id())
        );

        nameColumn.setCellValueFactory(
                g -> new ReadOnlyObjectWrapper<String>(g.getValue().name())
        );

        priceColumn.setCellValueFactory(
                g -> new ReadOnlyObjectWrapper<Double>(g.getValue().netPrice())
        );

        productTableView.setItems(FXCollections.observableArrayList(GUIPresenter.databaseManager.getTableOfProducts()));
    }

    void setGUIPresenter(GUIPresenter guiPresenter) {
        presenter = guiPresenter;
    }

}
