package StorageManagementSystem.fxmlAssistants;

import StorageManagementSystem.GUIPresenter;
import StorageManagementSystem.fxmlAssistants.FXMLManager;
import javafx.fxml.FXMLLoader;

public enum EnterValues implements FXMLManager {
    LOGIN() {
        @Override
        public void setLocation(FXMLLoader loader) {
            loader.setLocation(GUIPresenter.class.getResource("/loginWindow.fxml"));
        }
    },
    REGISTER() {
        @Override
        public void setLocation(FXMLLoader loader) {
            loader.setLocation(GUIPresenter.class.getResource("/registerWindow.fxml"));
        }
    };
}
