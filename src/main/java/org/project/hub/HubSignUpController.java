package org.project.hub;

import com.jfoenix.controls.JFXComboBox;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.project.login.LoginMainController;
import org.project.utility.RegistrationUtility;
import org.project.utility.WindowUtility;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HubSignUpController implements Initializable {

    @FXML
    public AnchorPane AP_ext;

    @FXML
    public ImageView BT_back;

    @FXML
    public ImageView BT_minimize;

    @FXML
    public ImageView BT_quit;

    @FXML
    public MFXTextField TF_name_hub;

    @FXML
    public MFXPasswordField PF_password;

    @FXML
    public MFXPasswordField PF_confirmed_password;

    @FXML
    public MFXTextField TF_address;

    @FXML
    public JFXComboBox<String> CB_typology;

    @FXML
    public MFXButton BT_sing_up;

    @FXML
    public Label LB_error_name;

    @FXML
    public Label LB_error_password;

    @FXML
    public Label Lb_error_confirmed_password;

    @FXML
    public Label LB_error_address;

    @FXML
    public Label LB_error_typology;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CB_typology.getItems().removeAll(CB_typology.getItems());
        CB_typology.getItems().addAll("Ospedaliero", "Aziendale", "Hub");
    }

    @FXML
    public void back() {
        try {
            WindowUtility.setRoot(LoginMainController.class.getResource("fxml/login.fxml"), AP_ext.getScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void darkStyleBack() {
        setDarkHover(BT_back);
    }

    @FXML
    public void restoreStyleBack() {
        resetDarkExit(BT_back);
    }

    @FXML
    private void minimize() {
        Stage stage = (Stage) AP_ext.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void darkStyleMinimize() {
        setDarkHover(BT_minimize);
    }

    @FXML
    private void restoreStyleMinimize() {
        resetDarkExit(BT_minimize);
    }

    @FXML
    private void quit() {
        System.exit(0);
    }

    @FXML
    private void darkStyleQuit() {
        setDarkHover(BT_quit);
    }

    @FXML
    private void restoreStyleQuit() {
        resetDarkExit(BT_quit);
    }

    private void setDarkHover(@NotNull ImageView iv) {
        iv.setEffect(new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.5), 10, 0, 5, 5));
    }

    private void resetDarkExit(@NotNull ImageView iv) {
        iv.setEffect(null);
    }

    @FXML
    public void sign_up() {

        Boolean saveOk = false;

        if (RegistrationUtility.checkName(TF_name_hub.getText().trim())) {
            LB_error_name.setVisible(false);
            saveOk = true;
        } else {
            LB_error_name.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtility.checkPassword(PF_password.getText().trim())) {
            LB_error_password.setVisible(false);
            saveOk = true;
        } else {
            LB_error_password.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtility.checkPasswordConfirmed(PF_password.getText().trim(), PF_confirmed_password.getText().trim())) {
            Lb_error_confirmed_password.setVisible(false);
            saveOk = true;
        } else {
            Lb_error_confirmed_password.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtility.checkAddress(TF_address.getText().trim())) {
            LB_error_address.setVisible(false);
            saveOk = true;
        } else {
            LB_error_address.setVisible(true);
            saveOk = false;
        }

        if (CB_typology.getValue() != null) {
            LB_error_typology.setVisible(false);
            saveOk = true;
        } else {
            LB_error_typology.setVisible(true);
            saveOk = false;
        }


    }
}
