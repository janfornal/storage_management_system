package StorageManagementSystem;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class GUIPresenter extends Application {
    public static DatabaseManager databaseManager;
    public static String login;
    private static Stage currentStage;

    @Override
    public void start(Stage stage) throws Exception {
        currentStage = stage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GUIPresenter.class.getResource("/loginWindow.fxml"));
        Parent root = loader.load();
        LoginWindow loginWindow = loader.getController();
        loginWindow.setGUIPresenter(this);
        Scene scene1 = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene1);
        stage.show();
    }

    public void enterMenu(String loginUser, Integer password) {
        if(!databaseManager.checkLoginExist(loginUser)) {
            Platform.runLater(
                    () -> new Alert(Alert.AlertType.ERROR, "Entered login does not exist").showAndWait()
            );
            return;
        }
        if(!databaseManager.checkPasswordCorrect(loginUser, password)) {
            Platform.runLater(
                    () -> new Alert(Alert.AlertType.ERROR, "Wrong password provided").showAndWait()
            );
            return;
        }
        try {
            login = loginUser;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUIPresenter.class.getResource("/productsTableMenu.fxml"));
            Parent root = loader.load();
            ProductsTableMenu productsTableMenu = loader.getController();
            productsTableMenu.setGUIPresenter(this);
            Scene scene1 = new Scene(root);
            currentStage.setScene(scene1);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
