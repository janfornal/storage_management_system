package StorageManagementSystem;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class RegisterWindow {

    @FXML
    private Label RegisterLabel;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private AnchorPane leftAnchor;

    @FXML
    private Button loginButton;

    @FXML
    private TextField passwordField;

    @FXML
    private Button registerButton;

    @FXML
    private AnchorPane rightAnchor;

    @FXML
    private PasswordField usernameField;

    @FXML
    void changeLoginWindow(ActionEvent event) {
        GUIPresenter.showEnterStage(EnterValues.LOGIN);
    }

    public void enterApplication(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String loginUser = usernameField.getText();
        String password = passwordField.getText();
        if(firstName == null || lastName == null || loginUser == null || password == null) {
            Platform.runLater(
                    () -> new Alert(Alert.AlertType.ERROR, "Please attach essential info").showAndWait()
            );
            return;
        }
        if(firstName.equals("") || lastName.equals("") || loginUser.equals("") || password.equals("")) {
            Platform.runLater(
                    () -> new Alert(Alert.AlertType.ERROR, "You entered no information").showAndWait()
            );
            return;
        }
        if(GUIPresenter.databaseManager.checkLoginExist(loginUser)) {
            Platform.runLater(
                    () -> new Alert(Alert.AlertType.ERROR, "Entered login is already taken").showAndWait()
            );
            return;
        }
        try {
            GUIPresenter.databaseManager.registerNewEmployee(loginUser, password, firstName, lastName);
        } catch (RuntimeException e) {
            Platform.runLater(
                    () -> new Alert(Alert.AlertType.ERROR, "Registration has failed!").showAndWait()
            );
            return;
        }
        GUIPresenter.enterMenu(loginUser);
    }

}
