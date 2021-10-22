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
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.project.login.LoginMainController;
import org.project.models.User;
import org.project.server.ServerReference;
import org.project.utils.RegistrationUtil;
import org.project.utils.WindowUtil;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Locale;

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
        String name = StringUtils.capitalize(TF_name.getText().toLowerCase(Locale.ROOT).strip());
        String surname = StringUtils.capitalize(TF_surname.getText().toLowerCase(Locale.ROOT).strip());
        String fiscalCode = TF_fiscal_code.getText().strip();
        String email = TF_email.getText().strip();
        String nickname = TF_nickname.getText().strip();
        String pwd = PF_password.getPassword().strip();
        String confirmPwd = PF_confirm_pwd.getPassword().strip();

        if (RegistrationUtil.checkLength(name)) {
            LB_error_name.setVisible(false);
        } else {
            LB_error_name.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtil.checkLength(surname)) {
            LB_error_surname.setVisible(false);
        } else {
            LB_error_surname.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtil.checkFiscalCode(fiscalCode)) {
            LB_error_code.setVisible(false);
            if (RegistrationUtil.checkDuplicateFiscalCode(fiscalCode)) {
                LB_error_code.setVisible(false);
            } else {
                LB_error_code.setText("Questo codice fiscale è già in uso!");
                LB_error_code.setVisible(true);
                saveOk = false;
            }
        } else {
            LB_error_code.setText("Questo codice fiscale non è valido!");
            LB_error_code.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtil.checkEmail(email)) {
            LB_error_email.setVisible(false);
            if (RegistrationUtil.checkDuplicateEmail(email)) {
                LB_error_email.setVisible(false);
            } else {
                LB_error_email.setText("Questa email è già in uso!");
                LB_error_email.setVisible(true);
                saveOk = false;
            }
        } else {
            LB_error_email.setText("Questa email non è valida!");
            LB_error_email.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtil.checkLength(nickname)) {
            LB_error_nickname.setVisible(false);
            if (RegistrationUtil.checkDuplicateNickname(nickname)) {
                LB_error_nickname.setVisible(false);
            } else {
                LB_error_nickname.setText("Il nickname è già in uso!");
                LB_error_nickname.setVisible(true);
                saveOk = false;
            }
        } else {
            LB_error_nickname.setText("Il nickname non può essere vuoto!");
            LB_error_nickname.setVisible(true);
            saveOk = false;
        }

        if (RegistrationUtil.checkPassword(pwd)) {
            LB_error_password.setVisible(false);
        } else {
            LB_error_password.setVisible(true);
            saveOk = false;
        }

        if(RegistrationUtil.checkLength(confirmPwd)){
            if (RegistrationUtil.checkPasswordConfirmed(pwd, confirmPwd)) {
                LB_error_confirmed_password.setVisible(false);
            } else {
                LB_error_confirmed_password.setText("La password non coincide!");
                LB_error_confirmed_password.setVisible(true);
                saveOk = false;
            }
        } else {
            LB_error_confirmed_password.setText("Questo campo non può essere vuoto");
            LB_error_confirmed_password.setVisible(true);
            saveOk = false;
        }

        if (saveOk) {
            String cryptPwd = Password.hash(pwd).addRandomSalt().withArgon2().getResult();
            User user = new User(name, surname, fiscalCode.toUpperCase(Locale.ROOT), email, nickname, cryptPwd);
            try {
                WindowUtil.setRoot(LoginMainController.class.getResource("fxml/login.fxml"), AP_ext.getScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(user);

            try {
                ServerReference.getServer().insertDataUser(user);
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }
    }
}
