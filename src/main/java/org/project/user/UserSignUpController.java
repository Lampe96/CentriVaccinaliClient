package org.project.user;

import com.password4j.Password;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Locale;
import java.util.ResourceBundle;

public class UserSignUpController implements Initializable {

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
    public ImageView IV_check_name;

    @FXML
    public ImageView IV_check_surname;

    @FXML
    public ImageView IV_check_fiscal_code;

    @FXML
    public ImageView IV_check_email;

    @FXML
    public ImageView IV_check_nickname;

    @FXML
    public ImageView IV_check_password;

    @FXML
    public ImageView IV_check_confirmed_password;

    private Stage stage;
    private boolean saveOk = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> stage = (Stage) AP_ext.getScene().getWindow());

        TF_name.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.strip();
            if (RegistrationUtil.checkLength(value)) {
                LB_error_name.setVisible(false);
                IV_check_name.setVisible(true);
                saveOk = true;
            } else {
                LB_error_name.setVisible(true);
                IV_check_name.setVisible(false);
                saveOk = false;
            }
        });

        TF_surname.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.strip();
            if (RegistrationUtil.checkLength(value)) {
                LB_error_surname.setVisible(false);
                IV_check_surname.setVisible(true);
                saveOk = true;

            } else {
                LB_error_surname.setVisible(true);
                IV_check_surname.setVisible(false);
                saveOk = false;
            }
        });

        TF_fiscal_code.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.strip();
            if (RegistrationUtil.checkFiscalCode(value)) {
                LB_error_code.setVisible(false);
                if (RegistrationUtil.checkDuplicateFiscalCode(value)) {
                    LB_error_code.setVisible(false);
                    IV_check_fiscal_code.setVisible(true);
                    saveOk = true;
                } else {
                    LB_error_code.setText("Questo codice fiscale è già in uso");
                    LB_error_code.setVisible(true);
                    IV_check_fiscal_code.setVisible(false);
                    saveOk = false;
                }
            } else {
                LB_error_code.setText("Questo codice fiscale non è valido");
                LB_error_code.setVisible(true);
                IV_check_fiscal_code.setVisible(false);
                saveOk = false;
            }
        });

        TF_email.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.strip();
            if (RegistrationUtil.checkEmail(value)) {
                LB_error_email.setVisible(false);
                if (RegistrationUtil.checkDuplicateEmail(value)) {
                    LB_error_email.setVisible(false);
                    IV_check_email.setVisible(true);
                    saveOk = true;
                } else {
                    LB_error_email.setText("Questa email è già in uso");
                    LB_error_email.setVisible(true);
                    IV_check_email.setVisible(false);
                    saveOk = false;
                }
            } else {
                LB_error_email.setText("Questa email non è valida");
                LB_error_email.setVisible(true);
                IV_check_email.setVisible(false);
                saveOk = false;
            }
        });

        TF_nickname.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.strip();
            if (RegistrationUtil.checkLength(value)) {
                LB_error_nickname.setVisible(false);
                if (RegistrationUtil.checkDuplicateNickname(value)) {
                    LB_error_nickname.setVisible(false);
                    IV_check_nickname.setVisible(true);
                    saveOk = true;
                } else {
                    LB_error_nickname.setText("Questo nickname è già in uso");
                    LB_error_nickname.setVisible(true);
                    IV_check_nickname.setVisible(false);
                    saveOk = false;
                }
            } else {
                LB_error_nickname.setText("Questo nickname non può essere vuoto");
                LB_error_nickname.setVisible(true);
                IV_check_nickname.setVisible(false);
                saveOk = false;
            }
        });


        PF_password.textProperty().addListener((observable, oldValue, newValue) ->
                Platform.runLater(() -> {
                    String value = PF_password.getPassword().strip();
                    if (RegistrationUtil.checkPassword(value)) {
                        LB_error_password.setVisible(false);
                        IV_check_password.setVisible(true);
                        saveOk = true;
                    } else {
                        LB_error_password.setVisible(true);
                        IV_check_password.setVisible(false);
                        saveOk = false;
                    }
                })
        );

        PF_confirm_pwd.textProperty().addListener((observable, oldValue, newValue) ->
                Platform.runLater(() -> {
                    String value = PF_confirm_pwd.getPassword().strip();
                    if (RegistrationUtil.checkLength(value)) {
                        if (RegistrationUtil.checkPasswordConfirmed(PF_password.getPassword().strip(), value)) {
                            LB_error_confirmed_password.setVisible(false);
                            IV_check_confirmed_password.setVisible(true);
                            saveOk = true;
                        } else {
                            LB_error_confirmed_password.setText("La password non coincide");
                            LB_error_confirmed_password.setVisible(true);
                            IV_check_confirmed_password.setVisible(false);
                            saveOk = false;
                        }
                    } else {
                        LB_error_confirmed_password.setText("Questo campo non può essere vuoto");
                        LB_error_confirmed_password.setVisible(true);
                        IV_check_confirmed_password.setVisible(false);
                        saveOk = false;
                    }
                })
        );
    }

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
        String name = StringUtils.capitalize(TF_name.getText().toLowerCase(Locale.ROOT).strip());
        String surname = StringUtils.capitalize(TF_surname.getText().toLowerCase(Locale.ROOT).strip());
        String fiscalCode = TF_fiscal_code.getText().strip();
        String email = TF_email.getText().strip();
        String nickname = TF_nickname.getText().strip();
        String pwd = PF_password.getPassword().strip();

        if (saveOk) {
            try {
                WindowUtil.setRoot(LoginMainController.class.getResource("fxml/login.fxml"), AP_ext.getScene());
            } catch (IOException e) {
                e.printStackTrace();
            }

            String cryptPwd = Password.hash(pwd).addRandomSalt().withArgon2().getResult();
            User user = new User(name, surname, fiscalCode.toUpperCase(Locale.ROOT), email, nickname, cryptPwd);
            System.out.println(user);

            try {
                ServerReference.getServer().insertDataUser(user);
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }
    }
}
