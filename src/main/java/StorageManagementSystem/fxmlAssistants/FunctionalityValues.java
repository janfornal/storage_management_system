package StorageManagementSystem.fxmlAssistants;


import StorageManagementSystem.GUIPresenter;
import StorageManagementSystem.fxmlAssistants.FXMLManager;
import javafx.fxml.FXMLLoader;

public enum FunctionalityValues implements FXMLManager {
    COMPLAINT() {
        @Override
        public void setLocation(FXMLLoader loader) {
            loader.setLocation(GUIPresenter.class.getResource("/complaints.fxml"));
        }
    },
    DELIVERY() {
        @Override
        public void setLocation(FXMLLoader loader) {
            loader.setLocation(GUIPresenter.class.getResource("/addDeliveryWindow.fxml"));
        }
    },
    SALE() {
        @Override
        public void setLocation(FXMLLoader loader) {
            loader.setLocation(GUIPresenter.class.getResource("/addSaleWindow.fxml"));
        }
    },
    RETURN() {
        @Override
        public void setLocation(FXMLLoader loader) {
            loader.setLocation(GUIPresenter.class.getResource("/returnWindow.fxml"));
        }
    },
    CUSTOMER(){
        @Override
        public void setLocation(FXMLLoader loader){
            loader.setLocation(GUIPresenter.class.getResource("/customerMenu.fxml"));
        }
    },
    EMPLOYEE(){
        @Override
        public void setLocation(FXMLLoader loader){
            loader.setLocation((GUIPresenter.class.getResource("/employeesMenu.fxml")));
        }
    }
}
