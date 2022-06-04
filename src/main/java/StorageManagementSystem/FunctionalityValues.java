package StorageManagementSystem;


import javafx.fxml.FXMLLoader;

public enum FunctionalityValues implements FXMLManager {
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
    }
}
