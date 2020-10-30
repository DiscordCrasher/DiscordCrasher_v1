package me.dc.automaticdiscordcrasher;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class AppManager {

    private Stage frameStage;
    private Stage changelogStage;

    public AppManager(double version) {
        Platform.runLater(() -> {
            frameStage = new Stage();
            frameStage.setTitle("DiscordCrasher - V" + version);
            frameStage.setResizable(false);
            frameStage.getIcons().add(new javafx.scene.image.Image("icon.png"));
            frameStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.exit();
                    System.exit(0);
                }
            });
        });

        Platform.runLater(() -> {
            changelogStage = new Stage();
            changelogStage.setTitle("ChangeLog");
            changelogStage.setResizable(false);
            changelogStage.getIcons().add(new javafx.scene.image.Image("icon.png"));
            changelogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    changelogStage.close();
                }
            });
        });
    }


    public void resetFrame() {
        Platform.runLater(() -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/Frame.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            frameStage.setScene(new Scene(root));
        });
    }

    public void showFrame() {
        Platform.runLater(() -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/Frame.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            frameStage.setScene(new Scene(root));
            frameStage.show();
        });
    }

    public void showChangelog() {
        Platform.runLater(() -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/Changelog.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            changelogStage.setScene(new Scene(root));
            changelogStage.show();
        });
    }
}
