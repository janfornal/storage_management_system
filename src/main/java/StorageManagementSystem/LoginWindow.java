package StorageManagementSystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
        GUIPresenter.enterMenu(usernameField.getText(), Integer.parseInt(passwordField.getText()));
    }

    public void changeRegisterWindow(ActionEvent actionEvent) {
        GUIPresenter.showEnterStage(EnterValues.REGISTER);
    }
}