package StorageManagementSystem;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class LoginWindow {

    @FXML
    private Label LoginLabel;

    @FXML
    private AnchorPane leftAnchor;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private AnchorPane rightAnchor;

    @FXML
    private TextField usernameField;

    @FXML
    public void initialize() {
    }

    public void enterApplication(ActionEvent event) {
        String loginUser = usernameField.getText();
        String password = passwordField.getText();
        if(loginUser == null || !GUIPresenter.databaseManager.checkLoginExist(loginUser)) {
            Platform.runLater(
                    () -> new Alert(Alert.AlertType.ERROR, "Entered login does not exist").showAndWait()
            );
            return;
        }
        if(password == null || !GUIPresenter.databaseManager.checkPasswordCorrect(loginUser, password)) {
            Platform.runLater(
                    () -> new Alert(Alert.AlertType.ERROR, "Wrong password provided").showAndWait()
            );
            return;
        }
        GUIPresenter.enterMenu(loginUser);
    }

    public void changeRegisterWindow(ActionEvent actionEvent) {
        GUIPresenter.showEnterStage(EnterValues.REGISTER);
    }
}