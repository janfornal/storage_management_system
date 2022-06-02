package StorageManagementSystem;

import javafx.fxml.FXMLLoader;

public interface FXMLManager {

    public void setLocation(FXMLLoader loader);
    public default void getController(FXMLLoader loader) {
        loader.getController();
    }
}
