package StorageManagementSystem.returnView;

import StorageManagementSystem.fxmlAssistants.FunctionalityWindow;
import StorageManagementSystem.records.ReturnProductRepr;
import javafx.fxml.FXML;

public class ReturnWindow implements FunctionalityWindow {

    public int selectedIdSale;

    @FXML
    private ReturnMenu returnMenuController;

    @FXML
    private ReturnProductAnchor returnProductAnchorController;

    public void actualizeList(int s) {
        returnProductAnchorController.actualizeList(s);
    }

    public ReturnProductRepr selectedItem() {
        return returnProductAnchorController.selectedItem();
    }
}

