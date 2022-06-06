package StorageManagementSystem.complaintsView;

import StorageManagementSystem.GUIPresenter;
import StorageManagementSystem.fxmlAssistants.FunctionalityWindow;
import StorageManagementSystem.records.ComplaintRepr;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Timestamp;
import java.util.Optional;

public class Complaints implements FunctionalityWindow {

    @FXML
    private TableColumn<ComplaintRepr, Double> amountColumn;

    @FXML
    private TableView<ComplaintRepr> complaintTableView;

    @FXML
    private TableColumn<ComplaintRepr, Timestamp> dateColumn;

    @FXML
    private TableColumn<ComplaintRepr, Integer> idComplaintColumn;

    @FXML
    private TableColumn<ComplaintRepr, Integer> idProductColumn;

    @FXML
    private TableColumn<ComplaintRepr, String> nameColumn;

    @FXML
    void initialize() {
        amountColumn.setCellValueFactory(
                g -> new ReadOnlyObjectWrapper<>(g.getValue().quantity())
        );

        dateColumn.setCellValueFactory(
                g -> new ReadOnlyObjectWrapper<>(g.getValue().complaint_date())
        );

        idComplaintColumn.setCellValueFactory(
                g -> new ReadOnlyObjectWrapper<>(g.getValue().id_complaint())
        );

        idProductColumn.setCellValueFactory(
                g -> new ReadOnlyObjectWrapper<>(g.getValue().id_product())
        );

        nameColumn.setCellValueFactory(
                g -> new ReadOnlyObjectWrapper<>(g.getValue().name())
        );

        actualizeList();

        complaintTableView.setRowFactory(tv -> {
            TableRow<ComplaintRepr> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Complaint with id: " + row.getItem().id_complaint());
                    alert.setHeaderText("Do you want to accept this complaint?");
                    alert.setContentText("Description: " + row.getItem().description());

                    ButtonType buttonTypeAccept = new ButtonType("Accept");
                    ButtonType buttonTypeDecline = new ButtonType("Decline");
                    ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                    alert.getButtonTypes().setAll(buttonTypeAccept, buttonTypeDecline, buttonTypeCancel);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == buttonTypeAccept) {
                        GUIPresenter.databaseManager.updateComplaint(true, GUIPresenter.idEmployee, row.getItem().id_complaint());  //TODO - id employee
                        complaintTableView.getItems().remove(row.getItem());
                    } else if (result.get() == buttonTypeDecline) {
                        GUIPresenter.databaseManager.updateComplaint(false, GUIPresenter.idEmployee, row.getItem().id_complaint());
                        complaintTableView.getItems().remove(row.getItem());
                    }
                }
            });
            return row;
        });
    }

    public void actualizeList() {
        complaintTableView.setItems(FXCollections.observableArrayList(GUIPresenter.databaseManager.getComplaints()));
    }
}

