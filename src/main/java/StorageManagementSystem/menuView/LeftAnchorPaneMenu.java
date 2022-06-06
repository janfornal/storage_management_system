package StorageManagementSystem.menuView;

import StorageManagementSystem.fxmlAssistants.FunctionalityValues;
import StorageManagementSystem.GUIPresenter;
import StorageManagementSystem.fxmlAssistants.FunctionalityWindow;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class LeftAnchorPaneMenu {

    @FXML
    private Button deliveryButton;

    @FXML
    private Label nameLabel;

    @FXML
    private Button returnButton;

    @FXML
    private Button saleButton;

    @FXML
    private Label surnameLabel;

    @FXML
    void initialize() {
        nameLabel.setText("Name: " + GUIPresenter.databaseManager.getFirstName(GUIPresenter.login));
        surnameLabel.setText("Surname: " + GUIPresenter.databaseManager.getSurname(GUIPresenter.login));
    }

    public void provideSaleWindow(javafx.event.ActionEvent actionEvent) {
        GUIPresenter.enterFunctionalityWindow(FunctionalityValues.SALE);
    }

    public void provideDeliveryWindow(javafx.event.ActionEvent actionEvent) {
        GUIPresenter.enterFunctionalityWindow(FunctionalityValues.DELIVERY);
    }

    public void provideReturnWindow(javafx.event.ActionEvent actionEvent) {
        GUIPresenter.enterFunctionalityWindow(FunctionalityValues.RETURN);
    }

    public void provideComplaintWindow(javafx.event.ActionEvent actionEvent) {
        GUIPresenter.enterFunctionalityWindow(FunctionalityValues.COMPLAINT);
    }

    public void provideCustomersWindow(javafx.event.ActionEvent actionEvent){
        GUIPresenter.enterFunctionalityWindow(FunctionalityValues.CUSTOMER);
    }

    public void provideEmployeesWindow(javafx.event.ActionEvent actionEvent){
        GUIPresenter.enterFunctionalityWindow(FunctionalityValues.EMPLOYEE);
    }
}

