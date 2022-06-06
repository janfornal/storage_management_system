package StorageManagementSystem.customers;

import StorageManagementSystem.GUIPresenter;
import StorageManagementSystem.fxmlAssistants.FunctionalityWindow;
import StorageManagementSystem.records.PersonRecord;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

public class Customers implements FunctionalityWindow {
    @FXML
    private TableColumn<PersonRecord, Integer> idColumn;

    @FXML
    private TableView<PersonRecord> customerTableView;

    @FXML
    private TableColumn<PersonRecord, String> loginColumn;

    @FXML
    private TableColumn<PersonRecord, String> nameColumn;

    @FXML
    private TableColumn<PersonRecord, String> surnameColumn;

    @FXML
    void initialize(){
        idColumn.setCellValueFactory(
                g -> new ReadOnlyObjectWrapper<>(g.getValue().id())
        );

        loginColumn.setCellValueFactory(
                g -> new ReadOnlyObjectWrapper<>(g.getValue().login())
        );

        nameColumn.setCellValueFactory(
                g -> new ReadOnlyObjectWrapper<>(g.getValue().name())
        );

        surnameColumn.setCellValueFactory(
                g -> new ReadOnlyObjectWrapper<>(g.getValue().surname())
        );

        actualizeList();

        customerTableView.setRowFactory(tv -> {
            TableRow<PersonRecord> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ArrayList<String> returnedArray = GUIPresenter.databaseManager.getAllDataCustomers(row.getItem().id());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Client Information");
                    alert.setTitle(row.getItem().login());
                    alert.setContentText(returnedArray.stream().reduce("", (first, second) -> first + " \n" + second));

                    alert.showAndWait();
                }
            });
            return row;
        });
    }

    public void actualizeList(){
        customerTableView.setItems(FXCollections.observableArrayList(GUIPresenter.databaseManager.getCustomers()));
    }
}
