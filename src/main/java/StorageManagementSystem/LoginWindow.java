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

    private GUIPresenter presenter;

    @FXML
    private Label LoginLabel;

    @FXML
    private AnchorPane leftAnchor;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private AnchorPane rightAnchor;

    @FXML
    private TextField usernameField;

    @FXML
    public void initialize() {
    }

    @FXML
    void enterApplication(ActionEvent event) {
        presenter.enterMenu(usernameField.getText(), Integer.parseInt(passwordField.getText()));
    }

    public void setGUIPresenter(GUIPresenter guiPresenter) {
        presenter = guiPresenter;
    }

}