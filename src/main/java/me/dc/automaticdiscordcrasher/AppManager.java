package me.dc.automaticdiscordcrasher;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;

public class AppManager {

    private Stage frameStage;
    private Stage changelogStage;
    private Stage loginStage;
    private String token;
    private String email;
    private String password;
    private ArrayList<String> lastMessagesId;
    private String lastChannelId;

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
            loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setResizable(false);
            loginStage.getIcons().add(new javafx.scene.image.Image("icon.png"));
            loginStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
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

    public void showLogin() {
        Platform.runLater(() -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            loginStage.setScene(new Scene(root));
            loginStage.show();
        });
    }

    public void closeLogin() {
        Platform.runLater(() -> {
            loginStage.close();
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getLastMessagesId() {
        return lastMessagesId;
    }

    public void setLastMessagesId(ArrayList<String> lastMessagesId) {
        this.lastMessagesId = lastMessagesId;
    }

    public String getLastChannelId() {
        return lastChannelId;
    }

    public void setLastChannelId(String lastChannelId) {
        this.lastChannelId = lastChannelId;
    }
}
