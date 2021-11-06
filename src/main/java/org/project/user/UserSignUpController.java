package org.project.user;

import com.password4j.Password;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.project.UserType;
import org.project.hub.HubSignUpController;
import org.project.login.LoginMainController;
import org.project.models.User;
import org.project.server.ServerReference;
import org.project.utils.RegistrationUtil;
import org.project.utils.WindowUtil;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class UserSignUpController implements Initializable {

    private final static String[] userField = {"name", "surname", "fiscalCode", "email", "nickname", "password", "confirm_password"};
    private final HashMap<String, Boolean> saveOk = new HashMap<>();

    @FXML
    private AnchorPane AP_ext;

    @FXML
    private ImageView BT_back;

    @FXML
    private ImageView BT_minimize;

    @FXML
    private ImageView BT_quit;

    @FXML
    private MFXTextField TF_name;

    @FXML
    private ImageView IV_check_name;

    @FXML
    private Label LB_error_name;

    @FXML
    private MFXTextField TF_surname;

    @FXML
    private ImageView IV_check_surname;

    @FXML
    private Label LB_error_surname;

    @FXML
    private MFXTextField TF_fiscal_code;

    @FXML
    private ImageView IV_check_fiscal_code;

    @FXML
    private Label LB_error_code;

    @FXML
    private MFXTextField TF_email;

    @FXML
    private ImageView IV_check_email;

    @FXML
    private Label LB_error_email;

    @FXML
    private MFXTextField TF_nickname;

    @FXML
    private ImageView IV_check_nickname;

    @FXML
    private Label LB_error_nickname;

    @FXML
    private MFXPasswordField PF_password;

    @FXML
    private ImageView IV_check_password;

    @FXML
    private Label LB_error_password;

    @FXML
    private MFXPasswordField PF_confirm_pwd;

    @FXML
    private ImageView IV_check_confirmed_password;

    @FXML
    private Label LB_error_confirmed_password;

    @FXML
    private MFXButton BT_sing_up;

    @FXML
    private MFXProgressSpinner PS_spinner;

    private Stage stage;
    private int countOk = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> stage = (Stage) AP_ext.getScene().getWindow());

        for (String s : userField) {
            saveOk.put(s, false);
        }

        TF_name.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.strip();
            checkName(value);
        });

        TF_surname.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.strip();
            checkSurname(value);
        });

        TF_fiscal_code.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.strip();
            checkFiscalCode(value);
        });

        TF_email.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.strip();
            checkEmail(value);
        });

        TF_nickname.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.strip();
            checkNickname(value);
        });

        PF_password.textProperty().addListener((observable, oldValue, newValue) ->
                Platform.runLater(() -> {
                    String value = PF_password.getPassword().strip();
                    checkPassword(value);
                })
        );

        PF_confirm_pwd.textProperty().addListener((observable, oldValue, newValue) ->
                Platform.runLater(() -> {
                    String value = PF_confirm_pwd.getPassword().strip();
                    checkConfirmedPassword(value);
                })
        );
    }

    private void checkName(String value) {
        if (RegistrationUtil.checkLength(value) && RegistrationUtil.checkName(value)) {
            LB_error_name.setVisible(false);
            IV_check_name.setVisible(true);
            saveOk.put("name", true);
        } else {
            LB_error_name.setVisible(true);
            IV_check_name.setVisible(false);
            saveOk.put("name", false);
        }

    }

    private void checkSurname(String value) {
        if (RegistrationUtil.checkLength(value) && RegistrationUtil.checkName(value)) {
            LB_error_surname.setVisible(false);
            IV_check_surname.setVisible(true);
            saveOk.put("surname", true);
        } else {
            LB_error_surname.setVisible(true);
            IV_check_surname.setVisible(false);
            saveOk.put("surname", false);
        }
    }

    private void checkFiscalCode(String value) {
        if (RegistrationUtil.checkFiscalCode(value)) {
            LB_error_code.setVisible(false);
            if (RegistrationUtil.checkDuplicateFiscalCode(value)) {
                LB_error_code.setVisible(false);
                IV_check_fiscal_code.setVisible(true);
                saveOk.put("fiscalCode", true);
            } else {
                LB_error_code.setText("Questo codice fiscale è già in uso");
                LB_error_code.setVisible(true);
                IV_check_fiscal_code.setVisible(false);
                saveOk.put("fiscalCode", false);
            }
        } else {
            LB_error_code.setText("Questo codice fiscale non è valido");
            LB_error_code.setVisible(true);
            IV_check_fiscal_code.setVisible(false);
            saveOk.put("fiscalCode", false);
        }
    }

    private void checkEmail(String value) {
        if (RegistrationUtil.checkEmail(value)) {
            LB_error_email.setVisible(false);
            if (RegistrationUtil.checkDuplicateEmail(value)) {
                LB_error_email.setVisible(false);
                IV_check_email.setVisible(true);
                saveOk.put("email", true);
            } else {
                LB_error_email.setText("Questa email è già in uso");
                LB_error_email.setVisible(true);
                IV_check_email.setVisible(false);
                saveOk.put("email", false);
            }
        } else {
            LB_error_email.setText("Questa email non è valida");
            LB_error_email.setVisible(true);
            IV_check_email.setVisible(false);
            saveOk.put("email", false);
        }
    }

    private void checkNickname(String value) {
        if (RegistrationUtil.checkLength(value)) {
            LB_error_nickname.setVisible(false);
            if (RegistrationUtil.checkDuplicateNickname(value)) {
                LB_error_nickname.setVisible(false);
                IV_check_nickname.setVisible(true);
                saveOk.put("nickname", true);
            } else {
                LB_error_nickname.setText("Questo nickname è già in uso");
                LB_error_nickname.setVisible(true);
                IV_check_nickname.setVisible(false);
                saveOk.put("nickname", false);
            }
        } else {
            LB_error_nickname.setText("Questo nickname non può essere vuoto");
            LB_error_nickname.setVisible(true);
            IV_check_nickname.setVisible(false);
            saveOk.put("nickname", false);
        }
    }

    private void checkPassword(String value) {
        if (RegistrationUtil.checkPassword(value)) {
            LB_error_password.setVisible(false);
            IV_check_password.setVisible(true);
            saveOk.put("password", true);
            if (RegistrationUtil.checkPasswordConfirmed(value, PF_confirm_pwd.getPassword().strip())) {
                LB_error_confirmed_password.setVisible(false);
                IV_check_confirmed_password.setVisible(true);
                saveOk.put("confirm_password", true);
            } else {
                LB_error_confirmed_password.setText("La password non coincide");
                LB_error_confirmed_password.setVisible(true);
                IV_check_confirmed_password.setVisible(false);
            }
        } else {
            LB_error_password.setVisible(true);
            IV_check_password.setVisible(false);
            saveOk.put("password", false);
        }
    }

    private void checkConfirmedPassword(String value) {
        if (RegistrationUtil.checkLength(value)) {
            if (RegistrationUtil.checkPasswordConfirmed(PF_password.getPassword().strip(), value)) {
                LB_error_confirmed_password.setVisible(false);
                IV_check_confirmed_password.setVisible(true);
                saveOk.put("confirm_password", true);
            } else {
                LB_error_confirmed_password.setText("La password non coincide");
                LB_error_confirmed_password.setVisible(true);
                IV_check_confirmed_password.setVisible(false);
            }
        } else {
            LB_error_confirmed_password.setText("Questo campo non può essere vuoto");
            LB_error_confirmed_password.setVisible(true);
            IV_check_confirmed_password.setVisible(false);
            saveOk.put("confirm_password", false);
        }
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
        BT_sing_up.setText("");
        PS_spinner.setVisible(true);

        String name = StringUtils.capitalize(TF_name.getText().toLowerCase(Locale.ROOT).strip());
        String surname = StringUtils.capitalize(TF_surname.getText().toLowerCase(Locale.ROOT).strip());
        String fiscalCode = TF_fiscal_code.getText().strip();
        String email = TF_email.getText().strip();
        String nickname = TF_nickname.getText().strip();
        String pwd = PF_password.getPassword().strip();

        checkFiscalCode(fiscalCode);
        checkEmail(email);
        checkNickname(nickname);

        for (String s : userField) {
            if (saveOk.get(s)) {
                countOk++;
            }
        }

        try {
            if (countOk == userField.length) {
                if (!ServerReference.getServer().checkDuplicateTempEmail(email)) {
                    verifyEmail();
                    if (TempUser.getEmailIsVerified()) {
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
                    } else {
                        countOk = 0;
                        BT_sing_up.setText("REGISTRATI");
                        PS_spinner.setVisible(false);
                    }
                } else {
                    LB_error_email.setText("Un altro utente sta creando un account con questa email");
                    LB_error_email.setVisible(true);
                    countOk = 0;
                    BT_sing_up.setText("REGISTRATI");
                    PS_spinner.setVisible(false);
                }
            } else {
                countOk = 0;
                errorAlert();
                BT_sing_up.setText("REGISTRATI");
                PS_spinner.setVisible(false);
            }
        } catch (IOException | NotBoundException e) {
            e.printStackTrace();
            BT_sing_up.setText("REGISTRATI");
            PS_spinner.setVisible(false);
        }
    }

    private void errorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore in fase di registrazione");
        alert.setHeaderText("Errore nei campi:");
        alert.setContentText("Uno o più campi non sono corretti!");
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);

        ButtonType buttonTypeCancel = new ButtonType("OK", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeCancel);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.lookupButton(buttonTypeCancel).setId("btnCancel");
        dialogPane.getStylesheets().add(Objects.requireNonNull(HubSignUpController.class.getResource("alert_error.css")).toExternalForm());
        dialogPane.getStyleClass().add("alert");

        Scene dialogScene = dialogPane.getScene();
        dialogScene.setFill(Color.TRANSPARENT);

        AtomicReference<Double> xOffset = new AtomicReference<>((double) 0);
        AtomicReference<Double> yOffset = new AtomicReference<>((double) 0);

        dialogScene.setOnMousePressed(mouseEvent -> {
            xOffset.set(mouseEvent.getSceneX());
            yOffset.set(mouseEvent.getSceneY());
        });

        dialogScene.setOnMouseDragged(mouseEvent -> {
            dialogScene.getWindow().setX(mouseEvent.getScreenX() - xOffset.get());
            dialogScene.getWindow().setY(mouseEvent.getScreenY() - yOffset.get());
        });

        Stage dialogStage = (Stage) dialogScene.getWindow();
        dialogStage.getIcons().add(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/primula.png"))));

        alert.showAndWait();
    }

    private void verifyEmail() throws IOException {
        TempUser.setEmail(TF_email.getText().strip());
        TempUser.setNickname(TF_nickname.getText().strip());

        Scene scene = new Scene(WindowUtil.newScene(UserHomeController.class.getResource("fxml/verify_email.fxml")));
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(this.stage);
        stage.setTitle("Verifica email");
        stage.getIcons().add(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/primula.png"))));

        AtomicReference<Double> xOffset = new AtomicReference<>((double) 0);
        AtomicReference<Double> yOffset = new AtomicReference<>((double) 0);

        scene.setOnMousePressed(mouseEvent -> {
            xOffset.set(mouseEvent.getSceneX());
            yOffset.set(mouseEvent.getSceneY());
        });

        scene.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset.get());
            stage.setY(mouseEvent.getScreenY() - yOffset.get());
        });

        stage.showAndWait();
    }
}
