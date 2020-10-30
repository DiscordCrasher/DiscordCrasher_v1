package me.dc.automaticdiscordcrasher.framecontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.converter.IntegerStringConverter;
import me.dc.automaticdiscordcrasher.utils.Flooder;
import me.dc.automaticdiscordcrasher.utils.Requester;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParsePosition;
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
    }


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
    private CheckBox embededCheckBox;

    @FXML
    private Spinner<Integer> delaySpinner;

    @FXML
    void onActionStartButton(ActionEvent event) {

        if (emailTextField.getText() == null || emailTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "O campo de e-mail não foi preenchido.");
            alert.show();
            return;
        }

        if (passwordTextField.getText() == null || passwordTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "O campo de senha não foi preenchido.");
            alert.show();
            return;
        }

        if (idTextField.getText() == null || idTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "O campo de id não foi preenchido.");
            alert.show();
            return;
        }

        String token = new Requester().getToken(emailTextField.getText(), passwordTextField.getText());

        if (token == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "E-mail ou senha incorreto(a). (Lembre-se o 2FA deve estar desativado)");
            alert.show();
            return;
        }

        startButton.setDisable(true);
        emailTextField.setEditable(false);
        passwordTextField.setEditable(false);
        idTextField.setEditable(false);
        embededCheckBox.setDisable(true);
        delaySpinner.setDisable(true);
        cancelButton.setDisable(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new Flooder(errorLabel, messageCountLabel, emailTextField, passwordTextField, idTextField, startButton, cancelButton, embededCheckBox, delaySpinner).attack(token, idTextField.getText());
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
