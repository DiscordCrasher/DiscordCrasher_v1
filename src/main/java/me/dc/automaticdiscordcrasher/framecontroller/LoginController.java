package me.dc.automaticdiscordcrasher.framecontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import me.dc.automaticdiscordcrasher.App;
import me.dc.automaticdiscordcrasher.jsonmanager.User;
import me.dc.automaticdiscordcrasher.utils.Requester;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emailComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (App.jsonFileManager.containsUser(newValue)) {
                User user = App.jsonFileManager.getUser(newValue);

                passwordTextField.setText(user.getPassword());
            }
        });

        for (User u : App.jsonFileManager.getUsers()) {
            emailComboBox.getItems().add(u.getEmail());
        }
    }

    @FXML
    private ComboBox<String> emailComboBox;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button loginButton;

    @FXML
    void loginButtonOnAction(ActionEvent event) {

        loginButton.setDisable(true);
        emailComboBox.setDisable(true);

        if (emailComboBox.getEditor().getText() == null || emailComboBox.getEditor().getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "O campo de e-mail não foi preenchido.");
            alert.show();
            loginButton.setDisable(false);
            emailComboBox.setDisable(false);
            return;
        }

        if (passwordTextField.getText() == null || passwordTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "O campo de senha não foi preenchido.");
            alert.show();
            loginButton.setDisable(false);
            emailComboBox.setDisable(false);
            return;
        }


        String email = emailComboBox.getEditor().getText();
        String password = passwordTextField.getText();
        String token = new Requester().getToken(email, password);

        if (token == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "E-mail ou senha incorreto(a). (Lembre-se o 2FA deve estar desativado)\n(Lembre-se de logar no navegador)");
            alert.show();
            loginButton.setDisable(false);
            emailComboBox.setDisable(false);
            return;
        }

        if (!App.jsonFileManager.containsUser(email)) {
            int i = JOptionPane.showConfirmDialog(null, "Você deseja salvar esses dados de login?", "Salvamento automático", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (i == JOptionPane.YES_OPTION) {
                App.jsonFileManager.saveUser(new User(email, password));
            }
        } else {
            User user = App.jsonFileManager.getUser(email);

            if (!user.getPassword().equals(password)) {
                int i = JOptionPane.showConfirmDialog(null, "Você está logando com uma senha diferente da salva anteriormente.\nVocê deseja salvar essa nova senha?", "Salvamento automático", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (i == JOptionPane.YES_OPTION) {
                    App.jsonFileManager.saveUser(new User(email, password));
                }
            }
        }

        App.appManager.setToken(token);
        App.appManager.setEmail(emailComboBox.getEditor().getText());
        App.appManager.setPassword(passwordTextField.getText());
        App.appManager.showFrame();
        App.appManager.showChangelog();
        App.appManager.closeLogin();


    }

}
