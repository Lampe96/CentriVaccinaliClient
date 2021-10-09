package org.project.user;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.password4j.Password;
import org.jetbrains.annotations.NotNull;
import org.project.login.LoginMainController;
import org.project.models.User;
import org.project.utility.RegistrationUtility;
import org.project.utility.WindowUtility;

import java.io.IOException;

public class UserSignUpController {

    @FXML
    public AnchorPane AP_ext;

    @FXML
    public ImageView BT_back;

    @FXML
    public ImageView BT_minimize;

    @FXML
    public ImageView BT_quit;

    @FXML
    public MFXTextField TF_name;

    @FXML
    public MFXTextField TF_surname;

    @FXML
    public MFXTextField TF_fiscal_code;

    @FXML
    public MFXTextField TF_email;

    @FXML
    public MFXTextField TF_nickname;

    @FXML
    public MFXPasswordField PF_password;

    @FXML
    public MFXPasswordField PF_confirm_pwd;

    @FXML
    public MFXButton BT_sing_up;

    @FXML
    public Label LB_error_name;

    @FXML
    public Label LB_error_surname;

    @FXML
    public Label LB_error_code;

    @FXML
    public Label LB_error_email;

    @FXML
    public Label LB_error_nickname;

    @FXML
    public Label LB_error_confirmed_password;

    @FXML
    public Label LB_error_password;

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
        boolean saveOk = true;
        String name = TF_name.getText().trim();
        String surname = TF_surname.getText().trim();
        String fiscal_code = TF_fiscal_code.getText().trim();
        String email = TF_email.getText().trim();
        String nickname = TF_nickname.getText().trim();
        String pwd = PF_password.getText().trim();
        String confirm_pwd = PF_confirm_pwd.getText().trim();

        if (RegistrationUtility.checkName(name)){
            LB_error_name.setVisible(false);
        }else {
            LB_error_name.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtility.checkName(surname)){
            LB_error_surname.setVisible(false);
        }else {
            LB_error_surname.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtility.checkFiscalCode(fiscal_code)){
            LB_error_code.setVisible(false);
        }else {
            LB_error_code.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtility.checkEmail(email)){
            LB_error_email.setVisible(false);
        }else {
            LB_error_email.setVisible(true);
            saveOk = false;
        }

        //TODO fare checknickname con controllo se è vuoto + vedere se esiste su DB
        if (RegistrationUtility.checkName(nickname)){
            LB_error_nickname.setVisible(false);
        }else {
            LB_error_nickname.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtility.checkPassword(pwd)){
            LB_error_password.setVisible(false);
        }else {
            LB_error_password.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtility.checkPasswordConfirmed(pwd, confirm_pwd)){
            LB_error_confirmed_password.setVisible(false);
        }else {
            LB_error_confirmed_password.setVisible(true);
            saveOk = false;
        }
        if(saveOk) {
            String crypt_pwd = Password.hash(pwd).addRandomSalt().withArgon2().getResult();
            User user = new User(name, surname, fiscal_code, email, nickname, crypt_pwd);
        }
    }
}
