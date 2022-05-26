package StorageManagementSystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    @FXML
    void enterApplication(ActionEvent event) {

    }

}
