package org.project.user;

import com.password4j.Password;
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
import org.jetbrains.annotations.NotNull;
import org.project.login.LoginMainController;
import org.project.models.User;
import org.project.utils.RegistrationUtil;
import org.project.utils.WindowUtil;

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
            WindowUtil.setRoot(LoginMainController.class.getResource("fxml/login.fxml"), AP_ext.getScene());
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
    public void signUp() {
        boolean saveOk = true;
        String name = TF_name.getText().strip();
        String surname = TF_surname.getText().strip();
        String fiscalCode = TF_fiscal_code.getText().strip();
        String email = TF_email.getText().strip();
        String nickname = TF_nickname.getText().strip();
        String pwd = PF_password.getText().strip();
        String confirmPwd = PF_confirm_pwd.getText().strip();

        if (RegistrationUtil.checkName(name)) {
            LB_error_name.setVisible(false);
        } else {
            LB_error_name.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtil.checkName(surname)) {
            LB_error_surname.setVisible(false);
        } else {
            LB_error_surname.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtil.checkFiscalCode(fiscalCode)) {
            LB_error_code.setVisible(false);
        } else {
            LB_error_code.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtil.checkEmail(email)) {
            LB_error_email.setVisible(false);
        } else {
            LB_error_email.setVisible(true);
            saveOk = false;
        }

        //TODO fare check nickname con controllo se è vuoto + vedere se esiste già su DB
        if (RegistrationUtil.checkName(nickname)) {
            LB_error_nickname.setVisible(false);
        } else {
            LB_error_nickname.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtil.checkPassword(pwd)) {
            LB_error_password.setVisible(false);
        } else {
            LB_error_password.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtil.checkPasswordConfirmed(pwd, confirmPwd)) {
            LB_error_confirmed_password.setVisible(false);
        } else {
            LB_error_confirmed_password.setVisible(true);
            saveOk = false;
        }
        if (saveOk) {
            String cryptPwd = Password.hash(pwd).addRandomSalt().withArgon2().getResult();
            User user = new User(name, surname, fiscalCode, email, nickname, cryptPwd);
            System.out.println(user);
        }
    }
}
