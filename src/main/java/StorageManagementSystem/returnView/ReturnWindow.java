package StorageManagementSystem.returnView;

import StorageManagementSystem.fxmlAssistants.FunctionalityWindow;
import StorageManagementSystem.records.SaleRepr;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.awt.event.ActionEvent;

public class ReturnWindow implements FunctionalityWindow {

    @FXML
    private ReturnMenu returnMenuController;

    @FXML
    private ReturnProductAnchor returnProductAnchorController;

    public void addComplaintToList(ActionEvent actionEvent){
        ReturnMenu rM = returnMenuController;
        if(rM.descriptionTextArea() == null || rM.saleComboBox() == null || rM.amountTextField() == null){
            Platform.runLater(
                    () -> new Alert(Alert.AlertType.ERROR, "Please provide all of data").showAndWait()
            );
            return;
        }
    }
}

