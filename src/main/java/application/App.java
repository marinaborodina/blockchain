package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {

    private static Stage mainStage;
    private static Scene blockchainScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        this.mainStage = stage;
        this.mainStage.setResizable(false);

        final KeyFrame kf1 = new KeyFrame(Duration.seconds(0), e -> App.switchScene("/view/preload.fxml", 800, 600, "Welcome!"));
        final KeyFrame kf2 = new KeyFrame(Duration.seconds(7), e -> switchScene("/view/blockchain.fxml", 800, 600, "Blockchain"));
        final Timeline timeline = new Timeline(kf1, kf2);
        Platform.runLater(timeline::play);
    }

    public static Scene getBlockchainScene() {
        return blockchainScene;
    }

    public static void switchScene(String resource, int v, int v1, String stageName) {
        try {
            Parent root = FXMLLoader.load(App.class.getResource(resource));
            Scene scene = new Scene(root, v, v1);

            if (stageName.contains("Blockchain")) {
                blockchainScene = scene;
            }

            mainStage.setScene(scene);
            mainStage.setTitle(stageName);
            mainStage.show();

        } catch (Exception exception) {
            exception.printStackTrace();
            App.showErrorDialog("Could not load new scene!");
        }
    }

    public static void showStage(Stage stage, Scene scene, String stageTitle) {
        try {
            stage.setScene(scene);
            stage.setTitle(stageTitle);
            stage.show();

        } catch (Exception exception) {
            exception.printStackTrace();
            App.showErrorDialog("Could not load stage!");
        }
    }

    public static Stage getMainStage() {

        return mainStage;
    }

    public static void showInfoDialog(String headertext, String contextText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(headertext);
        alert.setContentText(contextText);
        alert.show();
    }

    public static void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("An exception occurred: " + message);
        alert.showAndWait();
    }
}



