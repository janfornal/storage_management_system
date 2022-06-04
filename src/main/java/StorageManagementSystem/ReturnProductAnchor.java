package StorageManagementSystem;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
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
    }
}
