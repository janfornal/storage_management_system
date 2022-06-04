package StorageManagementSystem;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;

public class FlattenedProductAnchor2 {

    private ArrayList<UsedProductRepr> observedArray;

    @FXML
    private TableColumn<UsedProductRepr, Double> amountColumn;

    @FXML
    private TableColumn<UsedProductRepr, String> brandColumn;

    @FXML
    private TableColumn<UsedProductRepr, String> categoryColumn;

    @FXML
    private TableColumn<UsedProductRepr, Integer> idColumn;

    @FXML
    private TableColumn<UsedProductRepr, String> nameColumn;

    @FXML
    private TableColumn<UsedProductRepr, Double> priceColumn;

    @FXML
    private TableView<UsedProductRepr> productTableView;

    @FXML
    private TableColumn<UsedProductRepr, Integer> usedColumn;

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

        usedColumn.setCellValueFactory(
                g -> new ReadOnlyObjectWrapper<Integer>(g.getValue().used())
        );

        observedArray = new ArrayList<UsedProductRepr>();
    }

    public void add(UsedProductRepr productRepr) {
        observedArray.add(productRepr);
        productTableView.setItems(FXCollections.observableArrayList(observedArray));
    }

    public ArrayList<UsedProductRepr> getObservedArray() {
        return observedArray;
    }
}
