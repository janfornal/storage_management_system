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

import java.io.IOException;

public class GUIPresenter extends Application {
    public static DatabaseManager databaseManager;
    public static String login;
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
        switch(val) {
            case LOGIN:
                loader.setLocation(GUIPresenter.class.getResource("/loginWindow.fxml"));
                break;
            case REGISTER:
                loader.setLocation(GUIPresenter.class.getResource("/registerWindow.fxml"));
                break;
            default:
        }
        try {
            Parent root = loader.load();
            switch(val) {
                case LOGIN:
                    LoginWindow loginWindow = loader.getController();
                    break;
                case REGISTER:
                    RegisterWindow registerWindow = loader.getController();
                    break;
                default:
            }
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
            ProductsTableMenu productsTableMenu = loader.getController();
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
            switch(val) {
                case DELIVERY:
                    loader.setLocation(GUIPresenter.class.getResource("/addDeliveryWindow.fxml"));
                    break;
                case SALE:
                    loader.setLocation(GUIPresenter.class.getResource("/addSaleWindow.fxml"));
                    break;
                case RETURN:
                    loader.setLocation(GUIPresenter.class.getResource("/addReturnWindow.fxml"));
                    break;
                default:
            }
            Parent root = loader.load();
            switch(val) {
                case DELIVERY:
                    AddDeliveryWindow addDeliveryWindow = loader.getController();
                    break;
                case SALE:
                    AddSaleWindow addSaleWindow = loader.getController();
                    break;
                case RETURN:
//                    AddReturnWindow addReturnWindow = loader.getController();
                    break;
                default:
            }
            Scene scene1 = new Scene(root);
            functionalityStage = new Stage();
            functionalityStage.setScene(scene1);
            functionalityStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
