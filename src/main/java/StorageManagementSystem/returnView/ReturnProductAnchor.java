package StorageManagementSystem.returnView;

import StorageManagementSystem.GUIPresenter;
import StorageManagementSystem.records.ProductRepr;
import StorageManagementSystem.records.ReturnProductRepr;
import StorageManagementSystem.records.UsedProductRepr;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

import java.util.ArrayList;

public class ReturnProductAnchor {

    private ArrayList<ReturnProductRepr> observedArray = new ArrayList<>();

    @FXML
    private TableColumn<ReturnProductRepr, Double> amountColumn;

    @FXML
    private TableColumn<ReturnProductRepr, Integer> idColumn;

    @FXML
    private TableColumn<ReturnProductRepr, String> nameColumn;

    @FXML
    private TableColumn<ReturnProductRepr, Integer> usedColumn;

    @FXML
    private TableView<ReturnProductRepr> productTableView;

    @FXML
    void initialize() {
        amountColumn.setCellValueFactory(
                g -> new ReadOnlyObjectWrapper<Double>(g.getValue().amount())
        );

        idColumn.setCellValueFactory(
                g -> new ReadOnlyObjectWrapper<Integer>(g.getValue().id())
        );

        nameColumn.setCellValueFactory(
                g -> new ReadOnlyObjectWrapper<String>(g.getValue().name())
        );

        usedColumn.setCellValueFactory(
                g -> new ReadOnlyObjectWrapper<Integer>(g.getValue().used())
        );

        observedArray = new ArrayList<>();


        productTableView.setRowFactory(tv -> {
            TableRow<ReturnProductRepr> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ArrayList<String> returnedArray = GUIPresenter.databaseManager.getProductPropertiesFromId(row.getItem().id());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product properties");
                    alert.setTitle(row.getItem().name());
                    alert.setContentText(returnedArray.stream().reduce("", (first, second) -> first + " \n" + second));

                    alert.showAndWait();
                }
            });
            return row;
        });
    }
}
