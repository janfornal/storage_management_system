package StorageManagementSystem;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class LeftAnchorPaneMenu {

    private GUIPresenter presenter;

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

    void setGUIPresenter(GUIPresenter guiPresenter) {
        presenter = guiPresenter;
    }

}

