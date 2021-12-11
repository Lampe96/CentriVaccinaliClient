package org.project.user;

import com.password4j.Password;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.project.UserType;
import org.project.hub.HubSignUpController;
import org.project.login.LoginMainController;
import org.project.models.User;
import org.project.server.ServerReference;
import org.project.utils.RegistrationUtil;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Questa classe gestisce tutti i componenti presenti nella
 * schermata di registrazione degi cittadini, avviata dalla
 * classe {@link LoginMainController}. In caso di successo si viene
 * rinviati all'interfaccia di login.
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class UserSignUpController implements Initializable {

    /**
     * Array utilizzato salavare i diversi campi degli utenti.
     */
    private final static String[] USERFIELD = {
            "name",
            "surname",
            "fiscalCode",
            "email",
            "nickname",
            "password",
            "confirm_password"
    };

    /**
     * HashMap per il controllo della correttezza dei campi.
     */
    private final HashMap<String, Boolean> saveOk = new HashMap<>();

    /**
     * AnchorPane esterno.
     */
    @FXML
    private AnchorPane AP_ext;

    /**
     * Immagine che serve per tornare alla schermata precedente.
     */
    @FXML
    private ImageView BT_back;

    /**
     * Immagine che serve per ridurre ad icona l'applicazione.
     */
    @FXML
    private ImageView BT_minimize;

    /**
     * Immagine che funge da quit dall'applicazione.
     */
    @FXML
    private ImageView BT_quit;

    /**
     * Field dove inserire il nome dell'utente.
     */
    @FXML
    private MFXTextField TF_name;

    /**
     * Immagine utilizzata per confermare la
     * correttezza del campo name visivamente.
     */
    @FXML
    private ImageView IV_check_name;

    /**
     * Label di errore per il campo name.
     */
    @FXML
    private Label LB_error_name;

    /**
     * Field dove inserire il cognome dell'utente.
     */
    @FXML
    private MFXTextField TF_surname;

    /**
     * Immagine utilizzata per confermare la
     * correttezza del campo cognome visivamente.
     */
    @FXML
    private ImageView IV_check_surname;

    /**
     * Label di errore per il campo cognome.
     */
    @FXML
    private Label LB_error_surname;

    /**
     * Field dove inserire il codice fiscale dell'utente.
     */
    @FXML
    private MFXTextField TF_fiscal_code;

    /**
     * Immagine utilizzata per confermare la
     * correttezza del campo codice fiscale visivamente.
     */
    @FXML
    private ImageView IV_check_fiscal_code;

    /**
     * Immagine utilizzata per poter calcolare il codice fiscale online.
     */
    @FXML
    private ImageView IV_calculator_fiscal_code;

    /**
     * Label di errore per il campo codice fiscale.
     */
    @FXML
    private Label LB_error_code;

    /**
     * Field dove inserire l'email dell'utente.
     */
    @FXML
    private MFXTextField TF_email;

    /**
     * Immagine utilizzata per confermare la
     * correttezza del campo email visivamente.
     */
    @FXML
    private ImageView IV_check_email;

    /**
     * Label di errore per il campo email.
     */
    @FXML
    private Label LB_error_email;

    /**
     * Field dove inserire il nickname.
     */
    @FXML
    private MFXTextField TF_nickname;

    /**
     * Immagine utilizzata per confermare la
     * correttezza del campo nickname visivamente.
     */
    @FXML
    private ImageView IV_check_nickname;

    /**
     * Label di errore per il campo nickname.
     */
    @FXML
    private Label LB_error_nickname;

    /**
     * Field dove inserire la password.
     */
    @FXML
    private MFXPasswordField PF_password;

    /**
     * Immagine utilizzata per confermare la
     * correttezza del campo password visivamente.
     */
    @FXML
    private ImageView IV_check_password;

    /**
     * Label di errore per il password name.
     */
    @FXML
    private Label LB_error_password;

    /**
     * Field dove inserire la conferma della password.
     */
    @FXML
    private MFXPasswordField PF_confirm_pwd;

    /**
     * Immagine utilizzata per confermare la
     * correttezza del campo confirm password visivamene.
     */
    @FXML
    private ImageView IV_check_confirmed_password;

    /**
     * Label di errore per il campo confirmed password.
     */
    @FXML
    private Label LB_error_confirmed_password;

    /**
     * Bottone che avvia il processo di registrazione,
     * se avvenuta con successo viene avviata.
     * {@link LoginMainController}
     */
    @FXML
    private MFXButton BT_sing_up;

    /**
     * Spinner avviato sul bottone {@link #BT_sing_up}.
     */
    @FXML
    private MFXProgressSpinner PS_spinner;

    /**
     * Stage riferito a questo controller.
     */
    private Stage stage;

    /**
     * Scena riferito a questo controller.
     */
    private Scene scene;

    /**
     * Variabile per controllare se tutti i campi
     * sono stati inseriti correttamente.
     */
    private int countOk = 0;

    /**
     * Controller per impostare dati necessari al {@link UserVerifyEmailController}.
     */
    private UserVerifyEmailController userVerifyEmailController;

    /**
     * Controller per impostare dati necessari al {@link UserAlreadyVaccinatedController}.
     */
    private UserAlreadyVaccinatedController userAlreadyVaccinatedController;

    /**
     * Boolean per tener traccia della gia' avvenuta vaccinazione
     * di un utente.
     */
    private boolean alreadyVaccinated = false;

    /**
     * Utilizzato per inizializzare l'interfaccia prendendo la scena
     * e creare tutti i campi con i relativi listener, che gestiscono la scrittura
     * in modo dinamico.
     *
     * @param url            url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();
            scene = AP_ext.getScene();
            try {
                openPopUpAlreadyVaccinated();
                User vu = userAlreadyVaccinatedController.getVaccinatedUser();
                if (vu != null) {
                    alreadyVaccinated = true;
                    TF_name.setText(vu.getName());
                    TF_surname.setText(vu.getSurname());
                    TF_fiscal_code.setText(vu.getFiscalCode());

                    TF_name.setDisable(true);
                    TF_surname.setDisable(true);
                    TF_fiscal_code.setDisable(true);
                    LB_error_code.setVisible(false);
                    saveOk.put("name", true);
                    saveOk.put("surname", true);
                    saveOk.put("fiscalCode", true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Tooltip tool = new Tooltip("Calcola codice fiscale");
            tool.setShowDelay(new Duration(500));
            Tooltip.install(IV_calculator_fiscal_code, tool);
        });

        for (String s : USERFIELD) {
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

        if (!alreadyVaccinated) {
            TF_fiscal_code.textProperty().addListener((observable, oldValue, newValue) -> {
                String value = newValue.strip();
                checkFiscalCode(value);
            });
        }

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

    /**
     * Utilizzato per fare i diversi controlli sul nome dell'utente.
     *
     * @param value valore inserito
     * @see org.project.utils.RegistrationUtil#checkLength(String)
     * @see org.project.utils.RegistrationUtil#checkName(String)
     */
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

    /**
     * Utilizzato per fare i diversi controlli sul cognome dell'utente.
     *
     * @param value valore inserito
     * @see org.project.utils.RegistrationUtil#checkLength(String)
     * @see org.project.utils.RegistrationUtil#checkName(String)
     */
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

    /**
     * Utilizzato per fare i diversi controlli sul codice fiscale dell'utente.
     *
     * @param value valore inserito
     * @see org.project.utils.RegistrationUtil#checkFiscalCode(String)
     * @see org.project.utils.RegistrationUtil#checkDuplicateFiscalCode(String)
     */
    private void checkFiscalCode(String value) {
        if (RegistrationUtil.checkFiscalCode(value)) {
            LB_error_code.setVisible(false);
            if (RegistrationUtil.checkDuplicateFiscalCode(value)) {
                LB_error_code.setVisible(false);
                IV_check_fiscal_code.setVisible(true);
                IV_calculator_fiscal_code.setVisible(false);
                saveOk.put("fiscalCode", true);
            } else {
                LB_error_code.setText("Questo codice fiscale è già in uso");
                LB_error_code.setVisible(true);
                IV_check_fiscal_code.setVisible(false);
                IV_calculator_fiscal_code.setVisible(true);
                saveOk.put("fiscalCode", false);
            }
        } else {
            LB_error_code.setText("Questo codice fiscale non è valido");
            LB_error_code.setVisible(true);
            IV_check_fiscal_code.setVisible(false);
            IV_calculator_fiscal_code.setVisible(true);
            saveOk.put("fiscalCode", false);
        }
    }

    /**
     * Utilizzato per fare i diversi controlli sull'email dell'utente.
     *
     * @param value valore inserito
     * @see org.project.utils.RegistrationUtil#checkEmail(String)
     * @see org.project.utils.RegistrationUtil#checkDuplicateEmail(String)
     */
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

    /**
     * Utilizzato per fare i diversi controlli sul nickname dell'utente.
     *
     * @param value valore inserito
     * @see org.project.utils.RegistrationUtil#checkLength(String)
     * @see org.project.utils.RegistrationUtil#checkDuplicateNickname(String)
     */
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

    /**
     * Utilizzato per fare i diversi controlli sulla password dell'utente.
     *
     * @param value valore inserito
     * @see org.project.utils.RegistrationUtil#checkPassword(String)
     * @see org.project.utils.RegistrationUtil#checkPasswordConfirmed(String, String)
     */
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

    /**
     * Utilizzato per fare i diversi controlli sulla conferma della password dell'utente.
     *
     * @param value valore inserito
     * @see org.project.utils.RegistrationUtil#checkLength(String)
     * @see org.project.utils.RegistrationUtil#checkPasswordConfirmed(String, String)
     */
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

    /**
     * Utilizzato per inizializzare la backarrow.
     */
    @FXML
    private void back() {
        try {
            scene.setRoot(FXMLLoader.load(Objects.requireNonNull(LoginMainController.class.getResource("fxml/login.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Utilizzato per scurire l'icona backarrow
     * quando il cursore entra.
     */
    @FXML
    private void darkStyleBack() {
        setDarkHover(BT_back);
    }

    /**
     * Utilizzato per riportare l'immagine alla normalita'
     * una volta uscito il cursore.
     */
    @FXML
    private void restoreStyleBack() {
        resetDarkExit(BT_back);
    }

    /**
     * Utilizzato per ridurre il programma ad icona.
     */
    @FXML
    private void minimize() {
        stage.setIconified(true);
    }

    /**
     * Utilizzato per scurire l'icona minimize
     * quando il cursore entra.
     */
    @FXML
    private void darkStyleMinimize() {
        setDarkHover(BT_minimize);
    }

    /**
     * Utilizzato per riportare l'immagine alla normalita'
     * una volta uscito il cursore.
     */
    @FXML
    private void restoreStyleMinimize() {
        resetDarkExit(BT_minimize);
    }

    /**
     * Quando premuto il tasto exit chiude l'applicazione.
     */
    @FXML
    private void quit() {
        System.exit(0);
    }

    /**
     * Utilizzato per scurire l'icona quit
     * quando il cursore entra.
     */
    @FXML
    private void darkStyleQuit() {
        setDarkHover(BT_quit);
    }

    /**
     * Utilizzato per riportare l'immagine alla normalita'
     * una volta uscito il cursore.
     */
    @FXML
    private void restoreStyleQuit() {
        resetDarkExit(BT_quit);
    }

    /**
     * Utilizzato da certe immagini per scurire l'interno.
     *
     * @param iv ImageView che si vuole scurire
     */
    private void setDarkHover(@NotNull ImageView iv) {
        iv.setEffect(new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.5), 10, 0, 5, 5));
    }

    /**
     * Utilizzato da certe immagini per portare alla normalita'
     * l'effetto interno di scurimento.
     *
     * @param iv ImageView che si vuole portare alla normalita'
     */
    private void resetDarkExit(@NotNull ImageView iv) {
        iv.setEffect(null);
    }

    /**
     * Utilizzato per richiamare il metodo {@link #browseCalcFiscalCode}.
     */
    @FXML
    private void calculateWebFiscalCode() {
        try {
            browseCalcFiscalCode();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Utilizzato per aprire google alla pagina per calcolare il codice
     * fiscale.
     *
     * @throws URISyntaxException URISyntaxException
     * @throws IOException        IOException
     */
    private void browseCalcFiscalCode() throws URISyntaxException, IOException {
        URI uri = new URI("https://www.codicefiscaleonline.com/");
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            desktop.browse(uri);
        }
    }

    /**
     * Utilizzato per scurire l'icona della calcolatrice
     * quando il cursore entra.
     */
    @FXML
    private void darkStyleCalculator() {
        setDarkHover(IV_calculator_fiscal_code);
    }

    /**
     * Utilizzato per riportare l'immagine alla normalita'
     * una volta uscito il cursore.
     */
    @FXML
    private void restoreStyleCalculator() {
        resetDarkExit(IV_calculator_fiscal_code);
    }

    /**
     * Metodo per gestire la registrazione, andando a controllare tutti i campi tramite i metodi
     * richiamati dalla classe RegistrationUtil. In caso di successo viene creato il centro
     * sul DB tramite l'apposito metodo del server. Se avviene con successo, l'utente
     * viene mandato al {@link LoginMainController}.
     *
     * @see org.project.utils.RegistrationUtil#checkPasswordConfirmed(String, String)
     * @see org.project.server.Server#checkDuplicateEmail(String)
     * @see org.project.server.Server#insertDataUser(User)
     * @see org.project.server.Server#changeDataUser(User)
     */
    @FXML
    private void signUp() {
        BT_sing_up.setText("");
        PS_spinner.setVisible(true);

        String name = StringUtils.capitalize(TF_name.getText().toLowerCase(Locale.ROOT).strip());
        String surname = StringUtils.capitalize(TF_surname.getText().toLowerCase(Locale.ROOT).strip());
        String fiscalCode = TF_fiscal_code.getText().strip();
        String email = TF_email.getText().strip();
        String nickname = TF_nickname.getText().strip();
        String pwd = PF_password.getPassword().strip();

        if (!alreadyVaccinated) {
            checkFiscalCode(fiscalCode);
        }
        checkEmail(email);
        checkNickname(nickname);

        for (String s : USERFIELD) {
            if (saveOk.get(s)) {
                countOk++;
            }
        }

        try {
            if (countOk == USERFIELD.length) {
                if (!ServerReference.getServer().checkDuplicateTempEmail(email)) {
                    verifyEmail();
                    if (userVerifyEmailController.getIsVerified()) {
                        try {
                            scene.setRoot(FXMLLoader.load(Objects.requireNonNull(LoginMainController.class.getResource("fxml/login.fxml"))));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String cryptPwd = Password.hash(pwd).addRandomSalt().withArgon2().getResult();
                        User user = new User();
                        if (!alreadyVaccinated) {
                            user.setName(name);
                            user.setSurname(surname);
                            user.setFiscalCode(fiscalCode.toUpperCase(Locale.ROOT));
                            user.setEmail(email);
                            user.setNickname(nickname);
                            user.setPassword(cryptPwd);

                            System.out.println(user);

                            try {
                                ServerReference.getServer().insertDataUser(user);
                            } catch (RemoteException | NotBoundException e) {
                                e.printStackTrace();
                            }
                        } else {
                            user.setEmail(email);
                            user.setNickname(nickname);
                            user.setPassword(cryptPwd);
                            user.setFiscalCode(fiscalCode);

                            System.out.println(user);

                            try {
                                ServerReference.getServer().changeDataUser(user);
                            } catch (RemoteException | NotBoundException e) {
                                e.printStackTrace();
                            }
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
                errorAlertField();
                BT_sing_up.setText("REGISTRATI");
                PS_spinner.setVisible(false);
            }
        } catch (IOException | NotBoundException e) {
            e.printStackTrace();
            BT_sing_up.setText("REGISTRATI");
            PS_spinner.setVisible(false);
        }
    }

    /**
     * Alert utilizzato per segnalare eventuali errori in fase di registrazione, viene
     * chiamato dal metodo {@link #signUp}.
     */
    private void errorAlertField() {
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
        dialogPane.setPrefSize(290, 125);
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

    /**
     * Viene effettuata la verifica della mail, gestita da {@link UserVerifyEmailController}
     * vengono impostate la mail e il nickname.
     *
     * @throws IOException IOException
     */
    private void verifyEmail() throws IOException {
        FXMLLoader loader = new FXMLLoader(UserVerifyEmailController.class.getResource("fxml/verify_email.fxml"));
        Parent root = loader.load();

        userVerifyEmailController = loader.getController();
        userVerifyEmailController.setUserData(TF_email.getText().strip(), TF_nickname.getText().strip());

        Scene scene = new Scene(root);
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

    /**
     * Chiamato nella {@link #initialize}, va a gestire l'eventuale cittadino che
     * ha gia' ricevuto una o piu' dosi presso un centro.
     *
     * @throws IOException IOException
     */
    private void openPopUpAlreadyVaccinated() throws IOException {
        FXMLLoader loader = new FXMLLoader(UserSignUpController.class.getResource("fxml/user_already_vaccinated.fxml"));
        Parent root = loader.load();
        userAlreadyVaccinatedController = loader.getController();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setResizable(false);
        stage.setTitle("Impostazioni");
        stage.getIcons().add(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/primula.png"))));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(this.stage);

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
