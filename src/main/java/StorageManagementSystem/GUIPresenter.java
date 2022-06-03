package StorageManagementSystem;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class GUIPresenter extends Application {
    public static DatabaseManager databaseManager;
    public static String login;
    public static ProductsTableMenu menuController;
    public static FunctonalityWindow functionalityController;
    private static Stage enterStage;
    private static Stage currentStage;
    private static Stage functionalityStage;

    @Override
    public void start(Stage stage) throws Exception {
        enterStage = stage;
        showEnterStage(EnterValues.LOGIN);
    }

    public static void showEnterStage(EnterValues val) {
        FXMLLoader loader = new FXMLLoader();
        val.setLocation(loader);
        try {
            Parent root = loader.load();
            val.getController(loader);
            Scene scene1 = new Scene(root);
            enterStage.setResizable(false);
            enterStage.setScene(scene1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        enterStage.show();
    }

    public static void enterMenu(String loginUser) {
        login = loginUser;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUIPresenter.class.getResource("/productsTableMenu.fxml"));
            Parent root = loader.load();
            menuController = loader.getController();
            Scene scene1 = new Scene(root);
            enterStage.close();
            currentStage = new Stage();
            currentStage.setScene(scene1);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void enterFunctionalityWindow(FunctionalityValues val) {
        if(functionalityStage != null && functionalityStage.isShowing()) {
            Platform.runLater(
                    () -> new Alert(Alert.AlertType.ERROR, "Please end first last operation").showAndWait()
            );
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader();
            val.setLocation(loader);
            Parent root = loader.load();
            functionalityController = (FunctonalityWindow) val.getController(loader);
            Scene scene1 = new Scene(root);
            functionalityStage = new Stage();
            functionalityStage.setScene(scene1);
            functionalityStage.setOnCloseRequest(e -> closeFunctionalityStage());
            functionalityStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeFunctionalityStage() {
        if(functionalityController instanceof AddSaleWindow controllerCast) {
            controllerCast.close();
        }
        functionalityStage.close();
        functionalityStage = null;
        GUIPresenter.menuController.actualizeList();
    }
}
