package StorageManagementSystem;

import javafx.fxml.FXMLLoader;

public interface FXMLManager {

    public void setLocation(FXMLLoader loader);
    public default GeneralWindow getController(FXMLLoader loader) {
        return loader.getController();
    }
}
