package org.project.login;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.project.UserType;
import org.project.hub.HubHomeController;
import org.project.hub.HubSignUpController;
import org.project.server.ServerReference;
import org.project.user.UserHomeController;
import org.project.user.UserSignUpController;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Questa classe gestisce tutti i componenti presenti nella
 * schermata di login, da qui verrà poi reindirizzato
 * l'utente. In base alla scelta effettuata puo' esser indirizzato
 * alle schermate di:
 * <br>
 * Viene gestito l'avvio di altri {@link Stage} ovvero:
 * <uo>
 * <li>Home centro vaccinale {@link HubHomeController}</li>
 * <li>Registrazione centro vaccinale {@link HubSignUpController}</li>
 * <li>Home cittadini {@link UserHomeController}</li>
 * <li>Registrazione cittadini {@link UserSignUpController}</li>
 * </uo>
 *
 * @author Federico Mainini 740691 (VA)
 * @author Gianluca Latronico 739893 (VA)
 * @author Marc Alexander Orlando 741473 (VA)
 * @author Enrico Luigi Lamperti 740612 (VA)
 */
public class LoginMainController implements Initializable {

    /**
     * AnchorPane esterno
     */
    @FXML
    private AnchorPane AP_ext;

    /**
     * Immagine utilizzata per minimizzare l'applicazione
     */
    @FXML
    private ImageView BT_minimize;

    /**
     * Immagine che funge da quit dall'applicazione
     */
    @FXML
    private ImageView BT_quit;

    /**
     * Field per l'inserimento dell'email
     * o il nome del centro vaccinale
     */
    @FXML
    private MFXTextField TF_email;

    /**
     * Label di errore per il campo email
     */
    @FXML
    private Label LB_error_email;

    /**
     * Field per l'inserimento della password
     */
    @FXML
    private MFXPasswordField PF_password;

    /**
     * Label di errore per il campo password
     */
    @FXML
    private Label LB_error_password;

    /**
     * Bottone per loggarsi nell'applicazione
     */
    @FXML
    private MFXButton BT_login;

    /**
     * Label per entrare nell'applicazione
     * senza loggarsi
     */
    @FXML
    private Label BT_login_guest;

    /**
     * Label per accedere alla schermata di
     * registrazione
     */
    @FXML
    private Label BT_signUp;

    /**
     * Stage riferito a questo controller
     */
    private Stage stage;

    /**
     * Scena riferito a questo controller
     */
    private Scene scene;

    /**
     * Variabile uilizzata gestire
     * l'apertura dell'applicazione dove
     * si ha il puntatore del mouse
     */
    private double xPos = 0;

    /**
     * Variabile uilizzata gestire
     * l'apertura dell'applicazione dove
     * si ha il puntatore del mouse
     */
    private double yPos = 0;

    /**
     * Utilizzato per inizializzare lo stage e creare
     * i field per la login con i propri listener
     *
     * @param url            url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startUpLocation(AP_ext.getPrefWidth(), AP_ext.getPrefHeight());
        Platform.runLater(() -> {
            stage = (Stage) AP_ext.getScene().getWindow();
            scene = AP_ext.getScene();
            if (xPos == 0.0 && yPos == 0.0) {
                stage.centerOnScreen();
            } else {
                stage.setX(xPos);
                stage.setY(yPos);
            }

            initializeServer();
        });

        File rememberMe = new File(getPathRememberMe());
        if (rememberMe.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(rememberMe));
                TF_email.setText(br.readLine());
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        TF_email.textProperty().addListener((observable, oldValue, newValue) -> {
            LB_error_email.setVisible(false);
            LB_error_password.setVisible(false);
        });

        PF_password.textProperty().addListener((observable, oldValue, newValue) -> {
            LB_error_email.setVisible(false);
            LB_error_password.setVisible(false);
        });
    }

    /**
     * Utilizzato per tener traccia dello schermo
     * in cui il mouse è situato,
     * nel caso si avessero più schermi
     *
     * @param windowWidth  grandezza della schermata
     * @param windowHeight altezza della schermata
     */
    private void startUpLocation(double windowWidth, double windowHeight) {
        Point p = MouseInfo.getPointerInfo().getLocation();
        List<Screen> screens = Screen.getScreens();
        if (p != null && screens != null && screens.size() > 1) {
            Rectangle2D screenBounds;
            for (Screen screen : screens) {
                screenBounds = screen.getVisualBounds();
                if (screenBounds.contains(p.x, p.y)) {
                    xPos = screenBounds.getMinX() + ((screenBounds.getMaxX() - screenBounds.getMinX() - windowWidth) / 2);
                    yPos = screenBounds.getMinY() + ((screenBounds.getMaxY() - screenBounds.getMinY() - windowHeight) / 2);
                }
            }
        }
    }

    /**
     * Utilizzato per stabilire la connessione con il server
     * utilizzando il metodo contenuto nella classe {@link ServerReference}
     */
    private void initializeServer() {
        try {
            ServerReference.initializeServer();
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
            errorNoConnection();
        }
    }

