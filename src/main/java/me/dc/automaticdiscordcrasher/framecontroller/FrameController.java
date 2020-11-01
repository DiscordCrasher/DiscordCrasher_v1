package me.dc.automaticdiscordcrasher.framecontroller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.converter.IntegerStringConverter;
import me.dc.automaticdiscordcrasher.App;
import me.dc.automaticdiscordcrasher.utils.Flooder;
import okhttp3.*;
import org.json.JSONObject;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;


public class FrameController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        delaySpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));

        NumberFormat format = NumberFormat.getIntegerInstance();
        UnaryOperator<TextFormatter.Change> filter = c -> {
            if (c.isContentChange()) {
                ParsePosition parsePosition = new ParsePosition(0);
                format.parse(c.getControlNewText(), parsePosition);
                if (parsePosition.getIndex() == 0 ||
                        parsePosition.getIndex() < c.getControlNewText().length()) {
                    return null;
                }
            }
            return c;
        };
        TextFormatter<Integer> priceFormatter = new TextFormatter<Integer>(
                new IntegerStringConverter(), 0, filter);

        delaySpinner.getEditor().setTextFormatter(priceFormatter);

        emailLabel.setText("E-mail: " + App.appManager.getEmail());
        tokenLabel.setText("Token: " + App.appManager.getToken());


    }


    public static boolean cancel = false;

    @FXML
    private Label errorLabel;

    @FXML
    private Label messageCountLabel;

    @FXML
    private TextField idTextField;

    @FXML
    private Button startButton;

    @FXML
    private Button cancelButton;

    @FXML
    private CheckBox embededCheckBox;

    @FXML
    private Spinner<Integer> delaySpinner;

    @FXML
    private Label emailLabel;

    @FXML
    private Label tokenLabel;

    @FXML
    private Button anonymousButton;

    @FXML
    private Button desfazerTravaButton;


    @FXML
    void onActionStartButton(ActionEvent event) {

        if (idTextField.getText() == null || idTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "O campo de id não foi preenchido.");
            alert.show();
            return;
        }

        String token = App.appManager.getToken();


        startButton.setDisable(true);
        idTextField.setEditable(false);
        embededCheckBox.setDisable(true);
        delaySpinner.setDisable(true);
        desfazerTravaButton.setDisable(true);
        cancelButton.setDisable(false);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new Flooder(errorLabel, messageCountLabel, idTextField, startButton, cancelButton, embededCheckBox, delaySpinner, desfazerTravaButton).attack(token, idTextField.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @FXML
    void onActionAnonymousButton(ActionEvent event) {
        JSONObject userInfos = null;
        try {
            userInfos = new JSONObject(getUserInfos(App.appManager.getToken()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!userInfos.getString("username").equalsIgnoreCase("\u1CBC\u1CBC")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("password", App.appManager.getPassword());
            jsonObject.put("username", "\u1CBC\u1CBC");

            String response = null;

            try {
                try {
                    jsonObject.put("avatar", getClass().getResource("/anonymous.png").toURI().toString());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                response = changeUserInfos(App.appManager.getToken(), jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response.contains("errors")) {
                JSONObject errors = new JSONObject(response).getJSONObject("errors");

                if (errors.toString().contains("avatar")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Erro: " + ((JSONObject) errors.getJSONObject("avatar").getJSONArray("_errors").get(0)).getString("message"));
                    alert.show();
                } else if (errors.toString().contains("username")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Erro: " + ((JSONObject) errors.getJSONObject("username").getJSONArray("_errors").get(0)).getString("message"));
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Erro desconhecido.");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "O usuário entrou em modo anonymous.");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "O usuário já está no modo anonymous");
            alert.show();
        }
    }

    @FXML
    void onDesfazerTravaButton(ActionEvent event) {
        startButton.setDisable(true);
        desfazerTravaButton.setDisable(true);
        int count = 0;

        new Thread() {
            public void run() {
                deleteLastMessages(count);
            }
        }.start();
    }


    @FXML
    void onActionCancelButton(ActionEvent event) {
        cancel = true;
    }

    private void deleteLastMessages(int c) {
        String channel_id = App.appManager.getLastChannelId();

        int count = c;
        ArrayList<String> arrayList = new ArrayList<>(App.appManager.getLastMessagesId());

        for (String id : App.appManager.getLastMessagesId()) {

            Response response = null;
            String s = null;
            try {
                response = deleteMessage(App.appManager.getToken(), channel_id, id);
                s = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (s.isEmpty()) {
                count++;
                int i = count;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        errorLabel.setText("Status: Apagando...");
                        messageCountLabel.setText("Mensagens apagadas com sucesso: " + i);
                    }
                });
                arrayList.remove(id);
            } else if (response.code() == 429) {

                JSONObject j = new JSONObject(s);

                double rate_limit = j.getDouble("retry_after");

                DecimalFormat df = new DecimalFormat("#.##");
                DecimalFormat df2 = new DecimalFormat("#");

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        errorLabel.setText("Rate Limit. Aguarde " + df.format(rate_limit) + " segundos.");
                    }
                });
                try {
                    Thread.sleep(Integer.parseInt(String.valueOf(df2.format(rate_limit * 1000))));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                App.appManager.setLastMessagesId(arrayList);
                deleteLastMessages(count);
                return;
            } else {
                arrayList.remove(id);
            }
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "O último trava foi desfeito.");
                alert.show();
                errorLabel.setText("");
                messageCountLabel.setText("");
                startButton.setDisable(false);
            }
        });


    }

    OkHttpClient client = new OkHttpClient();
    MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private String changeUserInfos(String token, String json) throws IOException {

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url("https://discord.com/api/v8/users/@me")
                .patch(body)
                .addHeader("Authorization", token)
                .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) discord/0.0.12 Chrome/78.0.3904.130 Electron/7.3.2 Safari/537.36")
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private String getUserInfos(String token) throws IOException {
        Request request = new Request.Builder()
                .url("https://discord.com/api/v8/users/@me")
                .addHeader("Authorization", token)
                .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) discord/0.0.12 Chrome/78.0.3904.130 Electron/7.3.2 Safari/537.36")
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private Response deleteMessage(String token, String channel_id, String message_id) throws IOException {
        Request request = new Request.Builder()
                .url("https://discord.com/api/v8/channels/" + channel_id + "/messages/" + message_id)
                .delete()
                .addHeader("Authorization", token)
                .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) discord/0.0.12 Chrome/78.0.3904.130 Electron/7.3.2 Safari/537.36")
                .build();

        Response response = client.newCall(request).execute();
        return response;
    }


}
