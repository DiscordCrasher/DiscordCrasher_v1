package me.dc.automaticdiscordcrasher.framecontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import me.dc.automaticdiscordcrasher.utils.Flooder;
import me.dc.automaticdiscordcrasher.utils.Requester;

import java.io.IOException;


public class FrameController {


    public static boolean cancel = false;

    @FXML
    private Label errorLabel;

    @FXML
    private Label messageCountLabel;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField idTextField;

    @FXML
    private Button startButton;

    @FXML
    private Button cancelButton;

    @FXML
    void onActionStartButton(ActionEvent event) {


        startButton.setDisable(true);
        emailTextField.setEditable(false);
        passwordTextField.setEditable(false);
        idTextField.setEditable(false);

        if (emailTextField.getText() == null || emailTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "O campo de e-mail não foi preenchido.");
            alert.show();
            startButton.setDisable(false);
            emailTextField.setEditable(true);
            passwordTextField.setEditable(true);
            idTextField.setEditable(true);
            return;
        }

        if (passwordTextField.getText() == null || passwordTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "O campo de senha não foi preenchido.");
            alert.show();
            startButton.setDisable(false);
            emailTextField.setEditable(true);
            passwordTextField.setEditable(true);
            idTextField.setEditable(true);
            return;
        }

        if (idTextField.getText() == null || idTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "O campo de id não foi preenchido.");
            alert.show();
            startButton.setDisable(false);
            emailTextField.setEditable(true);
            passwordTextField.setEditable(true);
            idTextField.setEditable(true);
            return;
        }

        String token = new Requester().getToken(emailTextField.getText(), passwordTextField.getText());

        if (token == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "E-mail ou senha incorreto(a). (Lembre-se o 2FA deve estar desativado)");
            alert.show();
            startButton.setDisable(false);
            emailTextField.setEditable(true);
            passwordTextField.setEditable(true);
            idTextField.setEditable(true);
            return;
        }

        cancelButton.setDisable(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new Flooder(errorLabel, messageCountLabel, emailTextField, passwordTextField, idTextField, startButton, cancelButton).attack(token, idTextField.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @FXML
    void onActionCancelButton(ActionEvent event) {
        cancel = true;
    }


}