    /**
     * Quando premuto, minimizza la schermata
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
     * Quando premuto, il tasto exit chiude il programma
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
     * Utilizzato per loggare premendo
     * il tasto invio
     *
     * @param keyEvent evento della tastiera da gestire
     */
    @FXML
    private void pressEnter(@NotNull KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            login();
        }
    }

    /**
     * Utilizzato per caricare nel campo {@link #BT_login} la stringa contenuta nel file
     * remember_me, il quale contiene l'email o il nome dell'ultimo
     * utente o centro vaccinale che ha effettuato la login.
     * Quando premuto il bottone di login viene richiamato il metodo remoto
     * per verificare le credenziali, se queste sono correte viene restituito il tipo
     * di utente (User o centro vaccinale); con questa informazione viene avviata la
     * schermata home corretta
     *
     * @see org.project.server.Server#checkCredential(String, String)
     */
    @FXML
    private void login() {
        String email = TF_email.getText().strip();
        String pwd = PF_password.getPassword().strip();

        File rememberMe = new File(getPathRememberMe());
        if (!rememberMe.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                rememberMe.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(rememberMe));
            bw.write(email);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UserType userHub = null;
        try {
            userHub = ServerReference.getServer().checkCredential(email, pwd);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

        if (userHub == null) {
            LB_error_email.setVisible(true);
            LB_error_password.setVisible(true);
        } else if (userHub == UserType.HUB) {
            try {
                startRightHomeStage(UserType.HUB);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                startRightHomeStage(UserType.USER);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Utilizzato per aprire la schermata corretta in base altipo di utente
     * passato come parametro
     *
     * @param userType tipo di utente (User o centro vaccinale)
     * @throws IOException IOException
     */
    private void startRightHomeStage(UserType userType) throws IOException {
        Scene scene;
        if (userType == UserType.HUB) {
            FXMLLoader loader = new FXMLLoader(HubHomeController.class.getResource("fxml/hub_home.fxml"));
            Parent root = loader.load();
            HubHomeController hubHomeController = loader.getController();
            hubHomeController.setNameHub(TF_email.getText().strip());
            scene = new Scene(root);
        } else if (userType == UserType.USER) {
            FXMLLoader loader = new FXMLLoader(UserHomeController.class.getResource("fxml/user_home.fxml"));
            Parent root = loader.load();
            UserHomeController userHomeController = loader.getController();
            userHomeController.setEmail(TF_email.getText().strip());
            scene = new Scene(root);
        } else {
            FXMLLoader loader = new FXMLLoader(UserHomeController.class.getResource("fxml/user_home.fxml"));
            Parent root = loader.load();
            UserHomeController userHomeController = loader.getController();
            userHomeController.setEmail("Guest");
            scene = new Scene(root);
        }

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.setResizable(false);
        stage.setTitle("CVI");
        stage.getIcons().add(new Image(Objects.requireNonNull(UserType.class.getResourceAsStream("drawable/primula.png"))));
        stage.initOwner(this.stage);
        this.stage.close();

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

        stage.show();
    }

    /**
     * Utilizzato per ottrnere la path del file remember_me
     *
     * @return la path del file remember_me
     */
    @NotNull
    private String getPathRememberMe() {
        return System.getProperty("user.dir") + File.separator + "Remember_me";
    }

    /**
     * Funzione associata al {@link #BT_login_guest},
     * permette l'accesso in modalita' ospite, in questo
     * modo viene avviata {@link UserHomeController} con
     * alcune funzionalita' in meno
     */
    @FXML
    private void loginGuest() {
        try {
            startRightHomeStage(UserType.GUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Funzione associata al {@link #BT_signUp},
     * va a smistare gli utenti in base al tipo
     */
    @FXML
    private void signUp() {
        UserType userType = choiceAlert();
        if (userType == UserType.HUB) {
            try {
                scene.setRoot(FXMLLoader.load(Objects.requireNonNull(HubSignUpController.class.getResource("fxml/hub_sign_up.fxml"))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (userType == UserType.USER) {
            try {
                scene.setRoot(FXMLLoader.load(Objects.requireNonNull(UserSignUpController.class.getResource("fxml/user_sign_up.fxml"))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Alert mostrato in caso di mancata connessione con il server, viene
     * richiamato da {@link #initializeServer}
     */
    private void errorNoConnection() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Connessione assente");
        alert.setHeaderText("Connessione assente");
        alert.setContentText("Nessuna connessione riprova");
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);

        ButtonType buttonTypeQuit = new ButtonType("Esci", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeQuit);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setPrefSize(290, 125);
        dialogPane.lookupButton(buttonTypeQuit).setId("btnCancel");
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
        System.exit(0);
    }

    /**
     * Viene aperto nel momento in qui viene premuto {@link #BT_signUp},
     * l'utente puo' scegliere di aprire {@link HubSignUpController} o
     * {@link UserSignUpController}
     *
     * @return il tipo selezionato dall'utente, fra centro vaccinale e
     * cittadino
     */
    @Nullable
    private UserType choiceAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registrazione");
        alert.setHeaderText("Scegli come registrarti:");
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);

        ButtonType buttonTypeHub = new ButtonType("Hub");
        ButtonType buttonTypeUser = new ButtonType("Cittadino");
        ButtonType buttonTypeCancel = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeHub, buttonTypeUser, buttonTypeCancel);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setMaxSize(2, 2);
        dialogPane.lookupButton(buttonTypeCancel).setId("btnCancel");
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("alert_choice.css")).toExternalForm());
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

        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(null) == buttonTypeHub) {
            return UserType.HUB;
        } else if (result.orElse(null) == buttonTypeUser) {
            return UserType.USER;
        } else {
            return null;
        }
    }
}
