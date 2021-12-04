package org.project.hub;

import com.jfoenix.controls.JFXComboBox;
import com.password4j.Password;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import org.project.login.LoginMainController;
import org.project.models.Address;
import org.project.models.Hub;
import org.project.server.ServerReference;
import org.project.utils.RegistrationUtil;

import java.io.IOException;
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
 * schermata di registrazione dei centri vaccinali, avviata dalla
 * classe {@link LoginMainController}. In caso di successo si viene
 * rinviati all'interfaccia di login
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class HubSignUpController implements Initializable {

    /**
     * Array utilizzato per riempire la combo box {@link #CB_qualificator}
     */
    private final static String[] QUALIFICATOR = {
            "Via",
            "Corso",
            "Viale",
            "Piazza"
    };

    /**
     * Array utilizzato per riempire la combo box {@link #CB_province}
     */
    private final static String[] PROVINCES = {
            "AG", "AL", "AN", "AO", "AR", "AP", "AT", "AV", "BA", "BT", "BL", "BN", "BG", "BI", "BO", "BZ", "BS",
            "BS", "CA", "CL", "CB", "CI", "CE", "CT", "CZ", "CH", "CO", "CS", "CR", "KR", "CN", "EN", "FM", "FE",
            "FI", "FG", "FC", "FR", "GE", "GO", "GR", "IM", "IS", "SP", "AQ", "LT", "LE", "LC", "LI", "LO", "LU",
            "MC", "MN", "MS", "MT", "VS", "ME", "MI", "MO", "MB", "NA", "NO", "NU", "OG", "OT", "OR", "PD", "PA",
            "PR", "PV", "PG", "PU", "PE", "PC", "PI", "PT", "PN", "PZ", "PO", "RG", "RA", "RC", "RE", "RI", "RN",
            "RM", "RO", "SA", "SS", "SV", "SI", "SR", "SO", "TA", "TE", "TR", "TO", "TP", "TN", "TV", "TS", "UD",
            "VA", "VE", "VB", "VC", "VR", "VV", "VI", "VT"
    };

    /**
     * Array utilizzato per riempire la combo box {@link #CB_typology}
     */
    private final static String[] TYPOLOGY = {
            "OSPEDALIERO",
            "AZIENDALE",
            "HUB"
    };

    /**
     * Array utilizzato salavare i diversi campi dei centri vaccinali
     */
    private final static String[] hubField = {"name", "pwd", "confirmedPwd", "quali", "address", "number", "city", "prov", "cap", "typology"};

    /**
     * HashMap per il controllo della correttezza dei campi
     */
    private final HashMap<String, Boolean> saveOk = new HashMap<>();

    /**
     * AnchorPane esterno
     */
    @FXML
    private AnchorPane AP_ext;

    /**
     * Immagine che serve per tornare alla schermata precedente
     */
    @FXML
    private ImageView BT_back;

    /**
     * Immagine che serve per ridurre ad icona l'applicazione
     */
    @FXML
    private ImageView BT_minimize;

    /**
     * Immagine che funge da quit dall'applicazione
     */
    @FXML
    private ImageView BT_quit;

    /**
     * Field dove inserire il nome del centro vaccinale
     */
    @FXML
    private MFXTextField TF_name_hub;

    /**
     * Immagine utilizzata per confermare la
     * correttezza del campo name visivamene
     */
    @FXML
    private ImageView IV_check_name;

    /**
     * Label di errore per il campo name
     */
    @FXML
    private Label LB_error_name;

    /**
     * Field dove inserire la password
     */
    @FXML
    private MFXPasswordField PF_password;

    /**
     * Immagine utilizzata per confermare la
     * correttezza del campo password visivamene
     */
    @FXML
    private ImageView IV_check_password;

    /**
     * Label di errore per il password name
     */
    @FXML
    private Label LB_error_password;

    /**
     * Field dove inserire la conferma della password
     */
    @FXML
    private MFXPasswordField PF_confirmed_password;

    /**
     * Immagine utilizzata per confermare la
     * correttezza del campo confirm password visivamene
     */
    @FXML
    private ImageView IV_check_confirmed_password;

    /**
     * Label di errore per il campo confirmed password
     */
    @FXML
    private Label LB_error_confirmed_password;

    /**
     * Combobox utilizzata per scegliere il qualificatore
     */
    @FXML
    private JFXComboBox<String> CB_qualificator;

    /**
     * Label di errore per la combobox qualificator
     */
    @FXML
    private Label LB_error_qualificator;

    /**
     * Field dove inserire l'indirizzo del centro vaccinale
     */
    @FXML
    private MFXTextField TF_address;

    /**
     * Immagine utilizzata per confermare la
     * correttezza del campo address visivamene
     */
    @FXML
    private ImageView IV_check_address;

    /**
     * Label di errore per il campo address
     */
    @FXML
    private Label LB_error_address;

    /**
     * Field dove inserire il numero (indirizzo) del centro vaccinale
     */
    @FXML
    private MFXTextField TF_number;

    /**
     * Immagine utilizzata per confermare la
     * correttezza del campo number visivamene
     */
    @FXML
    private ImageView IV_check_number;

    /**
     * Label di errore per il campo number
     */
    @FXML
    private Label LB_error_number;

    /**
     * Field dove inserire la citta' del centro vaccinale
     */
    @FXML
    private MFXTextField TF_city;

    /**
     * Immagine utilizzata per confermare la
     * correttezza del campo city visivamene
     */
    @FXML
    private ImageView IV_check_city;

    /**
     * Label di errore per il campo city
     */
    @FXML
    private Label LB_error_city;

    /**
     * Combobox dove scegliere la provincia
     */
    @FXML
    private JFXComboBox<String> CB_province;

    /**
     * Label di errore per la combobox province
     */
    @FXML
    private Label LB_error_province;

    /**
     * Field dove inserire il CAP del centro vaccinale
     */
    @FXML
    private MFXTextField TF_cap;

    /**
     * Immagine utilizzata per confermare la
     * correttezza del campo cap visivamene
     */
    @FXML
    private ImageView IV_check_cap;

    /**
     * Label di errore per il campo cap
     */
    @FXML
    private Label LB_error_cap;

    /**
     * Combobox per scegliere la tipologia del centro vaccinale
     */
    @FXML
    private JFXComboBox<String> CB_typology;

    /**
     * Label di errore per la combobox tipologia
     */
    @FXML
    private Label LB_error_typology;

    /**
     * Bottone che avvia il processo di registrazione,
     * se avvenuta con successo viene avviata
     * {@link LoginMainController}
     */
    @FXML
    private MFXButton BT_sing_up;

    /**
     * Spinner avviato sul bottone {@link #BT_sing_up}
     */
    @FXML
    private MFXProgressSpinner PS_spinner;

    /**
     * Stage riferito a questo controller
     */
    private Stage stage;

    /**
     * Scena riferito a questo controller
     */
    private Scene scene;

    /**
     * Variabile per controllare se tutti i campi
     * sono stati inseriti correttamente
     */
    private int countOk = 0;

    /**
     * Utilizzato per inizializzare l'interfaccia prendendo la scena
     * e creare tutti i campi con i relativi listener, che gestiscono la scrittura
     * in modo dinamico
     *
     * @param url            url
     * @param resourceBundle resourceBundle
     * @see org.project.utils.RegistrationUtil#checkLength(String)
     * @see org.project.utils.RegistrationUtil#checkName(String)
     * @see org.project.utils.RegistrationUtil#checkDuplicateHubName(String)
     * @see org.project.utils.RegistrationUtil#checkPassword(String)
     * @see org.project.utils.RegistrationUtil#checkPasswordConfirmed(String, String)
     * @see org.project.utils.RegistrationUtil#checkAddress(String)
     * @see org.project.utils.RegistrationUtil#checkNumberAddress(String)
     * @see org.project.utils.RegistrationUtil#checkCityAddress(String)
     * @see org.project.utils.RegistrationUtil#checkCap(String)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();
            scene = AP_ext.getScene();
        });

        for (String s : hubField) {
            saveOk.put(s, false);
        }

        TF_cap.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                TF_cap.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        CB_qualificator.getItems().addAll(QUALIFICATOR);
        CB_province.getItems().addAll(PROVINCES);
        CB_typology.getItems().addAll(TYPOLOGY);

        TF_name_hub.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.strip();
            if (RegistrationUtil.checkLength(value) && RegistrationUtil.checkName(value)) {
                LB_error_name.setVisible(false);
                if (RegistrationUtil.checkDuplicateHubName(value)) {
                    LB_error_name.setVisible(false);
                    IV_check_name.setVisible(true);
                    saveOk.put("name", true);
                } else {
                    LB_error_name.setText("Questo centro vaccinale esiste già");
                    LB_error_name.setVisible(true);
                    IV_check_name.setVisible(false);
                    saveOk.put("name", false);
                }
            } else {
                LB_error_name.setText("Questo campo non può essere vuoto o contenere numeri");
                LB_error_name.setVisible(true);
                IV_check_name.setVisible(false);
                saveOk.put("name", false);
            }
        });

        PF_password.textProperty().addListener((observable, oldValue, newValue) ->
                Platform.runLater(() -> {
                    String value = PF_password.getPassword().strip();
                    if (RegistrationUtil.checkPassword(value)) {
                        LB_error_password.setVisible(false);
                        IV_check_password.setVisible(true);
                        saveOk.put("pwd", true);
                        if (RegistrationUtil.checkPasswordConfirmed(value, PF_confirmed_password.getPassword().strip())) {
                            LB_error_confirmed_password.setVisible(false);
                            IV_check_confirmed_password.setVisible(true);
                            saveOk.put("confirmedPwd", true);
                        } else {
                            LB_error_confirmed_password.setText("La password non coincide");
                            LB_error_confirmed_password.setVisible(true);
                            IV_check_confirmed_password.setVisible(false);
                            saveOk.put("confirmedPwd", false);
                        }
                    } else {
                        LB_error_password.setVisible(true);
                        IV_check_password.setVisible(false);
                        saveOk.put("pwd", false);
                    }
                })
        );

        PF_confirmed_password.textProperty().addListener((observable, oldValue, newValue) ->
                Platform.runLater(() -> {
                    String value = PF_confirmed_password.getPassword().strip();
                    if (RegistrationUtil.checkLength(value)) {
                        if (RegistrationUtil.checkPasswordConfirmed(PF_password.getPassword().strip(), value)) {
                            LB_error_confirmed_password.setVisible(false);
                            IV_check_confirmed_password.setVisible(true);
                            saveOk.put("confirmedPwd", true);
                        } else {
                            LB_error_confirmed_password.setText("La password non coincide");
                            LB_error_confirmed_password.setVisible(true);
                            IV_check_confirmed_password.setVisible(false);
                            saveOk.put("confirmedPwd", false);
                        }
                    } else {
                        LB_error_confirmed_password.setText("Questo campo non può essere vuoto");
                        LB_error_confirmed_password.setVisible(true);
                        IV_check_confirmed_password.setVisible(false);
                        saveOk.put("confirmedPwd", false);
                    }
                })
        );

        TF_address.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.strip();
            if (RegistrationUtil.checkLength(value)) {
                LB_error_address.setVisible(false);
                if (RegistrationUtil.checkAddress(value)) {
                    LB_error_address.setVisible(false);
                    IV_check_address.setVisible(true);
                    saveOk.put("address", true);
                } else {
                    LB_error_address.setText("Campo non corretto");
                    LB_error_address.setVisible(true);
                    IV_check_address.setVisible(false);
                    saveOk.put("address", false);
                }
            } else {
                LB_error_address.setText("Questo campo non può essere vuoto");
                LB_error_address.setVisible(true);
                IV_check_address.setVisible(false);
                saveOk.put("address", false);
            }
        });

        TF_number.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.strip();
            if (RegistrationUtil.checkNumberAddress(value)) {
                LB_error_number.setVisible(false);
                IV_check_number.setVisible(true);
                saveOk.put("number", true);
            } else {
                LB_error_number.setVisible(true);
                IV_check_number.setVisible(false);
                saveOk.put("number", false);
            }
        });

        TF_city.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.strip();
            if (RegistrationUtil.checkLength(value)) {
                LB_error_city.setVisible(false);
                if (RegistrationUtil.checkCityAddress(value)) {
                    LB_error_city.setVisible(false);
                    IV_check_city.setVisible(true);
                    saveOk.put("city", true);
                } else {
                    LB_error_city.setText("Campo non corretto");
                    LB_error_city.setVisible(true);
                    IV_check_city.setVisible(false);
                    saveOk.put("city", false);
                }
            } else {
                LB_error_city.setText("Questo campo non può essere vuoto");
                LB_error_city.setVisible(true);
                IV_check_city.setVisible(false);
                saveOk.put("city", false);
            }
        });

        TF_cap.textProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.strip();
            if (RegistrationUtil.checkLength(value)) {
                LB_error_cap.setVisible(false);
                if (RegistrationUtil.checkCap(value)) {
                    LB_error_cap.setVisible(false);
                    IV_check_cap.setVisible(true);
                    saveOk.put("cap", true);
                } else {
                    LB_error_cap.setText("Campo non corretto");
                    LB_error_cap.setVisible(true);
                    IV_check_cap.setVisible(false);
                    saveOk.put("cap", false);
                }
            } else {
                LB_error_cap.setText("Questo campo non può essere vuoto");
                LB_error_cap.setVisible(true);
                IV_check_cap.setVisible(false);
                saveOk.put("cap", false);
            }
        });
    }

    /**
     * Metodo per inizializzare la backarrow
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
     * Metodo per scurire il pulsante backarrow quando ci si passa sopra
     */
    @FXML
    private void darkStyleBack() {
        setDarkHover(BT_back);
    }

    /**
     * Metodo per resettare il colore del pulsante backarrow quando ci si toglie dal pulsante
     */
    @FXML
    private void restoreStyleBack() {
        resetDarkExit(BT_back);
    }

    /**
     * Metodo per ridurre il programma ad icona
     */
    @FXML
    private void minimize() {
        stage.setIconified(true);
    }

    /**
     * Utilizzato per scurire l'icona minimize
     * quando il cursore entra
     */
    @FXML
    private void darkStyleMinimize() {
        setDarkHover(BT_minimize);
    }

    /**
     * Utilizzato per riportare l'immagine alla normalità
     * una volta uscito il cursore
     */
    @FXML
    private void restoreStyleMinimize() {
        resetDarkExit(BT_minimize);
    }

    /**
     * Quando premuto, il tasto exit chiude l'applicazione
     */
    @FXML
    private void quit() {
        System.exit(0);
    }

    /**
     * Utilizzato per scurire l'icona quit
     * quando il cursore entra
     */
    @FXML
    private void darkStyleQuit() {
        setDarkHover(BT_quit);
    }

    /**
     * Utilizzato per riportare l'immagine alla normalità
     * una volta uscito il cursore
     */
    @FXML
    private void restoreStyleQuit() {
        resetDarkExit(BT_quit);
    }

    /**
     * Utilizzato da certe immagini per scurire l'interno
     *
     * @param iv ImageView che si vuole scurire
     */
    private void setDarkHover(@NotNull ImageView iv) {
        iv.setEffect(new InnerShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.5), 10, 0, 5, 5));
    }

    /**
     * Utilizzato da certe immagini per portare alla normalità
     * l'effetto interno di scurimento
     *
     * @param iv ImageView che si vuole portare alla normalità
     */
    private void resetDarkExit(@NotNull ImageView iv) {
        iv.setEffect(null);
    }

    /**
     * Metodo per gestire la registrazione, andando a controllare tutti i campi tramite i metodi
     * richiamati dalla classe RegistrationUtil. In caso di successo viene creato il centro
     * sul DB tramite l'apposito metodo del server. Se avviene con successo, l'utente
     * viene mandato al {@link LoginMainController}
     *
     * @see org.project.utils.RegistrationUtil#checkPasswordConfirmed(String, String)
     * @see org.project.utils.RegistrationUtil#checkDuplicateAddress(String)
     * @see org.project.server.Server#insertDataHub(Hub)
     */
    @FXML
    private void signUp() {
        BT_sing_up.setText("");
        PS_spinner.setVisible(true);

        String hubName = TF_name_hub.getText().strip();
        String pwd = PF_password.getPassword().strip();
        String confirmedPwd = PF_confirmed_password.getPassword().strip();
        String quali = CB_qualificator.getValue();
        String address = StringUtils.capitalize(TF_address.getText().toLowerCase(Locale.ROOT).strip());
        String number = TF_number.getText().strip().toUpperCase(Locale.ROOT);
        String city = StringUtils.capitalize(TF_city.getText().toLowerCase(Locale.ROOT).strip());
        String cap = TF_cap.getText().strip();
        String prov = CB_province.getValue();
        String typology = CB_typology.getValue();

        if (quali != null) {
            LB_error_qualificator.setVisible(false);
            saveOk.put("quali", true);
        } else {
            LB_error_qualificator.setVisible(true);
            saveOk.put("quali", false);
        }

        if (prov != null) {
            LB_error_province.setVisible(false);
            saveOk.put("prov", true);
        } else {
            LB_error_province.setVisible(true);
            saveOk.put("prov", false);
        }

        if (typology != null) {
            LB_error_typology.setVisible(false);
            saveOk.put("typology", true);
        } else {
            LB_error_typology.setVisible(true);
            saveOk.put("typology", false);
        }

        if (RegistrationUtil.checkPasswordConfirmed(pwd, confirmedPwd)) {
            LB_error_confirmed_password.setVisible(false);
            saveOk.put("confirmedPwd", true);
        } else {
            LB_error_confirmed_password.setVisible(true);
            saveOk.put("confirmedPwd", false);
        }

        for (String s : hubField) {
            if (saveOk.get(s)) {
                countOk++;
            }
        }

        String checkAddress = quali + address + number + city + cap + prov;
        if (!RegistrationUtil.checkDuplicateAddress(checkAddress)) {
            errorAlertAddress();
            countOk = 0;
            LB_error_qualificator.setVisible(false);
            LB_error_province.setVisible(false);
            LB_error_typology.setVisible(false);
            BT_sing_up.setText("REGISTRATI");
            PS_spinner.setVisible(false);
        } else if (countOk == hubField.length) {
            try {
                scene.setRoot(FXMLLoader.load(Objects.requireNonNull(LoginMainController.class.getResource("fxml/login.fxml"))));
            } catch (IOException e) {
                e.printStackTrace();
            }

            String cryptPwd = Password.hash(pwd).addRandomSalt().withArgon2().getResult();

            Address finalAddress = new Address();
            finalAddress.setQualificator(quali);
            finalAddress.setAddress(address);
            finalAddress.setNumber(number);
            finalAddress.setCity(city);
            finalAddress.setCap(cap);
            finalAddress.setProvince(prov);

            Hub hub = new Hub();
            hub.setNameHub(hubName);
            hub.setPassword(cryptPwd);
            hub.setAddress(finalAddress);
            hub.setType(typology);
            System.out.println(hub);

            try {
                ServerReference.getServer().insertDataHub(hub);
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        } else {
            countOk = 0;
            errorAlertField();
            LB_error_qualificator.setVisible(false);
            LB_error_province.setVisible(false);
            LB_error_typology.setVisible(false);
            BT_sing_up.setText("REGISTRATI");
            PS_spinner.setVisible(false);
        }
    }

    /**
     * Alert utilizzato per gestire eventuali errori sulle label dedicate
     * all'indirizzo del centro. Viene chiamato da {@link #signUp}
     */
    private void errorAlertAddress() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore Indirizzo");
        alert.setHeaderText("Errore nell'indirizzo:");
        alert.setContentText("All'indirizzo inserito esiste già un centro vaccinale!");
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);

        ButtonType buttonTypeCancel = new ButtonType("OK", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeCancel);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setPrefSize(290, 185);
        dialogPane.lookupButton(buttonTypeCancel).setId("btnCancel");
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("alert_error.css")).toExternalForm());
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
     * Alert utilizzato per segnalare eventuali errori in fase di registrazione, viene
     * chiamato dal metodo {@link #signUp}
     */
    private void errorAlertField() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore in fase di registrazione");
        alert.setHeaderText("Errore nei campi:");
        alert.setContentText("Uno o piu campi non sono corretti!");
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
}
