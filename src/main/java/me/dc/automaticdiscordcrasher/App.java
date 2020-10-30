package me.dc.automaticdiscordcrasher;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class App extends Application {


    private double version = 1.1;
    public static AppManager appManager;

    @Override
    public void start(Stage primaryStage) {


        String response = null;
        try {
            response = get("https://pastebin.com/raw/tbbevjFD");
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject(response);

        if (jsonObject.getDouble("version") != version) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Existe uma nova versão do DiscordCrasher!");
            alert.show();

            try {
                Desktop.getDesktop().browse(new URL("https://github.com/DiscordCrasher/DiscordCrasher_v1/releases/download/" + version + "/AutomaticDiscordCrasher.exe").toURI());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }


        appManager = new AppManager(version);
        appManager.showFrame();
        appManager.showChangelog();



    }

    private String get(String url) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}