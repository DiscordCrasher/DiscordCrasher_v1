package me.dc.automaticdiscordcrasher.framecontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import me.dc.automaticdiscordcrasher.App;
import me.dc.automaticdiscordcrasher.utils.Requester;

public class LoginController {

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button loginButton;

    @FXML
    void loginButtonOnAction(ActionEvent event) {

        loginButton.setDisable(true);

        if (emailTextField.getText() == null || emailTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "O campo de e-mail não foi preenchido.");
            alert.show();
            loginButton.setDisable(false);
            return;
        }

        if (passwordTextField.getText() == null || passwordTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "O campo de senha não foi preenchido.");
            alert.show();
            loginButton.setDisable(false);
            return;
        }

        String token = new Requester().getToken(emailTextField.getText(), passwordTextField.getText());

        if (token == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "E-mail ou senha incorreto(a). (Lembre-se o 2FA deve estar desativado)\n(Lembre-se de logar no navedador)");
            alert.show();
            loginButton.setDisable(false);
            return;
        }



        App.appManager.setToken(token);
        App.appManager.setEmail(emailTextField.getText());
        App.appManager.setPassword(passwordTextField.getText());
        App.appManager.showFrame();
        App.appManager.showChangelog();
        App.appManager.closeLogin();


    }



}
