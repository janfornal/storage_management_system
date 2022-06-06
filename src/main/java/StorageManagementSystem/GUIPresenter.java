package StorageManagementSystem;

import StorageManagementSystem.fxmlAssistants.EnterValues;
import StorageManagementSystem.fxmlAssistants.FunctionalityValues;
import StorageManagementSystem.fxmlAssistants.FunctionalityWindow;
import StorageManagementSystem.menuView.ProductsTableMenu;
import StorageManagementSystem.saleView.AddSaleWindow;
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
    public static Integer idEmployee;
    public static ProductsTableMenu menuController;
    public static FunctionalityWindow functionalityController;
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
        idEmployee = databaseManager.getIdEmployee(loginUser);
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
            functionalityController = (FunctionalityWindow) val.getController(loader);
            Scene scene1 = new Scene(root);
            functionalityStage = new Stage();
            functionalityStage.setScene(scene1);
            functionalityStage.setOnCloseRequest(e -> standardCloseFunctionalityStage());
            functionalityStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void standardCloseFunctionalityStage() {   //standard sie odpala, gdy chcemy zamknąć okienko za pomocą iksa
        if(functionalityController instanceof AddSaleWindow controllerCast) {
            controllerCast.close();
        }
        closeFunctionalityStage();
    }

    public static void closeFunctionalityStage() {    //zawsze przy zamknięciu okienka np. przy zatwierdzeniu transakcji
        functionalityStage.close();
        functionalityStage = null;
        functionalityController = null;
        GUIPresenter.menuController.actualizeList();
    }
}
