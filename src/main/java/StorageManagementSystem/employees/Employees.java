package StorageManagementSystem.employees;

import StorageManagementSystem.GUIPresenter;
import StorageManagementSystem.fxmlAssistants.FunctionalityWindow;
import StorageManagementSystem.records.PersonRecord;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.*;

public class Employees implements FunctionalityWindow {
    @FXML
    public TableView<PersonRecord> employeesTableView;
    @FXML
    public TableColumn<PersonRecord, String> loginColumn;
    @FXML
    public TableColumn<PersonRecord, String> nameColumn;
    @FXML
    public TableColumn<PersonRecord, String> surnameColumn;
    @FXML
    public TableColumn<PersonRecord, Integer> idColumn;

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
    }

    public void actualizeList(){
        employeesTableView.setItems(FXCollections.observableArrayList(GUIPresenter.databaseManager.getEmployees()));
    }
}
